package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private PPAplication app;
    private View view;
    private Button editar;
    private Button consultar;
    private ImageButton volver;
    private User user;

    private TextView nombre;
    private TextView email;

    private TableLayout tablaReservas;

    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_perfil, container, false);
        try{
            this.app = (PPAplication) requireActivity().getApplication();
            this.user = this.app.getPropietario();
        }
        catch(Exception e) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Error")
                    .setMessage("Error en la carga del usuario")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
        }
        if(this.user == null) this.user = new User("a","a","a","a");
        this.nombre = view.findViewById(R.id.perfil_name);
        this.email = view.findViewById(R.id.perfil_email);
        String sfirstName = user.getFirstName() != null ? user.getFirstName() : "";
        String slastName = user.getLastName() != null ? user.getLastName() : "";
        String semail = user.getEmail() != null ? user.getEmail() : "";
        this.nombre.setText(sfirstName + " " + slastName);
        this.email.setText(semail);

        this.editar = view.findViewById(R.id.perfil_edit_button);
        this.consultar = view.findViewById(R.id.perfil_consult_button);
        this.tablaReservas = view.findViewById(R.id.perfil_tabla_reservas);
        cargarReservas();

        this.volver = getActivity().findViewById(R.id.volver);
        this.volver.setVisibility(View.VISIBLE);
        this.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new BusquedaFragment());
            }
        });
        this.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new PasswordFragment());
            }
        });
        this.consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new CancelarFragment());
            }
        });

        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null); // Permitir regresar al fragmento previo
        transaction.commit();
    }

    private void cargarReservas() {
        app.getSolitariosReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tablaReservas.removeAllViews(); // Limpia la tabla antes de rellenarla

                    // Iterar sobre los elementos de la colección "Solitarios"
                    for (DataSnapshot reservaSnapshot : dataSnapshot.getChildren()) {
                        // Obtener los detalles de la reserva (lugar y fecha)
                        String lugar = reservaSnapshot.child("lugar").getValue(String.class);
                        String fecha = reservaSnapshot.child("fecha").getValue(String.class);

                        // Verificar si el usuario está asociado con esta reserva
                        if (reservaSnapshot.hasChild("usuarios")) {
                            // Iterar sobre los usuarios en la colección "Users"
                            for (DataSnapshot userSnapshot : reservaSnapshot.child("usuarios").getChildren()) {
                                String emailUsuario = userSnapshot.child("email").getValue(String.class);

                                // Sanitizar el email del usuario actual (reemplazar los puntos por comas)
                                String sanitizedCurrentUserEmail = user.getEmail().replace(".", ",");

                                // Si el email del usuario coincide con el email del usuario actual
                                if (emailUsuario != null && emailUsuario.equals(sanitizedCurrentUserEmail)) {
                                    // Crear una nueva fila para la tabla
                                    TableRow fila = new TableRow(getContext());
                                    fila.setPadding(8, 8, 8, 8);

                                    // Asegurarse de que la fila no sea interactiva
                                    fila.setClickable(false);
                                    fila.setFocusable(false);
                                    fila.setEnabled(false);

                                    // Crear las vistas de texto para cada campo
                                    TextView lugarView = new TextView(getContext());
                                    lugarView.setText(lugar != null ? lugar : "Desconocido");
                                    lugarView.setPadding(8, 8, 8, 8);
                                    lugarView.setFocusable(false); // No editable
                                    lugarView.setClickable(false); // No clickeable

                                    TextView fechaView = new TextView(getContext());
                                    fechaView.setText(fecha != null ? fecha : "Sin fecha");
                                    fechaView.setPadding(8, 8, 8, 8);
                                    fechaView.setFocusable(false); // No editable
                                    fechaView.setClickable(false); // No clickeable

                                    // Agregar las vistas a la fila
                                    fila.addView(lugarView);
                                    fila.addView(fechaView);

                                    // Agregar la fila a la tabla
                                    tablaReservas.addView(fila);
                                }
                            }
                        }
                    }
                } else {
                    Log.d("PerfilFragment", "No hay reservas disponibles.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al cargar reservas: " + databaseError.getMessage());
            }
        });
    }
}