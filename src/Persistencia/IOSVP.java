package utilidades;

import excepciones.SVPException;
import modelo.Pasaje;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
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

    public static void saveControladores(Object[] controladores) throws SVPException {
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

    public static Object[] readControladores() throws SVPException {
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

    public static void savePasajesDeVenta(Pasaje[] pasajes, String nombreArchivo) throws SVPException {
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

    public static Object[] readDatosIniciales() throws SVPException {
        // se implementara cuando se procese el archivo SVPDatosIniciales.txt
        throw new SVPException("Metodo readDatosIniciales aun no implementado.");
    }
}
