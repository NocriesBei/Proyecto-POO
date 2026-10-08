package com.penascal.controlador;

import com.penascal.bd.bd;
import com.penascal.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class controlador {
	 private bd bd = new bd();
	 
	 //Metodo para guardar en la base de datos
	 public boolean registrarProducto(Producto producto) {
		 String sql = "INSERT INTO productos(nombre, precio, tipo_consumo_default) VALUES (?,?,?)";
		 try (Connection con = bd.Conectar();
				 PreparedStatement ps = con.prepareStatement(sql)){
			 ps.setString(1,producto.getNombre());
			 ps.setDouble(2,producto.getPrecio());
			 ps.setString(3,producto.getTipoConsumo());
			 ps.execute();
			 return true;
			 
		 }catch (Exception e) {
			 System.err.println("Error al registrar: " + e.getMessage());
			 return false;
		 }
	 }
}

}
