/**
 * Clase principal que gestiona el menú y la interacción con el usuario.
 * @author Javier San Martin
 * @author Benjamin Carrasco
 * @author Genesis Castro
 * @author Beatriz Aguilera
 * @version 1.0
 */
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static SistemaVentaPasajes sistema =  new SistemaVentaPasajes();
    private static Scanner  leer = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    public static void main(String[] args) {

    }
}
