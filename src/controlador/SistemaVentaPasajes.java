package controlador;

import excepciones.SVPException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import modelo.Auxiliar;
import modelo.Bus;
import modelo.Cliente;
import modelo.Conductor;
import modelo.Pasaje;
import modelo.Pasajero;
import modelo.Terminal;
import modelo.TipoDocumento;
import modelo.Venta;
import modelo.Viaje;
import persistencia.IOSVP;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

/**
 * Controlador principal del sistema de venta de pasajes.
 * Maneja clientes, pasajeros, viajes y ventas.
 * @author Genesis Castro
 * @author Benjamin Carrasco
 */
public class SistemaVentaPasajes implements Serializable {

    private static SistemaVentaPasajes instancia;
    private ArrayList<Cliente> clientes;
    private ArrayList<Pasajero> pasajeros;
    private ArrayList<Viaje> viajes;
    private ArrayList<Venta> ventas;

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

    /**
     * Asigna la instancia persistente leida desde disco.
     */
    public static void setInstanciaPersistente(SistemaVentaPasajes persistente) {
        instancia = persistente;
    }

    /**
     * Invoca IOSVP.readDatosIniciales para recuperar un arreglo con los
     * objetos creados. El metodo almacena los objetos que correspondan
     * a sus colecciones, previamente vaciadas.
     * Finalmente invoca setDatosIniciales de ControladorEmpresas.
     */
    public void readDatosIniciales() {
        Object[] datos = IOSVP.readDatosIniciales();

        // vacio las colecciones antes de cargar los nuevos datos
        clientes.clear();
        pasajeros.clear();
        viajes.clear();
        ventas.clear();

        // paso todos los datos a ControladorEmpresas para que cargue empresas, terminales y buses PRIMERO
        ControladorEmpresas.getInstancia().setDatosIniciales(datos);

        // proceso personas (clientes y pasajeros) y viajes del arreglo
        int i = 0;
        while (i < datos.length) {
            String tipo = (String) datos[i];

            if (tipo.equals("CP")) {
                // cliente y pasajero: id, nombre, fono, email, nomContacto, fonoContacto
                IdPersona id = (IdPersona) datos[i + 1];
                Nombre nombre = (Nombre) datos[i + 2];
                String fono = (String) datos[i + 3];
                String email = (String) datos[i + 4];
                Nombre nomContacto = (Nombre) datos[i + 5];
                String fonoContacto = (String) datos[i + 6];

                Cliente c = new Cliente(id, nombre, email);
                c.setTelefono(fono);
                clientes.add(c);

                Pasajero p = new Pasajero(id, nombre, nomContacto, fonoContacto);
                p.setTelefono(fono);
                pasajeros.add(p);
                i += 7;

            } else if (tipo.equals("C")) {
                // solo cliente: id, nombre, fono, email
                IdPersona id = (IdPersona) datos[i + 1];
                Nombre nombre = (Nombre) datos[i + 2];
                String fono = (String) datos[i + 3];
                String email = (String) datos[i + 4];

                Cliente c = new Cliente(id, nombre, email);
                c.setTelefono(fono);
                clientes.add(c);
                i += 5;

            } else if (tipo.equals("P")) {
                // solo pasajero: id, nombre, fono, nomContacto, fonoContacto
                IdPersona id = (IdPersona) datos[i + 1];
                Nombre nombre = (Nombre) datos[i + 2];
                String fono = (String) datos[i + 3];
                Nombre nomContacto = (Nombre) datos[i + 4];
                String fonoContacto = (String) datos[i + 5];

                Pasajero p = new Pasajero(id, nombre, nomContacto, fonoContacto);
                p.setTelefono(fono);
                pasajeros.add(p);
                i += 6;

            } else if (tipo.equals("VIAJE")) {
                // viaje: fecha, hora, precio, duracion, patBus, idsTripulantes, termSalida, termLlegada
                String fechaStr = (String) datos[i + 1];
                String horaStr = (String) datos[i + 2];
                int precio = (Integer) datos[i + 3];
                int duracion = (Integer) datos[i + 4];
                String patBus = (String) datos[i + 5];
                String[] idsTripStr = (String[]) datos[i + 6];
                String termSalidaNombre = (String) datos[i + 7];
                String termLlegadaNombre = (String) datos[i + 8];

                LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));

                Bus bus = ControladorEmpresas.getInstancia().findBus(patBus);
                Terminal termSalida = ControladorEmpresas.getInstancia().findTerminal(termSalidaNombre);
                Terminal termLlegada = ControladorEmpresas.getInstancia().findTerminal(termLlegadaNombre);

                if (bus != null && termSalida != null && termLlegada != null) {
                    Rut rutEmpresa = bus.getEmpresa().getRut();

                    // primer id es auxiliar
                    Auxiliar auxiliar = ControladorEmpresas.getInstancia()
                            .findAuxiliar(Rut.of(idsTripStr[0]), rutEmpresa);
                    // segundo id es conductor principal
                    Conductor conductorPrincipal = ControladorEmpresas.getInstancia()
                            .findConductor(Rut.of(idsTripStr[1]), rutEmpresa);

                    if (auxiliar != null && conductorPrincipal != null) {
                        Viaje nuevoViaje = new Viaje(fecha, hora, precio, duracion,
                                bus, auxiliar, conductorPrincipal, termSalida, termLlegada);
                        viajes.add(nuevoViaje);
                    }
                }
                i += 9;

            } else if (tipo.equals("EMPRESA")) {
                i += 4;
            } else if (tipo.equals("TRIPULANTE")) {
                i += 6;
            } else if (tipo.equals("TERMINAL")) {
                i += 3;
            } else if (tipo.equals("BUS")) {
                i += 6;
            } else {
                i++;
            }
        }
    }

    /**
     * Invoca IOSVP.saveControladores para guardar los dos controladores.
     */
    public void saveDatosSistema() {
        Object[] controladores = new Object[] { this, ControladorEmpresas.getInstancia() };
        IOSVP.saveControladores(controladores);
    }

    /**
     * Invoca IOSVP.readControladores para recuperar los controladores desde disco.
     * Realiza la asignacion de this e invoca setInstanciaPersistente de ControladorEmpresas.
     */
    public void readDatosSistema() {
        Object[] controladores = IOSVP.readControladores();
        SistemaVentaPasajes svp = (SistemaVentaPasajes) controladores[0];
        ControladorEmpresas ce = (ControladorEmpresas) controladores[1];

        SistemaVentaPasajes.setInstanciaPersistente(svp);
        ControladorEmpresas.setInstanciaPersistente(ce);
    }

    /**
     * Invoca IOSVP.savePasajesDeVenta para generar un archivo de texto
     * con todos los pasajes de la venta cuyo id y tipo de documento
     * recibe como parametro.
     */
    public void generatePasajesVenta(String idDoc, TipoDocumento tipo) {
        Venta venta = findVenta(idDoc, tipo);
        if (venta == null) {
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }

        Pasaje[] pasajes = venta.getPasajes();
        if (pasajes.length == 0) {
            throw new SVPException("La venta no tiene pasajes asociados.");
        }

        String nombreArchivo = idDoc + "_" + tipo.toString() + ".txt";
        IOSVP.savePasajesDeVenta(pasajes, nombreArchivo);
    }

    public void createCliente(IdPersona id, Nombre nom, String fono, String email) {
        if (findCliente(id) != null) {
            throw new SVPException("Ya existe cliente con el id indicado.");
        }
        Cliente nuevoCliente = new Cliente(id, nom, email);
        nuevoCliente.setTelefono(fono);
        clientes.add(nuevoCliente);
    }

    public void createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) {
        if (findPasajero(id) != null) {
            throw new SVPException("Ya existe pasajero con el id indicado.");
        }
        Pasajero nuevoPasajero = new Pasajero(id, nom, nomContacto, fonoContacto);
        nuevoPasajero.setTelefono(fono);
        pasajeros.add(nuevoPasajero);
    }

    // createViaje usa la firma del constructor de Viaje (un solo conductor)
    // y agrega los conductores extra con addConductor
    public void createViaje(LocalDate fecha, LocalTime hora, int precio, int duracionMinutos,
                            String patBus, IdPersona[] idsTripulantes, String[] comunas) {

        Bus bus = ControladorEmpresas.getInstancia().findBus(patBus);
        if (bus == null) {
            throw new SVPException("No existe bus con la patente indicada.");
        }

        if (idsTripulantes == null || idsTripulantes.length < 2 || idsTripulantes.length > 3) {
            throw new SVPException("El viaje debe tener un auxiliar y uno o dos conductores.");
        }

        if (comunas == null || comunas.length != 2) {
            throw new SVPException("Debe indicar comuna de salida y comuna de llegada.");
        }

        Terminal termSalida = ControladorEmpresas.getInstancia().findTerminalPorComuna(comunas[0]);
        if (termSalida == null) {
            throw new SVPException("No existe terminal de salida en la comuna indicada.");
        }

        Terminal termLlegada = ControladorEmpresas.getInstancia().findTerminalPorComuna(comunas[1]);
        if (termLlegada == null) {
            throw new SVPException("No existe terminal de llegada en la comuna indicada.");
        }

        Rut rutEmpresa = bus.getEmpresa().getRut();

        // el primer id es el auxiliar
        Auxiliar auxiliar = ControladorEmpresas.getInstancia().findAuxiliar(idsTripulantes[0], rutEmpresa);
        if (auxiliar == null) {
            throw new SVPException("No existe auxiliar con el id indicado en la empresa.");
        }

        // el segundo id es el conductor principal
        Conductor conductorPrincipal = ControladorEmpresas.getInstancia().findConductor(idsTripulantes[1], rutEmpresa);
        if (conductorPrincipal == null) {
            throw new SVPException("No existe conductor con el id indicado en la empresa.");
        }

        // verifico que no exista un viaje con misma fecha hora y bus
        if (findViaje(fecha, hora, patBus) != null) {
            throw new SVPException("Ya existe viaje con fecha, hora y patente de bus indicados.");
        }

        // creo el viaje con un conductor
        Viaje nuevoViaje = new Viaje(fecha, hora, precio, duracionMinutos, bus,
                auxiliar, conductorPrincipal, termSalida, termLlegada);

        // si hay un segundo conductor lo agrego con addConductor
        if (idsTripulantes.length == 3) {
            Conductor segundoConductor = ControladorEmpresas.getInstancia().findConductor(idsTripulantes[2], rutEmpresa);
            if (segundoConductor == null) {
                throw new SVPException("No existe conductor con el id indicado en la empresa.");
            }
            nuevoViaje.addConductor(segundoConductor);
        }

        viajes.add(nuevoViaje);
    }

    public void iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta,
                            LocalDate fechaViaje, String comunaSalida, String comunaLlegada,
                            IdPersona idCliente, int nroPasajes) {

        if (findVenta(idDoc, tipo) != null) {
            throw new SVPException("Ya existe venta con el id y tipo de documento indicados.");
        }

        Cliente cliente = findCliente(idCliente);
        if (cliente == null) {
            throw new SVPException("No existe cliente con id indicado.");
        }

        if (getHorariosDisponibles(fechaViaje, comunaSalida, comunaLlegada, nroPasajes).length == 0) {
            throw new SVPException("No existen viajes disponibles en la fecha y comunas indicadas.");
        }

        ventas.add(new Venta(idDoc, tipo, fechaVenta, cliente));
    }

    // usa streams para filtrar viajes disponibles
    public String[][] getHorariosDisponibles(LocalDate fechaViaje, String comunaSalida,
                                             String comunaLlegada, int nroPasajes) {

        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        return viajes.stream()
                .filter(v -> v.getFecha().equals(fechaViaje))
                .filter(v -> v.getTerminalSalida().getDireccion().getComuna().equalsIgnoreCase(comunaSalida))
                .filter(v -> v.getTerminalLlegada().getDireccion().getComuna().equalsIgnoreCase(comunaLlegada))
                .filter(v -> v.existeDisponibilidad(nroPasajes))
                .map(v -> new String[]{
                        v.getBus().getPatente(),
                        v.getHora().format(horaFmt),
                        String.valueOf(v.getPrecio()),
                        String.valueOf(v.getNroAsientosDisponibles())
                })
                .toArray(String[][]::new);
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Viaje viaje = findViaje(fecha, hora, patBus);
        if (viaje != null) {
            return viaje.getAsientos();
        }
        return new String[0];
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {
        Venta venta = findVenta(idDocumento, tipo);
        if (venta != null) {
            return venta.getMonto();
        }
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero) {
        Pasajero p = findPasajero(idPasajero);
        if (p != null) {
            return p.getNombreCompleto().toString();
        }
        return null;
    }

    // la verificacion del asiento disponible se puede hacer desde la vista
    public void vendePasaje(String idDoc, TipoDocumento tipo, LocalDate fecha, LocalTime hora,
                            String patBus, int asiento, IdPersona idPasajero) {
        Venta venta = findVenta(idDoc, tipo);
        if (venta == null) {
            throw new SVPException("No existe venta con el id y tipo de documento indicados.");
        }

        Viaje viaje = findViaje(fecha, hora, patBus);
        if (viaje == null) {
            throw new SVPException("No existe viaje con la fecha, hora y patente indicados.");
        }

        Pasajero pasajero = findPasajero(idPasajero);
        if (pasajero == null) {
            throw new SVPException("No existe pasajero con el id indicado.");
        }

        venta.createPasaje(asiento, viaje, pasajero);
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipoDocumento) {
        Venta venta = findVenta(idDocumento, tipoDocumento);
        if (venta == null) {
            throw new SVPException("No existe venta con el id y tipo indicados.");
        }
        if (!venta.pagaMonto()) {
            throw new SVPException("La venta ya fue pagada.");
        }
    }

    public void pagaVenta(String idDocumento, TipoDocumento tipoDocumento, long nroTarjeta) {
        Venta venta = findVenta(idDocumento, tipoDocumento);
        if (venta == null) {
            throw new SVPException("No existe venta con el id y tipo indicados.");
        }
        if (!venta.pagaMonto(nroTarjeta)) {
            throw new SVPException("La venta ya fue pagada.");
        }
    }

    // usa streams para generar la lista formateada
    public String[][] listVentas() {
        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return ventas.stream()
                .map(v -> new String[]{
                        v.getIdDocumento(),
                        v.getTipo().toString(),
                        v.getFecha().format(fechaFmt),
                        v.getCliente().getNombreCompleto().toString(),
                        String.valueOf(v.getMonto()),
                        v.getTipoPago() != null ? v.getTipoPago() : "Sin pago"
                })
                .toArray(String[][]::new);
    }

    // usa streams para generar la lista formateada
    public String[][] listViajes() {
        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");

        return viajes.stream()
                .map(v -> new String[]{
                        v.getFecha().format(fechaFmt),
                        v.getHora().format(horaFmt),
                        v.getFechaHoraTermino().toLocalTime().format(horaFmt),
                        String.valueOf(v.getPrecio()),
                        String.valueOf(v.getNroAsientosDisponibles()),
                        v.getBus().getPatente(),
                        v.getTerminalSalida().getDireccion().getComuna(),
                        v.getTerminalLlegada().getDireccion().getComuna()
                })
                .toArray(String[][]::new);
    }

    public String[][] listPasajerosViaje(LocalDate fecha, LocalTime hora, String patBus) {
        Viaje viaje = findViaje(fecha, hora, patBus);
        if (viaje == null) {
            throw new SVPException("No existe viaje con la fecha, hora y patente indicados.");
        }
        return viaje.getListaPasajeros();
    }

    // -- metodos de busqueda usando streams y Optional --

    // busca un cliente por su id usando streams
    private Cliente findCliente(IdPersona idPersona) {
        Optional<Cliente> resultado = clientes.stream()
                .filter(c -> c.getIdPersona().equals(idPersona))
                .findFirst();
        return resultado.orElse(null);
    }

    // busca una venta por id de documento y tipo usando streams
    private Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        Optional<Venta> resultado = ventas.stream()
                .filter(v -> v.getIdDocumento().equals(idDocumento))
                .filter(v -> v.getTipo() == tipoDocumento)
                .findFirst();
        return resultado.orElse(null);
    }

    // busca un viaje por fecha, hora y patente del bus usando streams
    private Viaje findViaje(LocalDate fecha, LocalTime hora, String patenteBus) {
        Optional<Viaje> resultado = viajes.stream()
                .filter(v -> v.getFecha().equals(fecha))
                .filter(v -> v.getHora().equals(hora))
                .filter(v -> v.getBus().getPatente().equalsIgnoreCase(patenteBus))
                .findFirst();
        return resultado.orElse(null);
    }

    // busca un pasajero por su id usando streams
    private Pasajero findPasajero(IdPersona idPersona) {
        Optional<Pasajero> resultado = pasajeros.stream()
                .filter(p -> p.getIdPersona().equals(idPersona))
                .findFirst();
        return resultado.orElse(null);
    }
}
