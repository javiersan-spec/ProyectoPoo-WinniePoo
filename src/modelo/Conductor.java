package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;

/**
 * Conductor del bus, hereda de Tripulante.
 * Almacena los viajes que ha conducido.
 * @author Benjamin Jara
 */
public class Conductor extends Tripulante {

    public Conductor(IdPersona id, Nombre nom, Direccion dir) {
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
