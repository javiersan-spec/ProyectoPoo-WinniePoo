package modelo;
/**
 * @author Benjamin Jara
 */

import utilidades.Direccion;
import java.util.ArrayList;
import java.util.List;

public class Terminal {
    private String nombre;
    private Direccion direccion;
    private List<Viaje> llegadas;
    private List<Viaje> salidas;

    public Terminal(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.llegadas = new ArrayList<>();
        this.salidas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addLlegada(Viaje viaje) {
        if (viaje != null) {
            this.llegadas.add(viaje);
        }
    }

    public void addSalida(Viaje viaje) {
        if (viaje != null) {
            this.salidas.add(viaje);
        }
    }

    public Viaje[] getLlegadas() {
        return llegadas.toArray(new Viaje[0]);
    }

    public Viaje[] getSalidas() {
        return salidas.toArray(new Viaje[0]);
    }
}

