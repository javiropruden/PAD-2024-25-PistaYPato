package es.ucm.fdi.pistaypato;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.List;
import java.util.Objects;

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
    Solitario selecionado;

    PPAplication app;
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
            mParam2 = getArguments().getString(ARG_PARAM2); // Recuperamos "lugar"
        }
    }

    public ListarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar, container, false);
        app = (PPAplication) requireActivity().getApplication();
        lugarText = view.findViewById(R.id.lugarListar);
        diaText = view.findViewById(R.id.dia_sol);
        lugarText.setText(this.mParam2);
        lugarText.setEnabled(false);

        diaText.setText(this.mParam1);
        diaText.setEnabled(false);

        recyclerView = view.findViewById(R.id.listarSol);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        solitarioList = new ArrayList<>();
        adapter = new SolitarioAdapter(solitarioList, new SolitarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Solitario solitario) {
                selecionado = solitario;
            }
        });

        recyclerView.setAdapter(adapter);


        databaseReference = app.getSolitariosReference();
        this.anadir = view.findViewById(R.id.anadir);

        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selecionado != null){
                    DatabaseReference sol = databaseReference.child(selecionado.getId());
                    sol.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<User> perfiles = (List<User>) snapshot.getValue() ;

                            if (perfiles == null) {
                                perfiles = new ArrayList<>();
                            }
                            perfiles.add(app.getPropietario());

                            // Guardar la lista actualizada
                            List<User> finalPerfiles = perfiles;

                            sol.child("usuarios").setValue(perfiles)
                                    .addOnSuccessListener(aVoid ->{

                                        List<String> emails = new ArrayList<>();
                                        String emailes = "";
                                       for(DataSnapshot sna: snapshot.getChildren()){
                                           User user = sna.getValue(User.class);
                                           if (user != null) {
                                               emails.add(user.getEmail());
                                               emailes = emailes + user.getEmail() + " , ";
                                           }

                                       }
                                       emailes = emailes + app.getPropietario().getEmail();
                                       
                                        String mensaje = "<p>Buenas, jugador:</p>" +
                                                "<p>Usted se añadió en el siguiente solitario:</p>" +
                                                "<ul>" +
                                                "<li><strong>Pista:</strong> " + selecionado.getLugar() + "</li>" +
                                                "<li><strong>Día:</strong> " + selecionado.getFecha() + "</li>" +
                                                "</ul>" +
                                                "<p><strong>Email de los otros jugadores:</strong><br>" +
                                                emailes.replace(", ", "<br>") + "</p>" +
                                                "<p>Un saludo,<br> Pista y pato </p>";

                                        String asunto = "Información sobre su partida de solitario";

                                       for(String uno: emails) {
                                           Log.d("ANADIR", "Correo enviado a " + uno);
                                           app.escribirEmail(uno, asunto, mensaje);
                                       }
                                        app.escribirEmail(app.getPropietario().getEmail(), asunto,mensaje);
                                        Log.d("ANADIR", "Correo enviado a " + app.getPropietario().getEmail());

                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        MensagesFragment panelMensaje;
                                        panelMensaje = MensagesFragment.newInstance("TRUE", "ANADIR");
                                        transaction.replace(R.id.middle_section, panelMensaje);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        Log.d("Firebase", "Perfil agregado correctamente");
                                    } )
                                    .addOnFailureListener(e -> {
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        MensagesFragment panelMensaje;
                                        panelMensaje = MensagesFragment.newInstance("FALSE", "ANADIR");
                                        transaction.replace(R.id.middle_section, panelMensaje);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        Log.e("Firebase", "Error al agregar perfil", e);
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Error al leer perfiles", error.toException());
                        }
                    });



                }

            }
        });




        cargarDatos();

        return view;
    }

    private void cargarDatos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                solitarioList.clear();
                for (DataSnapshot solitarioSnapshot : snapshot.getChildren()) {
                    String nombre = solitarioSnapshot.child("lugar").getValue(String.class);
                    long cantidadPerfiles = solitarioSnapshot.child("usuarios").getChildrenCount();
                    String fecha = solitarioSnapshot.child("fecha").getValue(String.class);
                    String id = solitarioSnapshot.getKey();

                    if(Objects.equals(nombre, mParam2) && !Objects.equals(mParam2, "TODOS")
                        && Objects.equals(fecha, mParam1) && !Objects.equals(mParam1, "TODOS" )) {

                        solitarioList.add(new Solitario(id,nombre, (int) cantidadPerfiles, fecha));
                    }
                    else if(Objects.equals(mParam2, "TODOS") && Objects.equals(mParam1, "TODOS") ){
                        solitarioList.add(new Solitario(id,nombre, (int) cantidadPerfiles, fecha));
                    }
                    else if(Objects.equals(mParam2, "TODOS")  && !Objects.equals(mParam1, "TODOS")
                    && Objects.equals(fecha, mParam1)){
                        solitarioList.add(new Solitario(id,nombre, (int) cantidadPerfiles, fecha));
                    }
                    else if(!Objects.equals(mParam2, "TODOS")  && Objects.equals(mParam1, "TODOS")
                    && Objects.equals(nombre, mParam2)){
                        solitarioList.add(new Solitario(id,nombre, (int) cantidadPerfiles, fecha));
                    }
                }
                adapter.updateData(solitarioList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al cargar los datos", error.toException());
            }
        });
    }


}