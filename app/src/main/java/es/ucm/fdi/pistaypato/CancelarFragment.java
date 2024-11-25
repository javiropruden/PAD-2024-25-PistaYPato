package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CancelarFragment extends Fragment {

    // Fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters
    private String mParam1;
    private String mParam2;

    private EditText nombreEquipo;
    private EditText direccion;
    private EditText fecha;
    private EditText pista;
    private EditText hora;
    private Button cancelarButton;

    public CancelarFragment() {
    }

    public static CancelarFragment newInstance(String param1, String param2) {
        CancelarFragment fragment = new CancelarFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelar, container, false);

        // Referenciar elementos del layout
        nombreEquipo = view.findViewById(R.id.cancelar_nombre_equipo);
        direccion = view.findViewById(R.id.cancelar_direccion);
        fecha = view.findViewById(R.id.cancelar_fecha);
        pista = view.findViewById(R.id.cancelar_pista);
        hora = view.findViewById(R.id.cancelar_hora);
        cancelarButton = view.findViewById(R.id.cancelar_btn_cancelar_reserva);

        // Acción del botón "Cancelar Reserva"
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarReserva();
            }
        });

        return view;
    }

    private void cancelarReserva() {
        String equipo = nombreEquipo.getText().toString();
        String direccionReserva = direccion.getText().toString();
        String fechaReserva = fecha.getText().toString();
        String pistaReserva = pista.getText().toString();
        String horaReserva = hora.getText().toString();

        // Validar campos llenos antes de continuar
        if (equipo.isEmpty() || direccionReserva.isEmpty() || fechaReserva.isEmpty() || pistaReserva.isEmpty() || horaReserva.isEmpty()) {
            Log.e("CancelarReserva", "Todos los campos deben estar llenos");
            return;
        }

        Log.d("CancelarReserva", "Reserva cancelada para el equipo: " + equipo + " en la dirección: " + direccionReserva +
                " en la fecha: " + fechaReserva + " en la pista: " + pistaReserva + " a las: " + horaReserva);

        //Logica para cancelar hacer

        limpiarCampos();
    }

    private void limpiarCampos() {
        nombreEquipo.setText("");
        direccion.setText("");
        fecha.setText("");
        pista.setText("");
        hora.setText("");
    }
}
