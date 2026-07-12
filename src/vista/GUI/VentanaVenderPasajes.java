package vista.GUI;

import controlador.SistemaVentaPasajes;
import excepciones.SVPException;
import modelo.TipoDocumento;
import utilidades.IdPersona;
import utilidades.Nombre;
import utilidades.Pasaporte;
import utilidades.Rut;
import utilidades.Tratamiento;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaVenderPasajes extends JFrame {

    private VentanaPrincipal ventanaPrincipal;

    private JTextField txtIdDoc, txtFechaViaje, txtOrigen, txtDestino, txtIdCliente, txtNroPasajes;
    private JComboBox<String> comboTipoDoc;
    private JButton btnIniciarVenta;

    private JComboBox<String> comboViajes;
    private JButton btnBuscarViajes;
    private JTextField txtAsientos; // coma separados
    private JButton btnConfirmarViaje;

    private JPanel panelPasajeros;
    private JButton btnPagar;

    private int nroPasajes;
    private String idDocumentoActual;
    private TipoDocumento tipoDocumentoActual;
    private LocalDate fechaViajeActual;
    private String patenteActual;
    private LocalTime horaActual;
    private int[] asientosSeleccionados;
    private int pasajerosRegistrados = 0;

    public VentanaVenderPasajes(VentanaPrincipal principal) {
        this.ventanaPrincipal = principal;
        setTitle("Vender Pasajes");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                volver();
            }
        });
    }

    private void initComponents() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel pnlIniciar = new JPanel(new GridLayout(4, 4, 5, 5));
        pnlIniciar.setBorder(BorderFactory.createTitledBorder("Paso 1: Iniciar Venta"));

        pnlIniciar.add(new JLabel("ID Doc:"));
        txtIdDoc = new JTextField();
        pnlIniciar.add(txtIdDoc);

        pnlIniciar.add(new JLabel("Tipo Doc:"));
        comboTipoDoc = new JComboBox<>(new String[]{"Boleta", "Factura"});
        pnlIniciar.add(comboTipoDoc);

        pnlIniciar.add(new JLabel("Fecha Viaje (dd/MM/yyyy):"));
        txtFechaViaje = new JTextField();
        pnlIniciar.add(txtFechaViaje);

        pnlIniciar.add(new JLabel("RUT/Pasaporte Cliente:"));
        txtIdCliente = new JTextField();
        pnlIniciar.add(txtIdCliente);

        pnlIniciar.add(new JLabel("Comuna Origen:"));
        txtOrigen = new JTextField();
        pnlIniciar.add(txtOrigen);

        pnlIniciar.add(new JLabel("Comuna Destino:"));
        txtDestino = new JTextField();
        pnlIniciar.add(txtDestino);

        pnlIniciar.add(new JLabel("Nro. Pasajes:"));
        txtNroPasajes = new JTextField();
        pnlIniciar.add(txtNroPasajes);

        btnIniciarVenta = new JButton("Iniciar Venta");
        btnIniciarVenta.addActionListener(e -> iniciarVenta());
        pnlIniciar.add(btnIniciarVenta);

        add(pnlIniciar);

        JPanel pnlViaje = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlViaje.setBorder(BorderFactory.createTitledBorder("Paso 2: Seleccionar Viaje y Asientos"));

        btnBuscarViajes = new JButton("Buscar Viajes Disponibles");
        btnBuscarViajes.setEnabled(false);
        btnBuscarViajes.addActionListener(e -> buscarViajes());
        pnlViaje.add(btnBuscarViajes);

        comboViajes = new JComboBox<>();
        comboViajes.setEnabled(false);
        pnlViaje.add(comboViajes);

        pnlViaje.add(new JLabel("Asientos (ej: 1,4,5):"));
        txtAsientos = new JTextField();
        txtAsientos.setEnabled(false);
        pnlViaje.add(txtAsientos);

        btnConfirmarViaje = new JButton("Confirmar Asientos");
        btnConfirmarViaje.setEnabled(false);
        btnConfirmarViaje.addActionListener(e -> confirmarAsientos());
        pnlViaje.add(btnConfirmarViaje);

        add(pnlViaje);

        panelPasajeros = new JPanel();
        panelPasajeros.setLayout(new BoxLayout(panelPasajeros, BoxLayout.Y_AXIS));
        panelPasajeros.setBorder(BorderFactory.createTitledBorder("Paso 3: Pasajeros"));

        btnPagar = new JButton("Finalizar y Pagar Venta");
        btnPagar.setEnabled(false);
        btnPagar.addActionListener(e -> pagarVenta());

        JScrollPane scroll = new JScrollPane(panelPasajeros);
        add(scroll);

        JPanel pnlBotones = new JPanel();
        pnlBotones.add(btnPagar);
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> volver());
        pnlBotones.add(btnVolver);
        add(pnlBotones);
    }

    private void iniciarVenta() {
        try {
            idDocumentoActual = txtIdDoc.getText();
            tipoDocumentoActual = comboTipoDoc.getSelectedIndex() == 0 ? TipoDocumento.BOLETA : TipoDocumento.FACTURA;
            fechaViajeActual = LocalDate.parse(txtFechaViaje.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            IdPersona idCliente = parseIdPersona(txtIdCliente.getText());
            nroPasajes = Integer.parseInt(txtNroPasajes.getText());

            SistemaVentaPasajes.getInstancia().iniciaVenta(idDocumentoActual, tipoDocumentoActual, LocalDate.now(),
                    fechaViajeActual, txtOrigen.getText(), txtDestino.getText(), idCliente, nroPasajes);

            JOptionPane.showMessageDialog(this, "Venta iniciada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            deshabilitarPaso1();
            btnBuscarViajes.setEnabled(true);
            comboViajes.setEnabled(true);
            txtAsientos.setEnabled(true);
            btnConfirmarViaje.setEnabled(true);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de pasajes inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarViajes() {
        comboViajes.removeAllItems();
        String[][] viajes = SistemaVentaPasajes.getInstancia().getHorariosDisponibles(
                fechaViajeActual, txtOrigen.getText(), txtDestino.getText(), nroPasajes);

        if (viajes.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay viajes disponibles.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (String[] v : viajes) {
            comboViajes.addItem(v[0] + " | " + v[1] + " | $" + v[2] + " | Disp:" + v[3]);
        }
    }

    private void confirmarAsientos() {
        try {
            if (comboViajes.getSelectedItem() == null) {
                throw new SVPException("Debe seleccionar un viaje.");
            }

            String seleccion = (String) comboViajes.getSelectedItem();
            patenteActual = seleccion.split(" \\| ")[0];
            horaActual = LocalTime.parse(seleccion.split(" \\| ")[1], DateTimeFormatter.ofPattern("HH:mm"));

            String[] asientosStr = txtAsientos.getText().split(",");
            if (asientosStr.length != nroPasajes) {
                throw new SVPException("Debe ingresar exactamente " + nroPasajes + " asientos separados por coma.");
            }

            asientosSeleccionados = new int[nroPasajes];
            for (int i = 0; i < nroPasajes; i++) {
                asientosSeleccionados[i] = Integer.parseInt(asientosStr[i].trim());
            }

            generarFormularioPasajeros();

            btnBuscarViajes.setEnabled(false);
            comboViajes.setEnabled(false);
            txtAsientos.setEnabled(false);
            btnConfirmarViaje.setEnabled(false);

        } catch (SVPException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de asientos incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarFormularioPasajeros() {
        panelPasajeros.removeAll();
        for (int i = 0; i < nroPasajes; i++) {
            JPanel p = new JPanel(new GridLayout(5, 2, 2, 2));
            p.setBorder(BorderFactory.createTitledBorder("Pasajero " + (i + 1) + " (Asiento " + asientosSeleccionados[i] + ")"));

            JTextField txtId = new JTextField();
            JTextField txtNombres = new JTextField();
            JTextField txtFono = new JTextField();
            JTextField txtNomContacto = new JTextField();
            JTextField txtFonoContacto = new JTextField();

            p.add(new JLabel("RUT/Pasaporte:")); p.add(txtId);
            p.add(new JLabel("Nombres ApPat ApMat:")); p.add(txtNombres);
            p.add(new JLabel("Teléfono:")); p.add(txtFono);
            p.add(new JLabel("Nombres Contacto:")); p.add(txtNomContacto);
            p.add(new JLabel("Fono Contacto:")); p.add(txtFonoContacto);

            JButton btnRegistrar = new JButton("Registrar Pasajero");
            int indicePasaje = i;
            btnRegistrar.addActionListener(e -> registrarPasajero(p, txtId, txtNombres, txtFono, txtNomContacto, txtFonoContacto, indicePasaje, btnRegistrar));
            p.add(btnRegistrar);

            panelPasajeros.add(p);
        }
        panelPasajeros.revalidate();
        panelPasajeros.repaint();
    }

    private void registrarPasajero(JPanel p, JTextField txtId, JTextField txtNombres, JTextField txtFono,
                                   JTextField txtNomContacto, JTextField txtFonoContacto, int indice, JButton btnRegistrar) {
        try {
            IdPersona idPasajero = parseIdPersona(txtId.getText());
            String[] nomParts = txtNombres.getText().split(" ");
            Nombre nom = new Nombre(Tratamiento.SR, nomParts.length > 0 ? nomParts[0] : "",
                    nomParts.length > 1 ? nomParts[1] : "",
                    nomParts.length > 2 ? nomParts[2] : "");

            String[] nomCParts = txtNomContacto.getText().split(" ");
            Nombre nomC = new Nombre(Tratamiento.SR, nomCParts.length > 0 ? nomCParts[0] : "",
                    nomCParts.length > 1 ? nomCParts[1] : "",
                    nomCParts.length > 2 ? nomCParts[2] : "");

            try {
                SistemaVentaPasajes.getInstancia().createPasajero(idPasajero, nom, txtFono.getText(), nomC, txtFonoContacto.getText());
            } catch (SVPException ex) {
            }

            SistemaVentaPasajes.getInstancia().vendePasaje(idDocumentoActual, tipoDocumentoActual, fechaViajeActual,
                    horaActual, patenteActual, asientosSeleccionados[indice], idPasajero);

            btnRegistrar.setEnabled(false);
            btnRegistrar.setText("Registrado ✓");
            pasajerosRegistrados++;

            if (pasajerosRegistrados == nroPasajes) {
                btnPagar.setEnabled(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar pasajero: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pagarVenta() {
        int opcion = JOptionPane.showOptionDialog(this, "Seleccione el método de pago", "Pagar Venta",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Efectivo", "Tarjeta"}, "Efectivo");

        try {
            if (opcion == 1) {
                String nro = JOptionPane.showInputDialog(this, "Ingrese nro de tarjeta:");
                if (nro != null && !nro.isEmpty()) {
                    SistemaVentaPasajes.getInstancia().pagaVenta(idDocumentoActual, tipoDocumentoActual, Long.parseLong(nro));
                } else {
                    return;
                }
            } else if (opcion == 0) {
                SistemaVentaPasajes.getInstancia().pagaVenta(idDocumentoActual, tipoDocumentoActual);
            } else {
                return;
            }

            SistemaVentaPasajes.getInstancia().generatePasajesVenta(idDocumentoActual, tipoDocumentoActual);
            JOptionPane.showMessageDialog(this, "Venta completada. Archivo de pasajes generado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volver();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en el pago: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deshabilitarPaso1() {
        txtIdDoc.setEnabled(false);
        comboTipoDoc.setEnabled(false);
        txtFechaViaje.setEnabled(false);
        txtIdCliente.setEnabled(false);
        txtOrigen.setEnabled(false);
        txtDestino.setEnabled(false);
        txtNroPasajes.setEnabled(false);
        btnIniciarVenta.setEnabled(false);
    }

    private IdPersona parseIdPersona(String str) {
        if (str.contains("-")) {
            return Rut.of(str);
        } else {
            return Pasaporte.of(str, "CHILENA");
        }
    }

    private void volver() {
        this.setVisible(false);
        ventanaPrincipal.setVisible(true);
        this.dispose();
    }
}
