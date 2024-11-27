package es.ucm.fdi.pistaypato;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ModificarPerfilFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;
    private Button editar;
    private EditText nombre;
    private EditText apellido;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;

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
        this.editar = view.findViewById(R.id.modificar_edit);
        this.nombre = view.findViewById(R.id.modificar_name);
        this.apellido = view.findViewById(R.id.modificar_surname);
        this.email = view.findViewById(R.id.modificar_email);
        this.password = view.findViewById(R.id.modificar_password);
        this.repeatPassword = view.findViewById(R.id.modificar_repeat_password);

        editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().isEmpty() &&
                        password.getText().toString().equals(repeatPassword.getText().toString())){
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Error")
                            .setMessage("Passwords doesn't coincide")
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                            .show();
                }

                //CAMBIAR DATOS EN BASE DE DATOS

                openFragment(new PerfilFragment());
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
}