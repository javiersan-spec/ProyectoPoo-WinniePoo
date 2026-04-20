public class Pasajero extends Persona {

    private Nombre nomContacto;
    private String fonoContacto;

    public Pasajero(String id, Nombre nombre, String telefono,
                    Nombre nomContacto, String fonoContacto) {
        super(id, nombre, telefono);
        this.nomContacto = nomContacto;
        this.fonoContacto = fonoContacto;
    }

    public Nombre getNomContacto() {
        return nomContacto;
    }

    public String getFonoContacto() {
        return fonoContacto;
    }
}