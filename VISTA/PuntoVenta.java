package com.penascal.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PuntoVenta extends JFrame {
    private JPanel contentPane;
    private JTable tablaTicket;
    private DefaultTableModel modeloTicket;
    private JLabel lblTotal;
    private double totalVenta = 0.0;
    private String rolActual;

    public PuntoVenta(String rol) {
        this.rolActual = rol;
        Estilo.aplicarTema();
        setTitle("Taquería Peñascal - Punto de Venta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Estilo.CREMA);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null); 

        // --- 1. PANEL DERECHO (TICKET) ---
        JPanel panelTicket = new JPanel();
        panelTicket.setBackground(Estilo.CREMA);
        panelTicket.setBorder(new javax.swing.border.MatteBorder(0, 4, 0, 0, Estilo.ROSA));
        panelTicket.setBounds(580, 0, 400, 660);
        panelTicket.setLayout(null);
        contentPane.add(panelTicket);

        JButton btnRegresar = new JButton(Estilo.atras("Regresar"));
        Estilo.botonContorno(btnRegresar, Estilo.GRIS);
        btnRegresar.setBounds(14, 12, 120, 32);
        panelTicket.add(btnRegresar);
        btnRegresar.addActionListener(e -> {
            MenuPrincipal menu = new MenuPrincipal(this.rolActual);
            menu.setVisible(true);
            dispose();
        });

        JLabel lblTicket = new JLabel("TICKET DE VENTA");
        lblTicket.setHorizontalAlignment(SwingConstants.RIGHT);
        Estilo.titulo(lblTicket, 18f, Estilo.ROSA);
        lblTicket.setBounds(140, 12, 240, 32);
        panelTicket.add(lblTicket);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(14, 56, 366, 444);
        panelTicket.add(scrollPane);

        modeloTicket = new DefaultTableModel(new Object[][]{}, new String[]{"Producto", "Cant", "Precio", "Subtotal"});
        tablaTicket = new JTable(modeloTicket);
        scrollPane.setViewportView(tablaTicket);

        // --- ESTILO DE LA TABLA DEL TICKET ---
        Estilo.tabla(tablaTicket, scrollPane);
        Estilo.alinear(tablaTicket, 1, SwingConstants.CENTER);
        Estilo.dinero(tablaTicket, 2);
        Estilo.dinero(tablaTicket, 3);
        Estilo.ancho(tablaTicket, 0, 148);
        Estilo.ancho(tablaTicket, 1, 58);
        Estilo.ancho(tablaTicket, 2, 74);
        Estilo.ancho(tablaTicket, 3, 86);

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        Estilo.titulo(lblTotal, 28f, Estilo.TEXTO);
        lblTotal.setBounds(14, 510, 366, 40);
        panelTicket.add(lblTotal);

        JButton btnCobrar = new JButton("COBRAR");
        Estilo.boton(btnCobrar, Estilo.VERDE);
        btnCobrar.setFont(Estilo.fuente(Font.BOLD, 20f));
        btnCobrar.setBounds(14, 562, 366, 56);
        panelTicket.add(btnCobrar);
        
        // --- EVENTO DE COBRAR INTEGRADO CON MYSQL ---
        btnCobrar.addActionListener(e -> {
            if (modeloTicket.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "El ticket está vacío. Agrega platillos primero.");
                return;
            }

            String url = "jdbc:mysql://localhost:3306/proyectoPoo";
            String user = "root";
            String pass = "admin"; 

            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                
                // 1. Guardar la Venta General
                String sqlVenta = "INSERT INTO ventas (total, tipoConsumo, metodoPago, id_usuario) VALUES (?, 'Local', 'Efectivo', 1)";
                PreparedStatement psVenta = conn.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS);
                psVenta.setDouble(1, totalVenta);
                psVenta.executeUpdate();

                // 2. Obtener el idVenta generado
                ResultSet rs = psVenta.getGeneratedKeys();
                int idVenta = 0;
                if (rs.next()) {
                    idVenta = rs.getInt(1);
                }

                // 3. Guardar en detalleVenta
                String sqlDetalle = "INSERT INTO detalleVenta (idVenta, idProducto, cantidad, subtotal) VALUES (?, (SELECT idProducto FROM Productos WHERE nombre = ? LIMIT 1), ?, ?)";
                PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle);

                for (int i = 0; i < modeloTicket.getRowCount(); i++) {
                    String nombrePlatillo = (String) modeloTicket.getValueAt(i, 0);
                    int cantidad = (int) modeloTicket.getValueAt(i, 1);
                    double subtotal = (double) modeloTicket.getValueAt(i, 3);

                    psDetalle.setInt(1, idVenta);
                    psDetalle.setString(2, nombrePlatillo);
                    psDetalle.setInt(3, cantidad);
                    psDetalle.setDouble(4, subtotal);
                    psDetalle.addBatch(); 
                }
                psDetalle.executeBatch(); 

                // 4. Limpiar el ticket
                modeloTicket.setRowCount(0);
                totalVenta = 0.0;
                lblTotal.setText("Total: $0.00");
                JOptionPane.showMessageDialog(null, "¡Venta cobrada y guardada en la base de datos con éxito!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error en BD: " + ex.getMessage());
            }
        });


        // ================= TACOS ($15) =================
        JButton btn1 = new JButton("+");
        btn1.setBounds(25, 235, 20, 13);
        btn1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn1.setMargin(new Insets(0, 0, 0, 0));
        btn1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn1.addActionListener(e -> agregarAlTicket("Taco Maciza", 15.00));
        contentPane.add(btn1);
        
        JButton btn2 = new JButton("+");
        btn2.setBounds(25, 250, 20, 13);
        btn2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn2.setMargin(new Insets(0, 0, 0, 0));
        btn2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn2.addActionListener(e -> agregarAlTicket("Taco Surtido", 15.00));
        contentPane.add(btn2);

        JButton btn3 = new JButton("+");
        btn3.setBounds(25, 265, 20, 13);
        btn3.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn3.setMargin(new Insets(0, 0, 0, 0));
        btn3.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn3.addActionListener(e -> agregarAlTicket("Taco Cuerito", 15.00));
        contentPane.add(btn3);

        JButton btn4 = new JButton("+");
        btn4.setBounds(25, 280, 20, 13);
        btn4.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn4.setMargin(new Insets(0, 0, 0, 0));
        btn4.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn4.addActionListener(e -> agregarAlTicket("Taco Maciza con Cuerito", 15.00));
        contentPane.add(btn4);

        JButton btn5 = new JButton("+");
        btn5.setBounds(25, 305, 20, 13);
        btn5.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn5.setMargin(new Insets(0, 0, 0, 0));
        btn5.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn5.addActionListener(e -> agregarAlTicket("Taco Longaniza", 15.00));
        contentPane.add(btn5);

        JButton btn6 = new JButton("+");
        btn6.setBounds(25, 345, 20, 13);
        btn6.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn6.setMargin(new Insets(0, 0, 0, 0));
        btn6.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn6.addActionListener(e -> agregarAlTicket("Taco Cabeza", 15.00));
        contentPane.add(btn6);

        JButton btn7 = new JButton("+");
        btn7.setBounds(25, 319, 20, 13);
        btn7.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn7.setMargin(new Insets(0, 0, 0, 0));
        btn7.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn7.addActionListener(e -> agregarAlTicket("Taco Chicharrón", 15.00));
        contentPane.add(btn7);

        // ================= TACOS ($20 y $25) =================
        JButton btn8 = new JButton("+");
        btn8.setBounds(25, 360, 20, 13);
        btn8.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn8.setMargin(new Insets(0, 0, 0, 0));
        btn8.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn8.addActionListener(e -> agregarAlTicket("Taco Combinado", 20.00));
        contentPane.add(btn8);

        JButton btn9 = new JButton("+");
        btn9.setBounds(25, 376, 20, 13);
        btn9.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn9.setMargin(new Insets(0, 0, 0, 0));
        btn9.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn9.addActionListener(e -> agregarAlTicket("Taco Costilla Chile Seco", 20.00));
        contentPane.add(btn9);

        JButton btn10 = new JButton("+");
        btn10.setBounds(25, 404, 20, 13);
        btn10.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn10.setMargin(new Insets(0, 0, 0, 0));
        btn10.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn10.addActionListener(e -> agregarAlTicket("Taco Bistec", 20.00));
        contentPane.add(btn10);

        JButton btn11 = new JButton("+");
        btn11.setBounds(25, 444, 20, 13);
        btn11.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn11.setMargin(new Insets(0, 0, 0, 0));
        btn11.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn11.addActionListener(e -> agregarAlTicket("Taco Barbacoa", 20.00));
        contentPane.add(btn11);

        JButton btn12 = new JButton("+");
        btn12.setBounds(25, 475, 20, 13);
        btn12.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn12.setMargin(new Insets(0, 0, 0, 0));
        btn12.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn12.addActionListener(e -> agregarAlTicket("Taco Carne Enchilada", 20.00));
        contentPane.add(btn12);

        JButton btn13 = new JButton("+");
        btn13.setBounds(25, 490, 20, 13);
        btn13.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn13.setMargin(new Insets(0, 0, 0, 0));
        btn13.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn13.addActionListener(e -> agregarAlTicket("Taco Cecina", 25.00));
        contentPane.add(btn13);

        // ================= FIN DE SEMANA =================
        JButton btn14 = new JButton("+");
        btn14.setBounds(25, 520, 20, 13);
        btn14.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn14.setMargin(new Insets(0, 0, 0, 0));
        btn14.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn14.addActionListener(e -> agregarAlTicket("Taco Dorado Sesos", 13.00));
        contentPane.add(btn14);

        JButton btn15 = new JButton("+");
        btn15.setBounds(25, 546, 20, 13);
        btn15.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn15.setMargin(new Insets(0, 0, 0, 0));
        btn15.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn15.addActionListener(e -> agregarAlTicket("Taco Oreja", 15.00));
        contentPane.add(btn15);

        JButton btn16 = new JButton("+");
        btn16.setBounds(25, 576, 20, 13);
        btn16.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn16.setMargin(new Insets(0, 0, 0, 0));
        btn16.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn16.addActionListener(e -> agregarAlTicket("Taco Buche", 15.00));
        contentPane.add(btn16);

        JButton btn17 = new JButton("+");
        btn17.setBounds(25, 591, 20, 13);
        btn17.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn17.setMargin(new Insets(0, 0, 0, 0));
        btn17.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn17.addActionListener(e -> agregarAlTicket("Taco Costilla Ahumada", 20.00));
        contentPane.add(btn17);

        JButton btn18 = new JButton("+");
        btn18.setBounds(25, 607, 20, 13);
        btn18.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn18.setMargin(new Insets(0, 0, 0, 0));
        btn18.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn18.addActionListener(e -> agregarAlTicket("Taco Lomo Ahumado", 20.00));
        contentPane.add(btn18);

        // ================= QUESADILLAS Y TORTAS (Lado Derecho) =================
        JButton btn19 = new JButton("+");
        btn19.setBounds(285, 235, 20, 13);
        btn19.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn19.setMargin(new Insets(0, 0, 0, 0));
        btn19.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn19.addActionListener(e -> agregarAlTicket("Quesadilla Maciza", 38.00));
        contentPane.add(btn19);

        JButton btn20 = new JButton("+");
        btn20.setBounds(285, 247, 20, 13);
        btn20.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn20.setMargin(new Insets(0, 0, 0, 0));
        btn20.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn20.addActionListener(e -> agregarAlTicket("Quesadilla Surtido", 38.00));
        contentPane.add(btn20);

        JButton btn21 = new JButton("+");
        btn21.setBounds(285, 262, 20, 13);
        btn21.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn21.setMargin(new Insets(0, 0, 0, 0));
        btn21.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn21.addActionListener(e -> agregarAlTicket("Quesadilla Cuerito", 38.00));
        contentPane.add(btn21);

        JButton btn22 = new JButton("+");
        btn22.setBounds(285, 277, 20, 13);
        btn22.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn22.setMargin(new Insets(0, 0, 0, 0));
        btn22.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn22.addActionListener(e -> agregarAlTicket("Quesadilla Maciza/Cuerito", 38.00));
        contentPane.add(btn22);

        JButton btn23 = new JButton("+");
        btn23.setBounds(285, 308, 20, 13);
        btn23.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn23.setMargin(new Insets(0, 0, 0, 0));
        btn23.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn23.addActionListener(e -> agregarAlTicket("Quesadilla Champiñón", 40.00));
        contentPane.add(btn23);

        JButton btn24 = new JButton("+");
        btn24.setBounds(285, 330, 20, 13);
        btn24.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn24.setMargin(new Insets(0, 0, 0, 0));
        btn24.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn24.addActionListener(e -> agregarAlTicket("Quesadilla Costilla Chile Seco", 40.00));
        contentPane.add(btn24);

        JButton btn25 = new JButton("+");
        btn25.setBounds(285, 351, 20, 13);
        btn25.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn25.setMargin(new Insets(0, 0, 0, 0));
        btn25.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn25.addActionListener(e -> agregarAlTicket("Quesadilla Barbacoa", 40.00));
        contentPane.add(btn25);
        
        JButton btn26 = new JButton("+");
        btn26.setBounds(285, 445, 20, 13);
        btn26.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn26.setMargin(new Insets(0, 0, 0, 0));
        btn26.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn26.addActionListener(e -> agregarAlTicket("Torta Maciza", 38.00));
        contentPane.add(btn26);

        JButton btn27 = new JButton("+");
        btn27.setBounds(285, 460, 20, 13);
        btn27.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn27.setMargin(new Insets(0, 0, 0, 0));
        btn27.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn27.addActionListener(e -> agregarAlTicket("Torta Surtido", 38.00));
        contentPane.add(btn27);

        JButton btn28 = new JButton("+");
        btn28.setBounds(285, 475, 20, 13);
        btn28.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn28.setMargin(new Insets(0, 0, 0, 0));
        btn28.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn28.addActionListener(e -> agregarAlTicket("Torta Cuerito", 38.00));
        contentPane.add(btn28);

        JButton btn29 = new JButton("+");
        btn29.setBounds(285, 490, 20, 13);
        btn29.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn29.setMargin(new Insets(0, 0, 0, 0));
        btn29.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn29.addActionListener(e -> agregarAlTicket("Torta Costilla Chile Seco", 40.00));
        contentPane.add(btn29);

        JButton btn30 = new JButton("+");
        btn30.setBounds(285, 520, 20, 13);
        btn30.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn30.setMargin(new Insets(0, 0, 0, 0));
        btn30.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn30.addActionListener(e -> agregarAlTicket("Torta Barbacoa", 40.00));
        contentPane.add(btn30);
        
        JButton btn31 = new JButton("+");
        btn31.setMargin(new Insets(0, 0, 0, 0));
        btn31.setFont(new Font("Dialog", Font.BOLD, 10));
        btn31.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn31.setBounds(285, 542, 20, 13);
        btn31.addActionListener(e -> agregarAlTicket("Barbacoa de Res", 40.00));
        contentPane.add(btn31);
        
        JButton btn32 = new JButton("+");
        btn32.setMargin(new Insets(0, 0, 0, 0));
        btn32.setFont(new Font("Dialog", Font.BOLD, 10));
        btn32.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn32.setBounds(285, 558, 20, 13);
        btn32.addActionListener(e -> agregarAlTicket("Carne Enchilada", 40.00));
        contentPane.add(btn32);
        
        JButton btn33 = new JButton("+");
        btn33.setMargin(new Insets(0, 0, 0, 0));
        btn33.setFont(new Font("Dialog", Font.BOLD, 10));
        btn33.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn33.setBounds(285, 365, 20, 13);
        btn33.addActionListener(e -> agregarAlTicket("Carne Enchilada", 40.00));
        contentPane.add(btn33);

        // --- ESTILO DE LOS BOTONES ---
        for (Component c : contentPane.getComponents()) {
            if (c instanceof JButton) {
                Estilo.botonMas((JButton) c);
            }
        }

        // --- 3. FONDO DEL MENÚ ---
        JLabel lblFondoMenu = new JLabel("");
        lblFondoMenu.setBounds(0, 0, 580, 660);
        java.net.URL urlFondo = getClass().getResource("/Imagenes/Imagen_Taqueria.jpeg");
        if (urlFondo != null) {
            ImageIcon iconoOriginal = new ImageIcon(urlFondo);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(580, 660, Image.SCALE_SMOOTH);
            lblFondoMenu.setIcon(new ImageIcon(imagenEscalada));
        }
        contentPane.add(lblFondoMenu);
    }

    private void agregarAlTicket(String producto, double precio) {
        boolean existe = false;
        for (int i = 0; i < modeloTicket.getRowCount(); i++) {
            if (modeloTicket.getValueAt(i, 0).equals(producto)) {
                int cantidad = (int) modeloTicket.getValueAt(i, 1) + 1;
                modeloTicket.setValueAt(cantidad, i, 1);
                modeloTicket.setValueAt(cantidad * precio, i, 3); 
                existe = true;
                break;
            }
        }
        if (!existe) {
            modeloTicket.addRow(new Object[]{producto, 1, precio, precio});
        }
        
        totalVenta += precio;
        lblTotal.setText(String.format("Total: $%.2f", totalVenta));
    }
}
