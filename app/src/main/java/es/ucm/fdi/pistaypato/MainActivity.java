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


public class MainActivity extends AppCompatActivity {
    private Controller controller;

    Button chat;
    Button pistas;
    Button perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        controller = new Controller(this);

        ImageButton listar = findViewById(R.id.navigation_chat);
        ImageButton pistas = findViewById(R.id.navigation_home);
        ImageButton perfil = findViewById(R.id.navigation_profile);

        //aqui hay q iniciar sesion
        /*if (savedInstanceState == null) {
            showFragment(new Vista1Fragment()); // O cualquier vista inicial que desees
        }*/

        //control de botones

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new buscarListarFragment());
            }
        });

        pistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new BusquedaActivity());
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PerfilActivity());
            }
        });

        /*button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new Vista2Fragment());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new Vista3Fragment());
            }
        });*/
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null);  // Si deseas permitir la navegación hacia atrás
        transaction.commit();
    }


}
