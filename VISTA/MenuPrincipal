package com.penascal.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String rolActual;

    public MenuPrincipal(String rol) {
        this.rolActual = rol;
        
        setTitle("Taquería Peñascal - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 500);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // --- ENCABEZADOS ---
        JLabel lblTitulo = new JLabel("TAQUERÍA PEÑASCAL");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 28));
        lblTitulo.setBounds(10, 30, 564, 40);
        contentPane.add(lblTitulo);
        
        // --- BOTONES DE MÓDULOS ---
        JButton btnVentas = new JButton("1. Punto de Venta");
        btnVentas.setFont(new Font("Dialog", Font.BOLD, 14));
        btnVentas.setBackground(new Color(39, 174, 96));
        btnVentas.setForeground(Color.WHITE);
        btnVentas.setBounds(150, 130, 280, 50);
        contentPane.add(btnVentas);
        
        btnVentas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PuntoVenta ventanaVentas = new PuntoVenta(rolActual); 
                ventanaVentas.setVisible(true);
                dispose(); 
            }
        });
        
        JButton btnProductos = new JButton("2. Gestión de Productos");
        btnProductos.setFont(new Font("Dialog", Font.BOLD, 14));
        btnProductos.setBackground(new Color(41, 128, 185));
        btnProductos.setForeground(Color.WHITE);
        btnProductos.setBounds(150, 200, 280, 50);
        contentPane.add(btnProductos);
        
        JButton btnReportes = new JButton("3. Reportes y Cortes");
        btnReportes.setFont(new Font("Dialog", Font.BOLD, 14));
        btnReportes.setBackground(new Color(142, 68, 173));
        btnReportes.setForeground(Color.WHITE);
        btnReportes.setBounds(150, 270, 280, 50);
        contentPane.add(btnReportes);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(192, 57, 43));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBounds(210, 370, 160, 35);
        contentPane.add(btnCerrarSesion);
        
        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (java.awt.Window ventana : java.awt.Window.getWindows()) {
                    ventana.dispose();
                }
                
                MenuLogin login = new MenuLogin();
                login.setVisible(true);
            }
        });

        // --- RESTRICCIÓN DE ACCESOS POR ROL ---
        if (rolActual.equalsIgnoreCase("Cajero")) {
            btnProductos.setEnabled(false);
            btnReportes.setEnabled(false);
        }

        // --- ACCIONES DE LOS DEMÁS BOTONES ---
        btnProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GestionProductos ventanaProductos = new GestionProductos(rolActual);
                ventanaProductos.setVisible(true);
            }
        });
        
        btnReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Módulo de Reportes en construcción.");
            }
        });
    }
}
