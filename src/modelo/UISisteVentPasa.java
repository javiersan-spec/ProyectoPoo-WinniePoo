/** @author Genesis Castro
 * Avance 2
 */
import controlador.SistemaVentaPasajes;
import modelo.Empresa;
import utilidades.Nombre;
import utilidades.Rut;

import java.util.Scanner;

public class UISisteVentPasa {

    private static UISisteVentPasa instancia;
    private Scanner sc;
    private SistemaVentaPasajes sistema;

    private UISisteVentPasa() {
        sc = new Scanner(System.in);
        sistema = SistemaVentaPasajes.getInstancia();
    }

    public static UISisteVentPasa getInstancia() {

        if (instancia == null) {
            instancia = new UISisteVentPasa();
        }

        return instancia;
    }

    public void menu() {

        int opcion;

        do {

            System.out.println("===== SISTEMA VENTA PASAJES =====");
            System.out.println("1. Crear Empresa");
            System.out.println("2. Listar Empresas");
            System.out.println("0. Salir");
            System.out.print("Seleccione opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {

                case 1:
                    createEmpresa();
                    break;

                case 2:
                    listEmpresas();
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

        System.out.print("Rut empresa: ");
        String rut = sc.nextLine();

        System.out.print("Nombre empresa: ");
        String nombre = sc.nextLine();

        System.out.print("URL empresa: ");
        String url = sc.nextLine();

        boolean creada = sistema
                .getControladorEmpresas()
                .createEmpresa(
                        new Rut(rut),
                        new Nombre(nombre),
                        url
                );

        if (creada) {
            System.out.println("Empresa creada correctamente");
        } else {
            System.out.println("La empresa ya existe");
        }
    }

    private void listEmpresas() {

        Empresa[] empresas = sistema
                .getControladorEmpresas()
                .listEmpresas();

        if (empresas.length == 0) {
            System.out.println("No hay empresas registradas");
            return;
        }

        for (Empresa e : empresas) {
            System.out.println(e);
        }
    }
}