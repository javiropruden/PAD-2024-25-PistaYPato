package es.ucm.fdi.pistaypato;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

// Import Firebase
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Controller controller;

    Button chat;
    Button pistas;
    Button perfil;

    // Declarar referencia de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

        ImageButton listar = findViewById(R.id.navigation_chat);
        ImageButton pistas = findViewById(R.id.navigation_home);
        ImageButton perfil = findViewById(R.id.navigation_profile);

        // Inicializar Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference("messages");

        databaseReference.setValue("Hola");

        // Botón para listar (escribir algo en la base de datos al hacer clic)
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un fragmento
                showFragment(new buscarListarFragment());


            }
        });

        // Botón de pistas (escribir algo en la base de datos al hacer clic)
        pistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un fragmento
                showFragment(new BusquedaActivity());


            }
        });

        // Botón de perfil (escribir algo en la base de datos al hacer clic)
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un fragmento
                showFragment(new PerfilActivity());
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null);  // Si deseas permitir la navegación hacia atrás
        transaction.commit();
    }
}
