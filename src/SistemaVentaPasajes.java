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

    public boolean createCliente(IdPersona id, String nom, String fono, String email) {
        return true;
    }

    public boolean createPasajero(IdPersona id, String nom, String fono, String nomContacto, String fonoContacto) {
        return true;
    }

    public boolean createBus(String patente, String marca, String modelo, int nroAsientos) {
        return true;
    }

    public boolean createViaje(LocalDate fecha, LocalTime hora, LocalTime precio, Bus patBus) {
        return true;
    }

    public boolean iniciaVenta(String idDoc, String tipo, LocalDate fechaVenta, IdPersona idCliente) {
        return true;
    }

    public String[] getHorariosDisponibles(LocalDate fechaViaje) {
        return new String[0];
    }

    public String[][] listAsientosDeViaje(LocalDate fecha, LocalTime hora, String patBus) {
        return new String[0][0];
    }

    public int getMontoVenta(String idDocumento, String tipo) {
        return 0;
    }

    public String getNombrePasajero(IdPersona idPasajero) {
        return "";
    }

    public boolean vendePasaje(String idDoc, String tipo, LocalDate fecha, LocalTime hora,
                               String patBus, int asiento, IdPersona idPasajero) {
        return true;
    }

    public String[] listVentas() {
        return new String[0];
    }

    public String[] listViajes() {
        return new String[0];
    }

    public String[][] listPasajeros(LocalDate fecha, LocalTime hora, String patBus) {
        return new String[0][0];
    }

    public Cliente findCliente(IdPersona idPersona) {
        return null;
    }

    public Venta findVenta(String idDocumento, String tipoDocumento) {
        return null;
    }

    public Bus findBus(String patente) {
        return null;
    }

    public Viaje findViaje(LocalDate fecha, LocalTime hora, String patenteBus) {
        return null;
    }

    public Pasajero findPasajero(IdPersona idPasajero) {
        return null;
    }
}