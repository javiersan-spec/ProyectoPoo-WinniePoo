package controlador;

import excepciones.SVPException;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import modelo.Auxiliar;
import modelo.Bus;
import modelo.Cliente;
import modelo.Conductor;
import modelo.Pasajero;
import modelo.Terminal;
import modelo.TipoDocumento;
import modelo.Venta;
import modelo.Viaje;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;
/**
 * Controlador principal del sistema de venta de pasajes.
 * @author Benjamin Carrasco
 */
public class SistemaVentaPasajes implements Serializable {

    private static SistemaVentaPasajes instancia;
    private List<Cliente> clientes;
    private List<Pasajero> pasajeros;
    private List<Viaje> viajes;
    private List<Venta> ventas;

    private SistemaVentaPasajes() {
        this.clientes = new ArrayList<>();
        this.pasajeros = new ArrayList<>();
        this.viajes = new ArrayList<>();
        this.ventas = new ArrayList<>();
    }

    public static SistemaVentaPasajes getInstancia() {
        if (instancia == null) {
            instancia = new SistemaVentaPasajes();
        }
        return instancia;
    }

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) {
        if (findCliente(id).isPresent()) {
            throw new SVPException("Ya existe cliente con el id indicado.");
        }

        Cliente nuevoCliente = new Cliente(id, nom, email);
        nuevoCliente.setTelefono(fono);
        clientes.add(nuevoCliente);
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) {
        if (findPasajero(id).isPresent()) {
            throw new SVPException("Ya existe pasajero con el id indicado.");
        }

        Pasajero nuevoPasajero = new Pasajero(id, nom, nomContacto, fonoContacto);
        nuevoPasajero.setTelefono(fono);
        pasajeros.add(nuevoPasajero);
    }

    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracionMinutos,
                            String patBus, IdPersona[] idsTripulantes, String[] comunas) {
        Optional<Bus> busOpt = ControladorEmpresas.getInstancia().findBus(patBus);
        if (!busOpt.isPresent()) {
            throw new SVPException("No existe bus con la patente indicada.");
        }

        if (idsTripulantes == null || idsTripulantes.length < 2 || idsTripulantes.length > 3) {
            throw new SVPException("El viaje debe tener un auxiliar y uno o dos conductores.");
        }

        if (comunas == null || comunas.length != 2) {
            throw new SVPException("Debe indicar comuna de salida y comuna de llegada.");
        }

        Optional<Terminal> terminalSalidaOpt = ControladorEmpresas.getInstancia().findTerminalPorComuna(comunas[0]);
        if (!terminalSalidaOpt.isPresent()) {
            throw new SVPException("No existe terminal de salida en la comuna indicada.");
        }

        Optional<Terminal> terminalLlegadaOpt = ControladorEmpresas.getInstancia().findTerminalPorComuna(comunas[1]);
        if (!terminalLlegadaOpt.isPresent()) {
            throw new SVPException("No existe terminal de llegada en la comuna indicada.");
        }

        Bus bus = busOpt.get();
        Rut rutEmpresa = bus.getEmpresa().getRut();

        Optional<Auxiliar> auxiliarOpt = ControladorEmpresas.getInstancia().findAuxiliar(idsTripulantes[0], rutEmpresa);
        if (!auxiliarOpt.isPresent()) {
            throw new SVPException("No existe auxiliar con el id indicado en la empresa con el rut indicado.");
        }

        ArrayList<Conductor> conductores = new ArrayList<>();
        for (int i = 1; i < idsTripulantes.length; i++) {
            Optional<Conductor> conductorOpt = ControladorEmpresas.getInstancia().findConductor(idsTripulantes[i], rutEmpresa);
            if (!conductorOpt.isPresent()) {
                throw new SVPException("No existe conductor con el id indicado en la empresa con el rut indicado.");
            }
            if (!conductores.contains(conductorOpt.get())) {
                conductores.add(conductorOpt.get());
            }
        }

        if (findViaje(fecha, hora, patBus).isPresent()) {
            throw new SVPException("Ya existe viaje con fecha, hora y patente de bus indicados.");
        }

        Viaje nuevoViaje = new Viaje(fecha, hora, precio, duracionMinutos, bus,
                auxiliarOpt.get(), conductores.toArray(new Conductor[0]), terminalSalidaOpt.get(), terminalLlegadaOpt.get());
        viajes.add(nuevoViaje);
    }

    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta,
                            LocalDate fechaViaje, String comunaSalida, String comunaLlegada,
                            IdPersona idCliente, int nroPasajes) {
        if (findVenta(idDoc, tipo).isPresent()) {
            throw new SVPException("Ya existe venta con el id y tipo de documento indicados.");
        }

        Optional<Cliente> clienteOpt = findCliente(idCliente);
        if (!clienteOpt.isPresent()) {
            throw new SVPException("No existe cliente con id indicado.");
        }

        if (getHorariosDisponibles(fechaViaje, comunaSalida, comunaLlegada, nroPasajes).length == 0) {
            throw new SVPException("No existen viajes disponibles en la fecha y con terminales en las comunas de salida y llegada indicados.");
        }

        ventas.add(new Venta(idDoc, tipo, fechaVenta, clienteOpt.get()));
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje, String comunaSalida,
                                             String comunaLlegada, int nroPasajes) {
        ArrayList<Viaje> disponibles = new ArrayList<>();
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().equals(fechaViaje)
                    && viaje.getTerminalSalida().getDireccion().getComuna().equalsIgnoreCase(comunaSalida)
                    && viaje.getTerminalLlegada().getDireccion().getComuna().equalsIgnoreCase(comunaLlegada)
                    && viaje.existeDisponibilidad(nroPasajes)) {
                disponibles.add(viaje);
            }
        }

        String[][] horarios = new String[disponibles.size()][4];
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < disponibles.size(); i++) {
            Viaje viaje = disponibles.get(i);
            horarios[i][0] = viaje.getBus().getPatente();
            horarios[i][1] = viaje.getHora().format(horaFormatter);
            horarios[i][2] = String.valueOf(viaje.getPrecio());
            horarios[i][3] = String.valueOf(viaje.getNroAsientosDisponibles());
        }

        return horarios;
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Optional<Viaje> viajeOpt = findViaje(fecha, hora, patBus);
        return viajeOpt.isPresent() ? viajeOpt.get().getAsientos() : new String[0];
    }

    public Optional<Integer> getMontoVenta(String idDocumento, TipoDocumento tipo) {
        Optional<Venta> ventaOpt = findVenta(idDocumento, tipo);
        return ventaOpt.isPresent() ? Optional.of(ventaOpt.get().getMonto()) : Optional.empty();
    }

    public Optional<String> getNombrePasajero(IdPersona idPasajero) {
        Optional<Pasajero> pasajeroOpt = findPasajero(idPasajero);
        return pasajeroOpt.isPresent()
                ? Optional.of(pasajeroOpt.get().getNombreCompleto().toString())
                : Optional.empty();
    }

    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha, LocalTime hora,
                            String patBus, int asiento, IdPersona idPasajero) {
        Optional<Venta> ventaOpt = findVenta(idDoc, tipo);
        if (!ventaOpt.isPresent()) {
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }

        Optional<Viaje> viajeOpt = findViaje(fecha, hora, patBus);
        if (!viajeOpt.isPresent()) {
            throw new SVPException("No existe viaje con la fecha, hora y patente de bus indicados.");
        }

        Optional<Pasajero> pasajeroOpt = findPasajero(idPasajero);
        if (!pasajeroOpt.isPresent()) {
            throw new SVPException("No existe pasajero con el id indicado.");
        }

        String[] asientos = viajeOpt.get().getAsientos();
        if (asiento < 1 || asiento > asientos.length || "*".equals(asientos[asiento - 1])) {
            throw new SVPException("El asiento indicado no esta disponible.");
        }

        ventaOpt.get().createPasaje(asiento, viajeOpt.get(), pasajeroOpt.get());
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipoDocumento) {
        Optional<Venta> ventaOpt = findVenta(idDocumento, tipoDocumento);
        if (!ventaOpt.isPresent()) {
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }

        if (!ventaOpt.get().pagaMonto()) {
            throw new SVPException("La venta ya fue pagada.");
        }
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipoDocumento, long nroTarjeta) {
        Optional<Venta> ventaOpt = findVenta(idDocumento, tipoDocumento);
        if (!ventaOpt.isPresent()) {
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }

        if (!ventaOpt.get().pagaMonto(nroTarjeta)) {
            throw new SVPException("La venta ya fue pagada.");
        }
    }

    public String[][] listVentas() {
        String[][] lista = new String[ventas.size()][6];
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < ventas.size(); i++) {
            Venta venta = ventas.get(i);
            lista[i][0] = venta.getIdDocumento();
            lista[i][1] = venta.getTipo().toString();
            lista[i][2] = venta.getFecha().format(fechaFormatter);
            lista[i][3] = venta.getCliente().getNombreCompleto().toString();
            lista[i][4] = String.valueOf(venta.getMonto());
            lista[i][5] = venta.getTipoPago();
        }

        return lista;
    }

    public String[][] listViajes() {
        String[][] lista = new String[viajes.size()][8];
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < viajes.size(); i++) {
            Viaje viaje = viajes.get(i);
            lista[i][0] = viaje.getFecha().format(fechaFormatter);
            lista[i][1] = viaje.getHora().format(horaFormatter);
            lista[i][2] = viaje.getFechaHoraTermino().toLocalTime().format(horaFormatter);
            lista[i][3] = String.valueOf(viaje.getPrecio());
            lista[i][4] = String.valueOf(viaje.getNroAsientosDisponibles());
            lista[i][5] = viaje.getBus().getPatente();
            lista[i][6] = viaje.getTerminalSalida().getDireccion().getComuna();
            lista[i][7] = viaje.getTerminalLlegada().getDireccion().getComuna();
        }

        return lista;
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Optional<Viaje> viajeOpt = findViaje(fecha, hora, patBus);
        if (!viajeOpt.isPresent()) {
            throw new SVPException("No existe viaje con la fecha, hora y patente de bus indicados.");
        }

        return viajeOpt.get().getListaPasajeros();
    }

    private Optional<Cliente> findCliente(IdPersona idPersona) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdPersona().equals(idPersona)) {
                return Optional.of(cliente);
            }
        }
        return Optional.empty();
    }

    private Optional<Venta> findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta venta : ventas) {
            if (venta.getIdDocumento().equals(idDocumento) && venta.getTipo() == tipoDocumento) {
                return Optional.of(venta);
            }
        }
        return Optional.empty();
    }

    private Optional<Viaje> findViaje(LocalDate fecha, LocalTime hora, String patenteBus) {
        for (Viaje viaje : viajes) {
            if (viaje.getFecha().equals(fecha)
                    && viaje.getHora().equals(hora)
                    && viaje.getBus().getPatente().equalsIgnoreCase(patenteBus)) {
                return Optional.of(viaje);
            }
        }
        return Optional.empty();
    }

    private Optional<Pasajero> findPasajero(IdPersona idPersona) {
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getIdPersona().equals(idPersona)) {
                return Optional.of(pasajero);
            }
        }
        return Optional.empty();
    }
}
