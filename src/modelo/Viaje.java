package modelo;
/**
 * @author Benjamin Carrasco
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Viaje {
    private LocalDateTime fechaHoraSalida;
    private int precio;
    private int duracionMinutos;
    private Bus bus;
    private Terminal terminalSalida;
    private Terminal terminalLlegada;
    private Auxiliar auxiliar;
    private ArrayList<Conductor> conductores;
    private ArrayList<Pasaje> pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int duracionMinutos,
                 Bus bus, Auxiliar auxiliar, Conductor[] arrayConductores,
                 Terminal terminalSalida, Terminal terminalLlegada) {

        this.fechaHoraSalida = LocalDateTime.of(fecha, hora);
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
        this.bus = bus;
        this.auxiliar = auxiliar;
        this.terminalSalida = terminalSalida;
        this.terminalLlegada = terminalLlegada;
        this.conductores = new ArrayList<>();
        this.pasajes = new ArrayList<>();

        if (arrayConductores != null) {
            for (Conductor c : arrayConductores) {
                if (c != null) this.conductores.add(c);
            }
        }

        if (bus != null) bus.addViaje(this);
        if (terminalSalida != null) terminalSalida.addSalida(this);
        if (terminalLlegada != null) terminalLlegada.addLlegada(this);
        if (auxiliar != null) auxiliar.addViaje(this);
        for (Conductor conductor : this.conductores) {
            conductor.addViaje(this);
        }
    }

    public LocalDateTime getFechaHoraSalida() { return fechaHoraSalida; }
    public LocalDate getFecha() { return fechaHoraSalida.toLocalDate(); }
    public LocalTime getHora() { return fechaHoraSalida.toLocalTime(); }
    public int getPrecio() { return precio; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public Bus getBus() { return bus; }
    public Terminal getTerminalSalida() { return terminalSalida; }
    public Terminal getTerminalLlegada() { return terminalLlegada; }
    public Auxiliar getAuxiliar() { return auxiliar; }

    public Conductor[] getConductores() {
        return conductores.toArray(new Conductor[0]);
    }

    public LocalDateTime getFechaHoraTermino() {
        return fechaHoraSalida.plusMinutes(duracionMinutos);
    }

    public String[] getAsientos() {
        String[] asientos = new String[bus.getNroAsientos()];
        for (int i = 0; i < asientos.length; i++) {
            asientos[i] = String.valueOf(i + 1);
        }
        for (Pasaje pasaje : pasajes) {
            asientos[pasaje.getAsiento() - 1] = "*";
        }
        return asientos;
    }

    public String[][] getListaPasajeros() {
        String[][] lista = new String[pasajes.size()][4];
        for (int i = 0; i < pasajes.size(); i++) {
            Pasaje pasaje = pasajes.get(i);
            Pasajero pasajero = pasaje.getPasajero();
            lista[i][0] = String.valueOf(pasajero.getIdPersona());
            lista[i][1] = String.valueOf(pasajero.getNombreCompleto());
            lista[i][2] = String.valueOf(pasajero.getNomContacto());
            lista[i][3] = pasajero.getFonoContacto();
        }
        return lista;
    }

    public boolean existeDisponibilidad(int nroPasajes) {
        return bus.getNroAsientos() - pasajes.size() >= nroPasajes;
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }

    public Venta[] getVentas() {
        ArrayList<Venta> ventas = new ArrayList<>();
        for (Pasaje pasaje : pasajes) {
            if (!ventas.contains(pasaje.getVenta())) {
                ventas.add(pasaje.getVenta());
            }
        }
        return ventas.toArray(new Venta[0]);
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }
}

