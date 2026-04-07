import java.awt.Desktop;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        // 1. Define el enlace que quieres abrir
        String url = "https://matias.me/nsfw/";

        try {
            // 2. Verifica si el sistema soporta la clase Desktop y la acción de navegar
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

                // 3. Abre el enlace en el navegador predeterminado
                Desktop.getDesktop().browse(new URI(url));
                System.out.println("El navegador se ha abierto exitosamente.");

            } else {
                System.out.println("Tu sistema operativo no soporta la acción de abrir el navegador desde Java.");
            }
        } catch (Exception e) {
            // 4. Captura cualquier error (como una URL mal escrita)
            System.out.println("Ocurrió un error al intentar abrir el enlace:");
            e.printStackTrace();
        }
    }
}