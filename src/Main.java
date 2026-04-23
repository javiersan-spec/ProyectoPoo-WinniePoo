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
    private static SistemaVentaPasajes sistema;
    private static Scanner leerTeclado;
    private static DateTimeFormatter dateFormatter;
    private static DateTimeFormatter timeFormatter;

    public Main () {
        sistema = new SistemaVentaPasajes();
        leerTeclado = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }
    public static void main(String[] args) {
        Main app = new Main();
        app.menu();
    }
    private void menu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n...::: Menú principal :::...");
            System.out.println("1. Crear cliente");
            System.out.println("2. Crear Bus");
            System.out.println("3. Crear Viaje");
            System.out.println("4. Lista Venta de Pasajes");
            System.out.println("5. Lista de pasajeros");
            System.out.println("6. Lista de ventas");
            System.out.println("0. Salir");
            System.out.print("Ingrese opcion: ");
        }
    }
}

