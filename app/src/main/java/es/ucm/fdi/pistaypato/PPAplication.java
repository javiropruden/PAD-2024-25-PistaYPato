package es.ucm.fdi.pistaypato;

import android.app.Application;
import android.os.StrictMode;
import android.util.Base64;

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

public class PPAplication extends Application {

    // Almacena los campos de bádminton
    public ArrayList<String> badmintonFields;
    public JSONArray jsonArray;


    // Referencias para Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;
    private String emailRemitente ;
    private String contrasenaEmailRemitente;
    private User propietario;
    private DatabaseReference solitariosReference;
    private DatabaseReference instalacionesReference;


    @Override
    public void onCreate() {
        super.onCreate();


        // Inicializa la lista
        badmintonFields = new ArrayList<>();
        jsonArray = new JSONArray();
        this.emailRemitente = "jiayun.zhan.0515@gmail.com";
        this.contrasenaEmailRemitente = "pgyeeplgqdejxmny";
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");
        usersReference = firebaseDatabase.getReference("Users");

        //si no existe la referencia de usuarios la crea
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

        // Inicializa la referencia de solitarios
        this.solitariosReference = firebaseDatabase.getReference("Solitarios");
    }

    public DatabaseReference getSolitariosReference() {
        return solitariosReference;
    }

    // Método para agregar un nuevo usuario a Firebase Realtime Database

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
        usersReference.child(email).removeValue()
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

    // Método para obtener la lista de campos de bádminton
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

}
