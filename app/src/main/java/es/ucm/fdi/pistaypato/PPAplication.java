package es.ucm.fdi.pistaypato;

import android.app.Application;

import java.util.ArrayList;

public class PPAplication extends Application {
    public ArrayList<String> badmintonFields;

    @Override
    public void onCreate() {
        super.onCreate();
        badmintonFields = new ArrayList<>();
        //la base de datos se deberia inicializar aqui
        //FirebaseApp.initializeApp(this);
    }

    public ArrayList<String> getFiels(){
        return badmintonFields;
    }
}

