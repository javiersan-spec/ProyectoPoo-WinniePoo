package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

/**
 * @author Benjamin Jara
 */
public class Conductor extends Tripulante {

    private int nroViajes;

    public Conductor(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
        this.nroViajes = 0;
    }

    @Override
    public void addViaje(Viaje viaje) {
        if (viaje != null) {
            this.nroViajes++;
        }
    }

    @Override
    public int getNroViajes() {
        return this.nroViajes;
    }
}
