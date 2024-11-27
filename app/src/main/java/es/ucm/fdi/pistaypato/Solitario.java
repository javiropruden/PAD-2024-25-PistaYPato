package es.ucm.fdi.pistaypato;

public class Solitario {
    String lugar;
    int numPersonas;

    public Solitario() {
        // Constructor vacío requerido por Firebase
    }

    public Solitario(String nombre, int cantidadPerfiles) {
        this.lugar = nombre;
        this.numPersonas = cantidadPerfiles;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
