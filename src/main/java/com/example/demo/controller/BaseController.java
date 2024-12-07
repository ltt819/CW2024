package com.example.demo.controller;

import javafx.scene.control.Alert;

import java.lang.reflect.InvocationTargetException;

public class BaseController {

    public void handleException(Exception e) {
        if (e instanceof InvocationTargetException) {
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
                System.out.println("Cause of InvocationTargetException: " + cause.getMessage());
            }
        } else {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getClass().toString());
        alert.show();
    }
}
