package com.uacm.pf.farmaplus.controlador;

import com.uacm.pf.farmaplus.conexion.ConexionDB;
import java.sql.*;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class ReportesController {

    // Estas variables conectan con los fx:id de tu FXML
    @FXML
    private Label lblVentasDia;
    @FXML
    private Label lblMembresias;
    @FXML
    private BarChart<String, Number> graficaVentas;

    @FXML
    public void initialize() {
        cargarTarjetas();
        cargarGrafica();
    }

    private void cargarTarjetas() {
        // 1. Calculamos todo lo vendido HOY sumando la tabla 'ventas'
        String sqlVentas = "SELECT SUM(total) as total_dia FROM ventas WHERE DATE(fecha) = CURDATE()";
        try (Connection con = ConexionDB.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlVentas)) {

            if (rs.next()) {
                double total = rs.getDouble("total_dia");
                lblVentasDia.setText("$" + String.format("%.2f", total));
            }
        } catch (SQLException e) {
            System.out.println("Error cargando ventas: " + e.getMessage());
        }

        // 2. Contamos cuantos clientes tienen membresia (que no sea 'Ninguna')
        String sqlMem = "SELECT COUNT(*) as total_mem FROM clientes WHERE membresia != 'Ninguna'";
        try (Connection con = ConexionDB.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlMem)) {

            if (rs.next()) {
                int totalMembresias = rs.getInt("total_mem");
                lblMembresias.setText(totalMembresias + " Membresias");
            }
        } catch (SQLException e) {
            System.out.println("Error cargando membresias: " + e.getMessage());
        }
    }

    private void cargarGrafica() {
        graficaVentas.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ventas de Hoy");

        // TRUCO: Pre-llenamos todas las horas de la farmacia (8 AM a 11 PM) con $0
        java.util.Map<String, Double> ventasPorHora = new java.util.LinkedHashMap<>();
        for (int i = 8; i <= 23; i++) {
            ventasPorHora.put(i + ":00", 0.0);
        }

        // Buscamos las ventas reales en la base de datos
        String sql = "SELECT HOUR(fecha) as hora, SUM(total) as monto FROM ventas WHERE DATE(fecha) = CURDATE() GROUP BY HOUR(fecha)";

        try (Connection con = ConexionDB.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String horaStr = rs.getString("hora") + ":00";
                // Si hubo venta en esa hora, reemplazamos el 0 por el dinero ganado
                ventasPorHora.put(horaStr, rs.getDouble("monto"));
            }

            // Ahora si, metemos todo el horario completo a la grafica
            for (java.util.Map.Entry<String, Double> entry : ventasPorHora.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            graficaVentas.getData().add(serie);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
