package com.ensam.demo;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class Accueil implements Initializable {
    @FXML
    private TextField TF_username;
    @FXML
    private TextField TF_password;
    int attempts = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void switchSceneToSignUp() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sign_up.fxml"));
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(loader.load()));
        stage2.setTitle("Wallet APP");
        stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        stage2.show();
        //close the current scene
        Stage stage = (Stage) TF_username.getScene().getWindow();
        stage.close();
    }
    public void switchSceneToLogin() throws IOException, SQLException {
        if(TF_username.getText().equals("") || TF_password.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("les champs non renseignés");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;

        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_bank WHERE username= ? AND password = ?")) {
            preparedStatement.setString(1, TF_username.getText());
            preparedStatement.setString(2, TF_password.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                //i added a admin account option so the code will change a little bit
                String role = resultSet.getString("role"); // Fetch the user's role
                String username = resultSet.getString("username");
                if (role.equalsIgnoreCase("admin")) {
                    // Admin authentication
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Admin login successful!");
                    alert.setHeaderText(null);
                    alert.showAndWait();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml")); // Load Admin Dashboard
                    Stage stage2 = new Stage();
                    stage2.setScene(new Scene(loader.load()));
                    stage2.setTitle("Admin Dashboard");
                    stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
                    stage2.show();

                    // Close the current scene
                    Stage stage = (Stage) TF_username.getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Connexion réussie");

                    alert.setHeaderText(null);
                    alert.showAndWait();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Logged-INV2.fxml"));

                    Stage stage2 = new Stage();
                    stage2.setScene(new Scene(loader.load()));
                    stage2.setTitle("Wallet APP");
                    stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
                    LoggedInController controller = loader.getController();
                    controller.initializeData(TF_username.getText(), TF_password.getText());
                    stage2.show();
                    //close the current scene
                    Stage stage = (Stage) TF_username.getScene().getWindow();
                    stage.close();
                }
                //Version precedente
                /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Connexion réussie");

                alert.setHeaderText(null);
                alert.showAndWait();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Logged-INV2.fxml"));

                Stage stage2 = new Stage();
                stage2.setScene(new Scene(loader.load()));
                stage2.setTitle("Wallet APP");
                stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
                LoggedInController controller = loader.getController();
                controller.initializeData(TF_username.getText(), TF_password.getText());
                stage2.show();
                //close the current scene
                Stage stage = (Stage) TF_username.getScene().getWindow();
                stage.close();
            }*/
            }
            else{

                            while (attempts < 3) {
                                if (attempts == 2) {
                                    // Exit the application after 3 failed attempts
                                    Stage stage = (Stage) TF_username.getScene().getWindow();
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    //alert.setContentText("You have exceeded the number of attempts, the page will close."); (the simple one)
                                    alert.setContentText("You have exceeded the number of attempts. The page will close in: 5 seconds.");
                                    alert.setHeaderText(null);
                                    String imagePath = "C:/Users/USER/IdeaProjects/demo/src/main/resources/com/ensam/demo/ATTENTION.png";
                                    File file = new File(imagePath);
                                    Image image = new Image(file.toURI().toString());
                                    ImageView imageView = new ImageView(image);
                                    imageView.setFitHeight(50);
                                    imageView.setFitWidth(50);
                                    imageView.setPreserveRatio(true);
                                    alert.setGraphic(imageView);

                                    alert.show();
                                    //version 1 of alert shows and stage closes (simple)
                                    /*alert.showAndWait();
                                    stage.close();
                                    return;*/

                                    //version 2 includes time
                                    /*PauseTransition pause = new PauseTransition(Duration.seconds(5));
                                    pause.setOnFinished(e -> {
                                        Platform.runLater(() -> {
                                            alert.close();  // Close the alert
                                            stage.close();  // Close the stage automatically after 5 seconds
                                        });
                                    });
                                    pause.play(); // Start the timer for auto-close

                                    // Countdown timer (5 seconds)
                                    return */


                                    //Version 3 (avancée)

                                    final int[] countdown = {5}; // countdown from 5 seconds
                                    Timeline timeline = new Timeline(
                                            new KeyFrame(Duration.seconds(1), event -> {
                                                countdown[0]--; // Decrement countdown
                                                alert.setContentText("You have exceeded the number of attempts. The page will close in: " + countdown[0] + " seconds.");
                                                if (countdown[0] == 0) {
                                                    // When countdown reaches 0, close both alert and stage
                                                    Platform.runLater(() -> {
                                                        alert.close(); // Close the alert
                                                        stage.close(); // Close the stage automatically
                                                    });
                                                }
                                            })
                                    );
                                    timeline.setCycleCount(5); // Run for 5 seconds
                                    timeline.play(); // Start the countdown
                                    alert.setOnCloseRequest(event -> {
                                        stage.close(); // Close stage immediately when the alert is closed
                                    });
                                    return;
                                }
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Le username ou password est incorrect. \n"+ "attempts left :"+(3- (attempts+1)) +"\n"+"The page will close afterwards");
                                alert.setHeaderText(null);
                                alert.showAndWait();
                                attempts++;


                                return;
                            }

                        }
                    }


            }
        }

