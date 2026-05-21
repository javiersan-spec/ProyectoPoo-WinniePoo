package vista;

import controlador.SistemaVentaPasajes;
import java.util.Scanner;
import controlador.ControladorEmpresas;
import utilidades.Rut;

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

            System.out.println("=======================================");
            System.out.println("    ...:::: Menú principal ::::...");
            System.out.println(" 1) Crear Empresa");
            System.out.println(" 2) Contratar tripulante");
            System.out.println(" 3) Crear terminal");
            System.out.println(" 4) Crear cliente");
            System.out.println(" 5) Crear bus");
            System.out.println(" 6) Crear viaje");
            System.out.println(" 7) Vender Pasajes");
            System.out.println(" 8) Listar Ventas");
            System.out.println(" 9) Listar Viajes");
            System.out.println("10) Listar Pasajeros de viaje");
            System.out.println("11) Listar empresas");
            System.out.println("12) Listar llegadas/salidas de terminal");
            System.out.println("13) Listar ventas de empresa");
            System.out.println("14) Salir");
            System.out.println("---------------------------------------");
            System.out.print("..:: Ingrese número de opción: ");

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
        System.out.println("\n...:::: Creando una nueva Empresa ::::...");

        System.out.print("R.U.T (12345678-9) : ");
        String rutStr = sc.nextLine();
        utilidades.Rut rut = utilidades.Rut.of(rutStr);

        if (rut == null) {
            System.out.println("Error: Formato de RUT inválido.");
            return;
        }

        System.out.print("Nombre : ");
        String nombreEmpresa = sc.nextLine();

        utilidades.Nombre nom = new utilidades.Nombre(null, nombreEmpresa, "", "");

        System.out.print("url : ");
        String url = sc.nextLine();

        controlador.ControladorEmpresas controladorEmp = controlador.ControladorEmpresas.getInstancia();
        boolean exito = controladorEmp.createEmpresa(rut, nom, url);

        if (exito) {
            System.out.println("...:::: Empresa guardada exitosamente ::::...");
        } else {
            System.out.println("Error: Ya existe una empresa registrada con ese RUT.");
        }
    }

    private void contrataTripulante() {
        System.out.println("\n...:::: Contratando un nuevo tripulante ::::...");

        System.out.println(":::: Datos de la empresa");
        System.out.print("        R.U.T (12345678-9): ");
        String rutEmpStr = sc.nextLine();
        utilidades.Rut rutEmp = utilidades.Rut.of(rutEmpStr);

        if (rutEmp == null) {
            System.out.println("Error: RUT de empresa inválido.");
            return;
        }

        controlador.ControladorEmpresas controlEmp = controlador.ControladorEmpresas.getInstancia();
        java.util.Optional<modelo.Empresa> optEmpresa = controlEmp.findEmpresa(rutEmp);

        if (!optEmpresa.isPresent()) {
            System.out.println("Error: Empresa no encontrada en el sistema.");
            return;
        }

        modelo.Empresa empresa = optEmpresa.get();

        System.out.println(":::: Datos tripulante");
        System.out.print("Auxiliar[1] o Conductor[2]: ");
        int tipo = Integer.parseInt(sc.nextLine());

        System.out.print("Rut[1] o Pasaporte[2]: ");
        String rutTripStr = sc.nextLine();
        System.out.println("R.U.T (12345678-9): ");
        utilidades.Rut idTrip = utilidades.Rut.of(rutTripStr);
        if (idTrip == null) {
            System.out.println("Error: RUT de tripulante inválido.");
            return;
        }

        System.out.print("Nombres : ");
        String nombres = sc.nextLine();
        System.out.print("Apellido Paterno : ");
        String apPaterno = sc.nextLine();
        System.out.print("Apellido Materno : ");
        String apMaterno = sc.nextLine();

        utilidades.Nombre nombre = new utilidades.Nombre(utilidades.Tratamiento.SR, nombres, apPaterno, apMaterno);

        System.out.print("Calle : ");
        String calle = sc.nextLine();
        System.out.print("Número : ");
        int numero = Integer.parseInt(sc.nextLine());
        System.out.print("Comuna : ");
        String comuna = sc.nextLine();

        utilidades.Direccion dir = new utilidades.Direccion(calle, numero, comuna);

        boolean exito = false;
        if (tipo == 1) {
            // El último parámetro es "true" por el boolean lic (licencia) que pide tu método
            exito = empresa.addConductor(idTrip, nombre, dir, true);
        } else if (tipo == 2) {
            exito = empresa.addAuxiliar(idTrip, nombre, dir);
        } else {
            System.out.println("Error: Tipo de tripulante inválido.");
            return;
        }

        if (exito) {
            System.out.println("...:::: Auxiliar contratado exitosamente ::::..."); // asi dice el PDF, dice auxiliar en vez de tripulante
        } else {
            System.out.println("Error: El tripulante ya se encuentra registrado en esta empresa.");
        }
    }

    private void createTerminal() {
        System.out.println("\n...:::: Creando un nuevo Terminal ::::...");

        System.out.print("Nombre : ");
        String nombre = sc.nextLine();
        System.out.print("Calle: ");
        String calle = sc.nextLine();
        System.out.print("Número : ");
        int numero = Integer.parseInt(sc.nextLine());
        System.out.print("Comuna : ");
        String comuna = sc.nextLine();

        utilidades.Direccion dir = new utilidades.Direccion(calle, numero, comuna);

        boolean exito = sistema.createTerminal(nombre, dir);

        if (exito) {
            System.out.println("...:::: Terminal guardado exitosamente ::::...");
        } else {
            System.out.println("Error: Ya existe un terminal con ese nombre.");
        }
    }

    private void createCliente() {

    }

    private void createBus() {
        System.out.println("\n...:::: Creando un nuevo Bus ::::...");

        System.out.print("Patente (ABCD-12) : ");
        String patente = sc.nextLine();

        System.out.print("Marca : ");
        String marca = sc.nextLine();

        System.out.print("Modelo : ");
        String modelo = sc.nextLine();

        System.out.print("Número de asientos : ");
        int nroAsientos;
        try {
            nroAsientos = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Número de asientos inválido. Operación cancelada.");
            return;
        }
        System.out.println(":::: Dato de la empresa");
        System.out.print("R.U.T : ");
        String rutStr = sc.nextLine();
        utilidades.Rut rutEmpresa = utilidades.Rut.of(rutStr);

        if (rutEmpresa == null) {
            System.out.println("RUT inválido. Operación cancelada.");
            return;
        }
        boolean exito = controlador.ControladorEmpresas.getInstancia().createBus(rutEmpresa, patente, marca, modelo, nroAsientos);

        if (exito) {
            System.out.println("...:::: Bus guardado exitosamente ::::...");
        } else {
            System.out.println("Error: No se pudo crear el bus. Verifique si la empresa existe o si el bus ya está registrado.");
        }
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