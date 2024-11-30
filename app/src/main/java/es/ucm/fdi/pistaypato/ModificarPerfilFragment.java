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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ModificarPerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private PPAplication app;
    private View view;
    private Button editar;
    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private ImageButton volver;

    private User usuario;
    private DatabaseReference db;
    private SolitarioAdapter adapter;
    private List<Solitario> solitarioList;
    private Solitario seleccionado;

    private String emailOriginal;

    public ModificarPerfilFragment() {
        // Required empty public constructor
    }

    public static ModificarPerfilFragment newInstance(String param1, String param2) {
        ModificarPerfilFragment fragment = new ModificarPerfilFragment();
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
        this.view = inflater.inflate(R.layout.fragment_modificar_perfil, container, false);
        try{
            this.app = (PPAplication) requireActivity().getApplication();
            this.usuario = this.app.getPropietario();
        }
        catch(Exception e){
            showErrorMessage("Error", "Error en la carga del usuario");
        }
        if(this.usuario == null) this.usuario = new User("a", "a", "a", "a");
        this.editar = view.findViewById(R.id.modificar_edit);
        this.nombre = view.findViewById(R.id.modificar_name);
        this.apellido = view.findViewById(R.id.modificar_surname);
        this.email = view.findViewById(R.id.modificar_email);
        this.password = view.findViewById(R.id.modificar_password);
        this.repeatPassword = view.findViewById(R.id.modificar_repeat_password);
        this.volver = getActivity().findViewById(R.id.volver);

        this.emailOriginal = this.usuario.getEmail(); //Para guardarlo por si se cambia

        this.solitarioList = new ArrayList<>();
        this.adapter = new SolitarioAdapter(solitarioList, new SolitarioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Solitario solitario) {
                seleccionado = solitario;
            }
        });
        this.db = app.getSolitariosReference();

        this.volver.setVisibility(View.VISIBLE);
        this.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new PerfilFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        this.editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().isEmpty() &&
                        !password.getText().toString().equals(repeatPassword.getText().toString())){
                    showErrorMessage("Error", "Passwords don't match");
                }
                else{
                    if(!nombre.getText().toString().isEmpty()){
                        usuario.setFirstName(nombre.getText().toString());
                    }
                    if(!apellido.getText().toString().isEmpty()){
                        usuario.setLastName(apellido.getText().toString());
                    }
                    if(!email.getText().toString().isEmpty()){
                        usuario.setEmail(email.getText().toString());
                    }
                    if(!password.getText().toString().isEmpty() && !repeatPassword.getText().toString().isEmpty() &&
                    password.getText().toString().equals(repeatPassword.getText().toString())){
                        usuario.setPassword(password.getText().toString());
                    }

                    app.removeUser(emailOriginal);
                    app.addUser(usuario);
                    openFragment(new PerfilFragment());
                }
            }
        });

        return view;
    }

    private void showErrorMessage(String title, String message){
        new AlertDialog.Builder(view.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_section, fragment);
        transaction.addToBackStack(null); // Permitir regresar al fragmento previo
        transaction.commit();
    }
}