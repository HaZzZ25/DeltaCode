package com.uacm.pf.farmaplus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VentasController {

    @FXML private TextField txtBuscarProducto;
    @FXML private TableView<?> tablaCarrito;
    @FXML private TableColumn<?, ?> colProducto;
    @FXML private TableColumn<?, ?> colCantidad;
    @FXML private TableColumn<?, ?> colPrecio;
    @FXML private TableColumn<?, ?> colSubtotal;
    @FXML private ComboBox<String> comboClientes;
    @FXML private Label lblMembresia;

    @FXML
    private void agregarAlCarrito(ActionEvent event) {
        System.out.println("Agregando producto al carrito...");
    }

    @FXML
    private void completarVenta(ActionEvent event) {
        System.out.println("Procesando pago y registrando venta...");
    }
}