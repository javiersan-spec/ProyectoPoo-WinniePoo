package controlador;

import excepciones.SVPException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
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
public class ControladorEmpresas implements Serializable {

    private static ControladorEmpresas instancia;
    private ArrayList<Empresa> empresas;
    private ArrayList<Terminal> terminales;
    // lista de todos los buses creados en el sistema
    private ArrayList<Bus> buses;

    private ControladorEmpresas() {
        this.empresas = new ArrayList<>();
        this.terminales = new ArrayList<>();
        this.buses = new ArrayList<>();
    }

    public static ControladorEmpresas getInstancia() {
        if (instancia == null) {
            instancia = new ControladorEmpresas();
        }
        return instancia;
    }

    /**
     * Asigna la instancia persistente leida desde disco.
     */
    public static void setInstanciaPersistente(ControladorEmpresas persistente) {
        instancia = persistente;
    }

    /**
     * Revisa cada objeto del arreglo recibido y agrega los que
     * correspondan a las colecciones de this (empresas, terminales, buses).
     */
    public void setDatosIniciales(Object[] datos) {
        int i = 0;
        while (i < datos.length) {
            String tipo = (String) datos[i];

            if (tipo.equals("EMPRESA")) {
                Rut rut = (Rut) datos[i + 1];
                String nombre = (String) datos[i + 2];
                String url = (String) datos[i + 3];
                if (findEmpresa(rut) == null) {
                    empresas.add(new Empresa(rut, nombre, url));
                }
                i += 4;

            } else if (tipo.equals("TRIPULANTE")) {
                String tipoTrip = (String) datos[i + 1];
                IdPersona id = (IdPersona) datos[i + 2];
                Nombre nombre = (Nombre) datos[i + 3];
                Direccion dir = (Direccion) datos[i + 4];
                Rut rutEmpresa = (Rut) datos[i + 5];

                Empresa empresa = findEmpresa(rutEmpresa);
                if (empresa != null) {
                    if (tipoTrip.equals("A")) {
                        empresa.addAuxiliar(id, nombre, dir);
                    } else {
                        empresa.addConductor(id, nombre, dir);
                    }
                }
                i += 6;

            } else if (tipo.equals("TERMINAL")) {
                String nombre = (String) datos[i + 1];
                Direccion dir = (Direccion) datos[i + 2];
                if (findTerminal(nombre) == null) {
                    terminales.add(new Terminal(nombre, dir));
                }
                i += 3;

            } else if (tipo.equals("BUS")) {
                String patente = (String) datos[i + 1];
                String marca = (String) datos[i + 2];
                String modelo = (String) datos[i + 3];
                int nroAsientos = (Integer) datos[i + 4];
                Rut rutEmpresa = (Rut) datos[i + 5];

                Empresa empresa = findEmpresa(rutEmpresa);
                if (empresa != null && findBus(patente) == null) {
                    Bus bus = new Bus(patente, nroAsientos, empresa);
                    bus.setMarca(marca);
                    bus.setModelo(modelo);
                    empresa.addBus(bus);
                    buses.add(bus);
                }
                i += 6;

            } else if (tipo.equals("CP")) {
                i += 7;
            } else if (tipo.equals("C")) {
                i += 5;
            } else if (tipo.equals("P")) {
                i += 6;
            } else if (tipo.equals("VIAJE")) {
                i += 9;
            } else {
                i++;
            }
        }
    }

    public void createEmpresa(Rut rut, String nombre, String url) {
        // verifico que no exista otra empresa con ese rut
        if (findEmpresa(rut) != null) {
            throw new SVPException("Ya existe empresa con el rut indicado.");
        }
        empresas.add(new Empresa(rut, nombre, url));
    }

    public void createBus(String marca, String modelo, String patente, int nroAsientos, Rut rutEmpresa) {
        Empresa empresa = findEmpresa(rutEmpresa);
        if (empresa == null) {
            throw new SVPException("No existe empresa con el rut indicado.");
        }

        if (findBus(patente) != null) {
            throw new SVPException("Ya existe bus con la patente indicada.");
        }

        Bus bus = new Bus(patente, nroAsientos, empresa);
        bus.setMarca(marca);
        bus.setModelo(modelo);
        empresa.addBus(bus);
        // tambien lo guardo en la lista general de buses
        buses.add(bus);
    }

    public void createTerminal(String nombre, Direccion direccion) {
        if (findTerminal(nombre) != null) {
            throw new SVPException("Ya existe terminal con el nombre indicado.");
        }
        if (findTerminalPorComuna(direccion.getComuna()) != null) {
            throw new SVPException("Ya existe terminal en la comuna indicada.");
        }
        terminales.add(new Terminal(nombre, direccion));
    }

    public void hireConductorForEmpresa(Rut rutEmpresa, IdPersona id, Nombre nombre, Direccion direccion) {
        Empresa empresa = findEmpresa(rutEmpresa);
        if (empresa == null) {
            throw new SVPException("No existe empresa con el rut indicado.");
        }
        if (!empresa.addConductor(id, nombre, direccion)) {
            throw new SVPException("Ya esta contratado conductor/auxiliar con el id dado en la empresa senalada.");
        }
    }

