package com.penascal.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileOutputStream;

public class ReportesCorte extends JFrame {

    private JPanel contentPane;
    private JTable tablaReportes;
    private DefaultTableModel modeloReporte;
    private JLabel lblTotalCaja;
    private String rolActual;

    public ReportesCorte(String rol) {
        this.rolActual = rol;
        Estilo.aplicarTema();
        setTitle("Taquería Peñascal - Reportes y Corte de Caja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 860, 610);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Estilo.CREMA);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- TIRA DE PAPEL PICADO ---
        JPanel tira = Estilo.tira(26);
        tira.setBounds(0, 0, 860, 26);
        contentPane.add(tira);

        // --- BOTÓN REGRESAR ---
        JButton btnRegresar = new JButton(Estilo.atras("Menú"));
        Estilo.botonContorno(btnRegresar, Estilo.GRIS);
        btnRegresar.setBounds(22, 38, 100, 32);
        contentPane.add(btnRegresar);
        btnRegresar.addActionListener(e -> {
            MenuPrincipal menu = new MenuPrincipal(this.rolActual);
            menu.setVisible(true);
            dispose();
        });

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Corte de Caja y Reporte de Ventas");
        Estilo.titulo(lblTitulo, 24f, Estilo.ROSA);
        lblTitulo.setBounds(140, 36, 560, 36);
        contentPane.add(lblTitulo);

        // --- TABLA DE VENTAS / REPORTE ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(22, 90, 800, 350);
        contentPane.add(scrollPane);

        modeloReporte = new DefaultTableModel(new Object[][]{}, new String[]{"ID Venta", "Total", "Tipo Consumo", "Método Pago", "Fecha"});
        tablaReportes = new JTable(modeloReporte);
        scrollPane.setViewportView(tablaReportes);

        // --- ESTILO DE LA TABLA ---
        Estilo.tabla(tablaReportes, scrollPane);
        Estilo.alinear(tablaReportes, 0, SwingConstants.CENTER);
        Estilo.alinear(tablaReportes, 1, SwingConstants.RIGHT);
        Estilo.alinear(tablaReportes, 2, SwingConstants.CENTER);
        Estilo.alinear(tablaReportes, 3, SwingConstants.CENTER);
        Estilo.ancho(tablaReportes, 0, 80);
        Estilo.ancho(tablaReportes, 1, 110);
        Estilo.ancho(tablaReportes, 2, 150);
        Estilo.ancho(tablaReportes, 3, 150);
        Estilo.ancho(tablaReportes, 4, 230);

        // --- TARJETA DE TOTAL EN CAJA ---
        JPanel tarjetaTotal = Estilo.tarjeta();
        tarjetaTotal.setBounds(22, 458, 480, 78);
        contentPane.add(tarjetaTotal);

        lblTotalCaja = new JLabel("Total en Caja: $0.00");
        Estilo.titulo(lblTotalCaja, 26f, Estilo.VERDE);
        lblTotalCaja.setBounds(24, 16, 440, 40);
        tarjetaTotal.add(lblTotalCaja);

        // --- BOTÓN REALIZAR CORTE ---
        JButton btnCortarCaja = new JButton("Realizar Corte de Caja");
        Estilo.boton(btnCortarCaja, Estilo.TURQUESA);
        btnCortarCaja.setFont(Estilo.fuente(Font.BOLD, 16f));
        btnCortarCaja.setBounds(540, 462, 282, 66);
        contentPane.add(btnCortarCaja);

        // --- ACCIÓN DEL CORTE DE CAJA ---
        btnCortarCaja.addActionListener(e -> generarPDFCorte());

        // Cargar los datos automáticamente al abrir la ventana
        cargarDatosCorte();
    }

    private void cargarDatosCorte() {
        modeloReporte.setRowCount(0); // Limpiar tabla
        double sumaTotal = 0.0;

        String url = "jdbc:mysql://localhost:3306/proyectoPoo";
        String user = "root";
        String pass = "admin";

        // Ajustado a fechaHora según tu base de datos funcional
        String sql = "SELECT idVenta, total, tipoConsumo, metodoPago, fechaHora FROM ventas";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idVenta = rs.getInt("idVenta");
                double total = rs.getDouble("total");
                String tipoConsumo = rs.getString("tipoConsumo");
                String metodoPago = rs.getString("metodoPago");
                String fecha = rs.getString("fechaHora");

                modeloReporte.addRow(new Object[]{idVenta, "$" + total, tipoConsumo, metodoPago, fecha});
                sumaTotal += total;
            }

            lblTotalCaja.setText(String.format("Total en Caja: $%.2f", sumaTotal));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar las ventas: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarPDFCorte() {
        if (modeloReporte.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay ventas registradas para generar el corte.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Corte de Caja PDF");
        fileChooser.setSelectedFile(new File("Corte_Caja_Taqueria.pdf"));
        
        int seleccion = fileChooser.showSaveDialog(this);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoDestino = fileChooser.getSelectedFile();
            
            try {
                com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(documento, new FileOutputStream(archivoDestino));
                
                documento.open();

                com.itextpdf.text.BaseColor rosa = new com.itextpdf.text.BaseColor(232, 70, 107);
                com.itextpdf.text.BaseColor verde = new com.itextpdf.text.BaseColor(104, 140, 36);
                com.itextpdf.text.BaseColor texto = new com.itextpdf.text.BaseColor(43, 58, 66);
                com.itextpdf.text.BaseColor filaAlt = new com.itextpdf.text.BaseColor(251, 250, 240);
                com.itextpdf.text.BaseColor borde = new com.itextpdf.text.BaseColor(224, 220, 196);
                
                com.itextpdf.text.Font fuenteTitulo = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 22, com.itextpdf.text.Font.BOLD, rosa);
                com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph("Taquería Peñascal", fuenteTitulo);
                titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                documento.add(titulo);

                com.itextpdf.text.Font fuenteSub = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL, texto);
                String fechaGenerado = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
                com.itextpdf.text.Paragraph subtitulo = new com.itextpdf.text.Paragraph("Corte de Caja  -  " + fechaGenerado, fuenteSub);
                subtitulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                subtitulo.setSpacingAfter(18);
                documento.add(subtitulo);
                
                com.itextpdf.text.pdf.PdfPTable tablaPdf = new com.itextpdf.text.pdf.PdfPTable(5);
                tablaPdf.setWidthPercentage(100);
                tablaPdf.setWidths(new float[]{1.2f, 1.5f, 1.8f, 1.8f, 3.2f});
                
                com.itextpdf.text.Font fuenteEnc = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD, com.itextpdf.text.BaseColor.WHITE);
                String[] encabezados = {"ID Venta", "Total", "Consumo", "Pago", "Fecha"};
                for (String enc : encabezados) {
                    com.itextpdf.text.pdf.PdfPCell celdaEnc = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(enc, fuenteEnc));
                    celdaEnc.setBackgroundColor(rosa);
                    celdaEnc.setBorderColor(rosa);
                    celdaEnc.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    celdaEnc.setPadding(7);
                    tablaPdf.addCell(celdaEnc);
                }
                
