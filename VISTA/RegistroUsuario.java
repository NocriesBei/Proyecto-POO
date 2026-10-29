package com.penascal.vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.penascal.controlador.controlador;

public class RegistroUsuario extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;

    public RegistroUsuario() {
        Estilo.aplicarTema();
        setTitle("Taquería Peñascal - Registro de Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 430, 370);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Estilo.CREMA);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel tira = Estilo.tira(26);
        tira.setBounds(0, 0, 430, 26);
        contentPane.add(tira);

        JLabel lblTitulo = new JLabel("Registrar Nuevo Usuario");
        Estilo.titulo(lblTitulo, 20f, Estilo.ROSA);
        lblTitulo.setBounds(30, 38, 360, 30);
        contentPane.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario");
        Estilo.etiqueta(lblUsuario);
        lblUsuario.setBounds(30, 88, 100, 34);
        contentPane.add(lblUsuario);

        txtUsuario = new JTextField();
        Estilo.campo(txtUsuario);
        txtUsuario.setBounds(130, 88, 250, 34);
        contentPane.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña");
        Estilo.etiqueta(lblPassword);
        lblPassword.setBounds(30, 134, 100, 34);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        Estilo.campo(txtPassword);
        txtPassword.setBounds(130, 134, 250, 34);
        contentPane.add(txtPassword);

        JLabel lblRol = new JLabel("Rol");
        Estilo.etiqueta(lblRol);
        lblRol.setBounds(30, 180, 100, 34);
        contentPane.add(lblRol);

        comboRol = new JComboBox<>();
        comboRol.addItem("Administrador");
        comboRol.addItem("Cajero");
        Estilo.combo(comboRol);
        comboRol.setBounds(130, 180, 250, 34);
        contentPane.add(comboRol);

        JButton btnRegistrar = new JButton("Registrar");
        Estilo.boton(btnRegistrar, Estilo.ROSA);
        btnRegistrar.setBounds(30, 245, 170, 42);
        contentPane.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        Estilo.botonContorno(btnCancelar, Estilo.GRIS);
        btnCancelar.setBounds(210, 245, 170, 42);
        contentPane.add(btnCancelar);

        // Evento del botón Registrar
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                String rol = comboRol.getSelectedItem().toString();

                if (usuario.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                controlador ctrl = new controlador();
                if (ctrl.registrarUsuario(usuario, password, rol)) {
                    JOptionPane.showMessageDialog(null, "¡Usuario registrado con éxito!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar usuario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Evento del botón Cancelar
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
