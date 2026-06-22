package modelo;

/**
 * @author Benjamin Carrasco
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.io.Serializable;

public class Viaje implements Serializable {
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
                 Bus bus, Auxiliar auxiliar, Conductor conductor,
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

        if (conductor != null) {
            this.conductores.add(conductor);
            conductor.addViaje(this);
        }

        if (bus != null) bus.addViaje(this);
        if (terminalSalida != null) terminalSalida.addSalida(this);
        if (terminalLlegada != null) terminalLlegada.addLlegada(this);
        if (auxiliar != null) auxiliar.addViaje(this);
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public LocalDate getFecha() {
        return fechaHoraSalida.toLocalDate();
    }

    public LocalTime getHora() {
        return fechaHoraSalida.toLocalTime();
    }

    public int getPrecio() {
        return precio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Bus getBus() {
        return bus;
    }

    public Terminal getTerminalSalida() {
        return terminalSalida;
    }

    public Terminal getTerminalLlegada() {
        return terminalLlegada;
    }

    public Auxiliar getAuxiliar() {
        return auxiliar;
    }

    public void addConductor(Conductor conductor) {
        if (conductor != null) {
            this.conductores.add(conductor);
            conductor.addViaje(this);
        }
    }

    public Tripulante[] getTripulantes() {
        ArrayList<Tripulante> todos = new ArrayList<>();
        if (auxiliar != null) {
            todos.add(auxiliar);
        }
        for (int i = 0; i < conductores.size(); i++) {
            todos.add(conductores.get(i));
        }
        return todos.toArray(new Tripulante[0]);
    }

    public LocalDateTime getFechaHoraTermino() {
        return fechaHoraSalida.plusMinutes(duracionMinutos);
    }

    public String[] getAsientos() {
        String[] asientos = new String[bus.getNroAsientos()];
        for (int i = 0; i < asientos.length; i++) {
            asientos[i] = String.valueOf(i + 1);
        }
        for (int i = 0; i < pasajes.size(); i++) {
            asientos[pasajes.get(i).getAsiento() - 1] = "*";
        }
        return asientos;
    }

    public String[][] getListaPasajeros() {
        String[][] lista = new String[pasajes.size()][5];
        for (int i = 0; i < pasajes.size(); i++) {
            Pasaje pasaje = pasajes.get(i);
            Pasajero pasajero = pasaje.getPasajero();
            lista[i][0] = String.valueOf(pasajero.getIdPersona());
            lista[i][1] = String.valueOf(pasajero.getNombreCompleto());
            lista[i][2] = String.valueOf(pasajero.getNomContacto());
            lista[i][3] = pasajero.getFonoContacto();
            lista[i][4] = String.valueOf(pasaje.getAsiento());
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
        for (int i = 0; i < pasajes.size(); i++) {
            Venta v = pasajes.get(i).getVenta();
            if (!ventas.contains(v)) {
                ventas.add(v);
            }
        }
        return ventas.toArray(new Venta[0]);
    }

    public void addPasaje(Pasaje pasaje) {
        pasajes.add(pasaje);
    }
}

