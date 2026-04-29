package com.uacm.pf.farmaplus.controlador;

import com.uacm.pf.farmaplus.modelo.DetalleVenta;
import com.uacm.pf.farmaplus.modelo.Producto;
import com.uacm.pf.farmaplus.modelo.Sesion;
import com.uacm.pf.farmaplus.conexion.ConexionDB;
import java.sql.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class VentasController {

    @FXML
    private FlowPane panelCatalogo;
    @FXML
    private TextField txtBuscarProducto; // RESTAURADO
    @FXML
    private TableView<DetalleVenta> tablaCarrito;
    @FXML
    private TableColumn<DetalleVenta, String> colProducto;
    @FXML
    private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML
    private TableColumn<DetalleVenta, Double> colPrecio;
    @FXML
    private TableColumn<DetalleVenta, Double> colSubtotal;
    @FXML
    private ComboBox<String> comboClientes;
    @FXML
    private Label lblSubtotal, lblTotal;
    @FXML
    private Label lblDescuento;
    @FXML
    private Label lblClienteInfo;
    @FXML
    private Label lblMembresia;

    private ObservableList<DetalleVenta> listaCarrito = FXCollections.observableArrayList();
    private double totalVenta = 0.0;
    private int descuentoActual = 0;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarCatalogo("");
        cargarClientes();

        comboClientes.setOnAction(e -> calcularTotal());
    }

    // METODO DE BUSQUEDA RESTAURADO
    @FXML
    private void buscarEnCatalogo(ActionEvent event) {
        cargarCatalogo(txtBuscarProducto.getText());
    }

    private void configurarTabla() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tablaCarrito.setItems(listaCarrito);
    }

    private void cargarClientes() {
        ObservableList<String> nombres = FXCollections.observableArrayList();
        try (Connection con = ConexionDB.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT nombre FROM clientes")) {
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
            comboClientes.setItems(nombres);
            if (!nombres.isEmpty()) {
                comboClientes.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarCatalogo(String filtro) {
        panelCatalogo.getChildren().clear();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? OR sku LIKE ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + filtro + "%");
            ps.setString(2, "%" + filtro + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto(rs.getString("sku"), rs.getString("nombre"), rs.getString("marca"), rs.getDouble("precio"), rs.getInt("stock"), rs.getString("imagen_path"));
                panelCatalogo.getChildren().add(crearTarjeta(p));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox crearTarjeta(Producto p) {
        VBox t = new VBox(5);
        t.setAlignment(Pos.CENTER);
        t.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        ImageView img = new ImageView();
        if (p.getImagenPath() != null && !p.getImagenPath().isEmpty()) {
            try {
                img.setImage(new Image(p.getImagenPath()));
            } catch (Exception e) {
            }
        }
        img.setFitHeight(50);
        img.setFitWidth(50);

        int stockMaximo = p.getStock() > 0 ? p.getStock() : 1;
        Spinner<Integer> sp = new Spinner<>(1, stockMaximo, 1);
        sp.setPrefWidth(60);

        Button btn = new Button("Agregar");
        btn.setStyle("-fx-background-color: #449d6b; -fx-text-fill: white; -fx-cursor: hand;");

        if (p.getStock() <= 0) {
            btn.setDisable(true);
            sp.setDisable(true);
            btn.setText("Agotado");
            btn.setStyle("-fx-background-color: #9e9e9e; -fx-text-fill: white;");
        } else {
            btn.setOnAction(e -> {
                // Proteccion contra pasarse del stock usando el Spinner
                DetalleVenta itemExistente = null;
                for (DetalleVenta det : listaCarrito) {
                    if (det.getProductoObj().getSku().equals(p.getSku())) {
                        itemExistente = det;
                        break;
                    }
                }

                int cantidadDeseada = sp.getValue();
                int cantidadFutura = cantidadDeseada;
                if (itemExistente != null) {
                    cantidadFutura = itemExistente.getCantidad() + cantidadDeseada;
                }

                if (cantidadFutura > p.getStock()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Stock Insuficiente");
                    alerta.setHeaderText(null);
                    alerta.setContentText("No puedes agregar mas. Solo hay " + p.getStock() + " unidades.");
                    alerta.showAndWait();
                    return;
                }

                if (itemExistente != null) {
                    itemExistente.sumarCantidad(cantidadDeseada);
                } else {
                    listaCarrito.add(new DetalleVenta(p, cantidadDeseada));
                }
                tablaCarrito.refresh();
                calcularTotal();
            });
        }

        HBox controles = new HBox(5);
        controles.setAlignment(Pos.CENTER);
        controles.getChildren().addAll(sp, btn);

        t.getChildren().addAll(img, new Label(p.getNombre()), new Label("$" + p.getPrecio()), new Label("Stock: " + p.getStock()), controles);
        return t;
    }

    private void calcularTotal() {
        double sub = 0;

        for (DetalleVenta d : listaCarrito) {
            sub += d.getSubtotal();
        }

        String cliente = comboClientes.getValue();
        descuentoActual = 0;

        if (cliente != null && !cliente.isEmpty()) {

            try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(
                    "SELECT descuento, membresia FROM clientes WHERE nombre = ?")) {

                ps.setString(1, cliente);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    descuentoActual = rs.getInt("descuento");

                    String membresia = rs.getString("membresia");

                    lblClienteInfo.setText("Cliente: " + cliente);
                    lblMembresia.setText("Membresía: " + membresia);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            lblClienteInfo.setText("Cliente: Público General");
            lblMembresia.setText("Membresía: Ninguna");
        }

        double desc = sub * (descuentoActual / 100.0);
        totalVenta = sub - desc;

        lblSubtotal.setText("Subtotal: $" + String.format("%.2f", sub));
        lblDescuento.setText("Descuento: $" + String.format("%.2f", desc));
        lblTotal.setText("Total: $" + String.format("%.2f", totalVenta) + " (-" + descuentoActual + "%)");
    }
    
    @FXML
    private void eliminarDelCarrito(ActionEvent event) {
        // 1. Obtener el producto que el usuario seleccionó en la tabla
        DetalleVenta seleccionado = tablaCarrito.getSelectionModel().getSelectedItem();
        
        // 2. Si no seleccionó nada, le avisamos
        if (seleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Aviso");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un producto de la tabla para eliminar.");
            alerta.showAndWait();
            return;
        }
        
        // 3. Lo borramos de la lista temporal del carrito
        listaCarrito.remove(seleccionado);
        
        // 4. Actualizamos la tabla y recalculamos el total de dinero a cobrar
        tablaCarrito.refresh();
        calcularTotal();
    }

    @FXML
    private void completarVenta(ActionEvent event) {
        if (listaCarrito.isEmpty()) {
            return;
        }
        try (Connection con = ConexionDB.conectar()) {
            con.setAutoCommit(false);

            // Seguridad absoluta: Checamos la base de datos antes de cobrar
            String sqlCheck = "SELECT stock, nombre FROM productos WHERE sku = ?";
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                for (DetalleVenta det : listaCarrito) {
                    psCheck.setString(1, det.getProductoObj().getSku());
                    ResultSet rs = psCheck.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("stock") < det.getCantidad()) {
                            new Alert(Alert.AlertType.ERROR, "El producto " + rs.getString("nombre") + " no tiene stock suficiente.").showAndWait();
                            con.rollback();
                            return;
                        }
                    }
                }
            }

            PreparedStatement psV = con.prepareStatement("INSERT INTO ventas (total, vendedor) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            psV.setDouble(1, totalVenta);
            psV.setString(2, Sesion.usuarioActual != null ? Sesion.usuarioActual : "Vendedor");
            psV.executeUpdate();
            ResultSet rsKeys = psV.getGeneratedKeys();
            rsKeys.next();
            int idVenta = rsKeys.getInt(1);

            PreparedStatement psD = con.prepareStatement("INSERT INTO detalle_ventas (id_venta, sku_producto, cantidad, precio_unitario) VALUES (?,?,?,?)");
            PreparedStatement psS = con.prepareStatement("UPDATE productos SET stock = stock - ? WHERE sku = ?");

            for (DetalleVenta d : listaCarrito) {
                psD.setInt(1, idVenta);
                psD.setString(2, d.getProductoObj().getSku());
                psD.setInt(3, d.getCantidad());
                psD.setDouble(4, d.getPrecio());
                psD.executeUpdate();

                psS.setInt(1, d.getCantidad());
                psS.setString(2, d.getProductoObj().getSku());
                psS.executeUpdate();
            }

            con.commit();
            new Alert(Alert.AlertType.INFORMATION, "¡Venta Guardada con Exito!").showAndWait();
            listaCarrito.clear();
            calcularTotal();
            cargarCatalogo("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
