package es.ucm.fdi.pistaypato;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;

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
        this.app = (PPAplication) requireActivity().getApplication();
        this.user = this.app.getPropietario();
        this.nombre = view.findViewById(R.id.perfil_name);
        this.email = view.findViewById(R.id.perfil_email);
        String sfirstName = user.getFirstName() != null ? user.getFirstName() : "";
        String slastName = user.getLastName() != null ? user.getLastName() : "";
        String semail = user.getEmail() != null ? user.getEmail() : "";
        this.nombre.setText(sfirstName + " " + slastName);
        this.email.setText(semail);

        this.editar = view.findViewById(R.id.perfil_edit_button);
        this.consultar = view.findViewById(R.id.perfil_consult_button);

        this.volver = getActivity().findViewById(R.id.volver);
        this.volver.setVisibility(View.VISIBLE);
        this.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = getActivity().findViewById(R.id.middle_section);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.middle_section, new BusquedaActivity());
                transaction.addToBackStack(null);
                transaction.commit();
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

}