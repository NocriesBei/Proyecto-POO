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
        Estilo.aplicarTema();
        
        setTitle("Taquería Peñascal - Gestión de Productos (" + rolActual + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 840, 570);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Estilo.CREMA);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
         // --- TIRA DE PAPEL PICADO ---
        JPanel tira = Estilo.tira(26);
        tira.setBounds(0, 0, 840, 26);
        contentPane.add(tira);

        // --- BOTÓN REGRESAR ---
        JButton btnRegresar = new JButton("<- Menú");
        Estilo.botonContorno(btnRegresar, Estilo.GRIS);
        btnRegresar.setBounds(20, 30, 100, 32);
        contentPane.add(btnRegresar);

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Gestión de Productos");
        Estilo.titulo(lblTitulo, 24f, Estilo.ROSA);
        lblTitulo.setBounds(138, 28, 400, 36);
        contentPane.add(lblTitulo);

        // --- FORMULARIO (REGISTRAR) ---
        JPanel tarjeta = Estilo.tarjeta();
        tarjeta.setBounds(20, 85, 285, 400);
        contentPane.add(tarjeta);

        JLabel lblNuevo = new JLabel("Nuevo producto");
        Estilo.titulo(lblNuevo, 16f, Estilo.TEXTO);
        lblNuevo.setBounds(22, 16, 240, 26);
        tarjeta.add(lblNuevo);

        JPanel barra = Estilo.linea(Estilo.AMARILLO);
        barra.setBounds(22, 46, 50, 4);
        tarjeta.add(barra);

        JLabel lblNombre = new JLabel("Nombre");
        Estilo.etiqueta(lblNombre);
        lblNombre.setBounds(22, 64, 200, 18);
        tarjeta.add(lblNombre);
        
        txtNombre = new JTextField()
        Estilo.campo(txtNombre);
        txtNombre.setBounds(22, 86, 238, 34);
        tarjeta.add(txtNombre);

        JLabel lblPrecio = new JLabel("Precio ($)");
        Estilo.etiqueta(lblPrecio);
        lblPrecio.setBounds(22, 130, 200, 18);
        tarjeta.add(lblPrecio);

        txtPrecio = new JTextField();
        Estilo.campo(txtPrecio);
        txtPrecio.setBounds(22, 152, 238, 34);
        tarjeta.add(txtPrecio);

        JLabel lblTipo = new JLabel("Consumo");
        Estilo.etiqueta(lblTipo);
        lblTipo.setBounds(22, 196, 200, 18);
        tarjeta.add(lblTipo);

        comboTipo = new JComboBox<>();
        comboTipo.addItem("Local");
        comboTipo.addItem("Llevar");
        Estilo.combo(comboTipo);
        comboTipo.setBounds(22, 218, 238, 34);
        tarjeta.add(comboTipo);

        JButton btnGuardar = new JButton("Guardar Producto");
        Estilo.boton(btnGuardar, Estilo.ROSA);
        btnGuardar.setBounds(22, 282, 238, 42);
        tarjeta.add(btnGuardar);

        // --- BOTÓN ELIMINAR ---
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        Estilo.botonContorno(btnEliminar, Estilo.ROJO);
        btnEliminar.setBounds(22, 336, 238, 42);
        tarjeta.add(btnEliminar);
        
        // --- COMPONENTES DE BÚSQUEDA Y FILTRO ---
        JLabel lblBuscar = new JLabel("Buscar:");
        Estilo.etiqueta(lblBuscar);
        lblBuscar.setBounds(328, 92, 60, 22);
        contentPane.add(lblBuscar);

        txtBuscar = new JTextField();
        Estilo.campo(txtBuscar);
        txtBuscar.setBounds(385, 85, 420, 34);
        contentPane.add(txtBuscar);
        
        // --- TABLA (CONSULTAR) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(328, 132, 477, 353);
        contentPane.add(scrollPane);

        tablaProductos = new JTable();
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Tipo");
        tablaProductos.setModel(modeloTabla);
        scrollPane.setViewportView(tablaProductos);

        // --- ESTILO DE LA TABLA ---
        Estilo.tabla(tablaProductos, scrollPane);
        Estilo.alinear(tablaProductos, 0, SwingConstants.CENTER);
        Estilo.dinero(tablaProductos, 2);
        Estilo.alinear(tablaProductos, 3, SwingConstants.CENTER);
        Estilo.ancho(tablaProductos, 0, 50);
        Estilo.ancho(tablaProductos, 1, 210);
        Estilo.ancho(tablaProductos, 2, 90);
        Estilo.ancho(tablaProductos, 3, 90);

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
            btnGuardar.setEnabled(false);
            btnEliminar.setEnabled(false);
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
        
        // --- EVENTO DEL BOTÓN ELIMINAR ---
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaProductos.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona un producto de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);

                int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controlador ctrl = new controlador();
                    if (ctrl.eliminarProducto(idProducto)) {
                        JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                        cargarTabla(); 
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el producto de la base de datos.", "Error BD", JOptionPane.ERROR_MESSAGE);
                    }
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
