module com.ensam.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.ensam.demo to javafx.fxml;
    exports com.ensam.demo;
}