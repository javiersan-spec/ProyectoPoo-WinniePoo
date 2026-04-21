import java.util.ArrayList;
import java.util.List;

public class Bus {
    private String patente;
    private String modelo;
    private int nroAsientos;
    private List<Viaje> viajes; // a terminal de bus 🚌

    public Bus(String patente, String modelo, int nroAsientos) {
        this.patente = patente;
        this.modelo = modelo;
        this.nroAsientos = nroAsientos;
        this.viajes = new ArrayList<>();
    }
    public String getPatente() {
        return patente;
    }
    public String getMarca() {
        return modelo; //revisar 🔍
    }
    public void setMarca(String marca) {
        this.modelo = marca; //revisar 🔍
    }
    public String getModelo() {
        return modelo; //🔍
    }
    public void setModelo(String modelo) {
        this.modelo = modelo; //🔍
    }
    public int getNroAsientos() {
        return nroAsientos;
    }
    public void addViaje(Viaje viajes){
        this.viajes.add(viajes);
    }
}


