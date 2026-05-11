package modelo;
import modelo.Persona;
import modelo.Viaje;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

import java.util.ArrayList;

public abstract class Tripulante extends Persona {

    private Direccion direccion;
    private ArrayList<Viaje> viajes;

    public Tripulante(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom);
        this.direccion = dir;
        viajes = new ArrayList<>();
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addViaje(Viaje viaje) {
        if (!viajes.contains(viaje)) {
            viajes.add(viaje);
        }
    }

    public int getNroViajes() {
        return viajes.size();
    }
}