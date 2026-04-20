import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//Clase temporal para subir el primer commit
//Proyecto de sistema de ventas de pasajes de buses V1 🚌🚌🚌

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String url = "https://matias.me/nsfw/";

                Desktop.getDesktop().browse(new URI(url));
    }
}