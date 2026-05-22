package vista;

import controlador.ControladorEmpresas;
import controlador.SistemaVentaPasajes;
import excepciones.SistemaVentaPasajesException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;
import modelo.Empresa;
import modelo.Terminal;
import modelo.TipoDocumento;
import modelo.Venta;
import modelo.Viaje;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Pasaporte;
import utilidades.Rut;
import utilidades.Tratamiento;

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
                case 14: System.out.println("Saliendo..."); break;
                default: System.out.println("Opcion invalida");
            }

        } while (opcion != 14);
    }

    private void createEmpresa() {
        System.out.println("\n...:::: Creando una nueva Empresa ::::...");
        System.out.print("R.U.T (12345678-9): ");
        Rut rut = Rut.of(sc.nextLine());
        if (rut == null) {
            System.out.println("Error: Formato de RUT invalido.");
            return;
        }

        System.out.print("Nombre: ");
        String nombreEmpresa = sc.nextLine();
        System.out.print("URL: ");
        String url = sc.nextLine();

        try {
            ControladorEmpresas.getInstancia().createEmpresa(rut, nombreEmpresa, url);
            System.out.println("...:::: Empresa guardada exitosamente ::::...");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void contrataTripulante() {
        System.out.println("\n...:::: Contratando un nuevo tripulante ::::...");
        System.out.print("R.U.T empresa (12345678-9): ");
        Rut rutEmp = Rut.of(sc.nextLine());
        if (rutEmp == null) {
            System.out.println("Error: RUT de empresa invalido.");
            return;
        }

        System.out.print("Auxiliar[1] o Conductor[2]: ");
        int tipo = leerEntero();

        System.out.print("R.U.T tripulante (12345678-9): ");
        Rut idTrip = Rut.of(sc.nextLine());
        if (idTrip == null) {
            System.out.println("Error: RUT de tripulante invalido.");
            return;
        }

        Nombre nombre = leerNombre();
        Direccion dir = leerDireccion();

        try {
            if (tipo == 1) {
                ControladorEmpresas.getInstancia().hireAuxiliarForEmpresa(rutEmp, idTrip, nombre, dir);
            } else if (tipo == 2) {
                ControladorEmpresas.getInstancia().hireConductorForEmpresa(rutEmp, idTrip, nombre, dir);
            } else {
                System.out.println("Error: Tipo de tripulante invalido.");
                return;
            }
            System.out.println("...:::: Tripulante contratado exitosamente ::::...");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createTerminal() {
        System.out.println("\n...:::: Creando un nuevo Terminal ::::...");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        Direccion dir = leerDireccion();

        try {
            ControladorEmpresas.getInstancia().createTerminal(nombre, dir);
            System.out.println("...:::: Terminal guardado exitosamente ::::...");
        } catch (SistemaVentaPasajesException e) {
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
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createBus() {
        System.out.println("\n...:::: Creando un nuevo Bus ::::...");
        System.out.print("Patente (ABCD-12): ");
        String patente = sc.nextLine();
        System.out.print("Marca: ");
        String marca = sc.nextLine();
        System.out.print("Modelo: ");
        String modelo = sc.nextLine();
        System.out.print("Numero de asientos: ");
        int nroAsientos = leerEntero();

        System.out.print("R.U.T empresa (12345678-9): ");
        Rut rutEmpresa = Rut.of(sc.nextLine());
        if (rutEmpresa == null) {
            System.out.println("RUT invalido. Operacion cancelada.");
            return;
        }

        try {
            ControladorEmpresas.getInstancia().createBus(marca, modelo, patente, nroAsientos, rutEmpresa);
            System.out.println("...:::: Bus guardado exitosamente ::::...");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void createViaje() {
        System.out.println("\n...:::: Creando un nuevo Viaje ::::...");
        LocalDate fecha = leerFecha("Fecha (dd/MM/yyyy): ");
        LocalTime hora = leerHora("Hora (HH:mm): ");
        System.out.print("Precio pasaje: ");
        int precio = leerEntero();
        System.out.print("Duracion en minutos: ");
        int duracionMinutos = leerEntero();
        System.out.print("Patente bus: ");
        String patente = sc.nextLine();
        System.out.print("Comuna de salida: ");
        String comunaSalida = sc.nextLine();
        System.out.print("Comuna de llegada: ");
        String comunaLlegada = sc.nextLine();
        System.out.println(":::: Auxiliar del viaje");

        IdPersona idAuxiliar = leerIdPersona();
        if (idAuxiliar == null) return;

        System.out.print("Numero de conductores (1 o 2): ");
        int nroConductores = leerEntero();
        if (nroConductores < 1 || nroConductores > 2) {
            System.out.println("Error: El numero de conductores debe ser 1 o 2.");
            return;
        }

        IdPersona[] idsTripulantes = new IdPersona[nroConductores + 1];
        idsTripulantes[0] = idAuxiliar;
        for (int i = 0; i < nroConductores; i++) {
            System.out.println(":::: Conductor " + (i + 1) + " del viaje");
            idsTripulantes[i + 1] = leerIdPersona();
            if (idsTripulantes[i + 1] == null) return;
        }

        try {
            sistema.createViaje(fecha, hora, precio, duracionMinutos, patente,
                    idsTripulantes, new String[] { comunaSalida, comunaLlegada });
            System.out.println("...:::: Viaje guardado exitosamente ::::...");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void vendePasajes() {
        System.out.println("\n...:::: Vendiendo Pasajes ::::...");
        System.out.print("Documento de venta: ");
        String idDoc = sc.nextLine();
        TipoDocumento tipo = leerTipoDocumento();

        LocalDate fechaViaje = leerFecha("Fecha viaje (dd/MM/yyyy): ");
        System.out.print("Comuna de salida: ");
        String comunaSalida = sc.nextLine();
        System.out.print("Comuna de llegada: ");
        String comunaLlegada = sc.nextLine();
        System.out.print("Numero de pasajes: ");
        int nroPasajes = leerEntero();

        System.out.println(":::: Datos cliente");
        IdPersona idCliente = leerIdPersona();
        if (idCliente == null) return;

        if (!sistema.findCliente(idCliente).isPresent()) {
            System.out.println("Error: El cliente debe estar registrado antes de iniciar la venta.");
            return;
        }

        try {
            sistema.iniciaVenta(idDoc, tipo, LocalDate.now(), fechaViaje, comunaSalida, comunaLlegada, idCliente, nroPasajes);
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Viajes disponibles:");
        imprimirTabla(sistema.getHorariosDisponibles(fechaViaje, comunaSalida, comunaLlegada, nroPasajes));
        LocalTime hora = leerHora("Hora viaje (HH:mm): ");
        System.out.print("Patente bus: ");
        String patente = sc.nextLine();

        System.out.println("Asientos disponibles:");
        imprimirArreglo(sistema.listAsientosDeViaje(fechaViaje, hora, patente));

        for (int i = 0; i < nroPasajes; i++) {
            System.out.println(":::: Pasaje " + (i + 1));
            System.out.print("Asiento: ");
            int asiento = leerEntero();

            System.out.println(":::: Datos pasajero");
            IdPersona idPasajero = leerIdPersona();
            if (idPasajero == null) return;

            if (!sistema.findPasajero(idPasajero).isPresent()) {
                System.out.println("Pasajero no encontrado. Ingrese sus datos para registrarlo.");
                Nombre nombrePasajero = leerNombre();
                System.out.print("Telefono: ");
                String telefonoPasajero = sc.nextLine();
                System.out.println(":::: Contacto de emergencia");
                Nombre nombreContacto = leerNombre();
                System.out.print("Telefono contacto: ");
                String telefonoContacto = sc.nextLine();
                try {
                    sistema.createPasajero(idPasajero, nombrePasajero, telefonoPasajero, nombreContacto, telefonoContacto);
                } catch (SistemaVentaPasajesException e) {
                    System.out.println("Error: " + e.getMessage());
                    return;
                }
            }

            try {
                sistema.vendePasaje(idDoc, tipo, fechaViaje, hora, patente, asiento, idPasajero);
                System.out.println("...:::: Pasaje vendido exitosamente ::::...");
            } catch (SistemaVentaPasajesException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }
        }

        System.out.println("Monto venta: $" + sistema.getMontoVenta(idDoc, tipo).orElse(0));
        pagarVenta(idDoc, tipo);
    }

    private void pagarVenta(String idDocumento, TipoDocumento tipoDocumento) {
        System.out.print("Efectivo[1] o Tarjeta[2]: ");
        int tipoPago = leerEntero();

        try {
            if (tipoPago == 2) {
                System.out.print("Numero de tarjeta: ");
                long nroTarjeta = Long.parseLong(sc.nextLine());
                sistema.pagaVenta(idDocumento, tipoDocumento, nroTarjeta);
            } else {
                sistema.pagaVenta(idDocumento, tipoDocumento);
            }
            System.out.println("...:::: Venta pagada exitosamente ::::...");
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listVentas() {
        System.out.println("\n...:::: Listado de Ventas ::::...");
        imprimirTabla(sistema.listVentas());
    }

    private void listViajes() {
        System.out.println("\n...:::: Listado de Viajes ::::...");
        imprimirTabla(sistema.listViajes());
    }

    private void listPasajerosViaje() {
        System.out.println("\n...:::: Pasajeros de Viaje ::::...");
        LocalDate fecha = leerFecha("Fecha viaje (dd/MM/yyyy): ");
        LocalTime hora = leerHora("Hora viaje (HH:mm): ");
        System.out.print("Patente bus: ");
        String patente = sc.nextLine();

        try {
            imprimirTabla(sistema.listPasajerosViaje(fecha, hora, patente));
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listEmpresas() {
        System.out.println("\n...:::: Listado de Empresas ::::...");
        imprimirTabla(ControladorEmpresas.getInstancia().listEmpresas());
    }

    private void listLlegadasSalidasTerminal() {
        System.out.println("\n...:::: Llegadas y Salidas de Terminal ::::...");
        System.out.print("Nombre terminal: ");
        String nombre = sc.nextLine();
        LocalDate fecha = leerFecha("Fecha (dd/MM/yyyy): ");

        try {
            imprimirTabla(ControladorEmpresas.getInstancia().listLlegadasSalidasTerminal(nombre, fecha));
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listVentasEmpresa() {
        System.out.println("\n...:::: Ventas de Empresa ::::...");
        System.out.print("R.U.T empresa (12345678-9): ");
        Rut rut = Rut.of(sc.nextLine());
        if (rut == null) {
            System.out.println("Error: RUT invalido.");
            return;
        }

        try {
            imprimirTabla(ControladorEmpresas.getInstancia().listVentasEmpresa(rut));
        } catch (SistemaVentaPasajesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private IdPersona leerIdPersona() {
        System.out.print("Rut[1] o Pasaporte[2]: ");
        int tipo = leerEntero();

        if (tipo == 1) {
            System.out.print("R.U.T (12345678-9): ");
            Rut rut = Rut.of(sc.nextLine());
            if (rut == null) System.out.println("Error: RUT invalido.");
            return rut;
        }

        if (tipo == 2) {
            System.out.print("Numero de pasaporte: ");
            String numero = sc.nextLine();
            System.out.print("Nacionalidad: ");
            String nacionalidad = sc.nextLine();
            return Pasaporte.of(numero, nacionalidad);
        }

        System.out.println("Error: Tipo de documento invalido.");
        return null;
    }

    private Nombre leerNombre() {
        System.out.print("Nombres: ");
        String nombres = sc.nextLine();
        System.out.print("Apellido Paterno: ");
        String apPaterno = sc.nextLine();
        System.out.print("Apellido Materno: ");
        String apMaterno = sc.nextLine();
        return new Nombre(Tratamiento.SR, nombres, apPaterno, apMaterno);
    }

    private Direccion leerDireccion() {
        System.out.print("Calle: ");
        String calle = sc.nextLine();
        System.out.print("Numero: ");
        int numero = leerEntero();
        System.out.print("Comuna: ");
        String comuna = sc.nextLine();
        return new Direccion(calle, numero, comuna);
    }

    private TipoDocumento leerTipoDocumento() {
        System.out.print("Boleta[1] o Factura[2]: ");
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

    private void imprimirTabla(String[][] datos) {
        if (datos.length == 0) {
            System.out.println("No hay datos para mostrar.");
            return;
        }

        for (String[] fila : datos) {
            for (int i = 0; i < fila.length; i++) {
                if (i > 0) System.out.print(" | ");
                System.out.print(fila[i]);
            }
            System.out.println();
        }
    }

    private void imprimirArreglo(String[] datos) {
        if (datos.length == 0) {
            System.out.println("No hay datos para mostrar.");
            return;
        }

        for (String dato : datos) {
            System.out.print(dato + " ");
        }
        System.out.println();
    }

    private void imprimirViajes(Viaje[] viajes) {
        if (viajes.length == 0) {
            System.out.println("No hay viajes registrados.");
            return;
        }

        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Viaje viaje : viajes) {
            System.out.println(
                    viaje.getFecha().format(fechaFormatter) + " | "
                            + viaje.getHora().format(horaFormatter) + " | "
                            + viaje.getBus().getPatente() + " | $"
                            + viaje.getPrecio()
            );
        }
    }

    private void imprimirViajes(Viaje[] viajes, LocalDate fecha) {
        int encontrados = 0;
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Viaje viaje : viajes) {
            if (viaje.getFecha().equals(fecha)) {
                System.out.println(
                        viaje.getFecha().format(fechaFormatter) + " | "
                                + viaje.getHora().format(horaFormatter) + " | "
                                + viaje.getBus().getPatente() + " | $"
                                + viaje.getPrecio()
                );
                encontrados++;
            }
        }

        if (encontrados == 0) {
            System.out.println("No hay viajes registrados.");
        }
    }
}
