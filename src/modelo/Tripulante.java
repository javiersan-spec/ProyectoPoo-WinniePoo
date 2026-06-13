package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import java.util.ArrayList;

/**
 * Clase abstracta que representa a un tripulante del bus.
 * @author Benjamin Carrasco
 */
public abstract class Tripulante extends Persona {

    private Direccion direccion;
    protected ArrayList<Viaje> viajes;

    public Tripulante(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom);
        this.direccion = dir;
        this.viajes = new ArrayList<>();
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    // estos metodos los tienen que implementar Conductor y Auxiliar
    public abstract void addViaje(Viaje viaje);
    public abstract int getNroViajes();
}
