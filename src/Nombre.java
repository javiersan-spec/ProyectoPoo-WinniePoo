/**
 * Clase encargada de recibir los datos del cliente
 * @author  Beatriz Aguilera
 */


public class Nombre {
    private Tratamiento tratamiento;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    public Nombre() {
    }
    public Tratamiento getTratamiento() {
        return tratamiento;
    }
    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    @Override
    public String toString() {
        return tratamiento + " " + nombres + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Nombre n = (Nombre) otro;
        if (nombres == null || apellidoPaterno == null || apellidoMaterno == null)
            return false;
        return nombres.equals(n.nombres) &&
                apellidoPaterno.equals(n.apellidoPaterno) &&
                apellidoMaterno.equals(n.apellidoMaterno);
    }
}

