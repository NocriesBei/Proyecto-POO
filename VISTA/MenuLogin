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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 740, 636);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panelLogin = new JPanel();
        panelLogin.setBounds(146, 145, 443, 307);
        contentPane.add(panelLogin);
        panelLogin.setLayout(null);
        
        JLabel jlTaqueria = new JLabel("TAQUERIA PEÑASCAL");
        jlTaqueria.setHorizontalAlignment(SwingConstants.CENTER);
        jlTaqueria.setFont(new Font("Dialog", Font.BOLD, 26));
        jlTaqueria.setBounds(12, 37, 419, 71);
        panelLogin.add(jlTaqueria);
        
        JLabel jlUsuario = new JLabel("SELECCIONE SU USUARIO");
        jlUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        jlUsuario.setBounds(80, 120, 276, 17);
        panelLogin.add(jlUsuario);
        
        comUsuarios = new JComboBox<String>();
        comUsuarios.setBounds(90, 149, 266, 26);
        panelLogin.add(comUsuarios);
        
        JLabel jlContraseña = new JLabel("INGRESE SU CONTRASEÑA");
        jlContraseña.setHorizontalAlignment(SwingConstants.CENTER);
        jlContraseña.setBounds(80, 197, 276, 17);
        panelLogin.add(jlContraseña);
        
        jtContraseña = new JPasswordField();
        jtContraseña.setBounds(90, 222, 266, 21);
        jtContraseña.setColumns(10);
        panelLogin.add(jtContraseña);
        
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(157, 255, 133, 27);
        panelLogin.add(btnIngresar);

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
