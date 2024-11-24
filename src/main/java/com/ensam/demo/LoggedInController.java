package com.ensam.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    @FXML
    private Button button_logout;
    @FXML
    private Button Dashboard;
    @FXML
    private Button Historique;
    @FXML



    String username;
    String password;

    @FXML
    private void onHover(Button button) {
        button.setStyle("-fx-background-color: #5499e6; -fx-text-fill: #f0f0f0;-fx-background-radius: 20px");
    }
    public void onHoverLogOut(){
        onHover(button_logout);
    }
    public void onHoverDashboard(){
        onHover(Dashboard);
    }
    public void onHoverHistorique(){
        onHover(Historique);
    }

    @FXML
    private void onExit(Button button) {
        button.setStyle("-fx-background-color:  #61ad5c; -fx-background-radius :20px");

    }
    public void onExitLogOut(){
        onExit(button_logout);
    }
    public void onExitDashboard(){
        onExit(Dashboard);
    }
    public void onExitHistorique(){
        onExit(Historique);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void initializeData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void logOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(loader.load()));
        stage2.setTitle("Wallet APP");
        stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        stage2.show();
        //close the current scene
        Stage stage = (Stage) button_logout.getScene().getWindow();
        stage.close();
    }



    public void switchtoAccountInfo() throws IOException {
        FXMLLoader loader = new FXMLLoader(Signupcontrollerpart2.class.getResource("AccountInformation.fxml"));
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(loader.load()));
        stage2.setTitle("Wallet APP");
        stage2.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("Wallet-PNG-Background.png")));
        AccountInformation controller = loader.getController();
        controller.initializeData(username, password,false);//Only the admin can see the button return to Users list
        stage2.show();
        Stage stage = (Stage) button_logout.getScene().getWindow();
        stage.close();

    }
}
