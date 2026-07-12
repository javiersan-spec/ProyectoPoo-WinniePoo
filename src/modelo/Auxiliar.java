package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Direccion;

/**
 * Auxiliar del bus, hereda de Tripulante.
 * Guarda los viajes en los que participo como auxiliar.
 * @author Benjamin Jara
 */
public class Auxiliar extends Tripulante {

    public Auxiliar(IdPersona id, Nombre nom, Direccion dir) {
        super(id, nom, dir);
    }

    @Override
    public void addViaje(Viaje viaje) {
        if (viaje != null) {
            this.viajes.add(viaje);
        }
    }

    @Override
    public int getNroViajes() {
        return this.viajes.size();
    }
}

