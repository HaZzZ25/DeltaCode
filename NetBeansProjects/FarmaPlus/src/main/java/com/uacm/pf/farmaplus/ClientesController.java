package com.uacm.pf.farmaplus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ClientesController {
    @FXML private void guardarCliente(ActionEvent event) { System.out.println("Guardando cliente..."); }
    @FXML private void actualizarCliente(ActionEvent event) { System.out.println("Actualizando..."); }
    @FXML private void eliminarCliente(ActionEvent event) { System.out.println("Eliminando..."); }
    @FXML private void limpiarCampos(ActionEvent event) { System.out.println("Limpiando..."); }
}