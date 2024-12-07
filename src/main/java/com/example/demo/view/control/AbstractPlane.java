package com.example.demo.view.control;

import com.example.demo.model.PlaneModel;
import javafx.stage.Stage;

/**
 * plane base class
 */
public abstract class AbstractPlane extends AbstractBaseView<PlaneModel> {

    /**
     * window
     */
    public final Stage stage;
    /**
     * shield
     */
    public Shield shield;
    /**
     * Whether to enable shield
     */
    public boolean openShield = false;
    /**
     * Shield activation times
     */
    private int openShieldCount = 0;
    /**
     * Number of frames maintained
     */
    private int currentFrames = 0;
    /**
     * plane data
     */
    public final PlaneModel model;

    public AbstractPlane(PlaneModel model, Stage stage) {
        super(model);
        this.stage = stage;
        this.model = model;
        if (model.shieldModel != null) {
            shield = new Shield(model.shieldModel);
        }
    }


    /**
     * Get the X coordinate of the bullet
     *
     * @param xPositionOffset Offset
     * @return Y coordinate
     */
    protected double getBulletXPosition(double xPositionOffset) {
        return getLayoutX() + getTranslateX() + xPositionOffset;
    }

    /**
     * Get the Y coordinate of the bullet
     *
     * @param yPositionOffset Offset
     * @return X coordinate
     */
    protected double getBulletYPosition(double yPositionOffset) {
        return getLayoutY() + getTranslateY() + yPositionOffset;
    }

    /**
     * Determine whether the enemy plane has left the area
     *
     * @param width Area Width
     * @return true or false
     */
    public boolean isOutOfArea(double width) {
        return Math.abs(this.getTranslateX()) > width;
    }

    public void updateShield() {
        // 更新护盾数据
        if (shield != null) {
            double xPoint = getLayoutX() + getTranslateX();
            double yPoint = getLayoutY() + getTranslateY();
            shield.setPosition(xPoint + (double) model.shieldModel.height / 4, yPoint + (double) model.shieldModel.height / 4);

            if (openShield) {
                currentFrames++;
            } else if (Math.random() < model.shieldModel.openRate) {
                openShield = true;
                openShieldCount++;
                shield.showShield();
            }
            if (currentFrames >= model.shieldModel.maxFrames) {
                openShield = false;
                currentFrames = 0;
                shield.hideShield();
            }
        }
    }

    /**
     * shoot
     */
    public abstract Bullet shoot();

    /**
     * loose once life
     */
    public abstract void looseLife();

}
