import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Viaje {
    private LocalDate fecha;
    private LocalTime hora;
    private int precio;
    private Bus bus;
    private List<Pasaje> pasajes;

    public Viaje(LocalDate fecha, LocalTime hora, int precio, Bus bus) {
        this.fecha = fecha;
        this.hora = hora;
        this.precio = precio;
        this.bus = bus;
        this.pasajes = new ArrayList<>();

        if (this.bus != null) {
            this.bus.addViaje(this);
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Bus getBus() {
        return bus;
    }

    public void addPasaje(Pasaje pasaje) {
        if (pasaje != null) {
            this.pasajes.add(pasaje);
        }
    }

    public String[][] getAsientos() {
        int total = bus.getNroAsientos();
        String[][] asientos = new String[total][2];

        for (int i = 0; i < total; i++) {
            int numAsiento = i + 1;
            asientos[i][0] = String.valueOf(numAsiento);
            asientos[i][1] = "Libre";

            for (Pasaje p : pasajes) {
                if (p.getAsiento() == numAsiento) {
                    asientos[i][1] = "Ocupado";
                    break;
                }
            }
        }
        return asientos;
    }

    public String[][] getListaPasajeros() {
        String[][] lista = new String[pasajes.size()][4];

        for (int i = 0; i < pasajes.size(); i++) {
            Pasaje p = pasajes.get(i);
            Pasajero pas = p.getPasajero();

            lista[i][0] = pas.getIdPasajero();
            lista[i][1] = pas.getNombre();
            lista[i][2] = "-";
            lista[i][3] = "-";
        }
        return lista;
    }

    public boolean existeDisponibilidad() {
        return pasajes.size() < bus.getNroAsientos();
    }

    public int getNroAsientosDisponibles() {
        return bus.getNroAsientos() - pasajes.size();
    }
}