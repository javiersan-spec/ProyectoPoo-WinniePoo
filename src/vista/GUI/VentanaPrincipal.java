package vista.GUI;

import controlador.SistemaVentaPasajes;
import excepciones.SVPException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Sistema de Venta de Pasajes - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BorderLayout(10, 10));
        panelContenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelContenedor.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(7, 1, 10, 10));

        JButton btnLeerDatos = new JButton("Leer Datos Iniciales");
        JButton btnGuardarDatos = new JButton("Guardar Datos del Sistema");
        JButton btnRecuperarDatos = new JButton("Recuperar Datos del Sistema");
        JButton btnVenderPasajes = new JButton("Vender Pasajes");
        JButton btnCrearViaje = new JButton("Crear Viaje");
        JButton btnConsultas = new JButton("Consultas del Sistema");
        JButton btnSalir = new JButton("Salir");

        panelBotones.add(btnLeerDatos);
        panelBotones.add(btnGuardarDatos);
        panelBotones.add(btnRecuperarDatos);
        panelBotones.add(btnVenderPasajes);
        panelBotones.add(btnCrearViaje);
        panelBotones.add(btnConsultas);
        panelBotones.add(btnSalir);

        panelContenedor.add(panelBotones, BorderLayout.CENTER);
        add(panelContenedor);

        btnLeerDatos.addActionListener(e -> {
            try {
                SistemaVentaPasajes.getInstancia().readDatosIniciales();
                JOptionPane.showMessageDialog(this, "Datos iniciales leídos exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SVPException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnGuardarDatos.addActionListener(e -> {
            try {
                SistemaVentaPasajes.getInstancia().saveDatosSistema();
                JOptionPane.showMessageDialog(this, "Datos guardados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SVPException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRecuperarDatos.addActionListener(e -> {
            try {
                SistemaVentaPasajes.getInstancia().readDatosSistema();
                JOptionPane.showMessageDialog(this, "Datos recuperados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SVPException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVenderPasajes.addActionListener(e -> {
            new VentanaVenderPasajes(this).setVisible(true);
            this.setVisible(false);
        });

        btnCrearViaje.addActionListener(e -> {
            new VentanaCrearViaje(this).setVisible(true);
            this.setVisible(false);
        });

        btnConsultas.addActionListener(e -> {
            new VentanaConsultas(this).setVisible(true);
            this.setVisible(false);
        });

        btnSalir.addActionListener(e -> System.exit(0));
    }
}
