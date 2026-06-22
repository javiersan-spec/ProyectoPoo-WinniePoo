package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;
import java.util.ArrayList;

/**
 * Pasajero del sistema, tiene contacto de emergencia
 * y una lista de los pasajes que ha comprado.
 * @author Benjamin Carrasco
 */
public class Pasajero extends Persona {

    private Nombre nomContacto;
    private String fonoContacto;
    // asociacion a pasajes, un pasajero puede tener muchos
    private ArrayList<Pasaje> pasajes;

    public Pasajero(IdPersona id, Nombre nom,
                    Nombre nomContacto, String fonoContacto) {
        super(id, nom);
        this.nomContacto = nomContacto;
        this.fonoContacto = fonoContacto;
        this.pasajes = new ArrayList<>();
    }

    public Nombre getNomContacto() { return nomContacto; }
    public String getFonoContacto() { return fonoContacto; }

    public void setNomContacto(Nombre nomContacto) { this.nomContacto = nomContacto; }
    public void setFonoContacto(String fonoContacto) { this.fonoContacto = fonoContacto; }

    // para agregar un pasaje al historial del pasajero
    public void addPasaje(Pasaje pasaje) {
        if (pasaje != null) {
            this.pasajes.add(pasaje);
        }
    }

    public Pasaje[] getPasajes() {
        return pasajes.toArray(new Pasaje[0]);
    }
}
