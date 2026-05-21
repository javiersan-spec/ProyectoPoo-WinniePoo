package modelo;

public class Pasaje {
    private int asiento;
    private long numero;
    private Pasajero pasajero;
    private Venta venta;
    private Viaje viaje;

    public Pasaje(int Asiento, Pasajero pasajero, Venta venta, Viaje viaje) {
        this.asiento=Asiento;
        this.numero = System.currentTimeMillis() + (int)(Math.random() * 1000);
        this.pasajero = pasajero;
        this.venta = venta;
        this.viaje = viaje;

        if (viaje != null) {
            viaje.addPasaje(this);
        }
    }

    public int getNumero() {
        return Math.toIntExact(numero);
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Venta getVenta() {
        return venta;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public int getAsiento() {
        return asiento;
    }
}