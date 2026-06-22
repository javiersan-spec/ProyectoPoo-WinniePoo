package vista;

import controlador.ControladorEmpresas;
import controlador.SistemaVentaPasajes;
import excepciones.SVPException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import modelo.TipoDocumento;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Pasaporte;
import utilidades.Rut;
import utilidades.Tratamiento;

/**
 * Interfaz de usuario del sistema de venta de pasajes.
 * @author Javier San Martin
 * @author Benjamin Carrasco
 * @author Benjamin Jara
 * @author Genesis Castro
 * @author Beatriz Aguilera
 */
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
            System.out.println("    ...:::: Menu principal ::::...");
            System.out.println(" 1) Crear empresa");
            System.out.println(" 2) Contratar tripulante");
            System.out.println(" 3) Crear terminal");
            System.out.println(" 4) Crear cliente");
            System.out.println(" 5) Crear bus");
            System.out.println(" 6) Crear viaje");
            System.out.println(" 7) Vender pasajes");
            System.out.println(" 8) Listar ventas");
            System.out.println(" 9) Listar viajes");
            System.out.println("10) Listar pasajeros de viaje");
            System.out.println("11) Listar empresas");
            System.out.println("12) Listar llegadas/salidas de terminal");
            System.out.println("13) Listar ventas de empresa");
            System.out.println("14) Generar pasajes venta");
            System.out.println("15) Leer datos iniciales");
            System.out.println("16) Guardar datos del sistema");
            System.out.println("17) Leer datos del sistema");
            System.out.println("18) Salir");
            System.out.println("---------------------------------------");
            System.out.print("..:: Ingrese numero de opcion: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1: createEmpresa(); break;
                case 2: contrataTripulante(); break;
                case 3: createTerminal(); break;
                case 4: createCliente(); break;
                case 5: createBus(); break;
                case 6: createViaje(); break;
                case 7: vendePasajes(); break;
                case 8: listVentas(); break;
                case 9: listViajes(); break;
                case 10: listPasajerosViaje(); break;
                case 11: listEmpresas(); break;
                case 12: listLlegadasSalidasTerminal(); break;
                case 13: listVentasEmpresa(); break;
                case 14: generatePasajesVenta(); break;
                case 15: readDatosIniciales(); break;
                case 16: saveDatosSistema(); break;
                case 17: readDatosSistema(); break;
                case 18: System.out.println("Saliendo..."); break;
                default: System.out.println("Opcion invalida");
            }

        } while (opcion != 18);
    }

    private void createEmpresa() {
        System.out.println("\n...:::: Creando una nueva Empresa ::::...\n");
        System.out.printf("%25s : ", "R.U.T");
        Rut rut = Rut.of(sc.nextLine());
        if (rut == null) {
            System.out.println("Error: Formato de RUT invalido.");
            return;
        }

        System.out.printf("%25s : ", "Nombre");
        String nombreEmpresa = sc.nextLine();
        System.out.printf("%25s : ", "url");
        String url = sc.nextLine();

        try {
            ControladorEmpresas.getInstancia().createEmpresa(rut, nombreEmpresa, url);
            System.out.println("\n...::::: Empresa guardada exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void contrataTripulante() {
        System.out.println("\n...:::: Contatando un nuevo Tripulante ::::....\n");
        System.out.println(":::: Dato de la Empresa");
        System.out.printf("%25s : ", "R.U.T");
        Rut rutEmp = Rut.of(sc.nextLine());
        if (rutEmp == null) {
            System.out.println("Error: RUT de empresa invalido.");
            return;
        }

        System.out.println("\n:::: Datos tripulante");
        System.out.print("Auxiliar[1] o Conductor[2] : ");
        int tipo = leerEntero();

        IdPersona idTrip = leerIdPersona();
        if (idTrip == null) return;

        Nombre nombre = leerNombre();
        Direccion dir = leerDireccion();

        try {
            if (tipo == 1) {
                ControladorEmpresas.getInstancia().hireAuxiliarForEmpresa(rutEmp, idTrip, nombre, dir);
                System.out.println("\n...::::: Auxiliar contratado exitosamente ::::....");
            } else if (tipo == 2) {
                ControladorEmpresas.getInstancia().hireConductorForEmpresa(rutEmp, idTrip, nombre, dir);
                System.out.println("\n...::::: Conductor contratado exitosamente ::::....");
            } else {
                System.out.println("Error: Tipo de tripulante invalido.");
            }
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createTerminal() {
        System.out.println("\n...:::: Creando un nuevo Terminal ::::...\n");
        System.out.printf("%25s : ", "Nombre");
        String nombre = sc.nextLine();
        System.out.printf("%25s : ", "Calle");
        String calle = sc.nextLine();
        System.out.printf("%25s : ", "Numero");
        int numero = leerEntero();
        System.out.printf("%25s : ", "Comuna");
        String comuna = sc.nextLine();
        Direccion dir = new Direccion(calle, numero, comuna);

        try {
            ControladorEmpresas.getInstancia().createTerminal(nombre, dir);
            System.out.println("\n...::::: Terminal guardado exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createCliente() {
        System.out.println("\n...:::: Creando un nuevo Cliente ::::...");
        IdPersona id = leerIdPersona();
        if (id == null) return;

        Nombre nombre = leerNombre();
        System.out.print("Telefono: ");
        String telefono = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        try {
            sistema.createCliente(id, nombre, telefono, email);
            System.out.println("...:::: Cliente guardado exitosamente ::::...");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createBus() {
        System.out.println("\n...:::: Creando un nuevo Bus ::::...\n");
        System.out.printf("%25s : ", "Patente");
        String patente = sc.nextLine();
        System.out.printf("%25s : ", "Marca");
        String marca = sc.nextLine();
        System.out.printf("%25s : ", "Modelo");
        String modelo = sc.nextLine();
        System.out.printf("%25s : ", "Numero de asientos");
        int nroAsientos = leerEntero();

        System.out.println("\n:::: Dato de la empresa");
        System.out.printf("%25s : ", "R.U.T");
        Rut rutEmpresa = Rut.of(sc.nextLine());
        if (rutEmpresa == null) {
            System.out.println("RUT invalido. Operacion cancelada.");
            return;
        }

        try {
            ControladorEmpresas.getInstancia().createBus(marca, modelo, patente, nroAsientos, rutEmpresa);
            System.out.println("\n...::::: Bus guardado exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createViaje() {
        System.out.println("\n...:::: Creando un nuevo Viaje ::::...\n");
        System.out.printf("%25s : ", "Fecha[dd/mm/yyyy]");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.printf("%25s : ", "Hora[hh:mm]");
        LocalTime hora = LocalTime.parse(sc.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));
        System.out.printf("%25s : ", "Precio");
        int precio = leerEntero();
        System.out.printf("%25s : ", "Duracion (minutos)");
        int duracionMinutos = leerEntero();
        System.out.printf("%25s : ", "Patente Bus");
        String patente = sc.nextLine();
        System.out.printf("%25s : ", "Nro. de conductores");
        int nroConductores = leerEntero();
        if (nroConductores < 1 || nroConductores > 2) {
            System.out.println("Error: El numero de conductores debe ser 1 o 2.");
            return;
        }

        System.out.println(":: Id Auxiliar ::");
        IdPersona idAuxiliar = leerIdPersona();
        if (idAuxiliar == null) return;

        IdPersona[] idsTripulantes = new IdPersona[nroConductores + 1];
        idsTripulantes[0] = idAuxiliar;
        for (int i = 0; i < nroConductores; i++) {
            System.out.println(":: Id Conductor ::");
            idsTripulantes[i + 1] = leerIdPersona();
            if (idsTripulantes[i + 1] == null) return;
        }

        System.out.printf("%25s : ", "Nombre comuna salida");
        String comunaSalida = sc.nextLine();
        System.out.printf("%25s : ", "Nombre comuna llegada");
        String comunaLlegada = sc.nextLine();

        try {
            sistema.createViaje(fecha, hora, precio, duracionMinutos, patente,
                    idsTripulantes, new String[] { comunaSalida, comunaLlegada });
            System.out.println("\n...::::: Viaje guardado exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void vendePasajes() {
        System.out.println("\n...::::: Venta de pasajes ::::....\n");

        System.out.println(":::: Datos de la Venta");
        System.out.printf("%25s : ", "ID Documento");
        String idDoc = sc.nextLine();
        System.out.print("Tipo documento: [1] Boleta [2] Factura : ");
        TipoDocumento tipo = leerTipoDocumento();

        System.out.printf("%25s : ", "Fecha de viaje[dd/mm/yyyy]");
        LocalDate fechaViaje = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.printf("%25s : ", "Origen (comuna)");
        String comunaSalida = sc.nextLine();
        System.out.printf("%25s : ", "Destino (comuna)");
        String comunaLlegada = sc.nextLine();

        System.out.println("\n:::: Datos del cliente\n");
        IdPersona idCliente = leerIdPersona();
        if (idCliente == null) return;

        System.out.println("\n:::: Pasajes a vender");
        System.out.printf("%25s : ", "Cantidad de pasajes");
        int nroPasajes = leerEntero();

        try {
            sistema.iniciaVenta(idDoc, tipo, LocalDate.now(), fechaViaje,
                    comunaSalida, comunaLlegada, idCliente, nroPasajes);
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("\n:::: Listado de horarios disponibles");
        String[][] horarios = sistema.getHorariosDisponibles(fechaViaje, comunaSalida, comunaLlegada, nroPasajes);
        imprimirTabla(
                new String[]{"PATENTE", "HORA", "PRECIO", "ASIENTOS DISP."},
                horarios);
        System.out.printf("Seleccione viaje [1..%d] : ", horarios.length);
        int selViaje = leerEntero();
        if (selViaje < 1 || selViaje > horarios.length) {
            System.out.println("Error: Seleccion de viaje invalida.");
            return;
        }

        String patente = horarios[selViaje - 1][0];
        LocalTime hora = LocalTime.parse(horarios[selViaje - 1][1], DateTimeFormatter.ofPattern("HH:mm"));

        System.out.println("\n:::: Asientos disponibles para el viaje seleccionado");
        String[] listaAsientos = sistema.listAsientosDeViaje(fechaViaje, hora, patente);
        imprimirArreglo(listaAsientos);
        System.out.print("Seleccione sus asientos [separe por ,] : ");
        String asientosStr = sc.nextLine();
        String[] asientosParts = asientosStr.split(",");

        if (asientosParts.length != nroPasajes) {
            System.out.println("Error: La cantidad de asientos no coincide con la cantidad de pasajes.");
            return;
        }

        int[] asientos = new int[nroPasajes];
        for (int i = 0; i < nroPasajes; i++) {
            asientos[i] = Integer.parseInt(asientosParts[i].trim());
        }

        for (int i = 0; i < nroPasajes; i++) {
            if (asientos[i] < 1 || asientos[i] > listaAsientos.length
                    || listaAsientos[asientos[i] - 1].equals("*")) {
                System.out.println("Error: El asiento " + asientos[i] + " no esta disponible.");
                return;
            }
        }

        for (int i = 0; i < nroPasajes; i++) {
            System.out.println("\n:::: Datos pasajero " + (i + 1));
            IdPersona idPasajero = leerIdPersona();
            if (idPasajero == null) return;

            Nombre nombrePasajero = leerNombre();
            System.out.print("Telefono: ");
            String telefonoPasajero = sc.nextLine();
            System.out.println(":::: Contacto de emergencia");
            Nombre nombreContacto = leerNombre();
            System.out.print("Telefono contacto: ");
            String telefonoContacto = sc.nextLine();

            try {
                sistema.createPasajero(idPasajero, nombrePasajero, telefonoPasajero,
                        nombreContacto, telefonoContacto);
            } catch (SVPException e) {
            }

            try {
                sistema.vendePasaje(idDoc, tipo, fechaViaje, hora, patente, asientos[i], idPasajero);
                System.out.println("\n:::: Pasaje agregado exitosamente");
            } catch (SVPException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }

        System.out.println("\n:::: Monto total de la venta: $" + sistema.getMontoVenta(idDoc, tipo));
        pagarVenta(idDoc, tipo);
    }

    private void pagarVenta(String idDocumento, TipoDocumento tipoDocumento) {
        System.out.println("\n:::: Pago de la venta");
        System.out.print("Efectivo[1] o Tarjeta[2] : ");
        int tipoPago = leerEntero();

        try {
            if (tipoPago == 2) {
                System.out.print("Numero de tarjeta: ");
                long nroTarjeta = Long.parseLong(sc.nextLine());
                sistema.pagaVenta(idDocumento, tipoDocumento, nroTarjeta);
            } else {
                sistema.pagaVenta(idDocumento, tipoDocumento);
            }
            System.out.println("\n...::::: Venta realizada exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listVentas() {
        System.out.println("\n...:::: Listado de Ventas ::::...");
        imprimirTabla(
                new String[]{"ID DOC.", "TIPO", "FECHA", "CLIENTE", "MONTO", "TIPO PAGO"},
                sistema.listVentas());
    }

    private void listViajes() {
        System.out.println("\n...::::: Listado de viajes ::::....\n");
        imprimirTabla(
                new String[]{"FECHA", "HORA SALIDA", "HORA LLEGADA", "PRECIO", "ASIENTOS DISP.", "PATENTE BUS", "TERMINAL SALIDA", "TERMINAL LLEGADA"},
                sistema.listViajes());
    }

    private void listPasajerosViaje() {
        System.out.println("\n...:::: Pasajeros de Viaje ::::...");
        LocalDate fecha = leerFecha("Fecha viaje (dd/MM/yyyy): ");
        LocalTime hora = leerHora("Hora viaje (HH:mm): ");
        System.out.print("Patente bus: ");
        String patente = sc.nextLine();

        try {
            imprimirTabla(
                    new String[]{"ID PASAJERO", "NOMBRE", "CONTACTO EMERGENCIA", "FONO CONTACTO", "ASIENTO"},
                    sistema.listPasajerosViaje(fecha, hora, patente));
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listEmpresas() {
        System.out.println("\n...::::: Listado de empresas ::::....\n");
        imprimirTabla(
                new String[]{"RUT", "NOMBRE", "URL", "NRO. TRIPULANTES", "NRO. BUSES", "NRO. VENTAS"},
                ControladorEmpresas.getInstancia().listEmpresas());
    }

    private void listLlegadasSalidasTerminal() {
        System.out.println("\n...::::: Listado de llegadas y salidas de un terminal ::::....\n");
        System.out.printf("%25s : ", "Nombre terminal");
        String nombre = sc.nextLine();
        System.out.printf("%25s : ", "Fecha[dd/mm/yyyy]");
        LocalDate fecha = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            imprimirTabla(
                    new String[]{"LLEGADA/SALIDA", "HORA", "PATENTE BUS", "NOMBRE EMPRESA", "NRO. PASAJEROS"},
                    ControladorEmpresas.getInstancia().listLlegadasSalidasTerminal(nombre, fecha));
        } catch (SVPException e) {
            System.out.println("*** Error: " + e.getMessage() + " ***");
        }
    }

    // corregido: ahora tiene 6 columnas que coinciden con los datos del controlador
    private void listVentasEmpresa() {
        System.out.println("\n...::::: Listado de ventas de una empresa ::::....\n");
        System.out.printf("%25s : ", "R.U.T");
        Rut rut = Rut.of(sc.nextLine());
        if (rut == null) {
            System.out.println("Error: RUT invalido.");
            return;
        }

        try {
            imprimirTabla(
                    new String[]{"ID DOC.", "TIPO", "FECHA", "CLIENTE", "MONTO", "TIPO PAGO"},
                    ControladorEmpresas.getInstancia().listVentasEmpresa(rut));
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void generatePasajesVenta() {
        System.out.println("\n...:::: Generar pasajes de venta ::::...");
        System.out.printf("%25s : ", "ID Documento");
        String idDoc = sc.nextLine();
        System.out.print("Tipo documento: [1] Boleta [2] Factura : ");
        TipoDocumento tipo = leerTipoDocumento();

        try {
            sistema.generatePasajesVenta(idDoc, tipo);
            String nombreArchivo = idDoc + "_" + tipo.toString() + ".txt";
            System.out.println("\n...::::: Pasajes generados en archivo: " + nombreArchivo + " ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void readDatosIniciales() {
        System.out.println("\n...:::: Leer datos iniciales ::::...");
        try {
            sistema.readDatosIniciales();
            // actualizo la referencia del sistema por si cambio internamente
            sistema = SistemaVentaPasajes.getInstancia();
            System.out.println("\n...::::: Datos iniciales cargados exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void saveDatosSistema() {
        System.out.println("\n...:::: Guardar datos del sistema ::::...");
        try {
            sistema.saveDatosSistema();
            System.out.println("\n...::::: Datos del sistema guardados exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void readDatosSistema() {
        System.out.println("\n...:::: Leer datos del sistema ::::...");
        try {
            sistema.readDatosSistema();
            // actualizo la referencia al nuevo singleton cargado desde disco
            sistema = SistemaVentaPasajes.getInstancia();
            System.out.println("\n...::::: Datos del sistema cargados exitosamente ::::....");
        } catch (SVPException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // -- metodos auxiliares para leer datos del usuario --

    private IdPersona leerIdPersona() {
        System.out.printf("%25s : ", "Rut[1] o Pasaporte[2]");
        int tipo = leerEntero();

        if (tipo == 1) {
            System.out.printf("%25s : ", "R.U.T");
            Rut rut = Rut.of(sc.nextLine());
            if (rut == null) System.out.println("Error: RUT invalido.");
            return rut;
        }

        if (tipo == 2) {
            System.out.printf("%25s : ", "Numero de pasaporte");
            String numero = sc.nextLine();
            System.out.printf("%25s : ", "Nacionalidad");
            String nacionalidad = sc.nextLine();
            return Pasaporte.of(numero, nacionalidad);
        }

        System.out.println("Error: Tipo de documento invalido.");
        return null;
    }

    private Nombre leerNombre() {
        System.out.printf("%25s : ", "Sr.[1] o Sra.[2]");
        int trat = leerEntero();
        Tratamiento tratamiento = (trat == 2) ? Tratamiento.SRA : Tratamiento.SR;

        System.out.printf("%25s : ", "Nombres");
        String nombres = sc.nextLine();
        System.out.printf("%25s : ", "Apellido Paterno");
        String apPaterno = sc.nextLine();
        System.out.printf("%25s : ", "Apellido Materno");
        String apMaterno = sc.nextLine();
        return new Nombre(tratamiento, nombres, apPaterno, apMaterno);
    }

    private Direccion leerDireccion() {
        System.out.printf("%25s : ", "Calle");
        String calle = sc.nextLine();
        System.out.printf("%25s : ", "Numero");
        int numero = leerEntero();
        System.out.printf("%25s : ", "Comuna");
        String comuna = sc.nextLine();
        return new Direccion(calle, numero, comuna);
    }

    private TipoDocumento leerTipoDocumento() {
        int tipo = leerEntero();
        if (tipo == 2) return TipoDocumento.FACTURA;
        return TipoDocumento.BOLETA;
    }

    private LocalDate leerFecha(String mensaje) {
        System.out.print(mensaje);
        return LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private LocalTime leerHora(String mensaje) {
        System.out.print(mensaje);
        return LocalTime.parse(sc.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));
    }

    private int leerEntero() {
        return Integer.parseInt(sc.nextLine());
    }

    // -- metodos para imprimir tablas y arreglos --

    // version simplificada de imprimirTabla, mas directa con printf
    private void imprimirTabla(String[] encabezados, String[][] datos) {
        if (datos == null || datos.length == 0) {
            System.out.println("No hay datos para mostrar.");
            return;
        }

        int nroCols = encabezados.length;

        // calculo el ancho de cada columna
        int[] anchos = new int[nroCols];
        for (int j = 0; j < nroCols; j++) {
            anchos[j] = encabezados[j].length();
        }
        for (int i = 0; i < datos.length; i++) {
            for (int j = 0; j < nroCols; j++) {
                if (j < datos[i].length && datos[i][j] != null) {
                    if (datos[i][j].length() > anchos[j]) {
                        anchos[j] = datos[i][j].length();
                    }
                }
            }
        }

        // imprimo la linea separadora
        imprimirLinea(anchos);

        // imprimo los encabezados
        System.out.print("|");
        for (int j = 0; j < nroCols; j++) {
            System.out.printf(" %-" + anchos[j] + "s |", encabezados[j]);
        }
        System.out.println();
        imprimirLinea(anchos);

        // imprimo cada fila de datos
        for (int i = 0; i < datos.length; i++) {
            System.out.print("|");
            for (int j = 0; j < nroCols; j++) {
                String val = "";
                if (j < datos[i].length && datos[i][j] != null) {
                    val = datos[i][j];
                }
                System.out.printf(" %-" + anchos[j] + "s |", val);
            }
            System.out.println();
        }
        imprimirLinea(anchos);
    }

    // imprime una linea de guiones para separar filas de la tabla
    private void imprimirLinea(int[] anchos) {
        System.out.print("+");
        for (int j = 0; j < anchos.length; j++) {
            for (int k = 0; k < anchos[j] + 2; k++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
    }

    private void imprimirArreglo(String[] datos) {
        if (datos.length == 0) {
            System.out.println("No hay datos.");
            return;
        }
        for (int i = 0; i < datos.length; i++) {
            System.out.print(datos[i] + " ");
        }
        System.out.println();
    }
}
