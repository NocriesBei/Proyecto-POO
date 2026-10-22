package com.penascal.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.penascal.bd.bd;
import com.penascal.modelo.Producto;


public class controlador {
    private bd bd = new bd();

    public boolean registrarProducto(Producto producto) {
        String sql = "INSERT INTO Productos(nombre, precio, tipoConsumo) VALUES (?,?,?)";
        try (Connection con = bd.Conectar();
             PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setString(3, producto.getTipoConsumo());
            ps.execute();
            return true;
        }catch (Exception e) {
            System.err.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public List<Producto> obtenerProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT idProducto, nombre, precio, tipoConsumo FROM Productos";	        
        try (Connection con = bd.Conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto(
                    rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getString("tipoConsumo")
                );
                lista.add(p);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar productos: " + e.getMessage());
        }
        return lista;
    }
    
    public String validarLogin(String usuario, String password) {
        String rol = null;
        String sql =  "Select rol from usuarios Where nombre = ? and password = ?";
        bd conexionBd = new bd();
        try (Connection con = conexionBd.Conectar();
            PreparedStatement ps = con.prepareStatement(sql)){    				
            ps.setString(1, usuario);
            ps.setString(2, password);		
            try (ResultSet rs= ps.executeQuery()){
                if(rs.next()) {
                    rol = rs.getString("rol");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al validar el login : " + e.getMessage());
        }
        return rol;
    }
    
    public List<String> obtenerNombresUsuarios() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM usuarios"; 
        
        try (Connection con = bd.Conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String nombreBD = rs.getString(1);
                System.out.println("Usuario encontrado en BD: " + nombreBD);
                lista.add(nombreBD); 
            }
        } catch (SQLException e) {
            System.out.println("Error al extraer usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarUsuario(String usuario, String password, String rol) {
        boolean registrado = false;
        String sql = "INSERT INTO usuarios (usuario, password, rol) VALUES (?, ?, ?)";
        
        bd conexionBD = new bd(); 
        
        try (Connection con = conexionBD.Conectar();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, usuario);
            pst.setString(2, password);
            pst.setString(3, rol);
            
            if (pst.executeUpdate() > 0) {
                registrado = true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
        }
        
        return registrado;
    }
}
