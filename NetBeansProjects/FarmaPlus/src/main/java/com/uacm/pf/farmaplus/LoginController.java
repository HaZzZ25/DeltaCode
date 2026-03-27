package com.uacm.pf.farmaplus;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    // Estas variables se conectan con los fx:id de tu Login.fxml
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword; 

    // Este metodo se ejecuta al dar clic en el boton "Ingresar"
    @FXML
    private void iniciarSesion() throws java.io.IOException {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        // Validacion para el Administrador
        if ("admin".equals(usuario) && "1234".equals(password)) {
            Sesion.usuarioActual = "Admin";
            Sesion.rolActual = "Administrador";
            System.out.println("Acceso concedido como Administrador");
            App.setRoot("Dashboard");

            // Validacion para el Vendedor
        } else if ("vendedor".equals(usuario) && "1234".equals(password)) {
            Sesion.usuarioActual = "Juan (Vendedor)";
            Sesion.rolActual = "Vendedor";
            System.out.println("Acceso concedido como Vendedor");
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
