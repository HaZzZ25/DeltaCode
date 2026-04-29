package com.uacm.pf.farmaplus.controlador;

import com.uacm.pf.farmaplus.conexion.ConexionDB;
import com.uacm.pf.farmaplus.modelo.Sesion;
import com.uacm.pf.farmaplus.App;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevoUsuario;
    @FXML private PasswordField txtNuevaPassword;

    @FXML private AnchorPane panelLogin;
    @FXML private AnchorPane panelRegistro;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void ventanaPresionada(javafx.scene.input.MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void ventanaArrastrada(javafx.scene.input.MouseEvent event) {
        javafx.stage.Stage stage = (javafx.stage.Stage)
                ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void mostrarRegistro(ActionEvent event) {
        TranslateTransition slideLogin = new TranslateTransition(Duration.seconds(0.5), panelLogin);
        slideLogin.setToX(-550);

        FadeTransition fadeLogin = new FadeTransition(Duration.seconds(0.5), panelLogin);
        fadeLogin.setToValue(0);

        TranslateTransition slideRegistro = new TranslateTransition(Duration.seconds(0.5), panelRegistro);
        slideRegistro.setToX(0);

        FadeTransition fadeRegistro = new FadeTransition(Duration.seconds(0.5), panelRegistro);
        fadeRegistro.setToValue(1);

        new ParallelTransition(slideLogin, fadeLogin, slideRegistro, fadeRegistro).play();
    }

    @FXML
    private void mostrarLogin(ActionEvent event) {
        TranslateTransition slideLogin = new TranslateTransition(Duration.seconds(0.5), panelLogin);
        slideLogin.setToX(0);

        FadeTransition fadeLogin = new FadeTransition(Duration.seconds(0.5), panelLogin);
        fadeLogin.setToValue(1);

        TranslateTransition slideRegistro = new TranslateTransition(Duration.seconds(0.5), panelRegistro);
        slideRegistro.setToX(550);

        FadeTransition fadeRegistro = new FadeTransition(Duration.seconds(0.5), panelRegistro);
        fadeRegistro.setToValue(0);

        new ParallelTransition(slideLogin, fadeLogin, slideRegistro, fadeRegistro).play();
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {

        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Ingresa usuario y contraseña.");
            return;
        }

        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Sesion.usuarioActual = rs.getString("nombre");
                Sesion.rolActual = rs.getString("rol");

                App.setRoot("Dashboard");

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario o contraseña incorrectos.");
            }

        } catch (SQLException | IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error de conexión o vista no encontrada.");
            e.printStackTrace();
        }
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {

        if (txtNuevoNombre.getText().isEmpty()
                || txtNuevoUsuario.getText().isEmpty()
                || txtNuevaPassword.getText().isEmpty()) {

            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Llena todos los campos.");
            return;
        }

        String sql = "INSERT INTO usuarios (nombre, usuario, password, rol) VALUES (?, ?, ?, 'Vendedor')";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNuevoNombre.getText());
            ps.setString(2, txtNuevoUsuario.getText());
            ps.setString(3, txtNuevaPassword.getText());

            ps.executeUpdate();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado.");

            txtNuevoNombre.clear();
            txtNuevoUsuario.clear();
            txtNuevaPassword.clear();

            mostrarLogin(null);

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Usuario ya existe o error en BD.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}