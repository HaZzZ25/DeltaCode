module com.uacm.pf.farmaplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
    requires java.net.http;

    // Permite a JavaFX usar los FXML de tu aplicacion
    opens com.uacm.pf.farmaplus to javafx.fxml;
    opens com.uacm.pf.farmaplus.controlador to javafx.fxml;
    
    // Permite a las tablas de JavaFX leer tus clases del modelo
    opens com.uacm.pf.farmaplus.modelo to javafx.base;

    exports com.uacm.pf.farmaplus;
    exports com.uacm.pf.farmaplus.controlador;
    exports com.uacm.pf.farmaplus.modelo;
}