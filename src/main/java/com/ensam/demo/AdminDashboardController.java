package com.ensam.demo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.math.BigDecimal;

public class AdminDashboardController {
    @FXML
    private Button home;
    @FXML
    private TableView<User> userTable;


    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, Integer> ageColumn;
    @FXML
    private TableColumn<User, BigDecimal> soldAccountColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> genderColumn;
    @FXML
    private TableColumn<User, String> occupationColumn;

    @FXML
    private Button refreshButton;

    private final ObservableList<User> userList = FXCollections.observableArrayList();

    private TableRow<User> lastClickedRow = null;  // To track the last left-clicked row
    private ContextMenu currentContextMenu = null;  // To track the current context menu

    @FXML
    public void initialize() {
        loadData(); // Load users when the controller is initialized

        // Add right-click context menu to the table
        userTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();

            // Left-click event to select and color the row
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {  // Left-click
                    if (lastClickedRow != null) {
                        lastClickedRow.setStyle("-fx-text-fill: #333;"); // Reset text color of previous left-clicked row
                    }

                    // Set the color of the clicked row to purple and text to white
                    row.setStyle("-fx-background-color: purple; -fx-text-fill: white;");

                    // Update the last clicked row
                    lastClickedRow = row;
                }
            });

            // Right-click event to show context menu without changing the color to white
            row.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {  // Right-click
                    row.setStyle("");  // Reset all custom styles
                    row.getStyleClass().add("right-clicked");  // Apply the right-clicked class
                    // If the row was left-clicked previously, keep the text color the same (no change to white)
                    if (lastClickedRow != null && lastClickedRow != row) {
                        lastClickedRow.setStyle("-fx-text-fill: black;"); // Reset text color for previous left-clicked row
                    }

                    // Keep the text color of the current row as #333 even when right-clicked
                    row.setStyle("-fx-text-fill: black;"); // Ensure text color stays default on right-click

                    // If there is already a context menu, hide it
                    if (currentContextMenu != null) {
                        currentContextMenu.hide();
                    }


                    // Show context menu only if a row was previously left-clicked
                    if (lastClickedRow != null && lastClickedRow == row) {
                        User selectedUser = lastClickedRow.getItem(); // Get the selected user
                        if (selectedUser != null) {
                            // Create context menu
                            ContextMenu contextMenu = new ContextMenu();

                            // Menu item for accessing the user's dashboard
                            MenuItem accessDashboardItem = new MenuItem("Access User Dashboard");
                            accessDashboardItem.setOnAction(e -> onAccessUserDashboardAction(selectedUser));

                            // Menu item for deleting the user
                            // MenuItem deleteUserItem = new MenuItem("Delete User");
                            //deleteUserItem.setOnAction(e -> deleteUser(selectedUser));

                            // Add items to the context menu
                            contextMenu.getItems().addAll(accessDashboardItem);

                            // Show context menu at the mouse location
                            contextMenu.show(row, event.getScreenX(), event.getScreenY());

                            // Set current context menu to be tracked
                            currentContextMenu = contextMenu;
                        }
                    }
                }
            });

            return row;
        });

        // Initialize the columns using the properties in User
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        soldAccountColumn.setCellValueFactory(cellData -> cellData.getValue().soldAccountProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        occupationColumn.setCellValueFactory(cellData -> cellData.getValue().occupationProperty());

        // Load initial data
        loadData();
    }

    @FXML
    private void refreshData() {
        loadData();
    }

    private void openUserDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountInformation.fxml"));
            Stage stage2 = new Stage();
            stage2.setScene(new Scene(loader.load()));
            stage2.setTitle("User Dashboard");

            // Set the window icon
            stage2.getIcons().add(new Image(getClass().getResourceAsStream("Wallet-PNG-Background.png")));

            // Get the controller for Account Information
            AccountInformation controller = loader.getController();

            // Pass the user details (username, password) to the controller
            controller.initializeData(user.getUsername(), user.getPassword(), true); // Pass isAdmin = true for the admin

            // Show the new stage (user's dashboard)
            StageConfigurator.configureStage(stage2);
            stage2.show();

            // Close the current admin dashboard stage
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This method will be called when the admin clicks "Access User Dashboard" from the context menu
    public void onAccessUserDashboardAction(User selectedUser) {
        if (selectedUser != null) {
            openUserDashboard(selectedUser);
        }
    }

    // Method to delete a user from the database
//    private void deleteUser(User user) {
//        String query = "DELETE FROM users_bank WHERE id = ?";
//        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            ps.setInt(1, user.getId());
//            ps.executeUpdate();
//            loadData(); // Refresh the data after deletion
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void loadData() {
        // SQL query to fetch all users, excluding the admin
        String query = "SELECT id, first_Name, last_Name, Age_user, BALANCE, username, password, gender, occupation FROM users_bank";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-project", "root", "1234");

             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            userList.clear(); // Clear existing data
            int AdjustedId = 1; // Skip the admin ID
            while (rs.next()) {
                String firstName = rs.getString("first_Name");
                String lastName = rs.getString("last_Name");
                int age = rs.getInt("Age_user");
                BigDecimal soldAccount = rs.getBigDecimal("BALANCE");
                String username = rs.getString("username");
                String password = rs.getString("password");

                // Check if the user is not the admin (root user with username "root" and password "1234")
                if (username != null && password != null && !(username.equals("root") && password.equals("1234"))) {
                    String gender = rs.getString("gender");
                    String occupation = rs.getString("occupation");

                    // Create user object and add to list
                    User user = new User(AdjustedId, firstName, lastName, age, soldAccount, username, password, gender, occupation);
                    userList.add(user);
                    AdjustedId++; // Increment the adjusted ID for the next user
                }
            }

            userTable.setItems(userList);  // Set the user list in the table

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void switchToHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Stage stage2 = new Stage();
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("styleTEXTFields.css").toExternalForm());
        stage2.setScene(scene);

        stage2.setTitle("Wallet APP");
        stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));

        StageConfigurator.configureStage(stage2);
        stage2.show();
        //close the current scene
        Stage stage = (Stage) refreshButton.getScene().getWindow();
        stage.close();
    }
}
