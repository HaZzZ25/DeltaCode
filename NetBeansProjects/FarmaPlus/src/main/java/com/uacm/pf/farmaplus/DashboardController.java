package com.uacm.pf.farmaplus;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private StackPane contentArea;
    
    // Inyectamos los botones para poder ocultarlos
    @FXML private Button btnProductos;
    @FXML private Button btnReportes;

    @FXML
    public void initialize() {
        // Si el rol es Vendedor, ocultamos los modulos de Productos y Reportes
        if ("Vendedor".equals(Sesion.rolActual)) {
            btnProductos.setVisible(false);
            btnProductos.setManaged(false); // Evita que deje un espacio vacio
            
            btnReportes.setVisible(false);
            btnReportes.setManaged(false);
        }
    }

    private void cargarVentana(String archivoFxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(archivoFxml + ".fxml"));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(vista);
        } catch (IOException e) {
            System.out.println("Error al intentar cargar la vista: " + archivoFxml);
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirVentas(ActionEvent event) { cargarVentana("Ventas"); }

    @FXML
    private void abrirProductos(ActionEvent event) { cargarVentana("Productos"); }

    @FXML
    private void abrirClientes(ActionEvent event) { cargarVentana("Cliente"); }

    @FXML
    private void abrirReportes(ActionEvent event) { cargarVentana("Reportes"); }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        Sesion.rolActual = ""; // Limpiamos la sesion
        App.setRoot("Login");
    }
}