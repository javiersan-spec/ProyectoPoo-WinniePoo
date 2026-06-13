package modelo;

/**
 * Pasaje que se vende a un pasajero para un viaje.
 * Tiene asiento, numero, y se asocia al pasajero, viaje y venta.
 * @author Benjamin Carrasco
 */
public class Pasaje {
    private int asiento;
    private long numero;
    private Pasajero pasajero;
    private Venta venta;
    private Viaje viaje;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.asiento = asiento;
        this.numero = System.currentTimeMillis() + (int)(Math.random() * 1000);
        this.pasajero = pasajero;
        this.venta = venta;
        this.viaje = viaje;

        // registro el pasaje en el viaje
        if (viaje != null) {
            viaje.addPasaje(this);
        }
        // registro el pasaje en el pasajero tambien
        if (pasajero != null) {
            pasajero.addPasaje(this);
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
