package utilidades;

import java.io.Serializable;
/**
 * @author Javier San Martín
 * @version Avance 2
 */
public class Pasaporte implements IdPersona, Serializable {
    private String numero;
    private String nacionalidad;

    private Pasaporte(String num, String nacionalidad) {
        this.numero = num;
        this.nacionalidad = nacionalidad;
    }

    public String getNumero() {
        return numero; }

    public String getNacionalidad() {
        return nacionalidad; }

    public static Pasaporte of(String num, String nacionalidad) {
        return new Pasaporte(num, nacionalidad);
    }

    @Override
    public String toString() {
        return this.numero + " " + this.nacionalidad;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Pasaporte pasaporte = (Pasaporte) otro;
        return numero.equals(pasaporte.numero) && nacionalidad.equals(pasaporte.nacionalidad);
    }
}

