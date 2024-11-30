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

                    for (DataSnapshot solitarioSnapshot : dataSnapshot.getChildren()) {
                        if (solitarioSnapshot.hasChild("usuarios")) {
                            for (DataSnapshot usuarioSnapshot : solitarioSnapshot.child("usuarios").getChildren()) {
                                User usuario = usuarioSnapshot.getValue(User.class);

                                if (usuario != null && usuario.getEmail().equals(user.getEmail())) {
                                    String lugar = solitarioSnapshot.child("lugar").getValue(String.class);
                                    String fecha = solitarioSnapshot.child("fecha").getValue(String.class);

                                    TableRow fila = new TableRow(getContext());
                                    fila.setPadding(8, 8, 8, 8);

                                    TextView lugarView = new TextView(getContext());
                                    lugarView.setText(lugar != null ? lugar : "Desconocido");
                                    lugarView.setPadding(8, 8, 8, 8);

                                    TextView fechaView = new TextView(getContext());
                                    fechaView.setText(fecha != null ? fecha : "Sin fecha");
                                    fechaView.setPadding(8, 8, 8, 8);

                                    fila.addView(lugarView);
                                    fila.addView(fechaView);

                                    tablaReservas.addView(fila);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al cargar reservas: " + databaseError.getMessage());
            }
        });
    }


}