                com.itextpdf.text.Font fuenteCelda = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, texto);
                for (int i = 0; i < modeloReporte.getRowCount(); i++) {
                    for (int c = 0; c < 5; c++) {
                        com.itextpdf.text.pdf.PdfPCell celda = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(modeloReporte.getValueAt(i, c).toString(), fuenteCelda));
                        celda.setBackgroundColor(i % 2 == 0 ? com.itextpdf.text.BaseColor.WHITE : filaAlt);
                        celda.setBorderColor(borde);
                        celda.setHorizontalAlignment(c == 1 ? com.itextpdf.text.Element.ALIGN_RIGHT : com.itextpdf.text.Element.ALIGN_CENTER);
                        celda.setPadding(6);
                        tablaPdf.addCell(celda);
                    }
                }
                
                documento.add(tablaPdf);
                
                com.itextpdf.text.Font fuenteTotal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, verde);
                com.itextpdf.text.Paragraph total = new com.itextpdf.text.Paragraph(lblTotalCaja.getText(), fuenteTotal);
                total.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                total.setSpacingBefore(16);
                documento.add(total);
                
                documento.close();
                JOptionPane.showMessageDialog(null, "¡PDF generado exitosamente en:\n" + archivoDestino.getAbsolutePath());
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al crear el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
