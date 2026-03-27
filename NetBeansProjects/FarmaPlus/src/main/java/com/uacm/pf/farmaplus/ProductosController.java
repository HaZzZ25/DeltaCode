package com.uacm.pf.farmaplus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductosController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtMarca;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    @FXML private TableView<?> tablaProductos;
    @FXML private TableColumn<?, ?> colId;
    @FXML private TableColumn<?, ?> colNombre;
    @FXML private TableColumn<?, ?> colMarca;
    @FXML private TableColumn<?, ?> colPrecioProd;
    @FXML private TableColumn<?, ?> colStock;

    @FXML
    private void guardarProducto(ActionEvent event) {
        System.out.println("Guardando producto...");
    }

    @FXML
    private void actualizarProducto(ActionEvent event) {
        System.out.println("Actualizando producto...");
    }

    @FXML
    private void eliminarProducto(ActionEvent event) {
        System.out.println("Eliminando producto...");
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtId.clear();
        txtNombre.clear();
        txtMarca.clear();
        txtPrecio.clear();
        txtStock.clear();
        System.out.println("Campos limpiados.");
    }
}