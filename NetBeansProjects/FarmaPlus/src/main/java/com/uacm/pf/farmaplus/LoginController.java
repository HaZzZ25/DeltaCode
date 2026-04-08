package com.uacm.pf.farmaplus;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class LoginController {
    
    
    // Variables para calcular el movimiento de la ventana
    private double xOffset = 0;
    private double yOffset = 0;

    // Metodo para cerrar la app con tu propio boton
    @FXML
    private void cerrarAplicacion(ActionEvent event) {
        System.exit(0);
    }

    // Metodo para detectar donde haces clic
    @FXML
    private void ventanaPresionada(javafx.scene.input.MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    // Metodo para mover la ventana al arrastrar el mouse
    @FXML
    private void ventanaArrastrada(javafx.scene.input.MouseEvent event) {
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtNuevoUsuario;
    @FXML private PasswordField txtNuevaPassword;

    @FXML private AnchorPane panelLogin;
    @FXML private AnchorPane panelRegistro;

    @FXML
    private void mostrarRegistro(ActionEvent event) {
        // 1. Ocultar Login (Se desliza a la izquierda y se hace transparente)
        TranslateTransition slideLogin = new TranslateTransition(Duration.seconds(0.5), panelLogin);
        slideLogin.setToX(-550);
        FadeTransition fadeLogin = new FadeTransition(Duration.seconds(0.5), panelLogin);
        fadeLogin.setToValue(0); // 0 = invisible
        
        // 2. Mostrar Registro (Viene desde la derecha y se hace visible)
        TranslateTransition slideRegistro = new TranslateTransition(Duration.seconds(0.5), panelRegistro);
        slideRegistro.setToX(0);
        FadeTransition fadeRegistro = new FadeTransition(Duration.seconds(0.5), panelRegistro);
        fadeRegistro.setToValue(1); // 1 = visible

        // Ejecutar las 4 animaciones al mismo tiempo
        ParallelTransition pt = new ParallelTransition(slideLogin, fadeLogin, slideRegistro, fadeRegistro);
        pt.play();
    }

    @FXML
    private void mostrarLogin(ActionEvent event) {
        // 1. Regresar Login al centro y hacerlo visible
        TranslateTransition slideLogin = new TranslateTransition(Duration.seconds(0.5), panelLogin);
        slideLogin.setToX(0);
        FadeTransition fadeLogin = new FadeTransition(Duration.seconds(0.5), panelLogin);
        fadeLogin.setToValue(1);

        // 2. Ocultar Registro hacia la derecha y hacerlo invisible
        TranslateTransition slideRegistro = new TranslateTransition(Duration.seconds(0.5), panelRegistro);
        slideRegistro.setToX(550);
        FadeTransition fadeRegistro = new FadeTransition(Duration.seconds(0.5), panelRegistro);
        fadeRegistro.setToValue(0);

        ParallelTransition pt = new ParallelTransition(slideLogin, fadeLogin, slideRegistro, fadeRegistro);
        pt.play();
    }

    @FXML
    private void iniciarSesion() throws IOException {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if ("admin".equals(usuario) && "1234".equals(password)) {
            Sesion.usuarioActual = "Admin";
            Sesion.rolActual = "Administrador";
            App.setRoot("Dashboard"); 
        } else if ("vendedor".equals(usuario) && "1234".equals(password)) {
            Sesion.usuarioActual = "Juan (Vendedor)";
            Sesion.rolActual = "Vendedor";
            App.setRoot("Dashboard");
        } else {
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alerta.setTitle("Error de Acceso");
            alerta.setHeaderText(null);
            alerta.setContentText("Usuario o contrasena incorrectos. Intenta de nuevo.");
            alerta.showAndWait();
        }
    }
}