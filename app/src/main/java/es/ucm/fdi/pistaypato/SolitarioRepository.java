package es.ucm.fdi.pistaypato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

// Import Firebase
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SolitarioRepository {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference solitariosReference;
    List<String> emails;

    public SolitarioRepository(){
        firebaseDatabase = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");
        this.solitariosReference = firebaseDatabase.getReference("Solitarios");

    }

    public SolitarioRepository(DatabaseReference solitariosReference){
        this.solitariosReference = solitariosReference;
    }

    public String crearSolitario(Solitario newSolitario){
        String id = solitariosReference.push().getKey();
        newSolitario.setId(id);

        solitariosReference.child(id).setValue(newSolitario)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Solitario agregado correctamente");

                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error al agregar el Solitario", e);
                });

        return id;
    }

    public boolean anadirSolitario(String id, User propiedario){
        DatabaseReference sol = this.solitariosReference.child(id);

        sol.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> perfiles = (List<User>) snapshot.getValue() ;

                if (perfiles == null) {
                    perfiles = new ArrayList<>();
                }
                perfiles.add(propiedario);

                sol.child("usuarios").setValue(perfiles)
                        .addOnSuccessListener(aVoid ->{

                            Log.d("Firebase", "Perfil agregado correctamente");
                        } )
                        .addOnFailureListener(e -> {

                            Log.e("Firebase", "Error al agregar perfil", e);
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer perfiles", error.toException());
            }
        });

        return true;
    }


}
