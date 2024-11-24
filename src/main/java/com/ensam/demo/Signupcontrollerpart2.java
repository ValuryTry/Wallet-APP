package com.ensam.demo;

import javafx.event.ActionEvent;
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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Signupcontrollerpart2 implements Initializable {
    @FXML
    private  TextField TF_username;
    @FXML
    private  TextField TF_password;
    private String firstname;
    private String lastname;
    public void initializeData(String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public  void signUpUser2() {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
            PreparedStatement psCheckUserExists = connection.prepareStatement("SELECT * FROM users_bank WHERE username = ?");
            psCheckUserExists.setString(1, TF_username.getText());
            ResultSet resultSet = psCheckUserExists.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("u cannot use this username");
                alert.show();
            } else {
                // Mettre à jour les colonnes username et password dans la ligne existante
                PreparedStatement psUpdate = connection.prepareStatement("UPDATE users_bank SET username = ?, password = ? WHERE first_Name = ? AND last_Name = ?");
                psUpdate.setString(1, TF_username.getText());
                psUpdate.setString(2, TF_password.getText());
                psUpdate.setString(3, firstname);  // Variable partagée avec le premier contrôleur
                psUpdate.setString(4, lastname);  // Variable partagée avec le premier contrôleur
                if(TF_username.getText().isEmpty() || TF_password.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all the fields correctly.");
                    alert.showAndWait();
                    return;

                }

                int rowsUpdated = psUpdate.executeUpdate();
                if (rowsUpdated > 0) {
                    FXMLLoader loader = new FXMLLoader(Signupcontrollerpart2.class.getResource("main.fxml"));
                    Stage stage2 = new Stage();
                    stage2.setScene(new Scene(loader.load()));
                    stage2.setTitle("Wallet APP");
                    stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
                    stage2.show();
                    //close the current scene
                    Stage stage = (Stage) TF_username.getScene().getWindow();
                    stage.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteIncompleteUser() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234")) {
            String query = "DELETE FROM users_bank WHERE first_Name = ? AND last_Name = ? ";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setupStageCloseHandler(Stage stage) {
        stage.setOnCloseRequest(event -> {
            event.consume(); // Annule l'événement de fermeture, on va gérer cela nous-mêmes

            // Créer un message d'alerte pour confirmer la fermeture
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Quitter l'application");
            alert.setHeaderText("Attention !");
            alert.setContentText("Êtes-vous sûr de vouloir quitter ?\nLes données entrées avant seront supprimées.");
            String imagePath = "C:/Users/USER/IdeaProjects/demo/src/main/resources/com/ensam/demo/ATTENTION.png";
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            imageView.setPreserveRatio(true);
            alert.setGraphic(imageView);
            Stage stage2 = (Stage) alert.getDialogPane().getScene().getWindow();
            stage2.getIcons().add(new Image("file:C:/Users/USER/IdeaProjects/demo/src/main/resources/com/ensam/demo/attention-icone.png"));


            // Ajouter un style CSS personnalisé
            alert.getDialogPane().getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // Attendre la réponse de l'utilisateur (OK ou Cancel)
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Si l'utilisateur confirme, supprimer l'utilisateur incomplet et fermer la fenêtre
                    deleteIncompleteUser();
                    stage.close(); // Fermer la scène
                }
                // Sinon, rien ne se passe (la fenêtre reste ouverte)
            });
        });




    }

}