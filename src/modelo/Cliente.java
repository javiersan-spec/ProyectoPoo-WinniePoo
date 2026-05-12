package modelo;

import utilidades.IdPersona;
import utilidades.Nombre;

/**
 * Clase encargada de generar un cliente dentro del sistema de venta de pasajes.
 * Hereda los datos basicos de persona y agrega el email y el historial de ventas
 * realizadas por el Cliente.
 * @author Genesis Castro
 */
public class Cliente extends Persona {

    private String email;
    private Venta[] ventas;

    public Cliente(IdPersona id, Nombre nom, String email) {
        super(id, nom);
        this.email  = email;
        this.ventas = new Venta[0];
    }

    public String getEmail() { return email; }
    public Venta[] getVentas() { return ventas; }

    public void setEmail(String email) { this.email = email; }

    public void addVenta(Venta venta) {
        Venta[] nuevasVentas = new Venta[ventas.length + 1];
        for (int i = 0; i < ventas.length; i++) {
            nuevasVentas[i] = ventas[i];
        }
        nuevasVentas[ventas.length] = venta;
        ventas = nuevasVentas;
    }
}