package modelo;
import modelo.Bus;
import modelo.Terminal;
import utilidades.Direccion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Viaje {
    private LocalDateTime fechaHoraSalida;
    private int Precio;
    private int duracionMinutos;
    private int valorPasaje;
    private Bus bus;
    private Terminal terminalSalida;
    private Terminal terminalLlegada;
    private Auxiliar auxiliar;
    private ArrayList<Conductor> conductores;
    private ArrayList<Pasaje> pasajes;
    private Date fecha;
    
    public Viaje(LocalDateTime fechaHoraSalida, int duracionMinutos, int valorPasaje,
                 Bus bus, Terminal terminalSalida, Terminal terminalLlegada,
                 Auxiliar auxiliar, ArrayList<Conductor> conductores) {
        this.fechaHoraSalida = fechaHoraSalida;
        this.duracionMinutos = duracionMinutos;
        this.valorPasaje = valorPasaje;
        this.bus = bus;
        this.terminalSalida = terminalSalida;
        this.terminalLlegada = terminalLlegada;
        this.auxiliar = auxiliar;
        this.conductores = conductores;
        this.pasajes = new ArrayList<>();

        bus.addViaje(this);
        terminalSalida.addSalida(this);
        terminalLlegada.addLlegada(this);
        auxiliar.addViaje(this);

        for (Conductor conductor : conductores) {
            conductor.addViaje(this);
        }
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public int getValorPasaje() {
        return valorPasaje;
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

    public ArrayList<Conductor> getConductores() {
        return conductores;
    }

    public LocalDateTime getFechaHoraTermino() {
        return fechaHoraSalida.plusMinutes(duracionMinutos);
    }

    public String[] getAsientos() {
        String[] asientos = new String[bus.getCapacidad()];

        for (int i = 0; i < asientos.length; i++) {
            asientos[i] = String.valueOf(i + 1);
        }

        for (Pasaje pasaje : pasajes) {
            asientos[pasaje.getAsiento() - 1] = "*";
        }

        return asientos;
    }
// revisar desde aca para ver si esta correcto o se pierde informacion aca
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
        return bus.getCapacidad() - pasajes.size() >= cantidadAsientos;
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

    public int getPrecio() {
        return Precio;
    }

    public Date getfecha(){
        return fecha;
    }
}




