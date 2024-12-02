package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Reserva selecionado;

    private TextView nombre;
    private TextView email;
    private ReservaAdapter adapter;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private List<Reserva> reservaList;
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
        reservaList = new ArrayList<>();
        this.databaseReference = app.getReservasReference();
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
        this.recyclerView = view.findViewById(R.id.perfil_recycler_reservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter = new ReservaAdapter(reservaList, new ReservaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Reserva reserva) {
                selecionado = reserva;
            }
        });

        recyclerView.setAdapter(adapter);

        cargarDatos();

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

                if(selecionado != null){
                    openFragment(new CancelarFragment(selecionado));
                }
                else{
                    Toast.makeText(view.getContext(), "No se ha seleccionado ninguna reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void cargarDatos() {
        {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservaList.clear();

                for (DataSnapshot reservaSnapshot : snapshot.getChildren()) {
                    String nombre = reservaSnapshot.child("pista").getValue(String.class);
                    int hora = reservaSnapshot.child("hora").getValue(Integer.class);
                    String fecha = reservaSnapshot.child("fecha").getValue(String.class);
                    String id = reservaSnapshot.getKey();
                    String idUsuario = reservaSnapshot.child("idUsuario").getValue(String.class);
                    int numero = reservaSnapshot.child("numero").getValue(int.class);
                    String ubicacion = reservaSnapshot.child("ubicacion").getValue(String.class);

                    if(Objects.equals(idUsuario, user.getEmail())){
                        reservaList.add(new Reserva(id,numero, idUsuario,nombre, fecha,(int) hora, ubicacion));
                    }

                }
                adapter.updateData(reservaList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al cargar los datos", error.toException());
            }
        });
    }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null); // Permitir regresar al fragmento previo
        transaction.commit();
    }

}