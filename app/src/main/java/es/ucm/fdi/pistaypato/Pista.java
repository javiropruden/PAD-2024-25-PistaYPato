package es.ucm.fdi.pistaypato;

import java.util.ArrayList;

public class Pista {
    private ArrayList<Boolean> reservado;

    public Pista() {
        this.reservado = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            reservado.add(false);
        }
    }

    public ArrayList<Boolean> getReservado() {
        return reservado;
    }

    public void setReservado(ArrayList<Boolean> reservado) {
        this.reservado = reservado;
    }
}
