package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class PasswordFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private PPAplication app;
    private View view;
    private Button continuar;
    private EditText password;
    private ImageButton volver;

    private User usuario;

    public PasswordFragment() {
        // Required empty public constructor
    }


    public static PasswordFragment newInstance(String param1, String param2) {
        PasswordFragment fragment = new PasswordFragment();
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
        this.view = inflater.inflate(R.layout.fragment_password, container, false);
        try{
            this.app = (PPAplication) requireActivity().getApplication();
            this.usuario = this.app.getPropietario();
        }
        catch(NullPointerException e){
            showErrorMessage("Error", "Error en la carga del usuario");
        }
        if(this.usuario == null) this.usuario = new User("a", "a", "a", "a");
        this.continuar = view.findViewById(R.id.password_continue);
        this.password = view.findViewById(R.id.password_password);
        this.volver = getActivity().findViewById(R.id.volver);

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
        this.continuar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(password.getText().toString().isEmpty()){
                    showErrorMessage("Error", "Introduce your password");
                }
                else{
                    // Llama a checkPassword con el callback
                    app.checkPassword(usuario.getEmail().trim(), app.hashPassword(password.getText().toString().trim()), isValid -> {
                        if (isValid) {
                            // Contraseña correcta: abre el fragmento de modificar perfil
                            openFragment(new ModificarPerfilFragment());
                        } else {
                            // Contraseña incorrecta: muestra un mensaje de error
                            showErrorMessage("Error", "Incorrect password");
                        }
                    });
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