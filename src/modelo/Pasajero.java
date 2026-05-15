package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

/**
 * Clase encargada de definir a un pasajero dentro del sistema de venta de pasajes.
 * Hereda los datos basicos de Persona y agrega la informacion del contacto de emergencia,
 * es decir el nombre y el telefono de contacto.
 * @author Beatriz Aguilera
 */
public class Pasajero extends Persona {

    private Nombre nomContacto;
    private String fonoContacto;

    public Pasajero(IdPersona id, Nombre nom,
                    Nombre nomContacto, String fonoContacto) {
        super(id, nom);
        this.nomContacto = nomContacto;
        this.fonoContacto = fonoContacto;
    }

    public Nombre getNomContacto() { return nomContacto; }
    public String getFonoContacto() { return fonoContacto; }

    public void setNomContacto(Nombre nomContacto) { this.nomContacto = nomContacto; }
    public void setFonoContacto(String fonoContacto) { this.fonoContacto = fonoContacto; }

    @Override
    public String toString() {
        return super.toString()
                + " | Contacto: " + nomContacto.toString()
                + " - Fono: " + fonoContacto;
    }
}
