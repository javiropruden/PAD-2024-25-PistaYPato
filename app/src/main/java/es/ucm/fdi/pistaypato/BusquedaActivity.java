package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusquedaActivity extends AppCompatActivity {

    private Spinner spinner;
    private List<String> badmintonFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);  // Asegúrate de que el layout correcto está cargado

        spinner = findViewById(R.id.spinner);
        badmintonFields = new ArrayList<>();

        // Aquí iría la solicitud a la API y carga dinámica del Spinner
        String url = "https://tu_api_aqui.com";  // Reemplaza con la URL de tu API

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Aquí estamos parseando la respuesta
                            JSONArray jsonArray = new JSONArray(response);
                            badmintonFields.clear(); // Limpiamos el contenido anterior

                            // Iteramos por cada instalación de bádminton
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject field = jsonArray.getJSONObject(i);
                                String nombre = field.getString("title"); // Suponiendo que 'title' es el campo
                                badmintonFields.add(nombre);
                            }

                            // Ahora configuramos el Spinner con los datos obtenidos
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(BusquedaActivity.this, android.R.layout.simple_spinner_item, badmintonFields);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();  // Manejo de errores si la respuesta no es válida
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Error al obtener los datos", error);
            }
        });

        queue.add(stringRequest);
    }
}
