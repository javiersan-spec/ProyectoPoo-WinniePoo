package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

public class Tripulante extends Persona {

    private Direccion direccion;

    public Tripulante(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom);
        this.direccion = dir;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void addViaje(Viaje viaje) {

    }

    public int getNroViajes() {
        return 0;
    }
}