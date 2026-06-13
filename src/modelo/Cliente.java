package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de generar un cliente dentro del sistema de venta de pasajes.
 * Hereda los datos basicos de persona y agrega el email y el historial de ventas
 * realizadas por el Cliente.
 * @author Genesis Castro
 * Avance 2
 */
public class Cliente extends Persona {

    private String email;
    private List<Venta> ventas;

    public Cliente(IdPersona id, Nombre nom, String email) {
        super(id, nom);
        this.email = email;
        this.ventas = new ArrayList<>();
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Venta[] getVentas() {
        return ventas.toArray(new Venta[0]);
    }

    public void addVenta(Venta venta) {
        if (venta != null && !ventas.contains(venta)) {
            ventas.add(venta);
        }
    }
}

