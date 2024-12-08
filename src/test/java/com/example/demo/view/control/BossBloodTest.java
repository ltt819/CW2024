package com.example.demo.view.control;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class BossBloodTest extends ApplicationTest {

    private BossBlood bossBlood;

    @Override
    public void start(Stage stage) {
        bossBlood = new BossBlood(200, 100, 100, 10);  // 假设BOSS血量为10
        stage.setScene(new Scene(bossBlood));
        stage.show();
    }

    @Test
    void testBleeding() {
        // 在JavaFX线程中执行
        Platform.runLater(() -> {
            bossBlood.bleeding(7);
            assertEquals(0.7, bossBlood.getProgress(), 0.01, "Boss blood should be 70% after losing 3 health.");
        });
    }

    @Test
    void testBleedingLow() {
        // 在JavaFX线程中执行
        Platform.runLater(() -> {
            bossBlood.bleeding(2);
            assertEquals(0.2, bossBlood.getProgress(), 0.01, "Boss blood should be 20% after losing 8 health.");
        });
    }

    @Test
    void testFullyDepletedBlood() {
        // 在JavaFX线程中执行
        Platform.runLater(() -> {
            bossBlood.bleeding(0);
            assertEquals(0.0, bossBlood.getProgress(), 0.01, "Boss blood should be 0% when health is fully depleted.");
        });
    }
}
