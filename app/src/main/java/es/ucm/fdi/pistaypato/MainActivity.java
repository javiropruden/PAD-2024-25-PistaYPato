package es.ucm.fdi.pistaypato;

import android.os.Bundle;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Controller controller;
    PPAplication app;

    Button chat;
    Button pistas;
    Button perfil;

    Registracion usuario;
    User user; //PARA PASARLE LOS DATOS DEL USER A PERFIL

    // Declarar referencia de Firebase
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        app = (PPAplication) getApplication();

        getBadmintonFields();

        ImageButton listar = findViewById(R.id.navigation_chat);
        ImageButton pistas = findViewById(R.id.navigation_home);
        ImageButton perfil = findViewById(R.id.navigation_profile);

        // Inicializar Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");

        // Botón para listar (escribir algo en la base de datos al hacer clic)
        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new buscarListarFragment());
                transaction.addToBackStack(null);  // Permitir navegar hacia atrás al presionar el botón de "atrás"
                transaction.commit();

            }
        });

        inicial();
        // Botón de pistas (escribir algo en la base de datos al hacer clic)
        pistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");

                // Obtenemos la referencia a la base de datos en "Instalaciones"
                DatabaseReference db = firebaseDatabase.getReference("Instalaciones");

                // Crear una lista de pistas (puedes inicializarla con objetos si necesitas)
                List<Pista> pista = new ArrayList<>();
                pista.add(new Pista());
                pista.add(new Pista());

                // Crear una instalación con la lista de pistas
                Instalacion ins = new Instalacion("pru", pista);

                // Generar un ID único para esta instalación
                String id = db.push().getKey();

                // Asegurarnos de que el ID no sea nulo
                if (id != null) {
                    ins.setId(id);

                    // Guardar el objeto en Firebase usando el ID generado
                    db.child(id).setValue(ins)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "Instalación agregada correctamente");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Error al agregar la instalación", e);
                            });
                } else {
                    Log.e("Firebase", "Error: ID generado es nulo");
                }

                // Mostrar un fragmento
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BusquedaActivity());
                transaction.addToBackStack(null); // Permitir navegar hacia atrás al presionar el botón de "atrás"
                transaction.commit();
            }
        });

        // Botón de perfil (escribir algo en la base de datos al hacer clic)
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PerfilFragment());
            }
        });

    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null);  // Si deseas permitir la navegación hacia atrás
        transaction.commit();
    }

    private void getBadmintonFields() {
        String url = "https://datos.madrid.es/egob/catalogo/200186-0-polideportivos.json";
        //API de la comunidad de madrid
        RequestQueue queue = Volley.newRequestQueue(this);
        app.badmintonFields.clear();
        app.badmintonFields.add(getString(R.string.selecionar));
        //aqui hay q añadir las pistas privadas desde la base de datos
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("API Response", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            app.jsonArray = jsonResponse.optJSONArray("@graph");

                            if (app.jsonArray != null) {
                                app.badmintonFields.clear();
                                app.badmintonFields.add(getString(R.string.selecionar)); // Añadir una opción de selección

                                //aqui hay q añadir las pistas privadas desde la base de datos
                                for (int i = 0; i < app.jsonArray.length(); i++) {
                                    JSONObject field = app.jsonArray.getJSONObject(i);

                                    JSONObject organization = field.optJSONObject("organization");
                                    if (organization != null) {
                                        String services = organization.optString("services", ""); // Obtiene los servicios

                                        // Si contiene "bádminton" o "badminton", agrega el nombre del centro
                                        if (services.toLowerCase().contains("bádminton") || services.toLowerCase().contains("badminton")) {
                                            String nombre = field.optString("title", "Sin Título"); // Obtiene el nombre del centro
                                            app.badmintonFields.add(nombre); // Agrega el nombre del centro a la lista
                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error al parsear los datos JSON", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Error al obtener datos", error);
            }
        });

        queue.add(stringRequest);
    }

    public void inicial(){
        // Mostrar un fragmento
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, new BusquedaActivity());
        transaction.addToBackStack(null); // Permitir navegar hacia atrás al presionar el botón de "atrás"
        transaction.commit();
    }
}
