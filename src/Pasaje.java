/**
 * Clase encargada de facilitar la creacion del pasaje del cliente
 * @author Benjamin Carrasco
 */
public class Pasaje {
    private long numero;
    private int asiento;
    private Viaje viaje;
    private Pasajero pasajero;
    private Venta venta;

    public Pasaje(int asiento, Viaje viaje, Pasajero pasajero, Venta venta) {
        this.asiento = asiento;
        this.viaje = viaje;
        this.pasajero = pasajero;
        this.venta = venta;

        // Generación de número aleatorio usando Math.random()
        this.numero = System.currentTimeMillis() + (int)(Math.random() * 100000);

        if (this.viaje != null) {
            this.viaje.addPasaje(this);
        }
    }

    public int getNumero() {
        return (int) Math.abs(this.numero % Integer.MAX_VALUE);
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