package vista;

import controlador.SistemaVentaPasajes;
import java.util.Scanner;

public class UISVP {

    private static UISVP instancia;
    private Scanner sc;
    private SistemaVentaPasajes sistema;

    private UISVP() {
        sc = new Scanner(System.in);
        sistema = SistemaVentaPasajes.getInstancia();
    }

    public static UISVP getInstancia() {

        if (instancia == null) {
            instancia = new UISVP();
        }

        return instancia;
    }

    public void menu() {

        int opcion;

        do {

            System.out.println("===== SISTEMA VENTA PASAJES =====");
            System.out.println("1. Crear Empresa");
            System.out.println("2. Contratar Tripulante");
            System.out.println("3. Crear Terminal");
            System.out.println("4. Crear Cliente");
            System.out.println("5. Crear Bus");
            System.out.println("6. Crear Viaje");
            System.out.println("7. Vender Pasajes");
            System.out.println("8. Pagar Venta Pasajes");
            System.out.println("9. Listar Ventas");
            System.out.println("10. Listar Viajes");
            System.out.println("11. Listar Pasajeros Viaje");
            System.out.println("12. Listar Empresas");
            System.out.println("13. Listar Llegadas/Salidas Terminal");
            System.out.println("14. Listar Ventas Empresa");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {

                case 1:
                    createEmpresa();
                    break;

                case 2:
                    contrataTripulante();
                    break;

                case 3:
                    createTerminal();
                    break;

                case 4:
                    createCliente();
                    break;

                case 5:
                    createBus();
                    break;

                case 6:
                    createViaje();
                    break;

                case 7:
                    vendePasajes();
                    break;

                case 8:
                    pagaVentaPasajes();
                    break;

                case 9:
                    listVentas();
                    break;

                case 10:
                    listViajes();
                    break;

                case 11:
                    listPasajerosViaje();
                    break;

                case 12:
                    listEmpresas();
                    break;

                case 13:
                    listLlegadasSalidasTerminal();
                    break;

                case 14:
                    listVentasEmpresa();
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void createEmpresa() {

    }

    private void contrataTripulante() {

    }

    private void createTerminal() {

    }

    private void createCliente() {

    }

    private void createBus() {

    }

    private void createViaje() {

    }

    private void vendePasajes() {

    }

    private void pagaVentaPasajes() {

    }

    private void listVentas() {

    }

    private void listViajes() {

    }

    private void listPasajerosViaje() {

    }

    private void listEmpresas() {

    }

    private void listLlegadasSalidasTerminal() {

    }

    private void listVentasEmpresa() {

    }
}