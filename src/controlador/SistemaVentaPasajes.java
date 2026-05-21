package controlador;
/**
 * Clase encargada de trabajar con los metodos del menu
 * @author Genesis Castro
 * @author Benjamin Carrasco
 */

import modelo.*;
import utilidades.IdPersona;
import utilidades.Nombre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentaPasajes {

    private static SistemaVentaPasajes instancia;
    private List<Cliente> clientes = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();
    private List<Bus> buses = new ArrayList<>();
    private List<Viaje> viajes = new ArrayList<>();
    private List<Venta> ventas = new ArrayList<>();
    private List<Terminal> terminales = new ArrayList<>();

    private SistemaVentaPasajes() {
        this.clientes = new ArrayList<>();
        this.pasajeros = new ArrayList<>();
        this.buses = new ArrayList<>();
        this.viajes = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.terminales = new ArrayList<>();
    }
    public static SistemaVentaPasajes getInstancia() {
        if (instancia == null) {
            instancia = new SistemaVentaPasajes();
        }
        return instancia;
    }
    public boolean createCliente(IdPersona id, Nombre nom, String fono, String email) {
        if (findCliente(id) != null) {
            return false;
        }
        Cliente nuevoCliente = new Cliente(id, nom, email);
        nuevoCliente.setTelefono(fono);
        this.clientes.add(nuevoCliente);
        return true;
    }
    public boolean createPasajero(IdPersona id, Nombre nom, String fono, Nombre nomContacto, String fonoContacto) {
        if (findPasajero(id) != null) {
            return false;
        }
        Pasajero nuevoPasajero = new Pasajero(id, nom, nomContacto, fonoContacto);
        nuevoPasajero.setTelefono(fono);
        this.pasajeros.add(nuevoPasajero);
        return true;
    }

    public boolean createBus(String patente, int nroAsientos, utilidades.Rut rutEmpresa) {
        if (findBus(patente) != null) {
            return false;
        }

        java.util.Optional<Empresa> empOpt = controlador.ControladorEmpresas.getInstancia().findEmpresa(rutEmpresa);
        if (!empOpt.isPresent()) {
            return false; // La empresa no existe, no se puede crear el bus
        }

        Empresa empresa = empOpt.get();
        Bus nuevoBus = new Bus(patente, nroAsientos, empresa);

        this.buses.add(nuevoBus);
        empresa.addBus(nuevoBus);
        return true;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        Bus busEncontrado = findBus(patBus);
        if (busEncontrado == null) {
            return false;
        }

        for (Viaje v : viajes) {
            if (v.getFecha().equals(fecha) && v.getHora().equals(hora) && v.getBus().getPatente().equalsIgnoreCase(patBus)) {
                return false;
            }
        }

        Viaje nuevoViaje = new Viaje(fecha, hora, precio, busEncontrado);
        this.viajes.add(nuevoViaje);
        return true;
    }

    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta, IdPersona idCliente) {
        return true;
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {

        int contador = 0;
        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)) {
                contador++;
            }
        }

        if (contador == 0) {
            return new String[0][0];
        }

        String[][] horarios = new String[contador][4];
        int index = 0;
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        for (Viaje v : viajes) {
            if (v.getFecha().equals(fechaViaje)) {
                horarios[index][0] = v.getBus().getPatente();
                horarios[index][1] = v.getHora().format(timeFormatter);
                horarios[index][2] = String.valueOf(v.getPrecio());

                // Contar asientos libres
                int libres = 0;
                String[][] asientos = v.getAsientos();
                for (int j = 0; j < asientos.length; j++) {
                    if (asientos[j][1].equalsIgnoreCase("Libre")) {
                        libres++;
                    }
                }
                horarios[index][3] = String.valueOf(libres);
                index++;
            }
        }
        return horarios;
    }

    public String[] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        return new String[0];
    }

    public int getMontoVenta(String idDocumento, TipoDocumento tipo) {
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero) {
        return "";
    }

    public boolean vendePasaje(String idDoc, LocalDate fecha, LocalTime hora, String patBus, int asiento, IdPersona idPasajero) {
        return true;
    }

    public String[][] listVentas() {
        return new String[0][0];
    }

    public String[][] listViajes() {
        if (viajes.isEmpty()) {
            return new String[0][0];
        }

        String[][] lista = new String[viajes.size()][5];
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < viajes.size(); i++) {
            Viaje v = viajes.get(i);
            lista[i][0] = v.getFecha().format(dateFormatter);
            lista[i][1] = v.getHora().format(timeFormatter);
            lista[i][2] = String.valueOf(v.getPrecio());

            // Contamos los asientos disponibles
            int libres = 0;
            String[][] asientos = v.getAsientos();
            for (int j = 0; j < asientos.length; j++) {
                if (asientos[j][1].equalsIgnoreCase("Libre")) {
                    libres++;
                }
            }

            lista[i][3] = String.valueOf(libres);
            lista[i][4] = v.getBus().getPatente();
        }
        return lista;
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        return new String[0][0];
    }

    public Cliente findCliente(IdPersona idPersona) {
        for (Cliente c : clientes) {
            if (c.getIdPersona().equals(idPersona)) {
                return c;
            }
        }
        return null;
    }

    public Venta findVenta(String idDocumento, TipoDocumento tipoDocumento) {
        for (Venta v : ventas) {
            if (v.getIdDocumento().equals(idDocumento) && v.getTipo() == tipoDocumento) {
                return v;
            }
        }
        return null;
    }

    public Bus findBus(String patente) {
        for (Bus b : buses) {
            if (b.getPatente().equalsIgnoreCase(patente)) {
                return b;
            }
        }
        return null;
    }

    public Viaje findViaje(String fecha, String hora, String patenteBus) {
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        LocalDate f = LocalDate.parse(fecha, dateFormatter);
        LocalTime h = LocalTime.parse(hora, timeFormatter);

        for (Viaje v : viajes) {
            if (v.getFecha().equals(f) && v.getHora().equals(h) && v.getBus().getPatente().equalsIgnoreCase(patenteBus)) {
                return v;
            }
        }
        return null;
    }

    public Pasajero findPasajero(IdPersona idPersona) {
        for (Pasajero p : pasajeros) {
            if (p.getIdPersona().equals(idPersona)) {
                return p;
            }
        }
        return null;
    }

    public boolean createTerminal(String nombre, utilidades.Direccion dir) {
        for (Terminal t : terminales) {
            if (t.getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }
        Terminal nuevoTerminal = new Terminal(nombre, dir);
        this.terminales.add(nuevoTerminal);
        return true;
    }
}