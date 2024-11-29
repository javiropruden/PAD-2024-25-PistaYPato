package es.ucm.fdi.pistaypato;

import java.util.List;

import es.ucm.fdi.pistaypato.Pista;

public class Instalacion {
    private String id;
    private String nombre;
    private List<Pista> pistas; // Lista de pistas asociadas a la instalación

    // Constructor vacío requerido por Firebase
    public Instalacion() {
    }

    // Constructor principal
    public Instalacion(String nombre, List<Pista> pistas) {
        this.nombre = nombre;
        this.pistas = pistas;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pista> getPistas() {
        return pistas;
    }

    public void setPistas(List<Pista> pistas) {
        this.pistas = pistas;
    }

}
