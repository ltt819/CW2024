package com.example.demo.view.control;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class UserBloodTest extends ApplicationTest {

    private UserBlood userBlood;

    @Override
    public void start(Stage stage) {
        userBlood = new UserBlood(50, 50, 3);  // 假设玩家血量为3
        stage.setScene(new Scene(userBlood));
        stage.show();
    }

    @Test
    void testInitializeLife() {
        // 在JavaFX线程中执行
        Platform.runLater(() -> {
            assertEquals(3, userBlood.getChildren().size(), "UserBlood should have 3 hearts initially.");
        });
    }

    @Test
    void testBleeding() {
        // 确保代码在JavaFX应用线程中执行
        Platform.runLater(() -> {
            userBlood.bleeding();
            assertEquals(2, userBlood.getChildren().size(), "One heart should be removed after bleeding.");
        });
    }
}