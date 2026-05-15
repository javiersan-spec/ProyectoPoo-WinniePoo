package modelo;

public class Pasaje {
    private int Asiento;
    private long Numero;
    private Pasajero pasajero;
    private Venta venta;
    private Viaje viaje;

    public Pasaje(int Asiento, Pasajero pasajero, Venta venta, Viaje viaje) {
        this.Asiento=Asiento;
        this.Numero = Numero;
        this.pasajero = pasajero;
        this.venta = venta;
        this.viaje = viaje;

        viaje.addPasaje(this);
    }

    public int getNumero() {
        return Math.toIntExact(Numero);
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
        return Asiento;
    }
}