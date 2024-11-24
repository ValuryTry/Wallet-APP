package com.ensam.demo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AccountInformation implements Initializable, Serializable {
   @FXML
   private Text sex;
   @FXML
   private Circle circle;
    @FXML
    private Button btnQuit;  // Button to return to the user table
   String username;
   String password;

    public AccountInformation() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void initializeData(String username, String password,boolean isAdmin) {
         this.username = username;
         this.password = password;
        // Check if the user is an admin
        // If the user is admin, show the "Return to User List" button
        if (isAdmin) {
            btnQuit.setVisible(true);
        } else {
            btnQuit.setVisible(false); // Hide button for regular users
        }
        try {
            updateGender();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching gender: " + e.getMessage());
        }
    }



    public void updateGender() throws SQLException {
        String gender =null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_bank WHERE username= ? AND password = ? ")) {
            try {
                preparedStatement.setString(1, username);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                preparedStatement.setString(2, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                gender = resultSet.getString("gender"); // Fetch the gender column
            }
        }
        if (gender != null) {
            final String finalGender = gender; // For use in the Platform.runLater context
            Platform.runLater(() -> sex.setText(finalGender)); // Update the Text field safely


        } else {
            Platform.runLater(() -> sex.setText("Gender not found"));
        }
    }
    @FXML
    public void returnToUserList() throws IOException {
        // Ensure the path to the FXML file is correct
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ensam/demo/AdminDashboard.fxml"));

        if (loader.getLocation() == null) {
            System.out.println("FXML file not found!");
            return;
        }

        // Create a new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        // Get the controller instance of AdminDashboard and initialize it if needed
        AdminDashboardController controller = loader.getController();
        // Pass necessary data (e.g. admin username and password) to the controller
        controller.loadData(); // Make sure loadData is called to refresh the user table

        // Show the stage
        stage.show();

        // Close the current window (user dashboard)
        Stage currentStage = (Stage) btnQuit.getScene().getWindow();
        currentStage.close();
    }






}
