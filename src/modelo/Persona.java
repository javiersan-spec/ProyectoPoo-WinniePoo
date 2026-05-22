package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

/**
 * Clase abstracta que representa a una persona dentro del sistema de venta de pasajes.
 * Contiene los datos basicos comunes de todo tipo de persona:
 * Indentificador, nombre completo y telefono.
 *  @author Beatriz Aguilera
 */
public abstract class Persona {

    private IdPersona idPersona;
    private Nombre nombreCompleto;
    private String telefono;

    public Persona(IdPersona id, Nombre nombre) {
        this.idPersona = id;
        this.nombreCompleto = nombre;
    }

    public IdPersona getIdPersona() { return idPersona; }
    public Nombre getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }

    public void setNombreCompleto(Nombre nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "[" + idPersona.toString() + "] "
                + nombreCompleto.toString()
                + " - Tel: " + (telefono != null ? telefono : "sin registrar");
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Persona p = (Persona) otro;
        return idPersona.equals(p.idPersona);
    }
}
