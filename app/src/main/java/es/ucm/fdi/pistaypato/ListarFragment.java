package es.ucm.fdi.pistaypato;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button anadir;
    EditText lugarText, diaText;

    public ListarFragment() {

    }

    private RecyclerView recyclerView;
    private SolitarioAdapter adapter;
    private List<Solitario> solitarioList;
    private DatabaseReference databaseReference;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dia Parameter 1.
     * @param lugar Parameter 2.
     * @return A new instance of fragment ListarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarFragment newInstance(String dia, String lugar) {
        ListarFragment fragment = new ListarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, dia);
        args.putString(ARG_PARAM2, lugar);
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_listar, container, false);

        this.anadir = view.findViewById(R.id.anadir);

        this.lugarText = view.findViewById(R.id.lugarListar);
        this.diaText = view.findViewById(R.id.diaListar);

        lugarText.setEnabled(false);
        lugarText.setText(this.mParam2);

        diaText.setEnabled(false);
        diaText.setText(this.mParam1);

        recyclerView = view.findViewById(R.id.listarSol); // Asegúrate de usar el ID correcto
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        solitarioList = new ArrayList<>();
        adapter = new SolitarioAdapter(solitarioList);
        recyclerView.setAdapter(adapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pistaypato-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference("Solitario");

        this.cargarDatos();
        return view;
    }


    private void agregarPerfil(String idSolitario, String nombre, int puntuacion) {
        // Referencia al nodo "perfiles" dentro de un "Solitario" específico
        DatabaseReference perfilesRef = FirebaseDatabase.getInstance()
                .getReference("Solitario")
                .child(idSolitario) // Nodo específico, como "solitario1"
                .child("perfiles");

        // Crear un nuevo perfil
        Map<String, Object> nuevoPerfil = new HashMap<>();
        nuevoPerfil.put("nombre", nombre);
        nuevoPerfil.put("puntuacion", puntuacion);

        // Agregar el perfil con un ID único
        perfilesRef.push().setValue(nuevoPerfil)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Perfil agregado correctamente");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error al agregar el perfil", e);
                });
    }


    private void cargarDatos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                solitarioList.clear();
                for (DataSnapshot solitarioSnapshot : snapshot.getChildren()) {
                    String nombre = solitarioSnapshot.child("lugar").getValue(String.class);
                    long cantidadPerfiles = solitarioSnapshot.child("perfiles").getChildrenCount();

                    solitarioList.add(new Solitario(nombre, (int) cantidadPerfiles));
                }

                Log.d("Firebase", String.valueOf(solitarioList.size() ));
                adapter.updateData(solitarioList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al cargar los datos", error.toException());
            }
        });
    }

}