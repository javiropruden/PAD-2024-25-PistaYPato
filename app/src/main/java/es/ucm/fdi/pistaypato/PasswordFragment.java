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

public class PasswordFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;
    private Button continuar;
    private EditText password;
    private EditText repeatPassword;

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
        this.continuar = view.findViewById(R.id.password_continuar);
        this.password = view.findViewById(R.id.password_password);
        this.repeatPassword = view.findViewById(R.id.password_repeat_password);

        this.continuar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               if(password.getText().toString().isEmpty() || repeatPassword.getText().toString().isEmpty()){
                   new AlertDialog.Builder(view.getContext())
                           .setTitle("Error")
                           .setMessage("Empty field")
                           .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                           .show();
               }
               else if(!password.getText().toString().equals(repeatPassword.getText().toString())){
                   //CONTRASEÑAS NO COINCIDEN
                   new AlertDialog.Builder(view.getContext())
                           .setTitle("Error")
                           .setMessage("Passwords doesn't coincide")
                           .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                           .show();
               }
               else{
                   //CONTRASEÑAS INTRODUCIDAS CORRECTAMENTE, COMPROBAR EN BASE DE DATOS SI ES CORRECTA
                   openFragment(new ModificarPerfilFragment());
               }
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