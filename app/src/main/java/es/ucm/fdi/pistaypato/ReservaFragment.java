package es.ucm.fdi.pistaypato;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservaFragment extends Fragment {
    private View view;

    PPAplication app;
    private Instalacion in;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String fecha;
    private String pista_nombre;
    private String ubicacion_s;

    private TextView nombre;
    private TextView ubicacion;
    private TextView fech_a;
    private Button reserva;

    private int reserva_pista;
    private int reserva_hora;


    boolean celdaSeleccionada = false;

    Calendar calendar = Calendar.getInstance();
    String[] horarios = {
            "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00",
            "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00"
    };

    @SuppressLint("WrongViewCast")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_resultado, container, false);
        app = (PPAplication) requireActivity().getApplication();

        reserva_hora = -1;
        reserva_pista = -1;

        ImageButton volver = getActivity().findViewById(R.id.volver);
        volver.setVisibility(View.VISIBLE);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BusquedaFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        if (app.in == null) {
            Log.e("ReservaFragment", "Instalación no cargada. No se puede continuar.");
            Toast.makeText(getContext(), "Error: Instalación no cargada correctamente", Toast.LENGTH_SHORT).show();
            return view;
        }

        cargarTabla();
        reserva = view.findViewById(R.id.reservar);
        reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!celdaSeleccionada) {
                    Toast.makeText(getContext(), "Por favor, selecciona una celda para realizar la reserva", Toast.LENGTH_SHORT).show();
                    celdaSeleccionada = false;
                } else {
                    Instalacion instalacion = app.getInstalacion();

                    if (instalacion != null){
                        if (reserva_hora != -1 && reserva_pista != -1) {
                            List<Pista> pistas = instalacion.getPistas();

                            Pista pistaSeleccionada = pistas.get(reserva_pista);

                            ArrayList<Boolean> reservado = pistaSeleccionada.getReservado();

                            reservado.set(reserva_hora, true);  // Cambiar el valor de la hora seleccionada (true = reservado)

                            pistaSeleccionada.setReservado(reservado);
                            instalacion.setPistas(pistas);

                            DatabaseReference dbRef = app.getInstalacionesReference();
                            String ruta = instalacion.getId();
                            Log.e("Ruta Firebase", ruta);
                            dbRef.child(ruta)
                                    .setValue(instalacion)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.e("Firebase", "Instalación actualizada correctamente.");
                                        } else {
                                            Log.e("Firebase", "Error al actualizar la instalación: " + task.getException().getMessage());
                                        }
                                    });
                        }
                        
                        guardarReserva();
                        mandar_email(instalacion);

                    }else{
                        Log.e("Error","La instalacion no existe");
                    }
                    Toast.makeText(getContext(), R.string.reservado, Toast.LENGTH_SHORT).show();
                }
            }
        });
        try {
            ponerdatos();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void mandar_email(Instalacion i) {
        //enviar correo de confirmaci'on
        String correoDestinatario = app.getPropietario().getEmail(); // Correo del destinatario
        String asunto = "Confirmación de Reserva";
        app.escribirEmail_reserva(correoDestinatario, i.getNombre(), horarios[reserva_hora], String.valueOf(reserva_pista+1), fecha);

    }

    private void guardarReserva() {
        //no mira si ya hay otra en la bbdd por que no hay posibilidad
        DatabaseReference reservasRef = app.getReservasReference();

        String idReserva = reservasRef.push().getKey();
        if (idReserva != null) {
            Reserva nuevaReserva = new Reserva(idReserva, reserva_pista, app.getPropietario().getEmail(), pista_nombre, fecha, reserva_hora,  ubicacion_s);

            // Guardar la reserva en la base de datos
            reservasRef.child(idReserva).setValue(nuevaReserva)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Reserva guardada exitosamente");
                        } else {
                            Log.e("Firebase", "Error al guardar la reserva", task.getException());
                        }
                    });
        }
    }

    private void cargarTabla() {
        TableLayout tableLayout = view.findViewById(R.id.tabla);
        tableLayout.removeAllViews();
        TableRow headerRow = new TableRow(getContext());
        TextView emptyCell = new TextView(getContext());
        emptyCell.setPadding(16, 16, 16, 16);
        headerRow.addView(emptyCell);

        for (int i= 0; i < app.in.getPistas().size(); i++){
            TextView pistaText = new TextView(getContext());
            pistaText.setText(String.valueOf(i+1));
            pistaText.setPadding(16, 16, 16, 16);
            pistaText.setTextColor(Color.BLACK);
            pistaText.setGravity(Gravity.CENTER);
            pistaText.setTextSize(16);
            headerRow.addView(pistaText);
        }
        tableLayout.addView(headerRow);
        for (int j = 0; j < horarios.length; j++) {
            TableRow tableRow = new TableRow(getContext());
            TextView horario = new TextView(getContext());
            horario.setText(horarios[j]);
            horario.setPadding(16, 16, 16, 16);
            horario.setTextColor(Color.BLACK);
            horario.setGravity(Gravity.CENTER);
            tableRow.addView(horario);
            for (int i= 0; i < app.in.getPistas().size(); i++) {
                TextView estado = color(app.in.getPistas(),i,j);
                tableRow.addView(estado);
            }
            tableLayout.addView(tableRow);
        }
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.removeAllViews();
        scrollView.addView(tableLayout);
    }

    private TextView color(List<Pista> pistas, int pista, int h) {
        TextView estado = new TextView(getContext());
        ArrayList<Boolean> actual = pistas.get(pista).getReservado();
        if (actual.get(h)) {
            estado.setBackgroundColor(Color.rgb(150, 0, 0)); // Reservada
        } else {
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            String[] parts = horarios[h].split(" - ");
            String horaInicio = parts[0];  // "08:00"
            int horaIni = Integer.parseInt(horaInicio.split(":")[0]);
            String horaFin = parts[1];
            if(hora < horaIni || eshoy()) {
                estado.setBackgroundColor(Color.rgb(28, 151, 96)); // Libre
            }else{
                estado.setBackgroundColor(Color.rgb(201, 145, 0)); // fuera de tiempo
            }
        }
        estado.setPadding(32, 32, 32, 32);
        estado.setGravity(Gravity.CENTER);
        estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ColorDrawable) estado.getBackground()).getColor() == Color.rgb(150, 0, 0)) {
                    if(actual.get(h)) {
                        Toast.makeText(getContext(), R.string.ya_reservado, Toast.LENGTH_SHORT).show();
                    }else{
                        estado.setBackgroundColor(Color.rgb(28, 151, 96));
                    }
                } else if(((ColorDrawable) estado.getBackground()).getColor() == Color.rgb(201, 145, 0)){
                    Toast.makeText(getContext(), R.string.hora_pasada, Toast.LENGTH_SHORT).show();
                }
                else {
                    estado.setBackgroundColor(Color.rgb(150, 0, 0));
                    reserva_hora = h;
                    reserva_pista = pista;
                    celdaSeleccionada = true;
                }
            }
        });
        return estado;
    }

    private boolean eshoy() {
        Calendar hoy = Calendar.getInstance();

        //Formato de fecha en "dd/MM/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String fechaHoy = sdf.format(hoy.getTime());
        Calendar fechaSeleccionada = Calendar.getInstance();
        try {
            Date date = sdf.parse(fecha);  // Convertir la fecha seleccionada a Date
            fechaSeleccionada.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 6. Comparar el día, mes y año de las dos fechas
        if (hoy.get(Calendar.DAY_OF_MONTH) == fechaSeleccionada.get(Calendar.DAY_OF_MONTH) &&
                hoy.get(Calendar.MONTH) == fechaSeleccionada.get(Calendar.MONTH) &&
                hoy.get(Calendar.YEAR) == fechaSeleccionada.get(Calendar.YEAR)) {
            return false;
        } else {
            return true;
        }
    }

    private void ponerdatos() throws JSONException {
        nombre = view.findViewById(R.id.nombre);
        ubicacion = view.findViewById(R.id.direccion);
        fech_a = view.findViewById(R.id.fecha);
        ubicacion_s = "";

        nombre.setText(pista_nombre);
        fech_a.setText(fecha);

        for (int i = 0; i < app.jsonArray.length(); i++) {
            JSONObject field = app.jsonArray.getJSONObject(i);

            String nombre = field.optString("title");
            if (nombre.equals(pista_nombre)) {
                JSONObject address = field.optJSONObject("address");
                assert address != null;
                String locality = address.optString("locality", "");
                String postal_code = address.optString("postal-code", "");
                String street = address.optString("street-address", "");
                ubicacion_s = street + ", " + locality + ", " + postal_code;
            }
        }

        ubicacion.setText( ubicacion_s);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fecha = getArguments().getString(ARG_PARAM1);
            pista_nombre = getArguments().getString(ARG_PARAM2);
        }
    }

    public static ReservaFragment newInstance(String param1, String param2) {
        ReservaFragment fragment = new ReservaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
}
