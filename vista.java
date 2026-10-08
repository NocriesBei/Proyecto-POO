package com.penascal.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PantallaPrincipal extends JFrame {

    private JPanel contentPane;
    private JPanel panelCentral; 

    public PantallaPrincipal() {
        setTitle("Taquería Peñascal - Sistema POS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600); // Tamaño amplio y panorámico
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // --- SIDEBAR (Menú Lateral Oscuro) ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(41, 53, 65)); // Color azul marino/gris oscuro
        contentPane.add(sidebar, BorderLayout.WEST);
        sidebar.setLayout(null);
        
        // El truco para darle ancho fijo al sidebar en un BorderLayout
        sidebar.setPreferredSize(new java.awt.Dimension(200, 0));

        JLabel lblLogo = new JLabel("PEÑASCAL");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setBounds(0, 30, 200, 40);
        sidebar.add(lblLogo);

        // Botón Productos
        JButton btnProductos = new JButton("Productos");
        btnProductos.setForeground(Color.WHITE);
        btnProductos.setBackground(new Color(52, 73, 94)); // Ligeramente más claro
        btnProductos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnProductos.setBorderPainted(false); // Quita el borde feo
        btnProductos.setFocusPainted(false);
        btnProductos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnProductos.setBounds(0, 100, 200, 45);
        sidebar.add(btnProductos);

        // Botón Ventas
        JButton btnVentas = new JButton("Nueva Venta");
        btnVentas.setForeground(Color.WHITE);
        btnVentas.setBackground(new Color(41, 53, 65)); // Mismo fondo del sidebar
        btnVentas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnVentas.setBorderPainted(false);
        btnVentas.setFocusPainted(false);
        btnVentas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVentas.setBounds(0, 145, 200, 45);
        sidebar.add(btnVentas);

        // --- PANEL CENTRAL (Área de Trabajo Blanca/Gris Claro) ---
        panelCentral = new JPanel();
        panelCentral.setBackground(new Color(240, 244, 247)); 
        panelCentral.setLayout(new BorderLayout(0, 0));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        JLabel lblBienvenida = new JLabel("Bienvenido al Sistema POS");
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblBienvenida.setForeground(new Color(100, 100, 100));
        panelCentral.add(lblBienvenida, BorderLayout.CENTER);
    }
