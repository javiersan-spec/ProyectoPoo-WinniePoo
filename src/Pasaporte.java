/**
 * @author Benjamin Jara
 */
public class Pasaporte implements IdPersona {
    private String numero;
    private String nacionalidad;

    public Pasaporte(String num, String nacionalidad) {
        this.numero = num;
        this.nacionalidad = nacionalidad;
    }

    public String getNumero() { return numero; }
    public String getNacionalidad() { return nacionalidad; }

    public static Pasaporte of(String num, String nacionalidad) {
        return new Pasaporte(num, nacionalidad);
    }

    @Override
    public String toString() {
        return this.numero + " " + this.nacionalidad;
    }

    @Override
    public boolean equals(Object otro) {
        if (!(otro instanceof Pasaporte)) return false;
        Pasaporte p = (Pasaporte) otro;
        return this.numero.equals(p.numero) && this.nacionalidad.equals(p.nacionalidad);
    }
}