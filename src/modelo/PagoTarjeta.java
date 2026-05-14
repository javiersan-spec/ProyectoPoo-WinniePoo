package modelo;

/**
 * Clase que representa un pago realizado con targeta dentro del
 * Sistema de venta de pasajes; Hereda el monto de la clase pago
 * y agrega el numero de tarjeta utilizado en la trasaccion.
 * @author Beatriz Aguilera
 */
public class PagoTarjeta extends Pago {

    private long nroTarjeta;

    public PagoTarjeta(int monto, long nroTarjeta) {
        super(monto);
        this.nroTarjeta = nroTarjeta;
    }

    public long getNroTarjeta() { return nroTarjeta; }

    @Override
    public String toString() {
        return "Pago Tarjeta | Monto: $" + getMonto()
                + " | Nro. Tarjeta: "   + nroTarjeta;
    }
}

