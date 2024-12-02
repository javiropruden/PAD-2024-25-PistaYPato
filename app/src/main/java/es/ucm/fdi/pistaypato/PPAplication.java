package es.ucm.fdi.pistaypato;

import android.app.Application;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class PPAplication extends Application {
    public ArrayList<String> badmintonFields;
    public JSONArray jsonArray;
    public Instalacion in;


    // Referencias para Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;
    private String emailRemitente ;
    private String contrasenaEmailRemitente;
    private User propietario;
    private DatabaseReference solitariosReference;
    private DatabaseReference instalacionesReference;
    private DatabaseReference reservasReference;
    private SolitarioRepository SolitarioRepository;


    @Override
    public void onCreate() {
        super.onCreate();

        badmintonFields = new ArrayList<>();
        jsonArray = new JSONArray();

        this.emailRemitente = "jiayun.zhan.0515@gmail.com";
        this.contrasenaEmailRemitente = "pgyeeplgqdejxmny";
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");
        usersReference = firebaseDatabase.getReference("Users");

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    usersReference.setValue("Users");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error checking users reference: " + databaseError.getMessage());
            }
        });

        this.instalacionesReference = firebaseDatabase.getReference("Instalaciones");
        this.reservasReference = firebaseDatabase.getReference("Reservas");

        this.solitariosReference = firebaseDatabase.getReference("Solitarios");
        SolitarioRepository = new SolitarioRepository(solitariosReference);
    }

    public DatabaseReference getSolitariosReference() {
        return solitariosReference;
    }

    public DatabaseReference getReservasReference() {
        return reservasReference;
    }

    public void addUser( User newUser ) {
        String sanitizedEmail = newUser.getEmail().replace(".", ",");
        usersReference.child(sanitizedEmail).setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Usuario añadido correctamente");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error al añadir usuario: " + e.getMessage());
                });
    }

    public void removeUser(String email){
        String sanitizedEmail = email.replace(".", ",");
        usersReference.child(sanitizedEmail).removeValue()
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Usuario eliminado correctamente");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error al eliminar usuario: " + e.getMessage());
                });
    }

    public User getPropietario() {
        return propietario;
    }

    public void setPropietario(User propietario) {
        this.propietario = propietario;
    }

    public ArrayList<String> getFields() {
        return badmintonFields;
    }

    //metodo para consultas la contraseña de un usuario es correcta o no
    public void checkPassword(String email, String password, final PasswordCallback callback) {
        returnUser(email, new UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null && user.getPassword().equals(password)) {
                    setPropietario(user);
                    callback.onCallback(true);
                }else{
                    callback.onCallback(false);
                }
            }
        });
    }

    public void escribirEmail(String destinatario, String asunto, String mensaje){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Crear instancia de JavaMailAPI
        JavaMailAPI mailAPI = new JavaMailAPI(this.emailRemitente, this.contrasenaEmailRemitente);

        // Enviar el correo
        mailAPI.enviarCorreo(destinatario, asunto, mensaje);
    }

    public void escribirEmail_reserva(String destinatario, String instalacion, String hora, String pista, String fecha){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JavaMailAPI mailAPI = new JavaMailAPI(this.emailRemitente, this.contrasenaEmailRemitente);
        mailAPI.enviarCorreo(destinatario, "Confirmacion de reserva", instalacion, hora, pista, fecha);
    }

    public void returnUser(String email, final UserCallback callback) {
        String sanitizedEmail = email.replace(".", ",");
        usersReference.child(sanitizedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onCallback(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al obtener usuario: " + databaseError.getMessage());
                callback.onCallback(null);
            }
        });
    }

    public interface UserCallback {
        void onCallback(User user);
    }

    public interface PasswordCallback {
        void onCallback(boolean isValid);
    }

    public String hashPassword(String password) {
        String hashedPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            hashedPassword = Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public DatabaseReference getInstalacionesReference() {
        return instalacionesReference;
    }

    public String crearSolitario(Solitario newSol){
        String id = this.SolitarioRepository.crearSolitario(newSol);
        if(id!= null && !id.isEmpty()){
            String mensaje = "<p>Buenas, jugador:</p>" +
                    "<p>Usted se añadió en el siguiente solitario:</p>" +
                    "<ul>" +
                    "<li><strong>Pista:</strong> " + newSol.getLugar() + "</li>" +
                    "<li><strong>Día:</strong> " + newSol.getFecha() + "</li>" +
                    "</ul>" +
                    "<p><strong>Email de los otros jugadores:</strong><br>" +
                    propietario.getEmail().replace(", ", "<br>") + "</p>" +
                    "<p>Un saludo,<br> Pista y pato </p>";

            String asunto = "Información sobre su partida de solitario creado";

            this.escribirEmail(newSol.getUsuarios().get(0).getEmail(), asunto, mensaje);
        }

        return id;
    }

    public void anadirSolitario(String id, String lugar,String fecha){
       boolean ok = true;

        this.SolitarioRepository.anadirSolitario(id, getPropietario());

        DatabaseReference sol = this.solitariosReference.child(id);

        sol.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> emails = new ArrayList<>();
                String emailes = "";
                // Iterar sobre los elementos de la base de datos
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User u = userSnapshot.getValue(User.class);  // Convertir el DataSnapshot en un objeto User
                    if (u != null) {
                         // Agregar el objeto User a la lista
                        emails.add(u.getEmail());  // Agregar el correo electrónico a la lista de emails
                        emailes = emailes + u.getEmail() + " , ";  // Concatenar los correos electrónicos en un string
                    }
                }
                emails.add(propietario.getEmail());
                emailes = emailes + propietario.getEmail() + " , ";

                String mensaje = "<p>Buenas, jugador:</p>" +
                        "<p>Usted se añadió en el siguiente solitario:</p>" +
                        "<ul>" +
                        "<li><strong>Pista:</strong> " + lugar + "</li>" +
                        "<li><strong>Día:</strong> " + fecha + "</li>" +
                        "</ul>" +
                        "<p><strong>Email de los otros jugadores:</strong><br>" +
                        emailes.replace(", ", "<br>") + "</p>" +
                        "<p>Un saludo,<br> Pista y pato </p>";

                String asunto = "Información sobre su partida de solitario";

                for(String uno: emails) {
                    Log.d("ANADIR", "Correo enviado a " + uno);
                    escribirEmail(uno, asunto, mensaje);
                }

                escribirEmail(getPropietario().getEmail(), asunto, mensaje);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer perfiles", error.toException());
            }
        });

    }

    public Instalacion getInstalacion() {
        return in;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.in = instalacion;
    }
}
