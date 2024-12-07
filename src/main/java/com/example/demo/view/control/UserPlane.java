package com.example.demo.view.control;

import com.example.demo.model.BulletModel;
import com.example.demo.model.PlaneModel;
import javafx.stage.Stage;

/**
 * user plane
 */
public class UserPlane extends AbstractPlane {

    /**
     * Left boundary value
     */
    private static final double X_LEFT_BOUND = 5;
    private static final double BULLET_X_POSITION_OFFSET = 110.0;
    private static final int BULLET_Y_POSITION_OFFSET = 20;
    /**
     * Health value display position
     */
    private static final double LIFE_DISPLAY_X_POSITION = 5;
    /**
     * Health value display position
     */
    private static final double LIFE_DISPLAY_Y_POSITION = 25;
    private int verticalVelocity = 0;
    private int horizontallyVelocity = 0;

    /**
     * plane health
     */
    private final UserBlood userBlood;

    public UserBlood getUserBlood() {
        return userBlood;
    }

    /**
     * Constructor
     *
     * @param model plane data
     */
    public UserPlane(PlaneModel model, Stage stage) {
        super(model, stage);
        this.userBlood = new UserBlood(LIFE_DISPLAY_X_POSITION, LIFE_DISPLAY_Y_POSITION, model.blood);
    }

    @Override
    public void updatePosition() {
        double initialTranslateX = getTranslateX();
        this.moveHorizontally(this.model.horizontallyVelocity * horizontallyVelocity);
        double newXPosition = getLayoutX() + getTranslateX();
        if (newXPosition < X_LEFT_BOUND || newXPosition > this.stage.getWidth() / 3) {
            this.setTranslateX(initialTranslateX);
        }

        double initialTranslateY = getTranslateY();
        this.moveVertically(this.model.verticalVelocity * verticalVelocity);
        double newYPosition = getLayoutY() + getTranslateY();
        if (newYPosition < (double) -this.model.height / 3 || newYPosition > this.stage.getHeight() - this.model.height) {
            this.setTranslateY(initialTranslateY);
        }
        this.updateShield();
    }

    @Override
    public Bullet shoot() {
        BulletModel bulletModel = this.model.bulletModel;
        bulletModel.xPosition = getBulletXPosition(BULLET_X_POSITION_OFFSET);
        bulletModel.yPosition = getBulletYPosition(BULLET_Y_POSITION_OFFSET);
        return new Bullet(bulletModel);
    }

    @Override
    public void looseLife() {
        // 如果开启了护盾，不掉血
        if (this.openShield) return;
        this.model.blood -= 1;
        this.userBlood.bleeding();
        if (this.model.blood <= 0) {
            this.destroy();
        }
    }

    /**
     * Move up
     */
    public void moveUp() {
        verticalVelocity = -1;
    }

    /**
     * Move down
     */
    public void moveDown() {
        verticalVelocity = 1;
    }

    /**
     * Move Left
     */
    public void moveLeft() {
        horizontallyVelocity = -1;
    }

    /**
     * Move right
     */
    public void moveRight() {
        horizontallyVelocity = 1;
    }

    /**
     * stop move
     */
    public void stopMove() {
        horizontallyVelocity = 0;
        verticalVelocity = 0;
    }

}
