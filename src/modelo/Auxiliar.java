package modelo;
/**
 * @author Benjamin Jara
 */

public class Auxiliar extends Tripulante {

    private int nroViajes;

    public Auxiliar(IdPersona id, Nombre nom, Direccion dir) {
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
