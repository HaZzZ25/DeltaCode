package com.uacm.pf.farmaplus.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    private static final String URL = "jdbc:mariadb://localhost:3306/farmaplus_db";
    private static final String USUARIO = "admin"; 
    private static final String PASSWORD = "admin123"; 

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conexion a MariaDB exitosa!");
        } catch (SQLException e) {
            System.out.println("Error de conexion a MariaDB: " + e.getMessage());
        }
        return conexion;
    }
}