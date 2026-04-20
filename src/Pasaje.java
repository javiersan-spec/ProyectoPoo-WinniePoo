public class Pasaje {
    private long numero;
    private int asiento;
    private Viaje viaje;
    private Venta venta;
    private Pasajero pasajero;
public Pasaje(long numero, Pasajero pasajero, int asiento) {
    this.numero = numero;
    this.asiento = asiento;
    this.pasajero = pasajero;
    this.viaje = viaje;
    this.venta = venta;
}

    public int getNumero() {
        return numero;
    }

    public int getAsiento() {
        return asiento;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Venta getVenta() {
        return venta;
    }
}