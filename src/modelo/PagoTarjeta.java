package modelo;
/**
 * Clase que representa un pago realizado con targeta dentro del sistema de venta de pasajes.
 * @author Beatriz Aguilera
 */
public class PagoTarjeta extends Pago {

    private long nroTarjeta;

    public PagoTarjeta(int monto, long nroTarjeta) {
        super(monto);
        this.nroTarjeta = nroTarjeta;
    }

    public long getNroTarjeta() { return nroTarjeta; }
}



