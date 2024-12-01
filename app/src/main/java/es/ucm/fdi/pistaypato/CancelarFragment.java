package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CancelarFragment extends Fragment {

    private TextView nombreEquipo;
    private TextView direccion;
    private TextView fecha;
    private TextView pista;
    private TextView hora;
    private Button cancelarButton;
    private ImageButton volver;

    private Reserva reserva; // Objeto Reserva recibido desde PerfilFragment

    // Constructor personalizado para recibir la reserva directamente
    public CancelarFragment(Reserva reserva) {
        this.reserva = reserva;
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

        // Mostrar los datos de la reserva
        if (reserva != null) {
            mostrarDatosReserva(reserva);
        }

        // Configurar el botón "Cancelar Reserva"
        cancelarButton.setOnClickListener(v -> cancelarReserva());

        return view;
    }

    private void mostrarDatosReserva(Reserva reserva) {
        nombreEquipo.setText(reserva.getIdUsuario());
        direccion.setText(reserva.getUbicacion());
        fecha.setText(reserva.getFecha());
        pista.setText(reserva.getPista());
        hora.setText(String.valueOf(reserva.getHora()));
    }

    private void cancelarReserva() {
        Log.d("CancelarFragment", "Preparando para cancelar");
        if (reserva != null) {
            Log.d("CancelarFragment", "Reserva no es nula: " + reserva.getId());
        } else {
            Log.e("CancelarFragment", "Reserva es nula");
        }

        if (reserva != null && reserva.getId() != null) {
            PPAplication app = (PPAplication) requireActivity().getApplication();
            DatabaseReference reservasRef = app.getReservasReference();
            DatabaseReference instalacionesRef = app.getInstalacionesReference();

            // Eliminar la reserva del nodo "Reservas"
            reservasRef.child(reserva.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("CancelarFragment", "Reserva cancelada con éxito");

                        // Buscar la instalación por nombre y fecha, y liberar el horario
                        buscarInstalacionPorNombreYFecha(instalacionesRef);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CancelarFragment", "Error al cancelar la reserva", e);
                    });
        } else {
            openFragment(new PerfilFragment());
            Log.e("CancelarFragment", "Reserva o ID de la reserva no disponible");
        }
    }

    private void buscarInstalacionPorNombreYFecha(DatabaseReference instalacionesRef) {
        instalacionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot instalacionSnapshot : dataSnapshot.getChildren()) {
                    String nombreInstalacion = instalacionSnapshot.child("nombre").getValue(String.class);
                    String fechaInstalacion = instalacionSnapshot.child("fecha").getValue(String.class);

                    if (nombreInstalacion != null && fechaInstalacion != null &&
                            nombreInstalacion.equals(reserva.getPista()) &&
                            fechaInstalacion.equals(reserva.getFecha())) {
                        Log.d("CancelarFragment", "Instalación encontrada: " + nombreInstalacion + " con fecha: " + fechaInstalacion);

                        // Obtener el ID de la instalación
                        String instalacionId = instalacionSnapshot.getKey();

                        if (instalacionId != null) {
                            liberarHorario(instalacionesRef, instalacionId, reserva.getNumero(), reserva.getHora());
                        }
                        return; // Salir del bucle una vez encontrada la instalación
                    }
                }

                Log.e("CancelarFragment", "No se encontró una instalación con el nombre y fecha: " +
                        reserva.getPista() + ", " + reserva.getFecha());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CancelarFragment", "Error al buscar la instalación", databaseError.toException());
            }
        });
    }

    private void liberarHorario(DatabaseReference instalacionesRef, String instalacionId, int pistaNumero, int horario) {
        Log.d("CancelarFragment", "Liberando pista en Instalaciones/" + instalacionId + "/pistas/" + pistaNumero + "/reservado/" + horario);

        // Actualizar el valor del horario a `false`
        instalacionesRef.child(instalacionId)
                .child("pistas")
                .child(String.valueOf(pistaNumero))
                .child("reservado")
                .child(String.valueOf(horario))
                .setValue(false)
                .addOnSuccessListener(aVoid -> {
                    Log.d("CancelarFragment", "Horario liberado correctamente");
                    openFragment(new PerfilFragment());
                })
                .addOnFailureListener(e -> {
                    Log.e("CancelarFragment", "Error al liberar el horario", e);
                });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null); // Permitir regresar al fragmento previo
        transaction.commit();
    }
}
