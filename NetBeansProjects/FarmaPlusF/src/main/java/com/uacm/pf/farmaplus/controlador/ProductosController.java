package com.uacm.pf.farmaplus.controlador;

import com.uacm.pf.farmaplus.modelo.Producto;
import com.uacm.pf.farmaplus.conexion.ConexionDB;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class ProductosController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private ImageView imgPreview; // Nuestro visor de imagen

    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, String> colId;
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, String> colMarca;
    @FXML
    private TableColumn<Producto, Double> colPrecioProd;
    @FXML
    private TableColumn<Producto, Integer> colStock;

    private ObservableList<Producto> listaProductos;
    private String rutaImagenSeleccionada = ""; // Aqui guardamos la ruta temporal

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colPrecioProd.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        listaProductos = FXCollections.observableArrayList();
        cargarDatos();

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtId.setText(newSel.getSku());
                txtNombre.setText(newSel.getNombre());
                txtMarca.setText(newSel.getMarca());
                txtPrecio.setText(String.valueOf(newSel.getPrecio()));
                txtStock.setText(String.valueOf(newSel.getStock()));
                txtId.setDisable(true);

                // Mostrar la imagen cuando le das clic en la tabla
                rutaImagenSeleccionada = newSel.getImagenPath();
                if (rutaImagenSeleccionada != null && !rutaImagenSeleccionada.isEmpty()) {
                    imgPreview.setImage(new Image(rutaImagenSeleccionada));
                } else {
                    imgPreview.setImage(null);
                }
            }
        });
    }

    @FXML
    private void subirImagen(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar Imagen del Producto");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg"));

        // Configurar la ruta predeterminada
        File rutaPredeterminada = new File("/home/hazel/NetBeansProjects/FarmaPlusF/src/main/resources/img");
        // Comprobar que la carpeta realmente existe antes de asignarla
        if (rutaPredeterminada.exists() && rutaPredeterminada.isDirectory()) {
            fc.setInitialDirectory(rutaPredeterminada);
        }
        // Abre la ventana de busqueda
        File archivo = fc.showOpenDialog(null);

        if (archivo != null) {
            rutaImagenSeleccionada = archivo.toURI().toString(); // Guarda la ruta local
            imgPreview.setImage(new Image(rutaImagenSeleccionada)); // Muestra la foto
        }
    }

    private void cargarDatos() {
        listaProductos.clear();
        String sql = "SELECT * FROM productos";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getString("sku"), rs.getString("nombre"), rs.getString("marca"),
                        rs.getDouble("precio"), rs.getInt("stock"),
                        rs.getString("imagen_path") // Leemos la ruta de MariaDB
                );
                listaProductos.add(p);
            }
            tablaProductos.setItems(listaProductos);
        } catch (SQLException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }

    @FXML
    private void guardarProducto(ActionEvent event) {
        if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() || txtStock.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Error", "Llena todos los campos obligatorios.");
            return;
        }

        // Agregamos imagen_path al INSERT
        String sql = "INSERT INTO productos (sku, nombre, marca, precio, stock, imagen_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtId.getText());
            ps.setString(2, txtNombre.getText());
            ps.setString(3, txtMarca.getText());
            ps.setDouble(4, Double.parseDouble(txtPrecio.getText()));
            ps.setInt(5, Integer.parseInt(txtStock.getText()));
            ps.setString(6, rutaImagenSeleccionada); // Guardamos la ruta

            ps.executeUpdate();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Producto guardado.");
            limpiarCampos(null);
            cargarDatos();
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error BD", e.getMessage());
        }
    }

    @FXML
    private void actualizarProducto(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un producto primero.");
            return;
        }

        // Agregamos imagen_path al UPDATE
        String sql = "UPDATE productos SET nombre=?, marca=?, precio=?, stock=?, imagen_path=? WHERE sku=?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtMarca.getText());
            ps.setDouble(3, Double.parseDouble(txtPrecio.getText()));
            ps.setInt(4, Integer.parseInt(txtStock.getText()));
            ps.setString(5, rutaImagenSeleccionada);
            ps.setString(6, txtId.getText());

            ps.executeUpdate();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exito", "Producto actualizado.");
            limpiarCampos(null);
            cargarDatos();
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void eliminarProducto(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setContentText("¿Seguro que deseas eliminar este producto?");
        Optional<ButtonType> opcion = confirmacion.showAndWait();

        if (opcion.isPresent() && opcion.get() == ButtonType.OK) {
            String sql = "DELETE FROM productos WHERE sku=?";
            try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, txtId.getText());
                ps.executeUpdate();
                limpiarCampos(null);
                cargarDatos();
            } catch (SQLException e) {
            }
        }
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtId.clear();
        txtNombre.clear();
        txtMarca.clear();
        txtPrecio.clear();
        txtStock.clear();
        imgPreview.setImage(null);
        rutaImagenSeleccionada = "";
        txtId.setDisable(false);
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
