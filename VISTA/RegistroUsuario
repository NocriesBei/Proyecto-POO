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
        setTitle("Taquería Peñascal - Registro de Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 320);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Registrar Nuevo Usuario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(20, 20, 250, 25);
        contentPane.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(20, 70, 80, 25);
        contentPane.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(110, 70, 230, 25);
        contentPane.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(20, 110, 80, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(110, 110, 230, 25);
        contentPane.add(txtPassword);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setBounds(20, 150, 80, 25);
        contentPane.add(lblRol);

        comboRol = new JComboBox<>();
        comboRol.addItem("Administrador");
        comboRol.addItem("Cajero");
        comboRol.setBounds(110, 150, 230, 25);
        contentPane.add(comboRol);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBackground(new Color(41, 128, 185));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBounds(20, 210, 150, 35);
        contentPane.add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(190, 210, 150, 35);
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
