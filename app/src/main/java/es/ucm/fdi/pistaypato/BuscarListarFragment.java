package es.ucm.fdi.pistaypato;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class BuscarListarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BuscarListarFragment() {
        // Required empty public constructor
    }

    public static BuscarListarFragment newInstance(String param1, String param2) {
        BuscarListarFragment fragment = new BuscarListarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private String tiempo = "TODOS";
    private TextView dia;
    private FloatingActionButton floatingActionButton;
    private Spinner spinner;
    private View view;
    PPAplication app;
    private Button buscar, crear;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_buscar_listar, container, false);

        app = (PPAplication) requireActivity().getApplication();

        dia = view.findViewById(R.id.dia);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        spinner = view.findViewById(R.id.spinner);
        buscar = view.findViewById(R.id.buscarListar);
        crear = view.findViewById(R.id.crearListar);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = spinner.getSelectedItem().toString();

                if (selectedItem.isEmpty() || selectedItem.equals(getString(R.string.selecionar)) || Objects.equals(tiempo, "TODOS")) {
                    Toast.makeText(getContext(), "Por favor, selecciona una pista y fecha", Toast.LENGTH_SHORT).show();
                }
                else{

                    List<User> usuarios = new ArrayList<>();
                    usuarios.add(app.getPropietario());
                    Solitario s = new Solitario("",selectedItem ,usuarios, tiempo);
                    String id = app.crearSolitario(s);
                    s.setId(id);

                    if(id != null && !id.isEmpty()){
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        MensagesFragment panelMensaje;
                        panelMensaje = MensagesFragment.newInstance("TRUE", "CREAR");
                        transaction.replace(R.id.middle_section, panelMensaje);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                    else{

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        MensagesFragment panelMensaje;
                        panelMensaje = MensagesFragment.newInstance("FALSE", "CREAR");
                        transaction.replace(R.id.middle_section, panelMensaje);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }

                }
            }
        });

        getBadmintonFields();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ponerfecha();

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                String lugar = spinner.getSelectedItem().toString();
                if(spinner.getSelectedItemPosition() == 0){ lugar = "TODOS";}

                ListarFragment panelListar = ListarFragment.newInstance(tiempo, lugar);
                transaction.replace(R.id.middle_section, panelListar);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.middle_section), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        return view;
    }

    private void showDatePickerDialog() {
        // Obtiene la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea y muestra el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Actualiza el TextView con la fecha seleccionada
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dia.setText(selectedDate);
                        tiempo = selectedDate;
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

    private void getBadmintonFields() {
        if (app != null && app.badmintonFields != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, app.badmintonFields);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Log.e("Badminton Fields", "No se han cargado los campos de bádminton.");
        }
    }

    private void ponerfecha() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = dateFormat.format(calendar.getTime());

        dia.setText(fechaActual);
        tiempo = fechaActual;
    }
}