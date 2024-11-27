package es.ucm.fdi.pistaypato;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.ArrayList;

public class PPAplication extends Application {

    // Almacena los campos de bádminton
    public ArrayList<String> badmintonFields;
    public JSONArray jsonArray;

    // Referencias para Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    @Override
    public void onCreate() {
        super.onCreate();

        // Inicializa la lista
        badmintonFields = new ArrayList<>();
        jsonArray = new JSONArray();

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        usersReference = firebaseDatabase.getReference("users");


    }

    // Método para agregar un nuevo usuario a Firebase Realtime Database
    public void addUser(String userId, String firstName, String lastName, String email, String password) {
        User newUser = new User(userId, firstName, lastName, email, password);
        usersReference.child(userId).setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    System.out.println("Usuario añadido correctamente");
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error al añadir usuario: " + e.getMessage());
                });
    }

    // Método para obtener la lista de campos de bádminton
    public ArrayList<String> getFields() {
        return badmintonFields;
    }

}
