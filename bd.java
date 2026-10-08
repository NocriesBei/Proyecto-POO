package com.penascal.bd;

//CONEXIONES DE MYSQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class bd {
	private static final String URL = "admin:///var/lib/mysql/proyectoPoo";
	private static final String USUARIO = "root";
	private static final String PASSWORD = "admin";
	
	public Connection Conectar() {
		Connection conexion = null;
		try {
			conexion = DriverManager.getConnection(URL,USUARIO,PASSWORD);
			System.out.println("Conexion exitosa a la Base de Datos");
		} catch (SQLException e) {
			System.err.println("Error de conexion: " + e.getMessage());
		}
		return conexion;
	}
}
