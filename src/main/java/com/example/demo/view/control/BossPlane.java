package com.example.demo.view.control;

import com.example.demo.model.BulletModel;
import com.example.demo.model.PlaneModel;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class BossPlane extends AbstractPlane {
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final double BULLET_X_POSITION_OFFSET = -100.0;
    private static final double BULLET_Y_POSITION_OFFSET = 75.0;
    private static final int Y_POSITION_UPPER_BOUND = -100;
    private static final int Y_POSITION_LOWER_BOUND = 475;
    /**
     * Blood bar length
     */
    private static final double BLOOD_WIDTH = 200;
    /**
     * Blood bar PADDING value
     */
    private static final double BLOOD_DISPLAY_PADDING = 20;

    private int xPointIndex;
    private final List<Integer> xPositions;
    private int xConsecutiveMovesInSameDirection = 0;
    private int yPointIndex;
    private final List<Integer> yPositions;
    private int yConsecutiveMovesInSameDirection = 0;
    /**
     * plane health
     */
    private final BossBlood bossBlood;

    public BossBlood getBossBlood() {
        return bossBlood;
    }

    public BossPlane(PlaneModel model, Stage stage) {
        super(model, stage);
        this.bossBlood = new BossBlood(BLOOD_WIDTH, (stage.getWidth() - BLOOD_WIDTH - BLOOD_DISPLAY_PADDING), BLOOD_DISPLAY_PADDING, model.blood);
        this.xPositions = model.xPositions;
        if (this.xPositions != null) {
            Collections.shuffle(this.xPositions);
        }
        this.yPositions = model.yPositions;
        if (this.yPositions != null) {
            Collections.shuffle(this.yPositions);
        }
    }

    @Override
    public void updatePosition() {
        double initialTranslateX = getTranslateX();
        moveHorizontally(nextRandomXPosition());
        double xPosition = getLayoutX() + getTranslateX();
        if (xPosition < this.stage.getWidth() * 0.6 || xPosition > this.stage.getWidth() - this.model.width) {
            setTranslateX(initialTranslateX);
        }

        double initialTranslateY = getTranslateY();
        moveVertically(nextRandomYPosition());
        double yPosition = getLayoutY() + getTranslateY();
        if (yPosition < Y_POSITION_UPPER_BOUND || yPosition > Y_POSITION_LOWER_BOUND) {
            setTranslateY(initialTranslateY);
        }
        updateShield();
    }

    /**
     * Randomly get the next X coordinate
     *
     * @return Integer Next position coordinates
     */
    private int nextRandomXPosition() {
        if (xPositions == null || xPositions.isEmpty()) {
            return 0;
        }
        int position = xPositions.get(xPointIndex);
        xConsecutiveMovesInSameDirection++;
        if (xConsecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(xPositions);
            xConsecutiveMovesInSameDirection = 0;
            xPointIndex++;
        }
        if (xPointIndex == xPositions.size()) {
            xPointIndex = 0;
        }
        return position;
    }

    /**
     * Randomly get the next X coordinate
     *
     * @return Integer Next position coordinates
     */
    private int nextRandomYPosition() {
        if (yPositions == null || yPositions.isEmpty()) {
            return 0;
        }
        int position = yPositions.get(yPointIndex);
        yConsecutiveMovesInSameDirection++;
        if (yConsecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
            Collections.shuffle(yPositions);
            yConsecutiveMovesInSameDirection = 0;
            yPointIndex++;
        }
        if (yPointIndex == yPositions.size()) {
            yPointIndex = 0;
        }
        return position;
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
        this.bossBlood.bleeding(this.model.blood);
        if (this.model.blood <= 0) {
            this.destroy();
        }
    }
}