package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase que representa un Bus dentro del sistema.
 * @author Benjamin Jara
 */
public class Bus implements Serializable {
    private String patente;
    private String marca;
    private String modelo;
    private int nroAsientos;
    private Empresa empresa;
    private List<Viaje> viajes;

    public Bus(String patente, int nroAsientos, Empresa empresa) {
        this.patente = patente;
        this.nroAsientos = nroAsientos;
        this.empresa = empresa;
        this.viajes = new ArrayList<>();
    }
    public String getPatente() {
        return patente;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public int getNroAsientos() {
        return nroAsientos;
    }
    public Empresa getEmpresa() {
        return empresa;
    }
    public void addViaje(Viaje viaje) {
        if (viaje != null) {
            this.viajes.add(viaje);
        }
    }
    public Viaje[] getViajes() {
        return viajes.toArray(new Viaje[0]);
    }
}

