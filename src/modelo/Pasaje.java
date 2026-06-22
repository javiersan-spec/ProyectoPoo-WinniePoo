package modelo;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

/**
 * Pasaje que se vende a un pasajero para un viaje.
 * Tiene asiento, numero, y se asocia al pasajero, viaje y venta.
 * @author Benjamin Carrasco
 */
public class Pasaje implements Serializable {
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

    /**
     * Retorna un String con datos formateados del pasaje,
     * tal como se presenta en la Figura 3 del enunciado.
     */
    @Override
    public String toString() {
        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder sb = new StringBuilder();
        sb.append("--------------------- PASAJE ELECTRONICO ---------------------\n");

        String nombreEmpresa = viaje.getBus().getEmpresa().getNombre();
        sb.append(String.format("%-20s %s\n", "Nombre Empresa", "Numero de pasaje"));
        sb.append(String.format("%-20s %d\n", nombreEmpresa, numero));

        String nombrePasajero = pasajero.getNombreCompleto().toString();
        String idPasajero = pasajero.getIdPersona().toString();
        sb.append(String.format("%-35s %s\n", "Nombre Pasajero", "RUT/Pasaporte"));
        sb.append(String.format("%-35s %s\n", nombrePasajero, idPasajero));

        sb.append(String.format("%-15s %-10s %s\n", "Patente bus", "Asiento", "Valor Pagado"));
        sb.append(String.format("%-15s %-10d %d\n", viaje.getBus().getPatente(), asiento, viaje.getPrecio()));

        String termOrigen = viaje.getTerminalSalida().getNombre();
        String termDestino = viaje.getTerminalLlegada().getNombre();
        String fecha = viaje.getFecha().format(fechaFmt);
        String hora = viaje.getHora().format(horaFmt);

        sb.append(String.format("%-18s %-18s %-14s %s\n", "Terminal origen", "Terminal destino", "Fecha", "Hora"));
        sb.append(String.format("%-18s %-18s %-14s %s\n", termOrigen, termDestino, fecha, hora));

        sb.append("--------------------------------------------------------------");
        return sb.toString();
    }
}
