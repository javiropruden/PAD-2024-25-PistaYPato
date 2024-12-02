package es.ucm.fdi.pistaypato;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Objects;

public class MensagesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MensagesFragment() {
        // Required empty public constructor
    }

    public static MensagesFragment newInstance(String param1, String param2) {
        MensagesFragment fragment = new MensagesFragment();
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
    EditText mensaje;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensages, container, false);
        this.mensaje = view.findViewById(R.id.messageText);

        if(Objects.equals(mParam1, "TRUE") && Objects.equals(mParam2, "CREAR") ){
            this.mensaje.setText("Se creo correctamente");
        }
        else if(Objects.equals(mParam1, "TRUE") && Objects.equals(mParam2, "ANADIR")){
            this.mensaje.setText("Se añadio correctamente en la lista");
        }
        else {
            this.mensaje.setText("Intentalo mas tarde");
        }

        return view;
    }
}