package persistencia;

import excepciones.SVPException;
import modelo.Bus;
import modelo.Empresa;
import modelo.Pasaje;
import modelo.Terminal;
import modelo.Tripulante;
import utilidades.Direccion;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Rut;
import utilidades.Tratamiento;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase encargada de la entrada/salida de datos del sistema.
 * Lee y escribe archivos de texto y archivos de objetos.
 * @author Javier San Martin
 * @author Benjamin Carrasco
 * @version Avance 3
 */
public class IOSVP {

    private static final String ARCHIVO_OBJETOS = "SVPOjectos.obj";
    private static final String ARCHIVO_DATOS = "SVPDatosIniciales.txt";

    /**
     * Almacena los controladores en el archivo SVPOjectos.obj.
     * Lanza SVPException si no se puede crear o grabar.
     */
    public static void saveControladores(Object[] controladores) {
        try {
            FileOutputStream fos = new FileOutputStream(ARCHIVO_OBJETOS);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < controladores.length; i++) {
                oos.writeObject(controladores[i]);
            }
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new SVPException("No se pudo crear el archivo " + ARCHIVO_OBJETOS);
        } catch (IOException e) {
            throw new SVPException("Error al guardar los datos del sistema: " + e.getMessage());
        }
    }

    /**
     * Retorna un arreglo con los objetos leidos desde SVPOjectos.obj.
     * Lanza SVPException si no se puede abrir o leer.
     */
    public static Object[] readControladores() {
        try {
            FileInputStream fis = new FileInputStream(ARCHIVO_OBJETOS);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj1 = ois.readObject();
            Object obj2 = ois.readObject();
            ois.close();
            fis.close();
            return new Object[]{obj1, obj2};
        } catch (FileNotFoundException e) {
            throw new SVPException("No se encontro el archivo " + ARCHIVO_OBJETOS);
        } catch (IOException e) {
            throw new SVPException("Error al leer los datos del sistema: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new SVPException("Error al leer objetos del archivo: " + e.getMessage());
        }
    }

    /**
     * Graba los pasajes en el archivo de texto con el nombre indicado.
     * El formato corresponde al de la Figura 3 (pasaje electronico).
     * Lanza SVPException si no es posible crear el archivo.
     */
    public static void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivo) {
        try {
            PrintWriter pw = new PrintWriter(nombreArchivo);
            for (int i = 0; i < pasajes.length; i++) {
                pw.println(pasajes[i].toString());
                if (i < pasajes.length - 1) {
                    pw.println();
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            throw new SVPException("No se pudo crear el archivo " + nombreArchivo);
        }
    }

    /**
     * Lee el contenido del archivo SVPDatosIniciales.txt y construye
     * los objetos correspondientes de la capa modelo.
     * Retorna un arreglo con todos los objetos creados.
     * Lanza SVPException si no es posible abrir o leer el archivo.
     */
    public static Object[] readDatosIniciales() {
        ArrayList<Object> objetos = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_DATOS));
            String linea;
            int seccion = 0; // 0=personas, 1=empresas, 2=tripulantes, 3=terminales, 4=buses, 5=viajes

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                if (linea.equals("+")) {
                    seccion++;
                    continue;
                }

                switch (seccion) {
                    case 0:
                        parsePersonas(linea, objetos);
                        break;
                    case 1:
                        parseEmpresa(linea, objetos);
                        break;
                    case 2:
                        parseTripulante(linea, objetos);
                        break;
                    case 3:
                        parseTerminal(linea, objetos);
                        break;
                    case 4:
                        parseBus(linea, objetos);
                        break;
                    case 5:
                        parseViaje(linea, objetos);
                        break;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new SVPException("No se encontro el archivo " + ARCHIVO_DATOS);
        } catch (IOException e) {
            throw new SVPException("Error al leer el archivo " + ARCHIVO_DATOS + ": " + e.getMessage());
        }

        return objetos.toArray();
    }

    // --- metodos de parseo para cada seccion del archivo ---

    /**
     * Parsea una linea de la seccion de personas (clientes y/o pasajeros).
     * Formato CP: rut;tratamiento;nombres;apPat;apMat;fono;email;tratContacto;nombresContacto;apPatContacto;apMatContacto;fonoContacto
     * Formato C: solo cliente
     * Formato P: solo pasajero
     */
    private static void parsePersonas(String linea, ArrayList<Object> objetos) {
        String[] partes = linea.split(";");
        String tipo = partes[0];

        if (tipo.equals("CP")) {
            // es cliente y pasajero
            // CP;rut;trat;nombres;apPat;apMat;fono;email;tratContacto;nombresContacto;apPatContacto;apMatContacto;fonoContacto
            String rutStr = partes[1];
            Tratamiento trat = parseTratamiento(partes[2]);
            Nombre nombre = new Nombre(trat, partes[3], partes[4], partes[5]);
            String fono = partes[6];
            String email = partes[7];
            Tratamiento tratContacto = parseTratamiento(partes[8]);
            Nombre nomContacto = new Nombre(tratContacto, partes[9], partes[10], partes[11]);
            String fonoContacto = partes[12];
            IdPersona id = Rut.of(rutStr);

            // agrego: tipo, id, nombre, fono, email, nomContacto, fonoContacto
            objetos.add("CP");
            objetos.add(id);
            objetos.add(nombre);
            objetos.add(fono);
            objetos.add(email);
            objetos.add(nomContacto);
            objetos.add(fonoContacto);

        } else if (tipo.equals("C")) {
            // solo cliente
            // C;rut;trat;nombres;apPat;apMat;fono;email
            String rutStr = partes[1];
            Tratamiento trat = parseTratamiento(partes[2]);
            Nombre nombre = new Nombre(trat, partes[3], partes[4], partes[5]);
            String fono = partes[6];
            String email = partes[7];
            IdPersona id = Rut.of(rutStr);

            objetos.add("C");
            objetos.add(id);
            objetos.add(nombre);
            objetos.add(fono);
            objetos.add(email);

        } else if (tipo.equals("P")) {
            // solo pasajero
            // P;rut;trat;nombres;apPat;apMat;fono;tratContacto;nombresContacto;apPatContacto;apMatContacto;fonoContacto
            String rutStr = partes[1];
            Tratamiento trat = parseTratamiento(partes[2]);
            Nombre nombre = new Nombre(trat, partes[3], partes[4], partes[5]);
            String fono = partes[6];
            Tratamiento tratContacto = parseTratamiento(partes[7]);
            Nombre nomContacto = new Nombre(tratContacto, partes[8], partes[9], partes[10]);
            String fonoContacto = partes[11];
            IdPersona id = Rut.of(rutStr);

            objetos.add("P");
            objetos.add(id);
            objetos.add(nombre);
            objetos.add(fono);
            objetos.add(nomContacto);
            objetos.add(fonoContacto);
        }
    }

    /**
     * Parsea una linea de la seccion de empresas.
     * Formato: rut;nombre;url
     */
    private static void parseEmpresa(String linea, ArrayList<Object> objetos) {
        String[] partes = linea.split(";");
        Rut rut = Rut.of(partes[0]);
        String nombre = partes[1];
        String url = partes[2];

        objetos.add("EMPRESA");
        objetos.add(rut);
        objetos.add(nombre);
        objetos.add(url);
    }

    /**
     * Parsea una linea de la seccion de tripulantes.
     * Formato: tipo;rut;trat;nombres;apPat;apMat;calle;numero;comuna;rutEmpresa
     * tipo: A=auxiliar, C=conductor
     */
    private static void parseTripulante(String linea, ArrayList<Object> objetos) {
        String[] partes = linea.split(";");
        String tipo = partes[0]; // A o C
        String rutStr = partes[1];
        Tratamiento trat = parseTratamiento(partes[2]);
        Nombre nombre = new Nombre(trat, partes[3], partes[4], partes[5]);
        String calle = partes[6];
        int numero = Integer.parseInt(partes[7]);
        String comuna = partes[8];
        Direccion dir = new Direccion(calle, numero, comuna);
        Rut rutEmpresa = Rut.of(partes[9]);
        IdPersona id = Rut.of(rutStr);

        objetos.add("TRIPULANTE");
        objetos.add(tipo);
        objetos.add(id);
        objetos.add(nombre);
        objetos.add(dir);
        objetos.add(rutEmpresa);
    }

    /**
     * Parsea una linea de la seccion de terminales.
     * Formato: nombre;calle;numero;comuna
     */
    private static void parseTerminal(String linea, ArrayList<Object> objetos) {
        String[] partes = linea.split(";");
        String nombre = partes[0];
        String calle = partes[1];
        int numero = Integer.parseInt(partes[2]);
        String comuna = partes[3];
        Direccion dir = new Direccion(calle, numero, comuna);

        objetos.add("TERMINAL");
        objetos.add(nombre);
        objetos.add(dir);
    }

    /**
     * Parsea una linea de la seccion de buses.
     * Formato: patente;marca;modelo;nroAsientos;rutEmpresa
     */
    private static void parseBus(String linea, ArrayList<Object> objetos) {
        String[] partes = linea.split(";");
        String patente = partes[0];
        String marca = partes[1];
        String modelo = partes[2];
        int nroAsientos = Integer.parseInt(partes[3]);
        Rut rutEmpresa = Rut.of(partes[4]);

        objetos.add("BUS");
        objetos.add(patente);
        objetos.add(marca);
        objetos.add(modelo);
        objetos.add(nroAsientos);
        objetos.add(rutEmpresa);
    }

    /**
     * Parsea una linea de la seccion de viajes.
     * Formato: fecha;hora;precio;duracion;patBus;idAuxiliar;idConductor1[;idConductor2];nombreTermSalida;nombreTermLlegada
     */
    private static void parseViaje(String linea, ArrayList<Object> objetos) {
        String[] partes = linea.split(";");
        String fecha = partes[0];
        String hora = partes[1];
        int precio = Integer.parseInt(partes[2]);
        int duracion = Integer.parseInt(partes[3]);
        String patBus = partes[4];
        // los tripulantes van del indice 5 hasta el penultimo-1
        // los dos ultimos son terminal salida y terminal llegada
        String nombreTermSalida = partes[partes.length - 2];
        String nombreTermLlegada = partes[partes.length - 1];

        // los ids de tripulantes van del indice 5 hasta partes.length - 3
        int nroTrips = partes.length - 2 - 5;
        String[] idsTripulantes = new String[nroTrips];
        for (int i = 0; i < nroTrips; i++) {
            idsTripulantes[i] = partes[5 + i];
        }

        objetos.add("VIAJE");
        objetos.add(fecha);
        objetos.add(hora);
        objetos.add(precio);
        objetos.add(duracion);
        objetos.add(patBus);
        objetos.add(idsTripulantes);
        objetos.add(nombreTermSalida);
        objetos.add(nombreTermLlegada);
    }

    private static Tratamiento parseTratamiento(String texto) {
        if (texto.equalsIgnoreCase("SRA")) {
            return Tratamiento.SRA;
        }
        return Tratamiento.SR;
    }

    // --- metodos find con Optional y programacion funcional ---

    /**
     * Busca una empresa en la lista recibida cuyo rut coincida.
     * Retorna Optional con la empresa, o vacio si no se encuentra.
     */
    public static Optional<Empresa> findEmpresa(List<Empresa> empresas, Rut rut) {
        return empresas.stream()
                .filter(e -> e.getRut().equals(rut))
                .findFirst();
    }

    /**
     * Busca un bus en la lista recibida cuya patente coincida.
     * Retorna Optional con el bus, o vacio si no se encuentra.
     */
    public static Optional<Bus> findBus(List<Bus> buses, String patente) {
        return buses.stream()
                .filter(b -> b.getPatente().equalsIgnoreCase(patente))
                .findFirst();
    }

    /**
     * Busca un terminal en la lista recibida cuyo nombre coincida.
     * Retorna Optional con el terminal, o vacio si no se encuentra.
     */
    public static Optional<Terminal> findTerminal(List<Terminal> terminales, String nombre) {
        return terminales.stream()
                .filter(t -> t.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    /**
     * Busca un tripulante en la empresa que coincida con el id y rol indicados.
     * Los roles son "Auxiliar" o "Conductor".
     * Retorna Optional con el tripulante, o vacio si no se encuentra.
     */
    public static Optional<Tripulante> findTripulante(Empresa empresa, IdPersona id, String rol) {
        Tripulante[] trips = empresa.getTripulantes();
        ArrayList<Tripulante> lista = new ArrayList<>();
        for (int i = 0; i < trips.length; i++) {
            lista.add(trips[i]);
        }
        return lista.stream()
                .filter(t -> t.getIdPersona().equals(id))
                .filter(t -> {
                    if (rol.equals("Auxiliar")) {
                        return t instanceof modelo.Auxiliar;
                    } else {
                        return t instanceof modelo.Conductor;
                    }
                })
                .findFirst();
    }
}
