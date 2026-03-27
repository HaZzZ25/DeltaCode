module com.uacm.pf.farmaplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.uacm.pf.farmaplus to javafx.fxml;
    exports com.uacm.pf.farmaplus;
}
