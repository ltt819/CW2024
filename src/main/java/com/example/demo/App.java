package com.example.demo;

import com.example.demo.view.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;
    private static final String TITLE = "Sky Battle";

    @Override
    public void start(Stage stage) {
        stage.setTitle(TITLE);
        stage.setHeight(SCREEN_HEIGHT);
        stage.setWidth(SCREEN_WIDTH);
        stage.setResizable(false);
        stage.setScene(new MainScene(stage));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}