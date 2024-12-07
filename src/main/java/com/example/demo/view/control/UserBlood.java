package com.example.demo.view.control;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

/**
 * Plane health
 */
public class UserBlood extends HBox {

    private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart.png";
    private static final int HEART_HEIGHT = 50;
    private static final int INDEX_OF_FIRST_ITEM = 0;
    private final int blood;

    public UserBlood(double xPosition, double yPosition, int blood) {
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
        this.blood = blood;
        initializeLife();
    }

    private void initializeLife() {
        for (int i = 0; i < blood; i++) {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(HEART_IMAGE_NAME)).toExternalForm()));
            imageView.setFitHeight(HEART_HEIGHT);
            imageView.setPreserveRatio(true);
            this.getChildren().add(imageView);
        }
    }

    /**
     * bleed
     */
    public void bleeding() {
        if (!this.getChildren().isEmpty())
            this.getChildren().remove(INDEX_OF_FIRST_ITEM);
    }
}