    public void hireAuxiliarForEmpresa(Rut rutEmpresa, IdPersona id, Nombre nombre, Direccion direccion) {
        Empresa empresa = findEmpresa(rutEmpresa);
        if (empresa == null) {
            throw new SVPException("No existe empresa con el rut indicado.");
        }
        if (!empresa.addAuxiliar(id, nombre, direccion)) {
            throw new SVPException("Ya esta contratado auxiliar/conductor con el id dado en la empresa senalada.");
        }
    }

    // usa programacion funcional con streams para generar la lista
    public String[][] listEmpresas() {
        return empresas.stream()
                .map(emp -> new String[]{
                        emp.getRut().toString(),
                        emp.getNombre(),
                        emp.getUrl(),
                        String.valueOf(emp.getTripulantes().length),
                        String.valueOf(emp.getBuses().length),
                        String.valueOf(emp.getVentas().length)
                })
                .toArray(String[][]::new);
    }

    // usa programacion funcional con streams para filtrar y formatear
    public String[][] listLlegadasSalidasTerminal(String nombreTerminal, LocalDate fecha) {
        Terminal terminal = findTerminal(nombreTerminal);
        if (terminal == null) {
            throw new SVPException("No existe terminal con el nombre indicado.");
        }

        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<String[]> filas = new ArrayList<>();

        // agrego las salidas usando streams
        Arrays.stream(terminal.getSalidas())
                .filter(v -> v.getFecha().equals(fecha))
                .forEach(v -> filas.add(new String[]{
                        "Salida",
                        v.getHora().format(horaFmt),
                        v.getBus().getPatente(),
                        v.getBus().getEmpresa().getNombre(),
                        String.valueOf(v.getListaPasajeros().length)
                }));

        // agrego las llegadas usando streams
        Arrays.stream(terminal.getLlegadas())
                .filter(v -> v.getFecha().equals(fecha))
                .forEach(v -> filas.add(new String[]{
                        "Llegada",
                        v.getFechaHoraTermino().toLocalTime().format(horaFmt),
                        v.getBus().getPatente(),
                        v.getBus().getEmpresa().getNombre(),
                        String.valueOf(v.getListaPasajeros().length)
                }));

        return filas.toArray(new String[0][0]);
    }

    // usa programacion funcional con streams para formatear
    public String[][] listVentasEmpresa(Rut rutEmpresa) {
        Empresa empresa = findEmpresa(rutEmpresa);
        if (empresa == null) {
            throw new SVPException("No existe empresa con el rut indicado.");
        }

        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Arrays.stream(empresa.getVentas())
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

    // busca una empresa por su rut usando streams
    public Empresa findEmpresa(Rut rut) {
        Optional<Empresa> resultado = empresas.stream()
                .filter(e -> e.getRut().equals(rut))
                .findFirst();
        return resultado.orElse(null);
    }

    // busca terminal por nombre usando streams
    public Terminal findTerminal(String nombre) {
        Optional<Terminal> resultado = terminales.stream()
                .filter(t -> t.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
        return resultado.orElse(null);
    }

    // busca terminal por comuna usando streams
    public Terminal findTerminalPorComuna(String comuna) {
        Optional<Terminal> resultado = terminales.stream()
                .filter(t -> t.getDireccion().getComuna().equalsIgnoreCase(comuna))
                .findFirst();
        return resultado.orElse(null);
    }

    // busca bus por patente usando streams
    public Bus findBus(String patente) {
        Optional<Bus> resultado = buses.stream()
                .filter(b -> b.getPatente().equalsIgnoreCase(patente))
                .findFirst();
        return resultado.orElse(null);
    }

    // busca un conductor en los tripulantes de una empresa usando streams
    public Conductor findConductor(IdPersona id, Rut rutEmpresa) {
        Empresa empresa = findEmpresa(rutEmpresa);
        if (empresa == null) return null;

        Tripulante[] trips = empresa.getTripulantes();
        ArrayList<Tripulante> lista = new ArrayList<>();
        for (int i = 0; i < trips.length; i++) {
            lista.add(trips[i]);
        }

        Optional<Conductor> resultado = lista.stream()
                .filter(t -> t instanceof Conductor)
                .filter(t -> t.getIdPersona().equals(id))
                .map(t -> (Conductor) t)
                .findFirst();
        return resultado.orElse(null);
    }

    // busca un auxiliar en los tripulantes de una empresa usando streams
    public Auxiliar findAuxiliar(IdPersona id, Rut rutEmpresa) {
        Empresa empresa = findEmpresa(rutEmpresa);
        if (empresa == null) return null;

        Tripulante[] trips = empresa.getTripulantes();
        ArrayList<Tripulante> lista = new ArrayList<>();
        for (int i = 0; i < trips.length; i++) {
            lista.add(trips[i]);
        }

        Optional<Auxiliar> resultado = lista.stream()
                .filter(t -> t instanceof Auxiliar)
                .filter(t -> t.getIdPersona().equals(id))
                .map(t -> (Auxiliar) t)
                .findFirst();
        return resultado.orElse(null);
    }
}
