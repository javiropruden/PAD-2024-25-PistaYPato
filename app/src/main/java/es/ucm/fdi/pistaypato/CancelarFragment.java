package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CancelarFragment extends Fragment {

    private static final String ARG_RESERVA_ID = "reserva_id";

    private EditText nombreEquipo;
    private EditText direccion;
    private EditText fecha;
    private EditText pista;
    private EditText hora;
    private Button cancelarButton;
    private ImageButton volver;

    private String reservaId;

    // Constructor vacío necesario
    public CancelarFragment(Reserva selecionado) {
    }

    // Método newInstance para crear una nueva instancia del fragmento con argumentos
    public static CancelarFragment newInstance(Reserva selecionado) {
        CancelarFragment fragment = new CancelarFragment(selecionado);
        Bundle args = new Bundle();
        args.putString(ARG_RESERVA_ID, selecionado.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reservaId = getArguments().getString(ARG_RESERVA_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelar, container, false);

        // Inicializar vistas
        nombreEquipo = view.findViewById(R.id.cancelar_nombre_equipo);
        direccion = view.findViewById(R.id.cancelar_direccion);
        fecha = view.findViewById(R.id.cancelar_fecha);
        pista = view.findViewById(R.id.cancelar_pista);
        hora = view.findViewById(R.id.cancelar_hora);
        cancelarButton = view.findViewById(R.id.cancelar_btn_cancelar_reserva);

        volver = getActivity().findViewById(R.id.volver);
        volver.setVisibility(View.VISIBLE);
        volver.setOnClickListener(v -> openFragment(new PerfilFragment()));

        // Configurar listeners
        cancelarButton.setOnClickListener(v -> cancelarReserva());
        //volver.setOnClickListener(v -> openFragment());

        return view;
    }

    private void cancelarReserva() {
        if (reservaId != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Solitarios");
            databaseReference.child(reservaId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("CancelarFragment", "Reserva cancelada exitosamente");
                        // Navegar al fragmento anterior o mostrar un mensaje al usuario
                        openFragment(new PerfilFragment());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CancelarFragment", "Error al cancelar la reserva", e);
                    });
        } else {
            Log.e("CancelarFragment", "No hay ID de reserva disponible");
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null); // Permitir regresar al fragmento previo
        transaction.commit();
    }
}
