package es.ucm.fdi.pistaypato;

/*import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView dia;
    private FloatingActionButton floatingActionButton;
    private Spinner spinner;
    private ArrayList<String> badmintonFields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.busqueda);

        dia = findViewById(R.id.dia);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        spinner = findViewById(R.id.spinner);

        getBadmintonFields();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.middle_section), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePickerDialog() {
        // Obtiene la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea y muestra el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Actualiza el TextView con la fecha seleccionada
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dia.setText(selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void getBadmintonFields() {
        String url = "https://datos.madrid.es/egob/catalogo/200186-0-polideportivos.json";
        //API de la comunidad de madrid
        //https://datos.madrid.es/portal/site/egob/menuitem.214413fe61bdd68a53318ba0a8a409a0/?vgnextoid=b07e0f7c5ff9e510VgnVCM1000008a4a900aRCRD&vgnextchannel=b07e0f7c5ff9e510VgnVCM1000008a4a900aRCRD&vgnextfmt=default

        /*[
  {
    "uid": "string",
    "dtend": "string",
    "location": {
      "longitude": 0,
      "latitude": 0
    },
    "event-location": "string",
    "link": "string",
    "relation": "string",
    "id": "string",
    "organization": {
      "accesibility": "string",
      "services": "string",
      "schedule": "string",
      "organization-name": "string",
      "organization-desc": "string"
    },
    "title": "string",
    "dtstart": "string",
    "references": "string",
    "recurrence": {
      "interval": 0,
      "days": "string",
      "frequency": "string"
    },
    "price": 0,
    "address": {
      "area": "string",
      "locality": "string",
      "district": "string",
      "street-address": "string",
      "postal-code": "string"
    },
    "description": "string",
    "excluded-days": "string"
  }
]
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("API Response", response);
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("hola", String.valueOf(jsonArray));
                            badmintonFields.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject field = jsonArray.getJSONObject(i);

                                String nombre = field.getString("title");
                                Log.d("Añadido", nombre);
                                badmintonFields.add(nombre);
                            }

                            // Configura el spinner con los datos obtenidos
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, badmintonFields);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
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
}

/*

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);

        // Llama al método para obtener los datos de los campos
        getBadmintonFields();
    }


}
*/

/*import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView diaHora;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diaHora = findViewById(R.id.diaHora);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        // Configura el botón para mostrar el DatePickerDialog
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        // Obtiene la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Muestra el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    // Actualiza el TextView con la fecha seleccionada
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    diaHora.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }
}
*/
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

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;  // Definimos el Spinner
    private List<String> badmintonFields;  // Lista que contendrá los datos para el Spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busqueda);  // Asegúrate de que el layout correcto está cargado

        spinner = findViewById(R.id.spinner);  // Referenciamos el Spinner en el layout busqueda.xml
        badmintonFields = new ArrayList<>();  // Inicializamos la lista

        // URL de la API
        String url = "https://datos.madrid.es/egob/catalogo/200186-0-polideportivos.json";  // Reemplaza con la URL correcta de tu API

        // Configuramos la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Creamos la solicitud para obtener los datos
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parseamos la respuesta JSON
                            JSONArray jsonArray = new JSONArray(response);
                            badmintonFields.clear();  // Limpiamos la lista antes de llenarla

                            // Iteramos por cada objeto JSON y obtenemos el título
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject field = jsonArray.getJSONObject(i);
                                String nombre = field.getString("title");  // Suponiendo que 'title' es el campo
                                badmintonFields.add(nombre);  // Añadimos el nombre a la lista
                            }

                            // Creamos un ArrayAdapter para el Spinner
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, badmintonFields);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);  // Asignamos el adapter al Spinner

                        } catch (JSONException e) {
                            e.printStackTrace();  // Si hay un error al parsear la respuesta, lo imprimimos
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Error al obtener los datos", error);  // Si hay un error de red o API
            }
        });

        // Añadimos la solicitud a la cola
        queue.add(stringRequest);
    }
}