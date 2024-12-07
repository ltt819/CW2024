package com.example.demo.view.control;

import com.example.demo.model.BulletModel;
import com.example.demo.model.PlaneModel;
import javafx.stage.Stage;

/**
 * enemy plane
 */
public class EnemyPlane extends AbstractPlane {
    private static final double BULLET_X_POSITION_OFFSET = -100.0;
    private static final double BULLET_Y_POSITION_OFFSET = 50.0;

    public EnemyPlane(PlaneModel model, Stage stage) {
        super(model, stage);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(this.model.horizontallyVelocity);
    }

    @Override
    public Bullet shoot() {
        BulletModel bulletModel = this.model.bulletModel;
        if (Math.random() < bulletModel.fireRate) {
            double xPosition = getBulletXPosition(BULLET_X_POSITION_OFFSET);
            double yPosition = getBulletYPosition(BULLET_Y_POSITION_OFFSET);
            bulletModel.xPosition = xPosition;
            bulletModel.yPosition = yPosition;
            return new Bullet(bulletModel);
        }
        return null;
    }

    @Override
    public void looseLife() {
        // 如果开启了护盾，不掉血
        if (this.openShield) return;
        this.model.blood -= 1;
        if (this.model.blood <= 0) {
            destroy();
        }
    }
}
