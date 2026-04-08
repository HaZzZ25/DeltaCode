module com.uacm.pf.farmaplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    
    //Linea para usar base de datos
    requires java.sql;

    opens com.uacm.pf.farmaplus to javafx.fxml;
    exports com.uacm.pf.farmaplus;
}