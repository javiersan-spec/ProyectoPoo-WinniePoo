package modelo;
/**
 * @author Benjamin Jara
 */
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;
    private List<Bus> buses;
    private List<Tripulante> tripulantes;

    public Empresa(Rut rut, String nombre, String url) {
        this.rut = rut;
        this.nombre = nombre;
        this.url = url;
        this.buses = new ArrayList<>();
        this.tripulantes = new ArrayList<>();
    }

    public Empresa(Rut rut, String nombre) {
        this(rut, nombre, "");
    }

    public Rut getRut() {
        return rut;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void addBus(Bus bus) {
        if (bus != null) {
            this.buses.add(bus);
        }
    }

    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir, boolean lic) {
        for (Tripulante t : tripulantes) {
            if (t.getIdPersona().equals(id)) {
                return false;
            }
        }
        Conductor nuevoConductor = new Conductor(id, nom, dir);
        this.tripulantes.add(nuevoConductor);
        return true;
    }

    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir) {
        return addConductor(id, nom, dir, true);
    }

    public boolean addAuxiliar(IdPersona id, Nombre nom, Direccion dir) {
        for (Tripulante t : tripulantes) {
            if (t.getIdPersona().equals(id)) {
                return false;
            }
        }
        Auxiliar nuevoAuxiliar = new Auxiliar(id, nom, dir);
        this.tripulantes.add(nuevoAuxiliar);
        return true;
    }

    public Tripulante[] getTripulantes() {
        return tripulantes.toArray(new Tripulante[0]);
    }

    public Conductor findConductor(IdPersona id) {
        for (Tripulante tripulante : tripulantes) {
            if (tripulante instanceof Conductor && tripulante.getIdPersona().equals(id)) {
                return (Conductor) tripulante;
            }
        }

        return null;
    }

    public Auxiliar findAuxiliar(IdPersona id) {
        for (Tripulante tripulante : tripulantes) {
            if (tripulante instanceof Auxiliar && tripulante.getIdPersona().equals(id)) {
                return (Auxiliar) tripulante;
            }
        }

        return null;
    }

    public Venta[] getVentas() {
        List<Venta> ventasEmpresa = new ArrayList<>();
        for (Bus b : buses) {
            for (Viaje v : b.getViajes()) {
                Venta[] ventasViaje = v.getVentas();
                for (Venta ven : ventasViaje) {
                    if (!ventasEmpresa.contains(ven)) {
                        ventasEmpresa.add(ven);
                    }
                }
            }
        }
        return ventasEmpresa.toArray(new Venta[0]);
    }

    public Bus[] getBuses() {
        return buses.toArray(new Bus[0]);
    }
}
