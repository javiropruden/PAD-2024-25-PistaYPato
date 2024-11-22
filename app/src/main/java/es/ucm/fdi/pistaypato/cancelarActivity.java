package es.ucm.fdi.pistaypato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.List;

public class cancelarActivity extends Fragment {

    private Spinner spinner;
    private List<String> badmintonFields;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cancelar, container, false);
    }
}