package utilidades;
import java.io.Serializable;
/**
 * @author Beatriz Aguilera
 * @version Avance 3
 */
public class Direccion implements Serializable {

    private String calle;
    private int numero;
    private String comuna;

    public Direccion (String calle, int numero, String comuna) {
        this.calle = calle;
        this.numero = numero;
        this.comuna = comuna;
    }

    public String getCalle() { return calle; }
    public int getNumero() { return numero; }
    public String getComuna() { return comuna; }

    @Override
    public String toString() {
        return calle + " " + numero + ", " + comuna;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Direccion d = (Direccion) otro;
        return numero == d.numero
                && calle.equals(d.calle)
                && comuna.equals(d.comuna);
    }
}

