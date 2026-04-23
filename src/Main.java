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
            System.out.println("=========================================");
            System.out.println("\n   ...::: Menú principal :::...");
            System.out.println("1) Crear cliente");
            System.out.println("2) Crear Bus");
            System.out.println("3) Crear Viaje");
            System.out.println("4) Vender Pasaje");
            System.out.println("5) Lista de pasajeros");
            System.out.println("6) Lista de ventas");
            System.out.println("7) Lista de viajes");
            System.out.println("8) Consulta viajes disponibles por fecha");
            System.out.println("9) Salir");
            System.out.println("\n------------------------------------------");
            System.out.println("\n   ..:: Ingrese una opción (1-9) ::..");

            if (leerTeclado.hasNextInt()) {
                opcion = leerTeclado.nextInt();
                leerTeclado.nextLine();

                switch (opcion) {
                    case 1:
                        createCliente();
                        break;
                    case 2:
                        createBus();
                        break;
                    case 3:
                        createViaje();
                        break;
                    case 4:
                        vendePasaje();
                        break;
                    case 5:
                        listPasajerosViaje();
                        break;
                    case 6:
                        listVentas();
                        break;
                    case 7:
                        listViajes();
                        break;
                    case 8: //nota: como no está este metodo en el UML lo creamos en el mismo case 8...
                        System.out.println("\n--- CONSULTA VIAJES DISPONIBLES POR FECHA ---");
                        System.out.print("Ingrese fecha (dd/MM/yyyy): ");
                        String fechaStr = leerTeclado.nextLine();
                        LocalDate fechaCons = LocalDate.parse(fechaStr, dateFormatter);
                        String[] horarios = sistema.getHorariosDisponibles(fechaCons);
                        if (horarios.length == 0) {
                            System.out.println("No hay viajes disponibles para esa fecha.");
                        } else {
                            System.out.println("Horarios disponibles para " + fechaStr + ":");
                            for (String h : horarios) {
                                System.out.println("- " + h);
                            }
                        }
                        break;
                    case 9:
                        System.out.println("Cerrando el sistema...");
                        break;
                    default:
                        System.out.println("Error: Opción no válida.");
                }
            } else {
                System.out.println("Error: Debe ingresar un número.");
                leerTeclado.nextLine();
            }
                }
            }
        }
    }
}

