package controlador;
/**
 * @author Genesis Castro
 */
import modelo.Bus;
import modelo.Empresa;
import utilidades.Rut;
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

    public boolean createEmpresa(Rut rut, String nombre, String url) {

        Optional<Empresa> empresaEncontrada = findEmpresa(rut);

        if (empresaEncontrada.isPresent()) {
            return false;
        }

        Empresa empresa = new Empresa(rut, nombre, url);
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

    public boolean createBus(Rut rutEmpresa, String patente, String marca, String modelo, int nroAsientos) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);

        if (!empresaOpt.isPresent()) {
            return false;
        }

        Empresa empresa = empresaOpt.get();
        Bus nuevoBus = new Bus(patente, nroAsientos, empresa);
        nuevoBus.setMarca(marca);
        nuevoBus.setModelo(modelo);

        empresa.addBus(nuevoBus);
        return true;
    }
    public boolean hireConductorForEmpresa(Rut rutEmpresa, utilidades.IdPersona id, utilidades.Nombre nom, utilidades.Direccion dir, boolean tieneLicencia) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (empresaOpt.isPresent()) {
            return empresaOpt.get().addConductor(id, nom, dir, tieneLicencia);
        }
        return false;
    }
    public boolean hireAuxiliarForEmpresa(Rut rutEmpresa, utilidades.IdPersona id, utilidades.Nombre nom, utilidades.Direccion dir) {
        Optional<Empresa> empresaOpt = findEmpresa(rutEmpresa);
        if (empresaOpt.isPresent()) {
            return empresaOpt.get().addAuxiliar(id, nom, dir);
        }
        return false;
    }
}