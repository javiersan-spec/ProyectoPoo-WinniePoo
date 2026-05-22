package modelo;

import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

import java.util.ArrayList;
import java.util.List;

/**
 * * @author Benjamin Jara
 */

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

    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir) {
        // Valida que no exista ningún tripulante (conductor o auxiliar) con el mismo ID
        for (Tripulante t : tripulantes) {
            if (t.getIdPersona().equals(id)) {
                return false;
            }
        }
        Conductor nuevoConductor = new Conductor(id, nom, dir);
        this.tripulantes.add(nuevoConductor);
        return true;
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

    public Bus[] getBuses() {
        return buses.toArray(new Bus[0]);
    }

    public Venta[] getVentas() {
        List<Venta> ventasEmpresa = new ArrayList<>();
        for (Bus b : buses) {
            for (Viaje v : b.getViajes()) {
                Venta[] ventasViaje = v.getVentas();
                if (ventasViaje != null) {
                    for (Venta ven : ventasViaje) {
                        if (!ventasEmpresa.contains(ven)) {
                            ventasEmpresa.add(ven);
                        }
                    }
                }
            }
        }
        return ventasEmpresa.toArray(new Venta[0]);
    }
}
