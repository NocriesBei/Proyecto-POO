package com.penascal.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String rolActual;
    private PanelResumen panelResumen;

    public MenuPrincipal(String rol) {
        this.rolActual = rol;
        Estilo.aplicarTema();

        setTitle("Taquería Peñascal - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setPreferredSize(new java.awt.Dimension(1120, 690));
        contentPane.setBackground(Estilo.CREMA);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        pack();
        setLocationRelativeTo(null);

        // ================= BARRA LATERAL (MENÚ) =================
        JPanel barraLateral = new JPanel();
        barraLateral.setBackground(Estilo.BLANCO);
        barraLateral.setBorder(new MatteBorder(0, 0, 0, 1, Estilo.BORDE));
        barraLateral.setLayout(null);
        barraLateral.setBounds(0, 0, 300, 690);
        contentPane.add(barraLateral);

        // --- BANNER DE PAPEL PICADO ---
        JLabel banner = Estilo.banner(300);
        banner.setBounds(0, 0, 300, Estilo.altoBanner(300));
        barraLateral.add(banner);

        // --- ENCABEZADOS ---
        JLabel lblTitulo = new JLabel("TAQUERÍA PEÑASCAL");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        Estilo.titulo(lblTitulo, 23f, Estilo.ROSA);
        lblTitulo.setBounds(0, 92, 299, 34);
        barraLateral.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Menú principal  ·  Sesión: " + rolActual);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        Estilo.subtitulo(lblSubtitulo);
        lblSubtitulo.setBounds(0, 128, 299, 20);
        barraLateral.add(lblSubtitulo);

        JPanel barra = Estilo.linea(Estilo.AMARILLO);
        barra.setBounds(110, 160, 80, 4);
        barraLateral.add(barra);

        // --- BOTONES DE MÓDULOS ---
        JButton btnVentas = new JButton("Punto de Venta");
        Estilo.boton(btnVentas, Estilo.ROSA);
        btnVentas.setFont(Estilo.fuente(java.awt.Font.BOLD, 16f));
        btnVentas.setBounds(30, 196, 240, 58);
        barraLateral.add(btnVentas);

        btnVentas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PuntoVenta ventanaVentas = new PuntoVenta(rolActual); 
                ventanaVentas.setVisible(true);
                dispose(); 
            }
        });
        
        JButton btnProductos = new JButton("Gestión de Productos");
        Estilo.boton(btnProductos, Estilo.NARANJA);
        btnProductos.setFont(Estilo.fuente(java.awt.Font.BOLD, 16f));
        btnProductos.setBounds(30, 268, 240, 58);
        barraLateral.add(btnProductos);
        
        JButton btnReportes = new JButton("Reportes y Cortes");
        Estilo.boton(btnReportes, Estilo.TURQUESA);
        btnReportes.setFont(Estilo.fuente(java.awt.Font.BOLD, 16f));
        btnReportes.setBounds(30, 340, 240, 58);
        barraLateral.add(btnReportes);
        btnReportes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReportesCorte ventanaReportes = new ReportesCorte(rolActual);
                ventanaReportes.setVisible(true);
                dispose();
            }
        });
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        Estilo.botonContorno(btnCerrarSesion, Estilo.ROJO);
        btnCerrarSesion.setBounds(60, 560, 180, 40);
        barraLateral.add(btnCerrarSesion);
        
        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (java.awt.Window ventana : java.awt.Window.getWindows()) {
                    ventana.dispose();
                }
                
                MenuLogin login = new MenuLogin();
                login.setVisible(true);
            }
        });

        // ================= RESUMEN (GRÁFICAS) =================
        panelResumen = new PanelResumen(rolActual);
        panelResumen.setBounds(322, 24, 770, 640);
        contentPane.add(panelResumen);

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
            }
        });
    }
}
