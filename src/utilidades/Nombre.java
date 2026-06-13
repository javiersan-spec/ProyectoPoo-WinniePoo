package utilidades;
/**
 * @author Beatriz Aguilera
 * @version Avance 3
 */
public class Nombre {
    private Tratamiento tratamiento;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Nombre(Tratamiento tratamiento, String nombres,
                  String apellidoPaterno, String apellidoMaterno) {
        this.tratamiento  = tratamiento;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public Tratamiento getTratamiento() { return tratamiento; }
    public String getNombres() { return nombres; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }

    public void setTratamiento(Tratamiento tratamiento)  { this.tratamiento = tratamiento; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidoPaterno(String apellidoPaterno){ this.apellidoPaterno = apellidoPaterno; }
    public void setApellidoMaterno(String apellidoMaterno){ this.apellidoMaterno = apellidoMaterno; }

    @Override
    public String toString() {
        return tratamiento.toString() + " " + nombres
                + " " + apellidoPaterno + " " + apellidoMaterno;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) return true;
        if (otro == null || getClass() != otro.getClass()) return false;
        Nombre n = (Nombre) otro;
        return tratamiento == n.tratamiento
                && nombres.equals(n.nombres)
                && apellidoPaterno.equals(n.apellidoPaterno)
                && apellidoMaterno.equals(n.apellidoMaterno);
    }
}


