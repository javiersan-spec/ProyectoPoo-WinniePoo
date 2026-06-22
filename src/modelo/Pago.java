package modelo;

import java.io.Serializable;

/**
 * @author Genesis Castro
 * Avance 3
 */
public class Pago implements Serializable {

    private int monto;

    public Pago(int monto) {
        this.monto = monto;
    }

    public int getMonto() {
        return monto;
    }
}




