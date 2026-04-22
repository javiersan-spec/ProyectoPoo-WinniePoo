/**
 * Clase encargada de definir a la persona y sus datos
 *  @author Javier San Martin
 */
public class Persona {
    private IdPersona idPersona;
    private Nombre nombre;
    private String telefono;

    public Persona(IdPersona id, Nombre nombre) {
        this.idPersona = id;
        this.nombre = nombre;
    }

    public IdPersona getIdPersona() {
        return idPersona;
    }

    public Nombre getNombreCompleto() {
        return nombre;
    }

    public void setNombreCompleto(Nombre nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Pasajero:(" + "id= " + idPersona + ", nombre= " + nombre + ", telefono= " + telefono + ")";
    }

    public boolean equals(Object o) {
        if (o instanceof Persona) {
            Persona p = (Persona) o;
        }
        return false;
    }
}
