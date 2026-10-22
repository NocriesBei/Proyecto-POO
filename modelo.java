package com.penascal.modelo;

public class Producto {
	private int id;
    private String nombre;
    private double precio;
    private String tipoConsumo;

	public Producto(int id, String nombre, double precio, String tipoConsumo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.tipoConsumo = tipoConsumo;
    }
	public Producto(String nombre, double precio, String tipoConsumo) {
		this.nombre = nombre;
		this.precio = precio;
		this.tipoConsumo = tipoConsumo;
	}

	public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getTipoConsumo() { return tipoConsumo; }
}
