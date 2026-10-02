module com.tuempresa.ejercicioconexionbd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.tuempresa.ejercicioconexionbd to javafx.fxml;
    opens com.tuempresa.ejercicioconexionbd.controller to javafx.fxml;
    opens com.tuempresa.ejercicioconexionbd.model to javafx.base;

    exports com.tuempresa.ejercicioconexionbd;
    exports com.tuempresa.ejercicioconexionbd.controller;
}