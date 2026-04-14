/*
 * Clase que le da identidad a la persona
 * @author Javier San Martin <javier.san1901@alumnos.ubiobio.cl>
 */

public class Pasajero {
    private Nombre nomContacto;
    private String fonoContacto;
    private Persona persona;

    public Nombre getNomContacto(){
        return nomContacto;
    }
    public void setNomContacto(){
        this.nomContacto = nomContacto;
    }
    public String getFonoContacto(){
        return fonoContacto;
    }
    public void setFonoContacto(){
        this.fonoContacto = fonoContacto;
    }
}
