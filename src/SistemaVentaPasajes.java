//Autores, Genesis Castro y Benjamin Carrasco
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaVentaPasajes {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Pasajero> pasajeros = new ArrayList<>();
    private List<Bus> buses = new ArrayList<>();
    private List<Viaje> viajes = new ArrayList<>();
    private List<Venta> ventas = new ArrayList<>();

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
        return true;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        return true;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, int precio, String patBus) {
        return true;
    }

    public boolean iniciaVenta(String idDoc, TipoDocumento tipo, LocalDate fechaVenta, IdPersona idCliente) {
        return true;
    }

    public String[][] getHorariosDisponibles(LocalDate fechaViaje) {
        return new String[0][0];
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
        return new String[0][0];
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
        return null;
    }

    public Pasajero findPasajero(IdPersona idPasajero) {
        for (Pasajero p : pasajeros) {
            if (p.getIdPersona().equals(idPasajero)) {
                return p;
            }
        }
        return null;
    }
}