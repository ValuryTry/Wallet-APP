package com.ensam.demo;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageConfigurator {

    public static void configureStage(Stage stage) {
        // Disable resizing
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        // Disable the close button ("X")
        stage.setOnCloseRequest(event -> {
            // Consume the event to prevent the application from closing
            event.consume();
            System.out.println("Close button disabled!");
        });
    }
}
