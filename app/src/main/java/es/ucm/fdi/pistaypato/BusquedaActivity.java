package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

public class BusquedaActivity extends Fragment {

    private Spinner spinner;
    private List<String> badmintonFields;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_busqueda, container, false);
    }
}
/*
*
* package es.ucm.fdi.pistaypato;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        setContentView(R.layout.activity_busqueda);

        dia = findViewById(R.id.dia);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        spinner = findViewById(R.id.spinner);

        badmintonFields = new ArrayList<>();
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
RequestQueue queue = Volley.newRequestQueue(this);

StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                            /*Log.d("API Response", response);
                            JSONArray jsonArray = new JSONArray(response);
                            Log.d("hola", String.valueOf(jsonArray));
                    JSONObject jsonObject = new JSONObject(response);

                    // Verificamos y accedemos al array "@graph"
                    JSONArray graphArray = jsonObject.optJSONArray("@graph");
                    if (graphArray == null) {
                        Log.e("JSON Error", "El campo '@graph' no es un array o no existe");
                        return;
                    }
                    badmintonFields.clear();
                    badmintonFields.add(getString(R.string.selecionar));
                    for (int i = 0; i < graphArray.length(); i++) {
                        JSONObject field = graphArray.getJSONObject(i);

                        String services = field.optJSONObject("organization").optString("services", "");

                        if (services.toLowerCase().contains("bádminton") || services.toLowerCase().contains("badminton")) {
                            // Si contiene "bádminton", agregamos el título del centro
                            String nombre = field.optString("title", "Sin Título");
                            Log.d("Centro con Bádminton", nombre);
                            badmintonFields.add(nombre);
                        }
                    }

                    // Configura el spinner con los datos obtenidos
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, badmintonFields);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
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