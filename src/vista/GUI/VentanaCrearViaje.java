package vista.GUI;

import controlador.ControladorEmpresas;
import controlador.SistemaVentaPasajes;
import excepciones.SVPException;
import modelo.Auxiliar;
import modelo.Bus;
import modelo.Conductor;
import modelo.Empresa;
import modelo.Tripulante;
import utilidades.IdPersona;
import utilidades.Rut;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaCrearViaje extends JFrame {

    private VentanaPrincipal ventanaPrincipal;
    private JComboBox<String> comboEmpresas;
    private JComboBox<String> comboBuses;
    private JComboBox<String> comboAuxiliar;
    private JComboBox<String> comboConductor1;
    private JComboBox<String> comboConductor2;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtPrecio;
    private JTextField txtDuracion;
    private JTextField txtComunaSalida;
    private JTextField txtComunaLlegada;

    public VentanaCrearViaje(VentanaPrincipal principal) {
        this.ventanaPrincipal = principal;
        setTitle("Crear Viaje");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                volver();
            }
        });
        cargarEmpresas();
    }

    private void initComponents() {
        JPanel panelForm = new JPanel(new GridLayout(12, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelForm.add(new JLabel("Empresa:"));
        comboEmpresas = new JComboBox<>();
        comboEmpresas.addActionListener(e -> actualizarListasEmpresa());
        panelForm.add(comboEmpresas);

        panelForm.add(new JLabel("Fecha (dd/MM/yyyy):"));
        txtFecha = new JTextField();
        panelForm.add(txtFecha);

        panelForm.add(new JLabel("Hora (HH:mm):"));
        txtHora = new JTextField();
        panelForm.add(txtHora);

        panelForm.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelForm.add(txtPrecio);

        panelForm.add(new JLabel("Duración (minutos):"));
        txtDuracion = new JTextField();
        panelForm.add(txtDuracion);

        panelForm.add(new JLabel("Bus (Patente):"));
        comboBuses = new JComboBox<>();
        panelForm.add(comboBuses);

        panelForm.add(new JLabel("Auxiliar:"));
        comboAuxiliar = new JComboBox<>();
        panelForm.add(comboAuxiliar);

        panelForm.add(new JLabel("Conductor Principal:"));
        comboConductor1 = new JComboBox<>();
        panelForm.add(comboConductor1);

        panelForm.add(new JLabel("Conductor Secundario (Opcional):"));
        comboConductor2 = new JComboBox<>();
        comboConductor2.addItem("Ninguno");
        panelForm.add(comboConductor2);

        panelForm.add(new JLabel("Comuna de Salida:"));
        txtComunaSalida = new JTextField();
        panelForm.add(txtComunaSalida);

        panelForm.add(new JLabel("Comuna de Llegada:"));
        txtComunaLlegada = new JTextField();
        panelForm.add(txtComunaLlegada);

        JButton btnCrear = new JButton("Crear Viaje");
        btnCrear.addActionListener(e -> crearViaje());
        panelForm.add(btnCrear);

        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> volver());
        panelForm.add(btnVolver);

        add(panelForm);
    }

    private void cargarEmpresas() {
        comboEmpresas.removeAllItems();
        String[][] empresas = ControladorEmpresas.getInstancia().listEmpresas();
        for (String[] emp : empresas) {
            comboEmpresas.addItem(emp[0] + " - " + emp[1]);
        }
    }

    private void actualizarListasEmpresa() {
        if (comboEmpresas.getSelectedItem() == null) return;

        String seleccion = (String) comboEmpresas.getSelectedItem();
        String rutStr = seleccion.split(" - ")[0];
        Rut rut = Rut.of(rutStr);
        Empresa emp = ControladorEmpresas.getInstancia().findEmpresa(rut);

        comboBuses.removeAllItems();
        comboAuxiliar.removeAllItems();
        comboConductor1.removeAllItems();
        comboConductor2.removeAllItems();
        comboConductor2.addItem("Ninguno");

        if (emp != null) {
            for (Bus b : emp.getBuses()) {
                comboBuses.addItem(b.getPatente());
            }
            for (Tripulante t : emp.getTripulantes()) {
                if (t instanceof Auxiliar) {
                    comboAuxiliar.addItem(t.getIdPersona().toString() + " - " + t.getNombreCompleto().toString());
                } else if (t instanceof Conductor) {
                    comboConductor1.addItem(t.getIdPersona().toString() + " - " + t.getNombreCompleto().toString());
                    comboConductor2.addItem(t.getIdPersona().toString() + " - " + t.getNombreCompleto().toString());
                }
            }
        }
    }

    private void crearViaje() {
        try {
            if (txtFecha.getText().isEmpty() || txtHora.getText().isEmpty() || txtPrecio.getText().isEmpty() ||
                    txtDuracion.getText().isEmpty() || comboBuses.getSelectedItem() == null ||
                    comboAuxiliar.getSelectedItem() == null || comboConductor1.getSelectedItem() == null ||
                    txtComunaSalida.getText().isEmpty() || txtComunaLlegada.getText().isEmpty()) {
                throw new SVPException("Todos los campos obligatorios deben estar llenos.");
            }

            LocalDate fecha = LocalDate.parse(txtFecha.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalTime hora = LocalTime.parse(txtHora.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            int precio = Integer.parseInt(txtPrecio.getText());
            int duracion = Integer.parseInt(txtDuracion.getText());

            String patenteBus = (String) comboBuses.getSelectedItem();

            String auxStr = (String) comboAuxiliar.getSelectedItem();
            String rutAux = auxStr.split(" - ")[0];

            String cond1Str = (String) comboConductor1.getSelectedItem();
            String rutCond1 = cond1Str.split(" - ")[0];

            String cond2Str = (String) comboConductor2.getSelectedItem();
            IdPersona[] idsTripulantes;
            if (cond2Str.equals("Ninguno")) {
                idsTripulantes = new IdPersona[]{Rut.of(rutAux), Rut.of(rutCond1)};
            } else {
                String rutCond2 = cond2Str.split(" - ")[0];
                if (rutCond1.equals(rutCond2)) {
                    throw new SVPException("El conductor principal y secundario no pueden ser la misma persona.");
                }
                idsTripulantes = new IdPersona[]{Rut.of(rutAux), Rut.of(rutCond1), Rut.of(rutCond2)};
            }

            String[] comunas = {txtComunaSalida.getText(), txtComunaLlegada.getText()};

            SistemaVentaPasajes.getInstancia().createViaje(fecha, hora, precio, duracion, patenteBus, idsTripulantes, comunas);
            JOptionPane.showMessageDialog(this, "Viaje creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha u hora incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y duración deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtFecha.setText("");
        txtHora.setText("");
        txtPrecio.setText("");
        txtDuracion.setText("");
        txtComunaSalida.setText("");
        txtComunaLlegada.setText("");
    }

    private void volver() {
        this.setVisible(false);
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }
}
