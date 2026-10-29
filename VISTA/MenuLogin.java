package com.penascal.vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.penascal.controlador.controlador;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField jtContraseña; 
    private JComboBox<String> comUsuarios;
    private boolean cargandoUsuarios = false;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuLogin frame = new MenuLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MenuLogin() {
        Estilo.aplicarTema();
        setTitle("Taquería Peñascal - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 740, 620);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBackground(Estilo.CREMA);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- BANNER DE PAPEL PICADO ---
        JLabel banner = Estilo.banner(740);
        banner.setBounds(0, 0, 740, Estilo.altoBanner(740));
        contentPane.add(banner);

        // --- TARJETA DE LOGIN ---
        JPanel panelLogin = Estilo.tarjeta();
        panelLogin.setBounds(145, 175, 450, 330);
        contentPane.add(panelLogin);

        JLabel jlTaqueria = new JLabel("TAQUERÍA PEÑASCAL");
        jlTaqueria.setHorizontalAlignment(SwingConstants.CENTER);
        Estilo.titulo(jlTaqueria, 27f, Estilo.ROSA);
        jlTaqueria.setBounds(0, 26, 447, 38);
        panelLogin.add(jlTaqueria);

        JLabel jlSub = new JLabel("Sistema de punto de venta");
        jlSub.setHorizontalAlignment(SwingConstants.CENTER);
        Estilo.subtitulo(jlSub);
        jlSub.setBounds(0, 64, 447, 20);
        panelLogin.add(jlSub);

        JPanel barra = Estilo.linea(Estilo.AMARILLO);
        barra.setBounds(183, 94, 80, 4);
        panelLogin.add(barra);

        JLabel jlUsuario = new JLabel("SELECCIONE SU USUARIO");
        Estilo.etiqueta(jlUsuario);
        jlUsuario.setBounds(50, 112, 350, 18);
        panelLogin.add(jlUsuario);

        comUsuarios = new JComboBox<String>();
        Estilo.combo(comUsuarios);
        comUsuarios.setBounds(50, 134, 346, 34);
        panelLogin.add(comUsuarios);

        JLabel jlContraseña = new JLabel("INGRESE SU CONTRASEÑA");
        Estilo.etiqueta(jlContraseña);
        jlContraseña.setBounds(50, 184, 350, 18);
        panelLogin.add(jlContraseña);

        jtContraseña = new JPasswordField();
        jtContraseña.setColumns(10);
        Estilo.campo(jtContraseña);
        jtContraseña.setBounds(50, 206, 346, 34);
        panelLogin.add(jtContraseña);

        JButton btnIngresar = new JButton("Ingresar");
        Estilo.boton(btnIngresar, Estilo.ROSA);
        btnIngresar.setBounds(50, 260, 346, 44);
        panelLogin.add(btnIngresar);

        JLabel jlPie = new JLabel("© 2026 Taquería Peñascal");
        jlPie.setHorizontalAlignment(SwingConstants.CENTER);
        Estilo.subtitulo(jlPie);
        jlPie.setBounds(0, 530, 740, 20);
        contentPane.add(jlPie);

        // --- EVENTO AL SELECCIONAR EN EL COMBOBOX ---
        comUsuarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cargandoUsuarios) return;

                if (comUsuarios.getSelectedItem() != null && 
                    comUsuarios.getSelectedItem().toString().equals("+ agregar nuevo usuario...")) {
                    
                    RegistroUsuario ventanaRegistro = new RegistroUsuario();
                    ventanaRegistro.setVisible(true);
                    
                    ventanaRegistro.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            cargarUsuarios();
                        }
                    });
                }
            }
        });

        // --- EVENTO DEL BOTÓN INGRESAR ---
        btnIngresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (comUsuarios.getSelectedItem() == null) return;
                
                String usuarioSeleccionado = comUsuarios.getSelectedItem().toString();
                String password = new String(jtContraseña.getPassword());
                
                if (usuarioSeleccionado.equals("+ agregar nuevo usuario...")) {
                    RegistroUsuario ventanaRegistro = new RegistroUsuario();
                    ventanaRegistro.setVisible(true);
                    ventanaRegistro.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            cargarUsuarios();
                        }
                    });
                    return;
                }

                controlador ctrl = new controlador();
                String rolUsuario = ctrl.validarLogin(usuarioSeleccionado, password);

                if (rolUsuario != null) {
                    JOptionPane.showMessageDialog(null, "¡Bienvenido, " + usuarioSeleccionado + "! Rol " + rolUsuario);
                    MenuPrincipal menu = new MenuPrincipal(rolUsuario);
                    menu.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        cargandoUsuarios = true;
        comUsuarios.removeAllItems(); 
        comUsuarios.addItem("+ agregar nuevo usuario..."); 

        controlador ctrl = new controlador();
        List<String> listaUsuarios = ctrl.obtenerNombresUsuarios();

        if (listaUsuarios != null) {
            for (String usuario : listaUsuarios) {
                comUsuarios.addItem(usuario);
            }
        }
        cargandoUsuarios = false;
    }
}
