package es.ucm.fdi.pistaypato;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

public class ReservaActivity extends Fragment {
    private View view;

    PPAplication app;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String fecha;
    private String pista;

    private TextView nombre;
    private TextView ubicacion;
    private TextView fech_a;
    private Button reserva;


    boolean celdaSeleccionada = false;


    @SuppressLint("WrongViewCast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_resultado, container, false);

        ImageButton volver = getActivity().findViewById(R.id.volver);
        volver.setVisibility(View.VISIBLE);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BusquedaActivity());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        cargarTabla();
        reserva = view.findViewById(R.id.reservar);
        reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!celdaSeleccionada) {
                    Toast.makeText(getContext(), "Por favor, selecciona una celda para realizar la reserva", Toast.LENGTH_SHORT).show();

                    celdaSeleccionada = false;
                } else {
                    // Aquí puedes agregar la lógica para cambiar la base de datos
                    Toast.makeText(getContext(), R.string.reservado, Toast.LENGTH_SHORT).show();
                }
                //Y AQUI HABRÍA QUE CAMBIAR LA BASE DE DATOS
            }
        });
        app = (PPAplication) requireActivity().getApplication();
        try {
            ponerdatos();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void cargarTabla() {
        TableLayout tableLayout = view.findViewById(R.id.tabla);
        tableLayout.removeAllViews();
        TableRow headerRow = new TableRow(getContext());
        TextView emptyCell = new TextView(getContext());
        emptyCell.setPadding(16, 16, 16, 16);
        headerRow.addView(emptyCell);
        // Horarios para las filas
        String[] horarios = {
                "07:00 - 08:00", "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
                "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00"
        };
        //EJEMPLO DE RESERVAS Q HAY Q CARGAR DE LA BASE DE DATOS
        boolean[][] celdasReservadas = {
                {false, true},
                {true, false},
                {false, false},
                {true, true},
                {false, true},
                {true, false},
                {false, false},
                {true, true},
                {false, true},
                {true, false},
                {false, false},
                {true, true},
                {false, true}
        };
        String[] pistas = {"A", "B"}; // pistas de ejemplo esto depende de la base de datos
        for (String p : pistas) {
            TextView pistaText = new TextView(getContext());
            pistaText.setText(p);
            pistaText.setPadding(16, 16, 16, 16);
            pistaText.setTextColor(Color.BLACK);
            pistaText.setGravity(Gravity.CENTER);
            pistaText.setTextSize(16);
            headerRow.addView(pistaText);
        }
        tableLayout.addView(headerRow);
        for (int i = 0; i < horarios.length; i++) {
            TableRow tableRow = new TableRow(getContext());
            TextView horarioText = new TextView(getContext());
            horarioText.setText(horarios[i]);
            horarioText.setPadding(16, 16, 16, 16);
            horarioText.setTextColor(Color.BLACK);
            horarioText.setGravity(Gravity.CENTER);
            tableRow.addView(horarioText);
            for (int j = 0; j < pistas.length; j++) {
                TextView estadoText = getTextView(celdasReservadas, i, j);
                tableRow.addView(estadoText);
            }
            tableLayout.addView(tableRow);
        }
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.removeAllViews();
        scrollView.addView(tableLayout);
    }

    private @NonNull TextView getTextView(boolean[][] celdasReservadas, int i, int j) {
        TextView estado = new TextView(getContext());
        if (celdasReservadas[i][j]) {
            estado.setBackgroundColor(Color.RED); // Reservada
        } else {
            estado.setBackgroundColor(Color.GREEN); // Libre
        }
        estado.setPadding(32, 32, 32, 32);
        estado.setGravity(Gravity.CENTER);
        // Hacer clickeable cada celda O NO SI ESTA RESERVADO
        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ColorDrawable) estado.getBackground()).getColor() == Color.RED) {
                    Toast.makeText(getContext(), R.string.ya_reservado, Toast.LENGTH_SHORT).show();
                } else {
                    estado.setBackgroundColor(Color.RED);
                    celdaSeleccionada = true;
                }
            }
        });
        return estado;
    }

    private void ponerdatos() throws JSONException {
        nombre = view.findViewById(R.id.nombre);
        ubicacion = view.findViewById(R.id.direccion);
        fech_a = view.findViewById(R.id.fecha);
        String ubi = "";

        nombre.setText(pista);
        fech_a.setText(fecha);

        for (int i = 0; i < app.jsonArray.length(); i++) {
            JSONObject field = app.jsonArray.getJSONObject(i);

            String nombre = field.optString("title");
            if (nombre.equals(pista)) {
                JSONObject address = field.optJSONObject("address");
                assert address != null;
                String locality = address.optString("locality", "");
                String postal_code = address.optString("postal-code", "");
                String street = address.optString("street-address", "");
                ubi = street + ", " + locality + ", " + postal_code;
            }
        }

        ubicacion.setText(ubi);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fecha = getArguments().getString(ARG_PARAM1);
            pista = getArguments().getString(ARG_PARAM2);
        }
    }

    public static ReservaActivity newInstance(String param1, String param2) {
        ReservaActivity fragment = new ReservaActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
