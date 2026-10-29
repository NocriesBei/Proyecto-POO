package com.penascal.vista;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class Estilo {

    private Estilo() { }

    // ====================== PALETA ======================
    public static final Color CREMA     = new Color(246, 247, 233);
    public static final Color BLANCO    = Color.WHITE;
    public static final Color ROSA      = new Color(232, 70, 107);
    public static final Color NARANJA   = new Color(240, 112, 40);
    public static final Color AMARILLO  = new Color(255, 200, 74);
    public static final Color VERDE     = new Color(104, 140, 36);
    public static final Color TURQUESA  = new Color(46, 150, 160);
    public static final Color ROJO      = new Color(196, 58, 49);
    public static final Color TEXTO     = new Color(43, 58, 66);
    public static final Color GRIS      = new Color(125, 133, 138);
    public static final Color BORDE     = new Color(224, 220, 196);
    public static final Color FILA_ALT  = new Color(251, 250, 240);
    public static final Color SELECCION = new Color(255, 233, 168);

    private static final Color[] BANDERAS = {
        new Color(246, 106, 44), new Color(239, 204, 86), new Color(232, 70, 107),
        new Color(190, 189, 63), new Color(89, 180, 183)
    };

    // ====================== TEMA GENERAL ======================
    private static boolean temaAplicado = false;
    private static String familia;

    public static void aplicarTema() {
        if (temaAplicado) return;
        temaAplicado = true;

        Font normal = fuente(Font.PLAIN, 14f);
        Font negrita = fuente(Font.BOLD, 13f);

        UIManager.put("Panel.background", CREMA);
        UIManager.put("OptionPane.background", CREMA);
        UIManager.put("OptionPane.messageForeground", TEXTO);
        UIManager.put("OptionPane.messageFont", normal);
        UIManager.put("OptionPane.buttonFont", negrita);
        UIManager.put("Label.font", normal);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("TextField.font", normal);
        UIManager.put("PasswordField.font", normal);
        UIManager.put("ComboBox.font", normal);
        UIManager.put("Table.font", fuente(Font.PLAIN, 13f));
        UIManager.put("TableHeader.font", negrita);
        UIManager.put("Button.font", negrita);
        UIManager.put("ComboBox.background", BLANCO);
        UIManager.put("ComboBox.foreground", TEXTO);
        UIManager.put("ComboBox.selectionBackground", SELECCION);
        UIManager.put("ComboBox.selectionForeground", TEXTO);
        UIManager.put("List.selectionBackground", SELECCION);
        UIManager.put("List.selectionForeground", TEXTO);
    }

    public static String atras(String texto) {
        return fuente(Font.BOLD, 13f).canDisplay('\u2190') ? "\u2190  " + texto : "<  " + texto;
    }

    public static Font fuente(int estilo, float tamano) {
        if (familia == null) {
            Set<String> disponibles = new HashSet<String>(Arrays.asList(
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
            String[] candidatas = { "Segoe UI", "Poppins", "Montserrat", "Ubuntu", "Noto Sans", "Cantarell" };
            familia = "SansSerif";
            for (String c : candidatas) {
                if (disponibles.contains(c)) { familia = c; break; }
            }
        }
        return new Font(familia, estilo, Math.round(tamano)).deriveFont(estilo, tamano);
    }

    // ====================== TEXTO ======================
    public static void titulo(JLabel l, float tamano, Color color) {
        l.setFont(fuente(Font.BOLD, tamano));
        l.setForeground(color);
    }

    public static void etiqueta(JLabel l) {
        l.setFont(fuente(Font.BOLD, 12f));
        l.setForeground(GRIS);
    }

    public static void subtitulo(JLabel l) {
        l.setFont(fuente(Font.PLAIN, 13f));
        l.setForeground(GRIS);
    }

    // ====================== CAMPOS ======================
    private static LineBorder bordeCampo(Color c) {
        return new LineBorder(c, 1, true);
    }

    public static void campo(final JTextField f) {
        f.setFont(fuente(Font.PLAIN, 14f));
        f.setForeground(TEXTO);
        f.setBackground(BLANCO);
        f.setCaretColor(ROSA);
        f.setSelectionColor(SELECCION);
        f.setSelectedTextColor(TEXTO);
        f.setBorder(BorderFactory.createCompoundBorder(bordeCampo(BORDE), new EmptyBorder(3, 10, 3, 10)));
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(bordeCampo(ROSA), new EmptyBorder(3, 10, 3, 10)));
            }
            @Override public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(bordeCampo(BORDE), new EmptyBorder(3, 10, 3, 10)));
            }
        });
    }

    public static void combo(JComboBox<?> c) {
        c.setFont(fuente(Font.PLAIN, 14f));
        c.setForeground(TEXTO);
        c.setBackground(BLANCO);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ====================== BOTONES ======================
    private enum Tipo { SOLIDO, CONTORNO, MAS }

    public static void boton(JButton b, Color color) {
        prepararBoton(b, new BotonUI(Tipo.SOLIDO, color, 14));
        b.setForeground(BLANCO);
        b.setFont(fuente(Font.BOLD, 14f));
    }

    public static void botonContorno(JButton b, Color color) {
        prepararBoton(b, new BotonUI(Tipo.CONTORNO, color, 14));
        b.setForeground(color);
        b.setFont(fuente(Font.BOLD, 13f));
    }

    public static void botonMas(JButton b) {
        prepararBoton(b, new BotonUI(Tipo.MAS, ROSA, 10));
        b.setText("+");
        b.setForeground(BLANCO);
    }

    private static void prepararBoton(JButton b, BotonUI ui) {
        b.setUI(ui);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setRolloverEnabled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static class BotonUI extends BasicButtonUI {
        private final Tipo tipo;
        private final Color color;
        private final int radio;

        BotonUI(Tipo tipo, Color color, int radio) {
            this.tipo = tipo;
            this.color = color;
            this.radio = radio;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel m = b.getModel();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = c.getWidth();
            int h = c.getHeight();
            boolean activo = b.isEnabled();
            RoundRectangle2D forma = new RoundRectangle2D.Float(0.5f, 0.5f, w - 1.5f, h - 1.5f, radio, radio);

            Color relleno;
            Color textoColor;
            if (tipo == Tipo.CONTORNO) {
                relleno = !activo ? new Color(238, 236, 224)
                        : m.isPressed() ? mezclar(BLANCO, color, 0.82f)
                        : m.isRollover() ? mezclar(BLANCO, color, 0.92f) : BLANCO;
                textoColor = activo ? color : new Color(170, 172, 165);
                g2.setColor(relleno);
                g2.fill(forma);
                g2.setColor(activo ? color : new Color(210, 208, 196));
                g2.setStroke(new BasicStroke(1.6f));
                g2.draw(forma);
            } else {
                relleno = !activo ? new Color(205, 203, 190)
                        : m.isPressed() ? color.darker()
                        : m.isRollover() ? mezclar(color, BLANCO, 0.14f) : color;
                textoColor = activo ? BLANCO : new Color(130, 128, 115);
                g2.setColor(relleno);
                g2.fill(forma);
            }

            if (tipo == Tipo.MAS) {
                g2.setColor(BLANCO);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = w / 2;
                int cy = h / 2;
                int brazo = Math.max(2, Math.min(w, h) / 2 - 3);
                g2.drawLine(cx - brazo, cy, cx + brazo, cy);
                g2.drawLine(cx, cy - brazo, cx, cy + brazo);
            } else {
                String texto = b.getText();
                if (texto != null && !texto.isEmpty()) {
                    g2.setFont(b.getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (w - fm.stringWidth(texto)) / 2;
                    int y = (h - fm.getHeight()) / 2 + fm.getAscent();
                    g2.setColor(textoColor);
                    g2.drawString(texto, x, y);
                }
            }
            g2.dispose();
        }
    }

    private static Color mezclar(Color a, Color b, float t) {
        int r = Math.round(a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }

    // ====================== TARJETA ======================
    public static Color colorSerie(int i) {
        Color[] serie = { ROSA, TURQUESA, AMARILLO, NARANJA, new Color(190, 189, 63) };
        return serie[Math.abs(i) % serie.length];
    }

    public static void pintarTarjeta(Graphics2D g2, int ancho, int alto) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 14));
        g2.fill(new RoundRectangle2D.Float(2, 4, ancho - 4, alto - 4, 22, 22));
        g2.setColor(BLANCO);
        g2.fill(new RoundRectangle2D.Float(0, 0, ancho - 3, alto - 4, 20, 20));
        g2.setColor(BORDE);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, ancho - 4, alto - 5, 20, 20));
    }

    public static JPanel tarjeta() {
        JPanel p = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                pintarTarjeta(g2, getWidth(), getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setLayout(null);
        return p;
    }

    public static JPanel linea(Color color) {
        JPanel p = new JPanel();
        p.setBackground(color);
        return p;
    }

    // ====================== PAPEL PICADO ======================
    public static JLabel banner(int ancho) {
        URL url = Estilo.class.getResource("/Imagenes/banner_papel_picado.png");
        if (url == null) {
            return new JLabel();
        }
        ImageIcon original = new ImageIcon(url);
        int alto = Math.round(original.getIconHeight() * (ancho / (float) original.getIconWidth()));
        Image esc = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        JLabel l = new JLabel(new ImageIcon(esc));
        l.setSize(ancho, alto);
        return l;
    }

    public static int altoBanner(int ancho) {
        URL url = Estilo.class.getResource("/Imagenes/banner_papel_picado.png");
        if (url == null) return 0;
        ImageIcon original = new ImageIcon(url);
        return Math.round(original.getIconHeight() * (ancho / (float) original.getIconWidth()));
    }

    public static JPanel tira(int alto) {
        final int altoTira = alto;
        JPanel p = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int ancho = (int) Math.ceil(w / (double) BANDERAS.length);
                int cuerpo = altoTira - 9;
                for (int i = 0; i < BANDERAS.length; i++) {
                    int x = i * ancho;
                    g2.setColor(BANDERAS[i]);
                    g2.fillRect(x, 0, ancho, cuerpo);
                    int dientes = Math.max(2, ancho / 12);
                    int dw = ancho / dientes;
                    for (int d = 0; d < dientes; d++) {
                        int[] xs = { x + d * dw, x + (d + 1) * dw, x + d * dw + dw / 2 };
                        int[] ys = { cuerpo, cuerpo, cuerpo + 8 };
                        g2.fillPolygon(xs, ys, 3);
                    }
                }
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(100, altoTira));
        return p;
    }

    // ====================== TABLAS ======================
    private static class CeldaRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private final boolean dinero;
        private final int alineacion;

        CeldaRenderer(boolean dinero, int alineacion) {
            this.dinero = dinero;
            this.alineacion = alineacion;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel,
                boolean foco, int fila, int col) {
            if (dinero && valor instanceof Number) {
                valor = String.format(Locale.US, "$%.2f", ((Number) valor).doubleValue());
            }
            super.getTableCellRendererComponent(t, valor, sel, false, fila, col);
            setHorizontalAlignment(alineacion);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            setForeground(TEXTO);
            setBackground(sel ? SELECCION : (fila % 2 == 0 ? BLANCO : FILA_ALT));
            return this;
        }
    }

    private static class EncabezadoRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable t, Object valor, boolean sel,
                boolean foco, int fila, int col) {
            super.getTableCellRendererComponent(t, valor, false, false, fila, col);
            setBackground(ROSA);
            setForeground(BLANCO);
            setFont(fuente(Font.BOLD, 13f));
            Object alin = t == null ? null : t.getClientProperty("alin:" + col);
            setHorizontalAlignment(alin instanceof Integer ? (Integer) alin : SwingConstants.LEFT);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            return this;
        }
    }

    public static void tabla(JTable t, JScrollPane sp) {
        t.setRowHeight(30);
        t.setFont(fuente(Font.PLAIN, 13f));
        t.setForeground(TEXTO);
        t.setBackground(BLANCO);
        t.setGridColor(new Color(236, 232, 212));
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(SELECCION);
        t.setSelectionForeground(TEXTO);
        t.setFillsViewportHeight(true);

        CeldaRenderer normal = new CeldaRenderer(false, SwingConstants.LEFT);
        t.setDefaultRenderer(Object.class, normal);
        t.setDefaultRenderer(Number.class, normal);
        t.setDefaultRenderer(Double.class, normal);

        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 36));
        h.setDefaultRenderer(new EncabezadoRenderer());

        if (sp != null) {
            sp.setBorder(new LineBorder(BORDE, 1));
            sp.getViewport().setBackground(BLANCO);
        }
    }

    public static void dinero(JTable t, int columna) {
        t.getColumnModel().getColumn(columna).setCellRenderer(new CeldaRenderer(true, SwingConstants.RIGHT));
        t.putClientProperty("alin:" + columna, SwingConstants.RIGHT);
    }

    public static void alinear(JTable t, int columna, int alineacion) {
        t.getColumnModel().getColumn(columna).setCellRenderer(new CeldaRenderer(false, alineacion));
        t.putClientProperty("alin:" + columna, alineacion);
    }

    public static void ancho(JTable t, int columna, int ancho) {
        TableColumn c = t.getColumnModel().getColumn(columna);
        c.setPreferredWidth(ancho);
    }
}
