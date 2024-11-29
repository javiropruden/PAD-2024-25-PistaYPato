package es.ucm.fdi.pistaypato;

public class Reserva {

    private int numero;
    private String pista;
    private String fecha;
    private int hora;

    public Reserva(int numero, String pista, String fecha, int hora){
        this.numero = numero;
        this.pista = pista;
        this.fecha = fecha;
        this.hora = hora;
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
