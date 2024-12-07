package com.example.demo.controller;

import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Functional interface event handler
 * start game
 * end game
 */
public class MainController extends BaseController {

    /**
     * window
     */
    private final Stage stage;

    private final GameController gameController;

    /**
     * constructor
     *
     * @param stage window
     */
    public MainController(Stage stage) {
        this.stage = stage;
        this.gameController = new GameController(this.stage);
    }

    /**
     * Execute menu event
     *
     * @param selectedMenu Selected menu (0-start game; 1-end game)
     */
    public void executeMenuEvent(int selectedMenu) {
        switch (selectedMenu) {
            case 0:
                gameController.startGame();
                break;
            case 1:
                Platform.exit();
                break;
            default:
                break;
        }
    }
}
