package es.ucm.fdi.pistaypato;

import java.util.Date;
import java.util.List;

public class Solitario {
    String lugar;
    int numPersonas;
    String fecha;
    List<User> usuarios;
    String id;

    public String getId() {
        return id;
    }

    public void addUsuario(User Otro){
        this.usuarios.add(Otro);
    }
    public void setId(String id) {
        this.id = id;
    }

    public Solitario() {
        // Constructor vacío requerido por Firebase
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Solitario(String id, String lugar, List<User> nombres, String fecha) {
        this.id = id;
        this.lugar = lugar;
        this.numPersonas = nombres.size();
        this.usuarios = nombres;
        this.fecha = fecha;
    }

    public Solitario(String id, String lugar, int cantidadPerfiles, String fecha) {
        this.lugar = lugar;
        this.id = id;
        this.numPersonas = cantidadPerfiles;
        this.fecha = fecha;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
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
