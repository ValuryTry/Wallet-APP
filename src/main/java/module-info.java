module com.ensam.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;


    opens com.ensam.demo to javafx.fxml;
    exports com.ensam.demo;
}