/**
 * Clase encargada de definir al pasajero
 * @author Javier San Martin
 */
public class Pasajero extends Persona {

    private Nombre nomContacto;
    private String fonoContacto;

    public Pasajero(IdPersona id, Nombre nombre, Nombre nomContacto, String fonoContacto) {
        super(id, nombre);
        this.nomContacto = nomContacto;
        this.fonoContacto = fonoContacto;
    }
    //getters & setters
    public Nombre getNomContacto() {
        return nomContacto;
    }
    public  void setNomContacto(Nombre nomContacto) {
        this.nomContacto = nomContacto;
    }
    public String getFonoContacto() {
        return fonoContacto;
    }
    public void setFonoContacto(String fonoContacto) {
        this.fonoContacto = fonoContacto;
    }
}