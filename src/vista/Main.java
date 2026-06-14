package vista;
/**
 * Clase principal que gestiona el arranque de la aplicación.
 * @author Javier San Martin
 * @author Benjamin Carrasco
 * @author Genesis Castro
 * @author Beatriz Aguilera
 * @author Benjamin Jara
 * @version 3.0
 */
public class Main {

    public static void main(String[] args) {

        UISVP interfaz = UISVP.getInstancia();
        interfaz.menu();
    }
}
