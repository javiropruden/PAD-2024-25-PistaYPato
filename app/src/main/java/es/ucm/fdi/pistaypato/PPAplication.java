package es.ucm.fdi.pistaypato;

import android.app.Application;
import android.os.StrictMode;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

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
        usersReference = firebaseDatabase.getReference("users");
        this.solitariosReference = firebaseDatabase.getReference("Solitarios");
    }

    public DatabaseReference getSolitariosReference() {
        return solitariosReference;
    }

    // Método para agregar un nuevo usuario a Firebase Realtime Database
    public void addUser( User newUser ) {
        usersReference.child(newUser.getEmail()).setValue(newUser)
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
    public boolean returnPassword(String email, String password){

        return true;
    }

    public void escribirEmail(String destinatario, String asunto, String mensaje){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Crear instancia de JavaMailAPI
        JavaMailAPI mailAPI = new JavaMailAPI(this.emailRemitente, this.contrasenaEmailRemitente);

        // Enviar el correo
        mailAPI.enviarCorreo(destinatario, asunto, mensaje);
    }

    public User returnUser(String email) {
        final User[] user = new User[1];
        usersReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user[0] = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error al obtener usuario: " + databaseError.getMessage());
            }
        });
        return user[0];
    }
}
