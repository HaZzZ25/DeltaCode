package com.uacm.pf.farmaplus.modelo;

/**
 *
 * @author hazel
 */
public class DetalleVenta {

    private Producto productoObj; // Guardamos el producto completo para luego restarle stock
    private String nombre;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetalleVenta(Producto productoObj, int cantidad) {
        this.productoObj = productoObj;
        this.nombre = productoObj.getNombre();
        this.cantidad = cantidad;
        this.precio = productoObj.getPrecio();
        this.subtotal = cantidad * productoObj.getPrecio();
    }

    public Producto getProductoObj() {
        return productoObj;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // Para actualizar si agregan el mismo producto dos veces
    public void sumarCantidad(int extra) {
        this.cantidad += extra;
        this.subtotal = this.cantidad * this.precio;
    }
}
