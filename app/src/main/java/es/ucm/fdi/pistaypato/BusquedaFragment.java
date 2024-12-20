package es.ucm.fdi.pistaypato;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BusquedaFragment extends Fragment {
    private TextView dia;
    private FloatingActionButton floatingActionButton;
    private Spinner spinner;
    private View view;
    PPAplication app;
    private Button buscar;
    private String fecha;
    DatabaseReference db ;

    private boolean flagDatosCargados = false;
    private ProgressBar progressBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_busqueda, container, false);

        app = (PPAplication) requireActivity().getApplication();
        db = app.getInstalacionesReference();

        dia = view.findViewById(R.id.dia);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        spinner = view.findViewById(R.id.spinner);
        buscar = view.findViewById(R.id.buscar);

        getActivity().findViewById(R.id.volver).setVisibility(View.GONE);

        ponerfecha();

        getBadmintonFields();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinner.getSelectedItem().toString();
                if (selectedItem.isEmpty() || selectedItem.equals(getString(R.string.selecionar))) {
                    Toast.makeText(getContext(), "Por favor, selecciona una opción", Toast.LENGTH_SHORT).show();
                } else {
                    cargarDatosFirebase(selectedItem);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.middle_section), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        return view;
    }

    public void cargarDatosFirebase(String selectedItem) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean encontrado = false;

                // Recorremos todas las instalaciones para buscar la que coincida
                for (DataSnapshot instalacionSnapshot : dataSnapshot.getChildren()) {
                    Instalacion currentInstalacion = instalacionSnapshot.getValue(Instalacion.class);
                    Log.e("spinner", spinner.getSelectedItem().toString());
                    // Comprobamos si la instalación coincide con los datos seleccionados
                    if(currentInstalacion != null &&
                            currentInstalacion.getNombre() != null &&
                            currentInstalacion.getNombre().equals(selectedItem) &&
                            currentInstalacion.getFecha() != null &&
                            fecha != null &&
                            currentInstalacion.getFecha().equals(fecha)){
                        app.setInstalacion(currentInstalacion);
                        encontrado = true;
                        break;  // Salimos del bucle si encontramos la instalación
                    }
                }

                // Si no encontramos la instalación, creamos una nueva
                if (!encontrado) {
                    List<Pista> pistas = new ArrayList<>();
                    pistas.add(new Pista());
                    pistas.add(new Pista());
                    Instalacion instalacion = new Instalacion(selectedItem, pistas, fecha);
                    app.setInstalacion(instalacion);
                    crearBBDDinstalacion(instalacion);
                }

                // Establecemos el flag a true para indicar que los datos están cargados
                flagDatosCargados = true;
                // Procedemos a cargar el fragmento solo si los datos están listos
                cargarReservaFragment(app.getInstalacion());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error al leer datos: " + databaseError.getMessage());
            }
        });
    }

    private void cargarReservaFragment(Instalacion instalacion) {
        // Esperamos hasta que el flag sea true (los datos han sido cargados)
        if (flagDatosCargados && instalacion != null) {
            FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            ReservaFragment reservar = ReservaFragment.newInstance(fecha, spinner.getSelectedItem().toString());
            transaction.replace(R.id.middle_section, reservar);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.e("ReservaFragment", "Los datos no están disponibles o son nulos.");
        }
    }



    private void ponerfecha() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(calendar.getTime());
        fecha = fechaActual;
        dia.setText(fechaActual);
    }

    private void getBadmintonFields() {
        if (app != null && app.badmintonFields != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, app.badmintonFields);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Log.e("Badminton Fields", "No se han cargado los campos de bádminton.");
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Actualiza el TextView con la fecha seleccionada
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dia.setText(selectedDate);
                        fecha = selectedDate;
                    }
                },
                year, month, day
        );

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }



    public void crearBBDDinstalacion(Instalacion ins){
        String id = db.push().getKey();
        ins.setId(id);

        db.child(id).setValue(ins)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Instalación agregada correctamente");
                    } else {
                        Log.e("Firebase", "Error al agregar instalación", task.getException());
                    }
                });
    }
}
