/** @author Genesis Castro
 * Avance 2
 */

import java.util.ArrayList;
import java.util.Optional;

public class ControladorEmpresas {

    private static ControladorEmpresas instancia;
    private ArrayList<Empresa> empresas;

    private ControladorEmpresas() {
        this.empresas = new ArrayList<>();
    }

    public static ControladorEmpresas getInstancia() {

        if (instancia == null) {
            instancia = new ControladorEmpresas();
        }

        return instancia;
    }

    public boolean createEmpresa(Rut rut, Nombre nom, String url) {

        Optional<Empresa> empresaEncontrada = findEmpresa(rut);

        if (empresaEncontrada.isPresent()) {
            return false;
        }

        Empresa empresa = new Empresa(rut, nom, url);
        empresas.add(empresa);

        return true;
    }

    public Empresa[] listEmpresas() {
        return empresas.toArray(new Empresa[0]);
    }

    public Optional<Empresa> findEmpresa(Rut rut) {

        for (Empresa empresa : empresas) {

            if (empresa.getRut().equals(rut)) {
                return Optional.of(empresa);
            }
        }

        return Optional.empty();
    }
}