package com.ensam.demo;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signupcontroller implements Initializable {

    @FXML
    private  TextField firstname;
    @FXML
    private  TextField lastname;
    @FXML
    private  ComboBox<String> gender;
    @FXML
    private  DatePicker dob;
    @FXML
    private  ComboBox<String> occupation;
    @FXML
    private CheckBox check;



    public void switchSceneToLogin2() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(loader.load()));
        stage2.setTitle("Wallet APP");
        stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        StageConfigurator.configureStage(stage2);
        stage2.show();
        Stage stage = (Stage) firstname.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate ComboBoxes


        ObservableList<String> listOccupation = FXCollections.observableArrayList("Job", "Jobless");
        ObservableList<String> listGender = FXCollections.observableArrayList("Male", "Female");

        // Avoid NPE by checking if the ComboBoxes are null
        if (gender != null) {
            gender.setItems(listGender);
        } else {
            System.err.println("Gender ComboBox is null! Check your FXML bindings.");
        }

        if (occupation != null) {
            occupation.setItems(listOccupation);
        } else {
            System.err.println("Occupation ComboBox is null! Check your FXML bindings.");

        }
    }
    public  void signUpUser1(Event event) throws IOException {
        // Use try-with-resources to manage resources
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
             PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users_bank(first_Name, last_Name , gender , occupation ,Age_user ) VALUES (?,?,?,?,?)")) {




            // Set PreparedStatement parameters
            psInsert.setString(1, firstname.getText());
            psInsert.setString(2, lastname.getText());
            psInsert.setString(3, gender.getValue());
            psInsert.setString(4, occupation.getValue());
            LocalDate selectedDate1 = dob.getValue();
            int age1 = calculateAge(selectedDate1,LocalDate.now());
            psInsert.setInt(5, age1);
           if(firstname.getText().isEmpty() || lastname.getText().isEmpty() || gender.getValue() == null || occupation.getValue() == null) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText(null);
               alert.setContentText("Please fill all the fields correctly.");
               alert.showAndWait();
               check.setSelected(false);//if one of the conditions is not true and the checkbox was selected , so it will be unselected
               return;


           }
           else{
               if (validateInput(firstname.getText(), lastname.getText()) ) {
                   if(check.isSelected()) {
                       System.out.println("First Part of The Sign Up Form is submitted Successfully!");
                   }

               } else {
                   showAlert("Invalid Input", "Please ensure both First Name and Last Name contain only letters.");
                   check.setSelected(false);//if one of the conditions is not true and the checkbox was selected , so it will be unselected
                   return;

               }
               if(!check.isSelected() ) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Error");
                   alert.setHeaderText(null);
                   alert.setContentText("You need to accept the terms ");
                   alert.showAndWait();


                   return;
               }
               //the user must be 18+ to create an account and ofc he needs to choose a date of birth
               LocalDate selectedDate = dob.getValue();
               if(selectedDate == null ) {
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Error");
                   alert.setHeaderText(null);
                   alert.setContentText("You need to enter a valid date of birth");
                   alert.showAndWait();
                   return;
               }
               else{
                   int age = calculateAge(selectedDate,LocalDate.now());
                   if(age<18){
                       Alert alert = new Alert(Alert.AlertType.ERROR);
                       alert.setTitle("Error");
                       alert.setHeaderText(null);
                       alert.setContentText("You need to be 18+ to create an account ");
                       alert.showAndWait();
                       check.setSelected(false);
                       return;
                   }

               }
           }

            // Execute update
            psInsert.executeUpdate();

            // Load the next scene
            FXMLLoader loader = new FXMLLoader(Signupcontroller.class.getResource("sign_up_part2.fxml"));
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(loader.load()));
            stage2.setTitle("Wallet APP");
            stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
            Signupcontrollerpart2 controller = loader.getController();
            controller.initializeData(firstname.getText(),lastname.getText());
            controller.setupStageCloseHandler(stage2);
            StageConfigurator.configureStage(stage2);
            stage2.show();
            //close the current scene
            Stage stage = (Stage) firstname.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("FXML loading error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /*private void restrictToLetters(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                textField.setText(oldValue); // Revert to previous value if not valid
            }
        });
    }*/
    private boolean validateInput(String firstName, String lastName) {
        return firstName.matches("[a-zA-Z]+") && lastName.matches("[a-zA-Z]+");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

}
