package com.uacm.pf.farmaplus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductosController {

    @FXML private TextField txtId; 
    @FXML private TextField txtNombre;
    @FXML private TextField txtMarca;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colMarca;
    @FXML private TableColumn<Producto, Double> colPrecioProd;
    @FXML private TableColumn<Producto, Integer> colStock;

    private ObservableList<Producto> listaProductos;

    @FXML
    public void initialize() {
        // Configuramos la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colPrecioProd.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        listaProductos = FXCollections.observableArrayList();
        
        // AQUI ES DONDE SE CONECTA A MARIADB AUTOMATICAMENTE AL ABRIR LA PANTALLA
        cargarDatos();
    }

    private void cargarDatos() {
        listaProductos.clear(); 
        String sql = "SELECT * FROM productos";
        
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                Producto p = new Producto(
                    rs.getString("sku"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
                listaProductos.add(p);
            }
            
            tablaProductos.setItems(listaProductos);
            
        } catch (SQLException e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    @FXML
    private void guardarProducto(ActionEvent event) {
        String sku = txtId.getText();
        String nombre = txtNombre.getText();
        String marca = txtMarca.getText();
        String precioStr = txtPrecio.getText();
        String stockStr = txtStock.getText();

        if(sku.isEmpty() || nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()){
            mostrarAlerta(Alert.AlertType.WARNING, "Error", "Por favor llena todos los campos obligatorios.");
            return;
        }

        String sql = "INSERT INTO productos (sku, nombre, marca, precio, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
             
            ps.setString(1, sku);
            ps.setString(2, nombre);
            ps.setString(3, marca);
            ps.setDouble(4, Double.parseDouble(precioStr));
            ps.setInt(5, Integer.parseInt(stockStr));
            
            ps.executeUpdate(); 
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Producto guardado correctamente.");
            
            limpiarCampos(null); 
            cargarDatos(); // Recargamos para ver el producto en la tabla
            
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "El precio y stock deben ser numeros.");
        }
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
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}