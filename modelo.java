package com.penascal.modelo;

public class Producto {
	public String nombre;
	public double precio;
	public String tipoConsumo;
	
	public Producto(String nombre, double precio, String tipoConsumo) {
		this.nombre = nombre;
		this.precio = precio;
		this.tipoConsumo = tipoConsumo;
	}

	public String getNombre() {return nombre;}
	public double getPrecio() {return precio;}
	public String getTipoConsumo() {return tipoConsumo;}
}
