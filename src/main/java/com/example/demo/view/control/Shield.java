package com.example.demo.view.control;

import com.example.demo.model.ShieldModel;
import javafx.application.Platform;

/**
 * Boss shield
 */
public class Shield extends AbstractBaseView<ShieldModel> {

    public Shield(ShieldModel model) {
        super(model);
        initializeShield();
    }

    /**
     * Other configuration initialization
     */
    private void initializeShield() {
        this.setFitHeight(model.height);
        this.setFitWidth(model.width);
        this.setOpacity(1.0);
        this.setPreserveRatio(true);
        this.setVisible(false);
    }

    @Override
    public void updatePosition() {
    }

    /**
     * Setting coordinates
     *
     * @param xPosition X coordinate
     * @param yPosition Y coordinate
     */
    public void setPosition(double xPosition, double yPosition) {
        Platform.runLater(() -> {
            this.setLayoutX(xPosition);
            this.setLayoutY(yPosition);
        });
    }

    /**
     * Show Shield
     */
    public void showShield() {
        Platform.runLater(() -> {
            this.setVisible(true);
            this.toFront();
        });
    }

    /**
     * Hide Shield
     */
    public void hideShield() {
        Platform.runLater(() -> {
            this.setVisible(false);
        });
    }
}
