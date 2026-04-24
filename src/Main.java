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
                        vendePasajes();
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
                    case 8:
                        consultaViajesPorFecha();
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
    // Opción 1
    private void createCliente() {
        System.out.println("\n ...::: Crear un nuevo cliente :::...");

        System.out.print("rut[1] o Pasaporte[2] : ");
        int tipoId = 1; // Por defecto
        if (leerTeclado.hasNextInt()) {
            tipoId = leerTeclado.nextInt();
            leerTeclado.nextLine();
        } else {
            leerTeclado.nextLine();
        }

        IdPersona idCli = null;

        if (tipoId == 1) {
            System.out.print("R.U.T : ");
            String rutStr = leerTeclado.nextLine();
            idCli = Rut.of(rutStr);
        } else if (tipoId == 2) {
            System.out.print("Pasaporte : ");
            String pasStr = leerTeclado.nextLine();
            System.out.print("Nacionalidad : ");
            String nacStr = leerTeclado.nextLine();
            idCli = Pasaporte.of(pasStr, nacStr);
        } else {
            System.out.println("Error: Opción de documento no válida.");
            return;
        }

        // este if es por si el rut esta mal escrito
        if (idCli == null) {
            System.out.println("Error: Formato de ID inválido.");
            return;
        }

        System.out.print("Sr. [1] o Sra. [2] : ");
        int tipoTratamiento = 1;
        if (leerTeclado.hasNextInt()) {
            tipoTratamiento = leerTeclado.nextInt();
            leerTeclado.nextLine(); // Limpiar el buffer
        } else {
            leerTeclado.nextLine();
        }

        // Asignamos el enum
        Tratamiento tratamiento = (tipoTratamiento == 2) ? Tratamiento.SRA : Tratamiento.SR;

        System.out.print("Nombre: ");
        String nombres = leerTeclado.nextLine();

        System.out.print("Apellido Paterno: ");
        String apPaterno = leerTeclado.nextLine();

        System.out.print("Apellido Materno: ");
        String apMaterno = leerTeclado.nextLine();

        Nombre nom = new Nombre();
        nom.setTratamiento(tratamiento);
        nom.setNombres(nombres);
        nom.setApellidoPaterno(apPaterno);
        nom.setApellidoMaterno(apMaterno);

        System.out.print("Telefono movil: ");
        String fono = leerTeclado.nextLine();

        System.out.print("Email: ");
        String email = leerTeclado.nextLine();

        if (sistema.createCliente(idCli, nom, fono, email)) {
            System.out.println("...::: Cliente guardado exitosamente :::...");
        } else {
            System.out.println("Error al crear el cliente.");
        }
    }
    // Opción 2
    private void createBus() {
        System.out.println("\n...::: Creación de un nuevo BUS :::...");

        System.out.print("Patente : ");
        String patente = leerTeclado.nextLine();

        System.out.print("Marca : ");
        String marca = leerTeclado.nextLine();

        System.out.print("Modelo : ");
        String modelo = leerTeclado.nextLine();

        System.out.print("Número de asientos : ");
        int asientos = 0;
        if (leerTeclado.hasNextInt()) {
            asientos = leerTeclado.nextInt();
            leerTeclado.nextLine();
        } else {
            System.out.println("Error: El número de asientos debe ser un valor numérico.");
            leerTeclado.nextLine();
            return;
        }

        if (sistema.createBus(patente, marca, modelo, asientos)) {
            System.out.println("El bus ha sido creado con éxito.");
        } else {
            System.out.println("Error: No se pudo crear el bus. Ya existe un bus registrado con la patente " + patente + ".");
        }
    }
    // Opción 3
    private void createViaje() {
        System.out.println("\n....::: Creación de un nuevo viaje :::...");

        System.out.print("Fecha[dd/mm/yyyy] : ");
        String fechaStr = leerTeclado.nextLine();
        LocalDate fecha = LocalDate.parse(fechaStr, dateFormatter);

        System.out.print("Hora[hh:mm] : ");
        String horaStr = leerTeclado.nextLine();
        LocalTime hora = LocalTime.parse(horaStr, timeFormatter);

        System.out.print("Precio : ");
        int precio = 0;
        if (leerTeclado.hasNextInt()) {
            precio = leerTeclado.nextInt();
            leerTeclado.nextLine();
        } else {
            System.out.println("Error: El precio debe ser un número.");
            leerTeclado.nextLine();
            return;
        }

        System.out.print("Patente Bus : ");
        String patente = leerTeclado.nextLine();

        Bus bus = sistema.findBus(patente);
        if (bus == null) {
            System.out.println("Error: No existe un bus con la patente ingresada.");
            return;
        }

        if (sistema.createViaje(fecha, hora, precio, patente)) {
            System.out.println("\n...::: El viaje ha sido creado con éxito :::...");
        } else {
            System.out.println("Error: Ya existe un viaje para la fecha, hora y bus ingresados.");
        }
    }
    // Opción 4
    private void vendePasajes() {
        System.out.println("\n....:::: Venta de pasajes :::....");
        System.out.println(" ::::: Datos de la Venta ");

        System.out.print("ID Documento : ");
        String idDoc = leerTeclado.nextLine();

        System.out.print("Tipo documento: [1] Boleta [2] Factura: ");
        int opcTipo = leerTeclado.nextInt();
        leerTeclado.nextLine(); // Limpiar buffer
        TipoDocumento tipoDoc = (opcTipo == 2) ? TipoDocumento.FACTURA : TipoDocumento.BOLETA;

        // el PDF pide la fecha de venta
        System.out.print("Fecha de venta [dd/mm/yyyy] : ");
        String fechaVentaStr = leerTeclado.nextLine();
        LocalDate fechaVenta = LocalDate.parse(fechaVentaStr, dateFormatter);

        System.out.println(" :::: Datos del cliente ");
        System.out.print("Rut [1] o Pasaporte [2] : ");
        int tipoId = leerTeclado.nextInt();
        leerTeclado.nextLine();

        IdPersona idCli = null;
        if (tipoId == 1) {
            System.out.print("R.U.T : ");
            idCli = Rut.of(leerTeclado.nextLine());
        } else {
            System.out.print("Pasaporte : ");
            String pas = leerTeclado.nextLine();
            System.out.print("Nacionalidad : ");
            idCli = Pasaporte.of(pas, leerTeclado.nextLine());
        }

        System.out.print("Nombre Cliente : ");
        String nombreClienteInput = leerTeclado.nextLine();

        Cliente cliente = sistema.findCliente(idCli);
        if (cliente == null) {
            System.out.println("Error: La venta no se puede concretar. El cliente no existe.");
            return;
        }

        // Asumimos que iniciaVenta devuelve false si el ID de documento ya existe
        if (!sistema.iniciaVenta(idDoc, tipoDoc, fechaVenta, idCli)) {
            System.out.println("Error: La venta no se puede concretar. El ID de documento ya existe.");
            return;
        }

        System.out.println("\n::: Pasajes a vender ");
        System.out.print("Cantidad de Pasajes : ");
        int cantidadPasajes = leerTeclado.nextInt();
        leerTeclado.nextLine();

        System.out.print("Fecha de viaje[dd/mm/yyyy] : ");
        String fechaViajeStr = leerTeclado.nextLine();
        LocalDate fechaViaje = LocalDate.parse(fechaViajeStr, dateFormatter);

        // Mostrar tabla de viajes
        System.out.println("\n:::: Listado de horarios disponibles");
        System.out.println("  *---------*---------*---------*-----------*");
        System.out.println("  | BUS     |  SALIDA | VALOR   | ASIENTOS  |");
        System.out.println("  |---------+---------+---------+-----------|");

        String[][] viajesDisponibles = sistema.getHorariosDisponibles(fechaViaje);
        if (viajesDisponibles.length == 0) {
            System.out.println("No hay viajes disponibles para esta fecha.");
            return;
        }

        for (int i = 0; i < viajesDisponibles.length; i++) {
            // Se asume que el String viene formateado: "PATENTE | HORA | PRECIO | DISPONIBLES"
            System.out.println((i + 1) + " | " + viajesDisponibles[i]);
            System.out.println("  |---------+---------+---------+-----------|");
        }
        System.out.println("  *---------*---------*---------*-----------*");

        System.out.print("Seleccione un viaje [1.." + viajesDisponibles.length + "] : ");
        int seleccionViaje = leerTeclado.nextInt();
        leerTeclado.nextLine();

        System.out.print("Confirme la Patente del bus seleccionado: ");
        String patenteBus = leerTeclado.nextLine();
        System.out.print("Confirme la Hora del viaje seleccionado (HH:mm): ");
        String horaViajeStr = leerTeclado.nextLine();
        LocalTime horaViaje = LocalTime.parse(horaViajeStr, timeFormatter);

        // seleccion de asientos
        System.out.println("\n:::: Asientos disponibles para el viaje seleccionado ");
        String[][] matrizAsientos = sistema.listAsientosDeViaje(fechaViaje, horaViaje, patenteBus);

        // Dibujamos una matriz (4 por fila + pasillo)
        System.out.println("*---*---*---*---*---*");
        for (int i = 0; i < matrizAsientos.length; i += 4) {
            String a1 = matrizAsientos[i][1].equals("LIBRE") ? matrizAsientos[i][0] : "*";
            String a2 = (i + 1 < matrizAsientos.length) ? (matrizAsientos[i+1][1].equals("LIBRE") ? matrizAsientos[i+1][0] : "*") : " ";
            String a3 = (i + 2 < matrizAsientos.length) ? (matrizAsientos[i+2][1].equals("LIBRE") ? matrizAsientos[i+2][0] : "*") : " ";
            String a4 = (i + 3 < matrizAsientos.length) ? (matrizAsientos[i+3][1].equals("LIBRE") ? matrizAsientos[i+3][0] : "*") : " ";

            System.out.printf("| %2s | %2s |     | %2s | %2s |\n", a1, a2, a4, a3);
            System.out.println("|---+---+---+---+---|");
        }
        System.out.println("*---*---*---*---*---*");

        System.out.print("Seleccione sus asientos [separe por \",\"] : ");
        String asientosInput = leerTeclado.nextLine();
        String[] asientosSeleccionados = asientosInput.split(",");

        // 1. CREAMOS EL ARREGLO PARA GUARDAR LOS BOLETOS ANTES DEL FOR
        String[] boletosGenerados = new String[cantidadPasajes];

        for (int i = 0; i < cantidadPasajes; i++) {
            int numAsiento = Integer.parseInt(asientosSeleccionados[i].trim());
            System.out.println("\n:::: Datos Pasajero " + (i + 1));
            System.out.print("Rut[1] o Pasaporte[2] : ");
            int tIdP = leerTeclado.nextInt();
            leerTeclado.nextLine();

            IdPersona idPas = null;
            String idParaBoleto = ""; // Guardaremos el rut o pasaporte para imprimirlo luego

            if (tIdP == 1) {
                System.out.print("R.U.T : ");
                idParaBoleto = leerTeclado.nextLine();
                idPas = Rut.of(idParaBoleto);
            } else {
                System.out.print("Pasaporte : ");
                idParaBoleto = leerTeclado.nextLine();
                System.out.print("Nacionalidad : ");
                idPas = Pasaporte.of(idParaBoleto, leerTeclado.nextLine());
            }

            // Recuperar o pedir el nombre del pasajero (necesario para el boleto)
            String nomSistema = sistema.getNombrePasajero(idPas);
            String nombreParaBoleto = nomSistema;

            if (nomSistema == null || nomSistema.isEmpty()) {
                System.out.println("Pasajero no registrado. Ingrese sus datos:");

                Nombre nomP = new Nombre();
                System.out.print("Nombre Pasajero: ");
                nombreParaBoleto = leerTeclado.nextLine(); // Lo guardamos para el boleto
                nomP.setNombres(nombreParaBoleto);

                System.out.print("Teléfono: "); String fonP = leerTeclado.nextLine();

                Nombre nomC = new Nombre();
                System.out.print("Nombre de Contacto: "); nomC.setNombres(leerTeclado.nextLine());

                System.out.print("Teléfono de Contacto: "); String fonC = leerTeclado.nextLine();

                sistema.createPasajero(idPas, nomP, fonP, nomC, fonC);
            }

            sistema.vendePasaje(idDoc, fechaViaje, horaViaje, patenteBus, numAsiento, idPas);
            System.out.println(":::: pasaje agregado exitosamente");

            boletosGenerados[i] = imprimirBoleto(fechaViajeStr, horaViajeStr, patenteBus, numAsiento, idParaBoleto, nombreParaBoleto);
        }

        System.out.println("\n:::: Monto total de la venta: $" + sistema.getMontoVenta(idDoc, tipoDoc));

        System.out.println("\n::: imprimiendo los pasajes ");
        for (int i = 0; i < boletosGenerados.length; i++) {
            System.out.println(boletosGenerados[i]);
        }
    }
    // Opción 5
    private void listPasajerosViaje(){}
    // Opción 6
    private void listVentas(){}
    // Opción 7
    private void listViajes(){}
    // Opción 8
    // Metodo privado extra para mayor conveniencia lo implementamos.
    private void consultaViajesPorFecha() {
        System.out.println("\n...::: Viajes Disponibles por fecha :::...");
        System.out.print("Ingrese fecha (dd/MM/yyyy): ");
        String fechaStr = leerTeclado.nextLine();

        LocalDate fechaCons = LocalDate.parse(fechaStr, dateFormatter);

        // Obtenemos la matriz del sistema
        String[][] horarios = sistema.getHorariosDisponibles(fechaCons);

        if (horarios.length == 0) {
            System.out.println("No hay viajes disponibles para esa fecha.");
        } else {
            System.out.println("\nHorarios disponibles para " + fechaStr + ":");
            System.out.println("BUS | SALIDA | VALOR | ASIENTOS");
            for (int i = 0; i < horarios.length; i++) {

                System.out.println(String.join(" | ", horarios[i]));
            }
        }
    }
    // Metodo privado extra para mayor conveniencia lo implementamos.
    private String imprimirBoleto(String fecha, String hora, String patente, int asiento, String idPasajero, String nombrePasajero) {
        // Generamos un número de pasaje único basado en el milisegundo actual
        // obtenido de: https://es.sourcetrail.com/Java/java-obtiene-milisegundos-actuales/

        long numPasaje = System.currentTimeMillis() + (int)(Math.random() * 100);

        return "---------------- PASAJE ----------------\n" +
                "NUMERO DE PASAJE : " + numPasaje + "\n" +
                "FECHA DE VIAJE   : " + fecha + "\n" +
                "HORA DE VIAJE    : " + hora + "\n" +
                "PATENTE BUS      : " + patente + "\n" +
                "ASIENTO          : " + asiento + "\n" +
                "RUT/PASAPORTE    : " + idPasajero + "\n" +
                "NOMBRE PASAJERO  : " + nombrePasajero + "\n" +
                "-------------------------------------------";
    }
}
