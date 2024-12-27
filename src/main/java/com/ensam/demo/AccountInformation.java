package com.ensam.demo;
import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;  // Pour obtenir l'heure locale

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountInformation implements Initializable, Serializable {
   @FXML
   private Text sex;
   @FXML
   private AnchorPane anchor;
   @FXML
   private Label balance;
   @FXML
   private Circle circle;
    @FXML
    private Button btnQuit;  // Button to return to the user table
    @FXML
    private Button back;
    @FXML
    private Button deposit;
    @FXML
    private Button withdraw;
    @FXML
    private void onHover(Button button) {
        button.setStyle("-fx-background-color: green; -fx-text-fill: #f0f0f0;-fx-background-radius: 20px");
    }
    @FXML
    private void onHoverForMenu(Button button) {
        button.setStyle("-fx-background-color: green; -fx-text-fill: #f0f0f0");
    }
    public void onHoverDeposit(){onHover(deposit);}
    public void onHoverReturnToMenu(){onHoverForMenu(back);}
    public void onHoverWithdraw(){onHover(withdraw);}
    @FXML
    private void onExit(Button button) {
        button.setStyle("-fx-background-color: lightblue; -fx-background-radius :20px");

    }
    @FXML
    private void onExitforMenu(Button button) {
        button.setStyle("-fx-background-color: red");
    }
    public void onExitMenu(){onExitforMenu(back);}
    public void onExitDeposit(){onExit(deposit);}
    public void onExitWithdraw(){onExit(withdraw);}
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
            back.setVisible(false);
            deposit.setVisible(false);
            withdraw.setVisible(false);


        } else {
            btnQuit.setVisible(false); // Hide button for regular users
            back.setVisible(true);
            deposit.setVisible(true);
            withdraw.setVisible(true);
        }
        try {
            //updateGender();
            updateBalance();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching gender: " + e.getMessage());
        }
    }



    /*public void updateGender() throws SQLException {
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
    }*/

    public void updateBalance() throws SQLException {
        float userBalance = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_bank WHERE username = ? AND password = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userBalance = resultSet.getFloat("BALANCE"); // Fetch the balance column
            }

        }

        if (userBalance != 0) {
            float finalBalance = userBalance; // For use in the Platform.runLater context
            Platform.runLater(() -> balance.setText(String.valueOf(finalBalance))); // Update the Text field safely
        } else {
            Platform.runLater(() -> balance.setText("0"));
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
        StageConfigurator.configureStage(stage);
        // Show the stage
        stage.show();

        // Close the current window (user dashboard)
        Stage currentStage = (Stage) btnQuit.getScene().getWindow();
        currentStage.close();
    }
    private void updateBalanceInDB(float amount, boolean isDeposit) throws SQLException {
        // Validate the amount
        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
             PreparedStatement selectStatement = connection.prepareStatement("SELECT BALANCE FROM users_bank WHERE username = ? AND password = ?");
             PreparedStatement updateStatement = connection.prepareStatement("UPDATE users_bank SET balance = balance + ? WHERE username = ? AND password = ?")) {

            // Fetch the current balance
            selectStatement.setString(1, username);
            selectStatement.setString(2, password);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                float currentBalance = resultSet.getFloat("BALANCE");

                // If it's a withdrawal, validate sufficient balance
                if (!isDeposit && amount > currentBalance) {
                    showError("Insufficient balance. Please enter a smaller amount.");
                    return;
                }

                // For withdrawal, negate the amount to subtract
                float finalAmount = isDeposit ? amount : -amount;

                // Update the balance
                updateStatement.setFloat(1, finalAmount);
                updateStatement.setString(2, username);
                updateStatement.setString(3, password);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    Platform.runLater(() -> {
                        try {
                            updateBalance(); // Refresh the balance in the UI
                            
                            if(isDeposit) {
                                playDepositAnimation();
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\USER\\Desktop\\Users_Transaction\\" + username + ".txt", true))) {
                                    writer.newLine(); // Add a newline before appending
                                    LocalDate today=LocalDate.now();
                                    LocalTime currentTime = LocalTime.now();
                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                    String formattedTime = currentTime.format(timeFormatter);
                                    String contentToAdd ="+" + finalAmount + " " + "le" + " " +today + " " + "à" + " "+  formattedTime;
                                    writer.write(contentToAdd);
                                    writer.flush();
                                    System.out.println("Content added: " + contentToAdd);
                                } catch (IOException e) {
                                    System.err.println("Error writing to the file: " );
                                    e.printStackTrace();
                                }
                            }
                            else{
                                playwithdrawAnimation();
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\USER\\Desktop\\Users_Transaction\\" + username + ".txt", true))) {
                                    writer.newLine(); // Add a newline before appending
                                    LocalDate today=LocalDate.now();
                                    LocalTime currentTime = LocalTime.now();
                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                                    String formattedTime = currentTime.format(timeFormatter);
                                    String contentToAdd = finalAmount + " " + "le" + " " +today + " " + "à" + " "+  formattedTime;
                                    writer.write(contentToAdd);
                                    writer.flush();
                                    System.out.println("Content added: " + contentToAdd);
                                } catch (IOException e) {
                                    System.err.println("Error writing to the file: " );
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            showError("Failed to refresh balance: " + e.getMessage());
                        }
                    });
                } else {
                    showError("Failed to update balance. Please check your credentials.");
                }
            } else {
                showError("User not found. Please check your credentials.");
            }
        } catch (SQLException e) {
            showError("Error updating balance: " + e.getMessage());
        }
    }
    @FXML
    private void deposit() {
        showAmountDialog(true); // true indicates deposit

    }

    @FXML
    private void withdraw() {
        showAmountDialog(false); // false indicates withdrawal
    }
    private void showAmountDialog(boolean isDeposit) {
        TextInputDialog dialog = new TextInputDialog();
        String action = isDeposit ? "Deposit" : "Withdraw";
        dialog.setTitle(action + " Amount");
        dialog.setHeaderText("Specify the amount you want to " + action.toLowerCase() + ":");
        dialog.setContentText("Amount:");

        dialog.getEditor().setText("0");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amount -> handleAmountInput(amount, isDeposit));
    }

    private void handleAmountInput(String amount, boolean isDeposit) {
        try {
            float amountValue = Float.parseFloat(amount);
            updateBalanceInDB(amountValue, isDeposit);
        } catch (NumberFormatException e) {
            showError("Invalid amount entered. Please enter a valid number.");
        } catch (SQLException e) {
            showError("An error occurred while processing the transaction.");
        }
    }
    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    // Method to play a sound effect
    private void playSoundEffect(String soundFilePath) {
        try {
            AudioClip soundEffect = new AudioClip(getClass().getResource(soundFilePath).toExternalForm());
            soundEffect.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void playDepositAnimation() {

        // Play sound effect
        playSoundEffect("cash-register-fake-88639.mp3");
        // Create an ImageView for the banknote
        ImageView money = new ImageView(new Image(getClass().getResource("1106000.png").toExternalForm()));
        money.setFitWidth(100); // Adjust width for paper money
        money.setFitHeight(50); // Adjust height for paper money
        money.setLayoutX(150);  // Starting position X
        money.setLayoutY(300);   // Starting position Y

        // Add the banknote to the AnchorPane
        anchor.getChildren().add(money);

        // Create a KeyFrame for fluttering (rotation and movement)
        Timeline timeline = new Timeline();

        KeyValue rotateValue1 = new KeyValue(money.rotateProperty(), -15);
        KeyValue rotateValue2 = new KeyValue(money.rotateProperty(), 15);
        KeyValue translateYValue = new KeyValue(money.layoutYProperty(), 50);

        // Define keyframes for fluttering and falling
        KeyFrame flutterStart = new KeyFrame(Duration.ZERO, rotateValue1, new KeyValue(money.layoutYProperty(), 300));
        KeyFrame flutterMiddle = new KeyFrame(Duration.seconds(1), rotateValue2);
        KeyFrame flutterEnd = new KeyFrame(Duration.seconds(2), rotateValue1, translateYValue);

        timeline.getKeyFrames().addAll(flutterStart, flutterMiddle, flutterEnd);

        timeline.setOnFinished(event -> anchor.getChildren().remove(money)); // Remove after animation

        // Play the animation
        timeline.play();
    }
    public void playwithdrawAnimation() {
        // Play sound effect
        playSoundEffect("withdraw-sound.mp3");
        // Create an ImageView for the banknote
        ImageView money = new ImageView(new Image(getClass().getResource("1106000.png").toExternalForm()));
        money.setFitWidth(100); // Adjust width for paper money
        money.setFitHeight(50); // Adjust height for paper money
        money.setLayoutX(150);  // Starting position X
        money.setLayoutY(30);   // Starting position Y

        // Add the banknote to the AnchorPane
        anchor.getChildren().add(money);

        // Create a KeyFrame for fluttering (rotation and movement)
        Timeline timeline = new Timeline();

        KeyValue rotateValue1 = new KeyValue(money.rotateProperty(), -15);
        KeyValue rotateValue2 = new KeyValue(money.rotateProperty(), 15);
        KeyValue translateYValue = new KeyValue(money.layoutYProperty(), 350);

        // Define keyframes for fluttering and falling
        KeyFrame flutterStart = new KeyFrame(Duration.ZERO, rotateValue1, new KeyValue(money.layoutYProperty(), 30));
        KeyFrame flutterMiddle = new KeyFrame(Duration.seconds(1), rotateValue2);
        KeyFrame flutterEnd = new KeyFrame(Duration.seconds(3.5), rotateValue1, translateYValue);

        timeline.getKeyFrames().addAll(flutterStart, flutterMiddle, flutterEnd);

        timeline.setOnFinished(event -> anchor.getChildren().remove(money)); // Remove after animation

        // Play the animation
        timeline.play();
    }

    public void BACK() throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Logged-INV2.fxml"));

        Stage stage2 = new Stage();
        stage2.setScene(new Scene(loader.load()));
        stage2.setTitle("Wallet APP");
        stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        LoggedInController controller = loader.getController();
        controller.initializeData(username, password);
        StageConfigurator.configureStage(stage2);
        stage2.show();
        //close the current scene
        Stage stage = (Stage) balance.getScene().getWindow();
        stage.close();
    }




}
