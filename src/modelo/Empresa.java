package modelo;

/**
 * @author Benjamin Jara
 */
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;

import java.util.ArrayList;

public class Empresa {
    private Rut rut;
    private String nombre;
    private String url;
    private ArrayList<Bus> buses;
    private ArrayList<Tripulante> tripulantes;

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

    public boolean addConductor(IdPersona id, Nombre nom, Direccion dir) {
        // reviso si ya esta contratado con ese id
        for (int i = 0; i < tripulantes.size(); i++) {
            if (tripulantes.get(i).getIdPersona().equals(id)) {
                return false;
            }
        }
        Conductor nuevoConductor = new Conductor(id, nom, dir);
        this.tripulantes.add(nuevoConductor);
        return true;
    }

    public boolean addAuxiliar(IdPersona id, Nombre nom, Direccion dir) {
        for (int i = 0; i < tripulantes.size(); i++) {
            if (tripulantes.get(i).getIdPersona().equals(id)) {
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


    private Conductor findConductor(IdPersona id) {
        for (int i = 0; i < tripulantes.size(); i++) {
            if (tripulantes.get(i) instanceof Conductor && tripulantes.get(i).getIdPersona().equals(id)) {
                return (Conductor) tripulantes.get(i);
            }
        }
        return null;
    }

    private Auxiliar findAuxiliar(IdPersona id) {
        for (int i = 0; i < tripulantes.size(); i++) {
            if (tripulantes.get(i) instanceof Auxiliar && tripulantes.get(i).getIdPersona().equals(id)) {
                return (Auxiliar) tripulantes.get(i);
            }
        }
        return null;
    }

    public Venta[] getVentas() {
        ArrayList<Venta> ventasEmpresa = new ArrayList<>();
        for (int i = 0; i < buses.size(); i++) {
            Viaje[] viajesBus = buses.get(i).getViajes();
            for (int j = 0; j < viajesBus.length; j++) {
                Venta[] ventasViaje = viajesBus[j].getVentas();
                for (int k = 0; k < ventasViaje.length; k++) {
                    if (!ventasEmpresa.contains(ventasViaje[k])) {
                        ventasEmpresa.add(ventasViaje[k]);
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

