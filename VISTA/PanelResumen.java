package com.penascal.vista;

import com.penascal.controlador.Estadisticas;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PanelResumen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final boolean esAdmin;
    private final Tarjeta kpiPrincipal;
    private final Tarjeta kpiTickets;
    private final Tarjeta kpiEstrella;
    private final BarrasHorizontales graficaTop;
    private final Dona graficaConsumo;
    private final BarrasVerticales graficaSemana;
    private final JButton btnActualizar;
    private final List<Grafica> graficas = new ArrayList<Grafica>();

    public PanelResumen(String rol) {
        this.esAdmin = !"Cajero".equalsIgnoreCase(rol);
        setOpaque(false);
        setLayout(null);

        // --- Encabezado ---
        JLabel titulo = new JLabel("Resumen del negocio");
        Estilo.titulo(titulo, 26f, Estilo.ROSA);
        titulo.setBounds(0, 0, 450, 36);
        add(titulo);

        JLabel fecha = new JLabel(fechaHoy());
        Estilo.subtitulo(fecha);
        fecha.setBounds(2, 38, 450, 20);
        add(fecha);

        btnActualizar = new JButton("Actualizar");
        Estilo.botonContorno(btnActualizar, Estilo.TURQUESA);
        btnActualizar.setBounds(625, 8, 140, 36);
        add(btnActualizar);
        btnActualizar.addActionListener(e -> cargar());

        // --- Tarjetas de datos (KPI) ---
        kpiPrincipal = new Tarjeta(esAdmin ? "Ventas de hoy" : "Consumo preferido", Estilo.ROSA);
        kpiPrincipal.setBounds(0, 72, 245, 88);
        add(kpiPrincipal);

        kpiTickets = new Tarjeta("Tickets de hoy", Estilo.TURQUESA);
        kpiTickets.setBounds(260, 72, 245, 88);
        add(kpiTickets);

        kpiEstrella = new Tarjeta("Producto estrella", Estilo.NARANJA);
        kpiEstrella.setBounds(520, 72, 245, 88);
        add(kpiEstrella);

        // --- Gráficas ---
        graficaTop = new BarrasHorizontales("Productos más vendidos");
        graficaTop.setBounds(0, 176, 455, 232);
        add(graficaTop);

        graficaConsumo = new Dona("Local vs. Para llevar");
        graficaConsumo.setBounds(470, 176, 295, 232);
        add(graficaConsumo);

        graficaSemana = new BarrasVerticales(esAdmin ? "Ventas de los últimos 7 días" : "Tickets de los últimos 7 días", esAdmin);
        graficaSemana.setBounds(0, 424, 765, 214);
        add(graficaSemana);

        graficas.add(graficaTop);
        graficas.add(graficaConsumo);
        graficas.add(graficaSemana);

        for (Grafica g : graficas) {
            g.setMensaje("Cargando...");
        }
        cargar();
    }

    // ====================== CARGA DE DATOS ======================
    public void cargar() {
        btnActualizar.setEnabled(false);
        new SwingWorker<Estadisticas.Resumen, Void>() {
            @Override
            protected Estadisticas.Resumen doInBackground() {
                return new Estadisticas().cargar();
            }

            @Override
            protected void done() {
                try {
                    mostrar(get());
                } catch (Exception e) {
                    mostrar(new Estadisticas.Resumen());
                }
                btnActualizar.setEnabled(true);
            }
        }.execute();
    }

    public void mostrar(Estadisticas.Resumen r) {
        String sinDatos = r.conexionOk ? "Aún no hay ventas registradas" : "Sin conexión a la base de datos";

        // Tarjetas
        if (!r.conexionOk) {
            kpiPrincipal.setValor("—");
            kpiTickets.setValor("—");
            kpiEstrella.setValor("—");
        } else {
            if (esAdmin) {
                kpiPrincipal.setValor(String.format(Locale.US, "$%,.2f", r.ventasHoy));
            } else {
                kpiPrincipal.setValor(consumoPreferido(r.porConsumo));
            }
            kpiTickets.setValor(String.valueOf(r.ticketsHoy));
            kpiEstrella.setValor(r.topNombres.isEmpty() ? "—" : r.topNombres.get(0));
        }

        // Gráficas
        graficaTop.setDatos(r.topNombres, r.topCantidades);
        graficaTop.setMensaje(r.topNombres.isEmpty() ? sinDatos : null);

        graficaConsumo.setDatos(r.porConsumo);
        graficaConsumo.setMensaje(r.porConsumo.isEmpty() ? sinDatos : null);

        List<Double> valoresSemana = new ArrayList<Double>();
        boolean hayAlgo = false;
        for (int i = 0; i < r.diasEtiquetas.size(); i++) {
            double v = esAdmin ? r.diasMonto.get(i) : r.diasTickets.get(i);
            valoresSemana.add(v);
            if (v > 0) hayAlgo = true;
        }
        graficaSemana.setDatos(r.diasEtiquetas, valoresSemana);
        graficaSemana.setMensaje(hayAlgo ? null : (r.conexionOk ? "Sin ventas en los últimos 7 días" : sinDatos));

        animar();
    }

    private static String consumoPreferido(Map<String, Integer> m) {
        String mejor = "—";
        int max = 0;
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); mejor = e.getKey(); }
        }
        return mejor;
    }

    private static String fechaHoy() {
        String f = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "MX")));
        return f.substring(0, 1).toUpperCase() + f.substring(1);
    }

    /** Las barras "crecen" al aparecer. */
    private void animar() {
        for (Grafica g : graficas) g.progreso = 0f;
        final long inicio = System.currentTimeMillis();
        final Timer timer = new Timer(16, null);
        timer.addActionListener(e -> {
            float t = Math.min(1f, (System.currentTimeMillis() - inicio) / 700f);
            float suave = 1f - (float) Math.pow(1f - t, 3);
            for (Grafica g : graficas) {
                g.progreso = suave;
                g.repaint();
            }
            if (t >= 1f) timer.stop();
        });
        timer.start();
    }

    // ====================== TARJETA DE DATO ======================
    private static class Tarjeta extends JPanel {
        private static final long serialVersionUID = 1L;
        private final String etiqueta;
        private final Color acento;
        private String valor = "—";

        Tarjeta(String etiqueta, Color acento) {
            this.etiqueta = etiqueta;
            this.acento = acento;
            setOpaque(false);
        }

        void setValor(String v) {
            this.valor = v;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Estilo.pintarTarjeta(g2, getWidth(), getHeight());
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setColor(acento);
            g2.fill(new RoundRectangle2D.Float(16, 18, 5, getHeight() - 40, 5, 5));

            g2.setFont(Estilo.fuente(Font.BOLD, 12f));
            g2.setColor(Estilo.GRIS);
            g2.drawString(etiqueta.toUpperCase(Locale.ROOT), 32, 32);

            Font f = Estilo.fuente(Font.BOLD, 28f);
            int maxAncho = getWidth() - 32 - 18;
            g2.setFont(f);
            while (g2.getFontMetrics().stringWidth(valor) > maxAncho && f.getSize2D() > 15f) {
                f = f.deriveFont(f.getSize2D() - 1f);
                g2.setFont(f);
            }
            g2.setColor(Estilo.TEXTO);
            g2.drawString(recortar(g2.getFontMetrics(), valor, maxAncho), 32, 66);
            g2.dispose();
        }
    }

    // ====================== BASE DE LAS GRÁFICAS ======================
    private abstract static class Grafica extends JPanel {
        private static final long serialVersionUID = 1L;
        float progreso = 1f;
        private final String titulo;
        private String mensaje;

        Grafica(String titulo) {
            this.titulo = titulo;
            setOpaque(false);
        }

        void setMensaje(String m) {
            this.mensaje = m;
            repaint();
        }

        abstract void dibujar(Graphics2D g2, int x, int y, int ancho, int alto);

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Estilo.pintarTarjeta(g2, getWidth(), getHeight());
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setFont(Estilo.fuente(Font.BOLD, 15f));
            g2.setColor(Estilo.TEXTO);
            g2.drawString(titulo, 22, 32);
            g2.setColor(Estilo.AMARILLO);
            g2.fill(new RoundRectangle2D.Float(22, 40, 40, 4, 4, 4));

            int x = 22, y = 56, ancho = getWidth() - 3 - 44, alto = getHeight() - 4 - 56 - 14;
            if (mensaje != null) {
                g2.setFont(Estilo.fuente(Font.PLAIN, 13f));
                g2.setColor(Estilo.GRIS);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(mensaje, x + (ancho - fm.stringWidth(mensaje)) / 2, y + alto / 2 + fm.getAscent() / 2);
            } else {
                dibujar(g2, x, y, ancho, alto);
            }
            g2.dispose();
        }
    }

    // ====================== BARRAS HORIZONTALES (TOP PRODUCTOS) ======================
    private static class BarrasHorizontales extends Grafica {
        private static final long serialVersionUID = 1L;
        private List<String> nombres = new ArrayList<String>();
        private List<Integer> valores = new ArrayList<Integer>();

        BarrasHorizontales(String titulo) { super(titulo); }

        void setDatos(List<String> n, List<Integer> v) {
            this.nombres = n;
            this.valores = v;
            repaint();
        }

        @Override
        void dibujar(Graphics2D g2, int x, int y, int ancho, int alto) {
            int n = nombres.size();
            if (n == 0) return;
            int max = 1;
            for (int v : valores) max = Math.max(max, v);

            int fila = Math.min(36, alto / n);
            int etiqueta = 150;
            int espacioValor = 38;
            int anchoMax = ancho - etiqueta - espacioValor;
            int grosor = Math.max(10, fila - 14);

            for (int i = 0; i < n; i++) {
                int yy = y + i * fila;
                g2.setFont(Estilo.fuente(Font.PLAIN, 12f));
                g2.setColor(Estilo.TEXTO);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(recortar(fm, nombres.get(i), etiqueta - 10), x, yy + (fila + fm.getAscent()) / 2 - 3);

                int largo = Math.max(6, Math.round(valores.get(i) / (float) max * anchoMax * progreso));
                int by = yy + (fila - grosor) / 2;
                g2.setColor(new Color(240, 238, 222));
                g2.fill(new RoundRectangle2D.Float(x + etiqueta, by, anchoMax, grosor, grosor, grosor));
                g2.setColor(Estilo.colorSerie(i));
                g2.fill(new RoundRectangle2D.Float(x + etiqueta, by, largo, grosor, grosor, grosor));

                g2.setFont(Estilo.fuente(Font.BOLD, 12f));
                g2.setColor(Estilo.TEXTO);
                g2.drawString(String.valueOf(valores.get(i)), x + etiqueta + anchoMax + 8, yy + (fila + fm.getAscent()) / 2 - 3);
            }
        }
    }

    // ====================== DONA (LOCAL VS LLEVAR) ======================
    private static class Dona extends Grafica {
        private static final long serialVersionUID = 1L;
        private final List<String> etiquetas = new ArrayList<String>();
        private final List<Integer> valores = new ArrayList<Integer>();

        Dona(String titulo) { super(titulo); }

        void setDatos(Map<String, Integer> datos) {
            etiquetas.clear();
            valores.clear();
            for (Map.Entry<String, Integer> e : datos.entrySet()) {
                etiquetas.add(e.getKey());
                valores.add(e.getValue());
            }
            repaint();
        }

        @Override
        void dibujar(Graphics2D g2, int x, int y, int ancho, int alto) {
            int total = 0;
            for (int v : valores) total += v;
            if (total == 0) return;

            int diametro = Math.min(alto, 118) - 14;
            int grosor = 22;
            int cx = x + 8;
            int cy = y + (alto - diametro) / 2;

            // aro de fondo
            g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            g2.setColor(new Color(240, 238, 222));
            g2.draw(new Arc2D.Float(cx + grosor / 2f, cy + grosor / 2f, diametro - grosor, diametro - grosor, 0, 360, Arc2D.OPEN));

            float inicio = 90f;
            for (int i = 0; i < valores.size(); i++) {
                float barrido = -360f * valores.get(i) / total * progreso;
                g2.setColor(Estilo.colorSerie(i));
                g2.draw(new Arc2D.Float(cx + grosor / 2f, cy + grosor / 2f, diametro - grosor, diametro - grosor, inicio, barrido, Arc2D.OPEN));
                inicio += barrido;
            }

            g2.setFont(Estilo.fuente(Font.BOLD, 22f));
            g2.setColor(Estilo.TEXTO);
            FontMetrics fm = g2.getFontMetrics();
            String t = String.valueOf(total);
            g2.drawString(t, cx + (diametro - fm.stringWidth(t)) / 2, cy + diametro / 2 + 2);
            g2.setFont(Estilo.fuente(Font.PLAIN, 10f));
            g2.setColor(Estilo.GRIS);
            fm = g2.getFontMetrics();
            g2.drawString("tickets", cx + (diametro - fm.stringWidth("tickets")) / 2, cy + diametro / 2 + 16);

            int lx = cx + diametro + 18;
            int ly = y + (alto - valores.size() * 36) / 2 + 6;
            for (int i = 0; i < valores.size(); i++) {
                int yy = ly + i * 36;
                g2.setColor(Estilo.colorSerie(i));
                g2.fill(new RoundRectangle2D.Float(lx, yy, 12, 12, 4, 4));
                g2.setFont(Estilo.fuente(Font.BOLD, 12f));
                g2.setColor(Estilo.TEXTO);
                g2.drawString(recortar(g2.getFontMetrics(), etiquetas.get(i), x + ancho - lx - 20), lx + 20, yy + 11);
                g2.setFont(Estilo.fuente(Font.PLAIN, 11f));
                g2.setColor(Estilo.GRIS);
                int pct = Math.round(valores.get(i) * 100f / total);
                g2.drawString(valores.get(i) + " tickets (" + pct + "%)", lx + 20, yy + 26);
            }
        }
    }

    // ====================== BARRAS VERTICALES (ÚLTIMOS 7 DÍAS) ======================
    private static class BarrasVerticales extends Grafica {
        private static final long serialVersionUID = 1L;
        private final boolean dinero;
        private List<String> etiquetas = new ArrayList<String>();
        private List<Double> valores = new ArrayList<Double>();

        BarrasVerticales(String titulo, boolean dinero) {
            super(titulo);
            this.dinero = dinero;
        }

        void setDatos(List<String> e, List<Double> v) {
            this.etiquetas = e;
            this.valores = v;
            repaint();
        }

        @Override
        void dibujar(Graphics2D g2, int x, int y, int ancho, int alto) {
            int n = valores.size();
            if (n == 0) return;
            double max = 1;
            for (double v : valores) max = Math.max(max, v);

            int pie = 20;
            int cabeza = 18;
            int areaAlto = alto - pie - cabeza;
            int ranura = ancho / n;
            int barra = Math.min(54, ranura - 26);
            int base = y + cabeza + areaAlto;

            g2.setColor(Estilo.BORDE);
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(x, base, x + ancho, base);

            for (int i = 0; i < n; i++) {
                int bx = x + i * ranura + (ranura - barra) / 2;
                int h = (int) Math.round(valores.get(i) / max * areaAlto * progreso);
                boolean esHoy = i == n - 1;
                if (valores.get(i) > 0) {
                    g2.setColor(esHoy ? Estilo.ROSA : Estilo.AMARILLO);
                    g2.fill(new RoundRectangle2D.Float(bx, base - h, barra, h, 10, 10));
                    // la base de la barra va recta
                    g2.fillRect(bx, base - Math.min(h, 8), barra, Math.min(h, 8));

                    g2.setFont(Estilo.fuente(Font.BOLD, 11f));
                    g2.setColor(Estilo.TEXTO);
                    String txt = dinero ? String.format(Locale.US, "$%,.0f", valores.get(i)) : String.valueOf(Math.round(valores.get(i)));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(txt, bx + (barra - fm.stringWidth(txt)) / 2, base - h - 5);
                }

                g2.setFont(Estilo.fuente(esHoy ? Font.BOLD : Font.PLAIN, 11f));
                g2.setColor(esHoy ? Estilo.ROSA : Estilo.GRIS);
                FontMetrics fm = g2.getFontMetrics();
                String et = esHoy ? "hoy" : etiquetas.get(i);
                g2.drawString(et, bx + (barra - fm.stringWidth(et)) / 2, base + 15);
            }
        }
    }

    // ====================== UTILIDAD ======================
    private static String recortar(FontMetrics fm, String texto, int ancho) {
        if (fm.stringWidth(texto) <= ancho) return texto;
        String puntos = "…";
        while (texto.length() > 1 && fm.stringWidth(texto + puntos) > ancho) {
            texto = texto.substring(0, texto.length() - 1);
        }
        return texto.trim() + puntos;
    }
}
