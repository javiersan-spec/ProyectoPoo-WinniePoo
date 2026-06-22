package modelo;

/**
 * Representa un viaje de bus entre dos terminales.
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

    // el constructor recibe un solo conductor, si se necesita otro se usa addConductor
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

        // agrego el conductor principal
        if (conductor != null) {
            this.conductores.add(conductor);
            conductor.addViaje(this);
        }

        // registro el viaje en bus, terminales y auxiliar
        if (bus != null) bus.addViaje(this);
        if (terminalSalida != null) terminalSalida.addSalida(this);
        if (terminalLlegada != null) terminalLlegada.addLlegada(this);
        if (auxiliar != null) auxiliar.addViaje(this);
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

    // para agregar mas conductores al viaje
    public void addConductor(Conductor conductor) {
        if (conductor != null) {
            this.conductores.add(conductor);
            conductor.addViaje(this);
        }
    }

    // getTripulantes devuelve conductores + auxiliar juntos
    public Tripulante[] getTripulantes() {
        ArrayList<Tripulante> todos = new ArrayList<>();
        // primero el auxiliar
        if (auxiliar != null) {
            todos.add(auxiliar);
        }
        // luego los conductores
        for (int i = 0; i < conductores.size(); i++) {
            todos.add(conductores.get(i));
        }
        return todos.toArray(new Tripulante[0]);
    }

    public LocalDateTime getFechaHoraTermino() {
        return fechaHoraSalida.plusMinutes(duracionMinutos);
    }

    // devuelve los asientos, los ocupados se marcan con *
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

    // lista de pasajeros con 5 datos: id, nombre, contacto, fono, asiento
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

    // junto todas las ventas de los pasajes sin repetir
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
