package es.ucm.fdi.pistaypato;

import android.util.Log;

public class Reserva {

    private int numero;
    private String pista;
    private String fecha;
    private int hora;
    private String ubicacion;
    private String idUsuario;
    private String id;

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getNumero() {
        return numero;
    }

    public Reserva(int numero, String pista, String fecha, int hora){
        this.numero = numero;
        this.pista = pista;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Reserva(String id,int numPista, String idUsuario, String pista, String fecha, int hora, String ubicacion){
        this.id = id;
        this.numero = numPista;
        this.idUsuario = idUsuario;
        this.pista = pista;
        this.fecha = fecha;
        this.hora = hora;
        this.ubicacion = ubicacion;
    }


    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

}
