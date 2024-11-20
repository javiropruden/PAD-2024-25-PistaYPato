package es.ucm.fdi.pistaypato;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Controller {
    private Context context;

    public Controller(Context context) {
        this.context = context;
    }

    public void uno(Fragment fragment){
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;

        }
    }

}
