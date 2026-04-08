package com.uacm.pf.farmaplus;

public class Producto {
    
    private String sku;
    private String nombre;
    private String marca;
    private double precio;
    private int stock;

    public Producto(String sku, String nombre, String marca, double precio, int stock) {
        this.sku = sku;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
    }

    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
}