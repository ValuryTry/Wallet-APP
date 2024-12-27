package com.ensam.demo;

import javafx.animation.TranslateTransition;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class AnimationHelper {

    public static void addTextFieldAnimation(TextField textField) {
        // Add focus listener to the TextField
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // On focus gain
                TranslateTransition transition = new TranslateTransition(Duration.millis(300), textField);
                transition.setByY(-5); // Move the field upward
                transition.play();
            } else { // On focus loss
                TranslateTransition transition = new TranslateTransition(Duration.millis(300), textField);
                transition.setByY(5); // Move the field downward
                transition.play();
            }
        });
    }
}
