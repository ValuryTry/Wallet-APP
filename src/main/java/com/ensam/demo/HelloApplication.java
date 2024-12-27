package com.ensam.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
//    public void start(Stage primarystage) throws IOException {
//
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primarystage.setTitle("Wallet APP");
//        primarystage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
//        primarystage.setScene(scene);
//        StageConfigurator.configureStage(primarystage);
//        primarystage.show();
//    }
//    public void start(Stage primaryStage) throws IOException {
//        // Show the loading screen
//        Stage loadingStage = new Stage();
//        loadingStage.initStyle(StageStyle.TRANSPARENT);
//        loadingStage.setTitle("Loading...");
//
//        VBox loadingVBox = new VBox();
//        loadingVBox.setAlignment(Pos.CENTER);
//        loadingVBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20px;");
//        loadingVBox.setMinSize(300, 100);
//
//        Label loadingLabel = new Label("Application is loading, please wait...");
//        loadingVBox.getChildren().add(loadingLabel);
//
//        Scene loadingScene = new Scene(loadingVBox);
//        loadingStage.setScene(loadingScene);
//        loadingStage.show();
//
//        // Load main FXML after a delay
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        primaryStage.setTitle("Wallet APP");
//        primaryStage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
//        primaryStage.setScene(scene);
//        StageConfigurator.configureStage(primaryStage);
//
//        // Hide the loading stage and show the main stage
//        new Thread(() -> {
//            try {
//                Thread.sleep(3000);  // Simulate loading delay
//                Platform.runLater(() -> {
//                    loadingStage.close();
//                    primaryStage.show();
//                });
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
    public void start(Stage primaryStage) throws IOException {
        // Show the loading screen
        Stage loadingStage = new Stage();
        loadingStage.initStyle(StageStyle.TRANSPARENT);
        loadingStage.setTitle("Loading...");

        // Creating a VBox layout with a transparent background
        VBox loadingVBox = new VBox();
        loadingVBox.setAlignment(Pos.CENTER);
        loadingVBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20px;");

        // Adding an image logo and a loading spinner
        ImageView logo = new ImageView(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        logo.setFitHeight(100);
        logo.setFitWidth(100);

        Label loadingLabel = new Label("Application is loading, please wait...");
        loadingLabel.setTextFill(Color.WHITE);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(300);

        loadingVBox.getChildren().addAll(logo, loadingLabel, progressBar);

        Scene loadingScene = new Scene(loadingVBox);
        loadingStage.setScene(loadingScene);
        loadingStage.show();

        // Load main FXML after a delay
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("styleTEXTFields.css").toExternalForm());
        primaryStage.setTitle("Wallet APP");
        primaryStage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        primaryStage.setScene(scene);
        StageConfigurator.configureStage(primaryStage);

        // Simulating a loading process
        new Thread(() -> {
            try {
                Thread.sleep(3000);  // Simulate loading delay
                Platform.runLater(() -> {
                    loadingStage.close();
                    primaryStage.show();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void main(String[] args) {
        launch();
    }
}