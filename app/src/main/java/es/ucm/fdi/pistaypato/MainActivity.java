package es.ucm.fdi.pistaypato;

import android.content.pm.ActivityInfo;
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

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    PPAplication app;

    Button chat;
    Button pistas;
    Button perfil;

    Registracion usuario;
    User user;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        app = (PPAplication) getApplication();

        getBadmintonFields();

        ImageButton listar = findViewById(R.id.navigation_chat);
        ImageButton pistas = findViewById(R.id.navigation_home);
        ImageButton perfil = findViewById(R.id.navigation_profile);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BuscarListarFragment());
                transaction.addToBackStack(null);  // Permitir navegar hacia atrás al presionar el botón de "atrás"
                transaction.commit();

            }
        });

        inicial();
        pistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BusquedaFragment());
                transaction.addToBackStack(null); // Permitir navegar hacia atrás al presionar el botón de "atrás"
                transaction.commit();
            }
        });

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
                            //Log.d("API Response", response);
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
        transaction.replace(R.id.middle_section, new BusquedaFragment());
        transaction.addToBackStack(null); // Permitir navegar hacia atrás al presionar el botón de "atrás"
        transaction.commit();
    }
}
