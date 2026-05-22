package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Viaje {
    private LocalDateTime fechaHoraSalida;
    private int duracionMinutos;
    private int precio;
    private Bus bus;
    private Terminal terminalSalida;
    private Terminal terminalLlegada;
    private Auxiliar auxiliar;
    private List<Conductor> conductores;
    private ArrayList<Pasaje> pasajes;

    public Viaje(LocalDateTime fechaHoraSalida, int duracionMinutos, int precio,
                 Bus bus, Terminal terminalSalida, Terminal terminalLlegada,
                 Auxiliar auxiliar, List<Conductor> conductores) {
        this.fechaHoraSalida = fechaHoraSalida;
        this.duracionMinutos = duracionMinutos;
        this.precio = precio;
        this.bus = bus;
        this.terminalSalida = terminalSalida;
        this.terminalLlegada = terminalLlegada;
        this.auxiliar = auxiliar;
        this.conductores = conductores;
        this.pasajes = new ArrayList<>();

        if (bus != null) {
            bus.addViaje(this);
        }
        if (terminalSalida != null) {
            terminalSalida.addSalida(this);
        }
        if (terminalLlegada != null) {
            terminalLlegada.addLlegada(this);
        }
        if (auxiliar != null) {
            auxiliar.addViaje(this);
        }

        for (Conductor conductor : conductores) {
            conductor.addViaje(this);
        }
    }

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this(fecha.atTime(hora), 0, precio, bus, null, null, null, new ArrayList<>());
    }

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int duracionMinutos,
                 Bus bus, Auxiliar auxiliar, Conductor conductor,
                 Terminal terminalSalida, Terminal terminalLlegada) {
        this(fecha.atTime(hora), duracionMinutos, precio, bus, terminalSalida,
                terminalLlegada, auxiliar, new ArrayList<>());
        addConductor(conductor);
    }

    public Viaje(LocalDate fecha, LocalTime hora, int precio, int duracionMinutos,
                 Bus bus, Auxiliar auxiliar, ArrayList<Conductor> conductores,
                 Terminal terminalSalida, Terminal terminalLlegada) {
        this(fecha.atTime(hora), duracionMinutos, precio, bus, terminalSalida,
                terminalLlegada, auxiliar, conductores);
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

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setDuracion(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
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

    public List<Conductor> getConductores() {
        return conductores;
    }

    public Tripulante[] getTripulantes() {
        ArrayList<Tripulante> tripulantes = new ArrayList<>();
        if (auxiliar != null) {
            tripulantes.add(auxiliar);
        }
        tripulantes.addAll(conductores);
        return tripulantes.toArray(new Tripulante[0]);
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

    public boolean existeDisponibilidad(int cantidadAsientos) {
        return bus.getNroAsientos() - pasajes.size() >= cantidadAsientos;
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

    public void addConductor(Conductor conductor) {
        if (conductor != null && !conductores.contains(conductor)) {
            conductores.add(conductor);
            conductor.addViaje(this);
        }
    }

    public int getPrecio() {
        return precio;
    }
}

