package com.uacm.pf.farmaplus.modelo;

/**
 *
 * @author hazel
 */
public class Cliente {

    private int id;
    private String nombre;
    private String telefono;
    private String correo;
    private String membresia;
    private int descuento;

    public Cliente(int id, String nombre, String telefono, String correo, String membresia, int descuento) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.membresia = membresia;
        this.descuento = descuento;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getMembresia() {
        return membresia;
    }

    public int getDescuento() {
        return descuento;
    }
}
