package com.uacm.pf.farmaplus.controlador;

import com.uacm.pf.farmaplus.modelo.Cliente;
import com.uacm.pf.farmaplus.conexion.ConexionDB;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientesController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> comboMembresia;

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colMembresia;
    @FXML private TableColumn<Cliente, Integer> colDescuento;

    private ObservableList<Cliente> listaClientes;

    @FXML
    public void initialize() {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colMembresia.setCellValueFactory(new PropertyValueFactory<>("membresia"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));

        ObservableList<String> opciones =
                FXCollections.observableArrayList("Ninguna", "Bronce", "Plata", "Oro");
        comboMembresia.setItems(opciones);
        comboMembresia.getSelectionModel().selectFirst();

        listaClientes = FXCollections.observableArrayList();
        cargarDatos();

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtId.setText(String.valueOf(newSel.getId()));
                txtNombre.setText(newSel.getNombre());
                txtTelefono.setText(newSel.getTelefono());
                txtCorreo.setText(newSel.getCorreo());
                comboMembresia.setValue(newSel.getMembresia());
            }
        });
    }

    private void cargarDatos() {
        listaClientes.clear();

        String sql = "SELECT * FROM clientes";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("membresia"),
                        rs.getInt("descuento")
                );
                listaClientes.add(c);
            }

            tablaClientes.setItems(listaClientes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int calcularDescuento(String membresia) {
        switch (membresia) {
            case "Bronce": return 5;
            case "Plata": return 10;
            case "Oro": return 15;
            default: return 0;
        }
    }

    @FXML
    private void guardarCliente(ActionEvent event) {

        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "El nombre es obligatorio.");
            return;
        }

        String mem = comboMembresia.getValue();
        int desc = calcularDescuento(mem);

        String sql = "INSERT INTO clientes (nombre, telefono, correo, membresia, descuento) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtTelefono.getText());
            ps.setString(3, txtCorreo.getText());
            ps.setString(4, mem);
            ps.setInt(5, desc);

            ps.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Cliente guardado. Descuento: " + desc + "%");

            limpiarCampos(null);
            cargarDatos();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error BD", e.getMessage());
        }
    }

    @FXML
    private void actualizarCliente(ActionEvent event) {

        if (txtId.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Aviso",
                    "Selecciona un cliente primero.");
            return;
        }

        String mem = comboMembresia.getValue();
        int desc = calcularDescuento(mem);

        String sql = "UPDATE clientes SET nombre=?, telefono=?, correo=?, membresia=?, descuento=? WHERE id=?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtTelefono.getText());
            ps.setString(3, txtCorreo.getText());
            ps.setString(4, mem);
            ps.setInt(5, desc);
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Cliente actualizado.");

            limpiarCampos(null);
            cargarDatos();

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void eliminarCliente(ActionEvent event) {

        if (txtId.getText().isEmpty()) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setContentText("¿Seguro que deseas eliminar este cliente?");
        Optional<ButtonType> opcion = confirmacion.showAndWait();

        if (opcion.isPresent() && opcion.get() == ButtonType.OK) {

            String sql = "DELETE FROM clientes WHERE id=?";

            try (Connection con = ConexionDB.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(txtId.getText()));
                ps.executeUpdate();

                limpiarCampos(null);
                cargarDatos();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        txtId.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        comboMembresia.getSelectionModel().selectFirst();
        tablaClientes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}