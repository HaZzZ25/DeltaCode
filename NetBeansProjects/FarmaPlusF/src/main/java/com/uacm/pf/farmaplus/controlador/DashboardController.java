package com.uacm.pf.farmaplus.controlador;

import com.uacm.pf.farmaplus.App;
import com.uacm.pf.farmaplus.modelo.Sesion;
import java.io.IOException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class DashboardController {

    @FXML private StackPane contentArea;

    @FXML private Button btnProductos;
    @FXML private Button btnReportes;

    @FXML
    public void initialize() {
        // Ocultar módulos si es vendedor
        if ("Vendedor".equals(Sesion.rolActual)) {
            btnProductos.setVisible(false);
            btnProductos.setManaged(false);

            btnReportes.setVisible(false);
            btnReportes.setManaged(false);
        }

        // Vista inicial (opcional)
        cargarVentana("Ventas");
    }

    private void cargarVentana(String archivoFxml) {
        try {
            String ruta = "/vistas/" + archivoFxml + ".fxml";

            System.out.println("Cargando: " + ruta);

            URL url = getClass().getResource(ruta);

            if (url == null) {
                System.out.println("No se encontró la vista: " + ruta);
                return;
            }

            Parent vista = FXMLLoader.load(url);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(vista);

        } catch (IOException e) {
            System.out.println("Error al cargar la vista: " + archivoFxml);
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirVentas(ActionEvent event) {
        cargarVentana("Ventas");
    }

    @FXML
    private void abrirProductos(ActionEvent event) {
        cargarVentana("Productos");
    }

    @FXML
    private void abrirClientes(ActionEvent event) {
        cargarVentana("Cliente");
    }

    @FXML
    private void abrirReportes(ActionEvent event) {
        cargarVentana("Reportes");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        try {
            Sesion.rolActual = "";
            Sesion.usuarioActual = "";
            App.setRoot("Login");
        } catch (IOException e) {
            System.out.println("Error al cerrar sesión");
            e.printStackTrace();
        }
    }
}