package com.penascal.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.penascal.bd.bd;

public class Estadisticas {

    public static class Resumen {
        public boolean conexionOk = false;

        // Hoy
        public double ventasHoy = 0;
        public int ticketsHoy = 0;

        // Productos más vendidos
        public List<String> topNombres = new ArrayList<String>();
        public List<Integer> topCantidades = new ArrayList<Integer>();

        // Tickets por tipo de consumo
        public Map<String, Integer> porConsumo = new LinkedHashMap<String, Integer>();

        // Últimos 7 días 
        public List<String> diasEtiquetas = new ArrayList<String>();
        public List<Double> diasMonto = new ArrayList<Double>();
        public List<Integer> diasTickets = new ArrayList<Integer>();
    }

    public Resumen cargar() {
        Resumen r = new Resumen();
        try (Connection con = new bd().Conectar()) {
            if (con == null) {
                return r;
            }
            r.conexionOk = true;
            cargarHoy(con, r);
            cargarTopProductos(con, r);
            cargarConsumo(con, r);
            cargarSemana(con, r);
        } catch (Exception e) {
            System.err.println("Error al cargar estadísticas: " + e.getMessage());
        }
        return r;
    }

    private void cargarHoy(Connection con, Resumen r) {
        String sql = "SELECT COALESCE(SUM(total), 0), COUNT(*) FROM ventas WHERE fechaHora >= ? AND fechaHora < ?";
        LocalDate hoy = LocalDate.now();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(hoy.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(hoy.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r.ventasHoy = rs.getDouble(1);
                    r.ticketsHoy = rs.getInt(2);
                }
            }
        } catch (SQLException e) {
            System.err.println("Estadísticas (hoy): " + e.getMessage());
        }
    }

    private void cargarTopProductos(Connection con, Resumen r) {
        String sql = "SELECT p.nombre, SUM(d.cantidad) AS vendidos "
                + "FROM detalleVenta d JOIN Productos p ON p.idProducto = d.idProducto "
                + "GROUP BY p.idProducto, p.nombre ORDER BY vendidos DESC LIMIT 5";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                r.topNombres.add(rs.getString(1));
                r.topCantidades.add(rs.getInt(2));
            }
        } catch (SQLException e) {
            System.err.println("Estadísticas (top productos): " + e.getMessage());
        }
    }

    private void cargarConsumo(Connection con, Resumen r) {
        String sql = "SELECT tipoConsumo, COUNT(*) FROM ventas GROUP BY tipoConsumo ORDER BY COUNT(*) DESC";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tipo = rs.getString(1);
                r.porConsumo.put(tipo == null ? "Sin dato" : tipo, rs.getInt(2));
            }
        } catch (SQLException e) {
            System.err.println("Estadísticas (consumo): " + e.getMessage());
        }
    }

    private void cargarSemana(Connection con, Resumen r) {
        String sql = "SELECT DATE(fechaHora), SUM(total), COUNT(*) FROM ventas "
                + "WHERE fechaHora >= ? GROUP BY DATE(fechaHora)";
        LocalDate hoy = LocalDate.now();
        Map<LocalDate, double[]> datos = new HashMap<LocalDate, double[]>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(hoy.minusDays(6).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date d = rs.getDate(1);
                    if (d != null) {
                        datos.put(d.toLocalDate(), new double[] { rs.getDouble(2), rs.getInt(3) });
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Estadísticas (semana): " + e.getMessage());
        }

        for (int i = 6; i >= 0; i--) {
            LocalDate dia = hoy.minusDays(i);
            double[] v = datos.get(dia);
            String nombreDia = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es", "MX"));
            r.diasEtiquetas.add(nombreDia.replace(".", "") + " " + dia.getDayOfMonth());
            r.diasMonto.add(v == null ? 0.0 : v[0]);
            r.diasTickets.add(v == null ? 0 : (int) v[1]);
        }
    }
}
