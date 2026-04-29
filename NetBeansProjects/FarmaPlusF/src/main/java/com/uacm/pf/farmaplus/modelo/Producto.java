package com.uacm.pf.farmaplus.modelo;

public class Producto {
    
    private String sku;
    private String nombre;
    private String marca;
    private double precio;
    private int stock;
    private String imagenPath; // Nueva variable para la foto

    public Producto(String sku, String nombre, String marca, double precio, int stock, String imagenPath) {
        this.sku = sku;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
        this.imagenPath = imagenPath;
    }

    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getImagenPath() { return imagenPath; } // Nuevo metodo
}