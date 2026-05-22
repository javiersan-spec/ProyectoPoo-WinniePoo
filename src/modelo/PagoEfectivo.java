package modelo;

public class PagoEfectivo extends Pago {

    public PagoEfectivo(int monto) {
        super(monto);
    }

    @Override
    public String toString() {
        return "Pago Efectivo | Monto: $" + getMonto();
    }
}
