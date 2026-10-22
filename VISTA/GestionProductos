package com.penascal.vista;

import com.penascal.controlador.controlador;
import com.penascal.modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionProductos extends JFrame {

    private JPanel contentPane;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboTipo;
    private JTextField txtBuscar;
    private String rolActual;

    public GestionProductos(String rolUsuario) {
        this.rolActual = rolUsuario;
        
        setTitle("Taquería Peñascal - Gestión de Productos (" + rolActual + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- BOTÓN REGRESAR ---
        JButton btnRegresar = new JButton("<- Menú");
        btnRegresar.setBounds(12, 0, 90, 25);
        contentPane.add(btnRegresar);

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Gestión de Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBounds(36, 28, 200, 30);
        contentPane.add(lblTitulo);

        // --- FORMULARIO (REGISTRAR) ---
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 70, 80, 25);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(100, 70, 180, 25);
        contentPane.add(txtNombre);

        JLabel lblPrecio = new JLabel("Precio ($):");
        lblPrecio.setBounds(20, 110, 80, 25);
        contentPane.add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(100, 110, 180, 25);
        contentPane.add(txtPrecio);

        JLabel lblTipo = new JLabel("Consumo:");
        lblTipo.setBounds(20, 150, 80, 25);
        contentPane.add(lblTipo);

        comboTipo = new JComboBox<>();
        comboTipo.addItem("Local");
        comboTipo.addItem("Llevar");
        comboTipo.setBounds(100, 150, 180, 25);
        contentPane.add(comboTipo);

        JButton btnGuardar = new JButton("Guardar Producto");
        btnGuardar.setBackground(new Color(41, 128, 185));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBounds(20, 200, 260, 35);
        contentPane.add(btnGuardar);
        
        // --- COMPONENTES DE BÚSQUEDA Y FILTRO ---
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setBounds(295, 25, 60, 25);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(360, 25, 350, 25);
        contentPane.add(txtBuscar);
        
        // --- TABLA (CONSULTAR) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(310, 60, 400, 370);
        contentPane.add(scrollPane);

        tablaProductos = new JTable();
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Tipo");
        tablaProductos.setModel(modeloTabla);
        scrollPane.setViewportView(tablaProductos);

        cargarTabla();

        // --- EVENTO DEL BOTÓN REGRESAR ---
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuPrincipal menu = new MenuPrincipal(rolActual);
                menu.setVisible(true);
                dispose();
            }
        });

        // --- EVENTO DE BÚSQUEDA GLOBAL EN TIEMPO REAL ---
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String query = txtBuscar.getText().trim();
                filtrarTabla(query);
            }
        });

        // --- RESTRICCIÓN POR ROL ---
        if ("Cajero".equalsIgnoreCase(this.rolActual)) {
            lblTitulo.setText("Gestión (Cajero)");
        }

        // EVENTO DEL BOTÓN GUARDAR
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                String precioTexto = txtPrecio.getText().trim();
                String tipo = comboTipo.getSelectedItem().toString();

                if (nombre.isEmpty() || precioTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos antes de guardar.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    double precio = Double.parseDouble(precioTexto);

                    if (precio <= 0) {
                        JOptionPane.showMessageDialog(null, "El precio debe ser mayor a cero.", "Regla de negocio", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Producto nuevoProducto = new Producto(nombre, precio, tipo);
                    controlador ctrl = new controlador();

                    if (ctrl.registrarProducto(nuevoProducto)) {
                        JOptionPane.showMessageDialog(null, "¡Producto registrado con éxito!");
                        txtNombre.setText("");
                        txtPrecio.setText("");
                        cargarTabla(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al guardar en la base de datos.", "Error BD", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduce un precio válido en número (ej. 15.50).", "Error de captura", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        controlador ctrl = new controlador();
        List<Producto> lista = ctrl.obtenerProductos();

        for (Producto p : lista) {
            Object[] fila = { p.getId(), p.getNombre(), p.getPrecio(), p.getTipoConsumo() };
            modeloTabla.addRow(fila);
        }
    }

    private void filtrarTabla(String texto) {
        modeloTabla.setRowCount(0);
        controlador ctrl = new controlador();
        List<Producto> lista = ctrl.obtenerProductos();
        String query = texto.toLowerCase();

        for (Producto p : lista) {
            String idStr = String.valueOf(p.getId());
            String nombreStr = p.getNombre().toLowerCase();
            String precioStr = String.valueOf(p.getPrecio());
            String tipoStr = p.getTipoConsumo().toLowerCase();

            if (idStr.contains(query) || nombreStr.contains(query) || precioStr.contains(query) || tipoStr.contains(query)) {
                Object[] fila = { p.getId(), p.getNombre(), p.getPrecio(), p.getTipoConsumo() };
                modeloTabla.addRow(fila);
            }
        }
    }
}
