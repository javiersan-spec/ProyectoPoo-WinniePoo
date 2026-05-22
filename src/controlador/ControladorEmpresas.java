package controlador;

import excepciones.SistemaVentaPasajesException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import modelo.Auxiliar;
import modelo.Bus;
import modelo.Conductor;
import modelo.Empresa;
import modelo.Terminal;
import modelo.Tripulante;
import modelo.Venta;
import modelo.Viaje;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

/**
 * @author Genesis Castro
 */
public class ControladorEmpresas {

    private static ControladorEmpresas instancia;
    private ArrayList<Empresa> empresas;
    private ArrayList<Terminal> terminales;

    private ControladorEmpresas() {
        this.empresas = new ArrayList<>();
        this.terminales = new ArrayList<>();
    }

    public static ControladorEmpresas getInstancia() {
        if (instancia == null) {
            instancia = new ControladorEmpresas();
        }
        return instancia;
    }

    public void createEmpresa(Rut rut, String nombre, String url) {
        if (findEmpresa(rut).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe empresa con el rut indicado.");
        }
        empresas.add(new Empresa(rut, nombre, url));
    }

    public void createBus(String marca, String modelo, String patente, int nroAsientos, Rut rutEmpresa) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (!empresaOpt.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }

        if (findBus(patente).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe bus con la patente indicada.");
        }

        Empresa empresa = empresaOpt.get();
        Bus bus = new Bus(patente, nroAsientos, empresa);
        bus.setMarca(marca);
        bus.setModelo(modelo);
        empresa.addBus(bus);
    }

    public void createTerminal(String nombre, Direccion direccion) {
        if (findTerminal(nombre).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe terminal con el nombre indicado.");
        }

        if (findTerminalPorComuna(direccion.getComuna()).isPresent()) {
            throw new SistemaVentaPasajesException("Ya existe terminal en la comuna indicada.");
        }

        terminales.add(new Terminal(nombre, direccion));
    }

    public void hireConductorForEmpresa(Rut rutEmpresa, IdPersona id, Nombre nombre, Direccion direccion) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (!empresaOpt.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }

        if (!empresaOpt.get().addConductor(id, nombre, direccion)) {
            throw new SistemaVentaPasajesException("Ya esta contratado conductor/auxiliar con el id dado en la empresa senalada.");
        }
    }

    public void hireAuxiliarForEmpresa(Rut rutEmpresa, IdPersona id, Nombre nombre, Direccion direccion) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (!empresaOpt.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }

        if (!empresaOpt.get().addAuxiliar(id, nombre, direccion)) {
            throw new SistemaVentaPasajesException("Ya esta contratado auxiliar/conductor con el id dado en la empresa senalada.");
        }
    }

    public String[][] listEmpresas() {
        String[][] lista = new String[empresas.size()][6];

        for (int i = 0; i < empresas.size(); i++) {
            Empresa empresa = empresas.get(i);
            lista[i][0] = empresa.getRut().toString();
            lista[i][1] = empresa.getNombre();
            lista[i][2] = empresa.getUrl();
            lista[i][3] = String.valueOf(empresa.getTripulantes().length);
            lista[i][4] = String.valueOf(empresa.getBuses().length);
            lista[i][5] = String.valueOf(empresa.getVentas().length);
        }

        return lista;
    }

    public String[][] listLlegadasSalidasTerminal(String nombreTerminal, LocalDate fecha) {
        Optional<Terminal> terminalOpt = findTerminal(nombreTerminal);
        if (!terminalOpt.isPresent()) {
            throw new SistemaVentaPasajesException("No existe terminal con el nombre indicado.");
        }

        Terminal terminal = terminalOpt.get();
        ArrayList<String[]> filas = new ArrayList<>();
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Viaje viaje : terminal.getSalidas()) {
            if (viaje.getFecha().equals(fecha)) {
                filas.add(new String[] {
                        "Salida",
                        viaje.getHora().format(horaFormatter),
                        viaje.getBus().getPatente(),
                        viaje.getBus().getEmpresa().getNombre(),
                        String.valueOf(viaje.getListaPasajeros().length)
                });
            }
        }

        for (Viaje viaje : terminal.getLlegadas()) {
            if (viaje.getFecha().equals(fecha)) {
                filas.add(new String[] {
                        "Llegada",
                        viaje.getFechaHoraTermino().toLocalTime().format(horaFormatter),
                        viaje.getBus().getPatente(),
                        viaje.getBus().getEmpresa().getNombre(),
                        String.valueOf(viaje.getListaPasajeros().length)
                });
            }
        }

        return filas.toArray(new String[0][0]);
    }

    public String[][] listVentasEmpresa(Rut rutEmpresa) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (!empresaOpt.isPresent()) {
            throw new SistemaVentaPasajesException("No existe empresa con el rut indicado.");
        }

        Venta[] ventas = empresaOpt.get().getVentas();
        String[][] lista = new String[ventas.length][4];
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < ventas.length; i++) {
            lista[i][0] = ventas[i].getFecha().format(fechaFormatter);
            lista[i][1] = ventas[i].getTipo().toString();
            lista[i][2] = String.valueOf(ventas[i].getMontoPagado());
            lista[i][3] = ventas[i].getTipoPago();
        }

        return lista;
    }

    public Optional<Empresa> findEmpresa(Rut rut) {
        for (Empresa empresa : empresas) {
            if (empresa.getRut().equals(rut)) {
                return Optional.of(empresa);
            }
        }
        return Optional.empty();
    }

    public Optional<Terminal> findTerminal(String nombre) {
        for (Terminal terminal : terminales) {
            if (terminal.getNombre().equalsIgnoreCase(nombre)) {
                return Optional.of(terminal);
            }
        }
        return Optional.empty();
    }

    public Optional<Terminal> findTerminalPorComuna(String comuna) {
        for (Terminal terminal : terminales) {
            if (terminal.getDireccion().getComuna().equalsIgnoreCase(comuna)) {
                return Optional.of(terminal);
            }
        }
        return Optional.empty();
    }

    public Optional<Bus> findBus(String patente) {
        for (Empresa empresa : empresas) {
            for (Bus bus : empresa.getBuses()) {
                if (bus.getPatente().equalsIgnoreCase(patente)) {
                    return Optional.of(bus);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Conductor> findConductor(IdPersona id, Rut rutEmpresa) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (!empresaOpt.isPresent()) {
            return Optional.empty();
        }

        for (Tripulante t : empresaOpt.get().getTripulantes()) {
            if (t instanceof Conductor && t.getIdPersona().equals(id)) {
                return Optional.of((Conductor) t);
            }
        }
        return Optional.empty();
    }

    public Optional<Auxiliar> findAuxiliar(IdPersona id, Rut rutEmpresa) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (!empresaOpt.isPresent()) {
            return Optional.empty();
        }

        for (Tripulante t : empresaOpt.get().getTripulantes()) {
            if (t instanceof Auxiliar && t.getIdPersona().equals(id)) {
                return Optional.of((Auxiliar) t);
            }
        }
        return Optional.empty();
    }
}
