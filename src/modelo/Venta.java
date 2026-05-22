package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author Benjamin Jara
 */
public class Venta {
    private String idDocumento;
    private TipoDocumento tipo;
    private LocalDate fecha;
    private Cliente cliente;
    private ArrayList<Pasaje> pasajes;
    private Pago pago;

    public Venta(String id, TipoDocumento tipo, LocalDate fec, Cliente cliente) {
        this.idDocumento = id;
        this.tipo = tipo;
        this.fecha = fec;
        this.cliente = cliente;
        this.pasajes = new ArrayList<>();

        if (this.cliente != null) {
            this.cliente.addVenta(this);
        }
    }

    public String getIdDocumento() {
        return idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void createPasaje(int asiento, Viaje viaje, Pasajero pasajero) {
        Pasaje nuevoPasaje = new Pasaje(asiento, viaje, pasajero, this);
        this.pasajes.add(nuevoPasaje);
    }

    public Pasaje[] getPasajes() {
        return pasajes.toArray(new Pasaje[0]);
    }

    public int getMonto() {
        int total = 0;
        for (Pasaje p : pasajes) {
            if (p.getViaje() != null) {
                total += p.getViaje().getPrecio();
            }
        }
        return total;
    }

    public boolean pagaMonto() {
        if (pago != null) {
            return false;
        }

        this.pago = new PagoEfectivo(getMonto());
        return true;
    }

    public boolean pagaMonto(long nroTarjeta) {
        if (pago != null) {
            return false;
        }

        this.pago = new PagoTarjeta(getMonto(), nroTarjeta);
        return true;
    }

    public int getMontoPagado() {
        if (pago == null) {
            return 0;
        }

        return pago.getMonto();
    }

    public String getTipoPago() {
        if (pago == null) {
            return null;
        }
        if (pago instanceof PagoTarjeta) {
            return "Pago Tarjeta";
        }
        return "Pago Efectivo";
    }
}