package com.example.demo.controller;

import com.example.demo.model.GameModel;
import com.example.demo.view.GameScene;
import com.example.demo.view.control.*;
import javafx.scene.Group;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Game interface controller
 */
public class GameController extends BaseController {

    /**
     * mission 1 game constants
     */
    public static final int MISSION_1 = 1;
    /**
     * mission 2 game constants
     */
    public static final int MISSION_2 = 2;
    /**
     * mission 3 game constants
     */
    public static final int MISSION_3 = 3;
    /**
     * user
     */
    public static final int USER_PLANE = 1;
    /**
     * enemy
     */
    public static final int ENEMY_PLANE = 2;

    private final Stage stage;
    /**
     * Game Data
     */
    private GameModel model;
    /**
     * Bullets fired by user plane
     */
    private final List<Bullet> userPlaneBullets = new ArrayList<>();

    /**
     * enemy plane
     */
    private final List<EnemyPlane> enemyPlanes = new ArrayList<>();
    /**
     * Bullets fired by enemy plane
     */
    private final List<Bullet> enemyPlaneBullets = new ArrayList<>();
    /**
     * Number of enemy plane shot down
     */
    public int shootDownCount = 0;

    public List<Bullet> getUserPlaneBullets() {
        return userPlaneBullets;
    }

    public List<EnemyPlane> getEnemyPlanes() {
        return enemyPlanes;
    }

    public List<Bullet> getEnemyPlaneBullets() {
        return enemyPlaneBullets;
    }

    public GameController(Stage stage) {
        this.stage = stage;
    }

    /**
     * game scene
     */
    private GameScene scene;

    /**
     * Game layout
     */
    private Group root;

    /**
     * Start game interface
     */
    public void startGame() {
        this.scene = new GameScene(stage, this);
        this.root = (Group) scene.getRoot();
        stage.setScene(scene);
        startMission(MISSION_1); // 默认开始第一关
    }

    /**
     * Start mission
     *
     * @param mission mission
     */
    public void startMission(int mission) {
        this.enemyPlanes.clear();
        this.userPlaneBullets.clear();
        this.enemyPlaneBullets.clear();
        this.shootDownCount = 0;
        this.model = getMissionModel(mission);
        if (this.model == null) { // 如果没有下一关游戏数据，默认赢下游戏
            this.scene.winGame();
        } else {
            this.scene.startMission(this.model);
        }
    }

    /**
     * Get mission data
     *
     * @param mission mission
     * @return Return game data
     */
    public GameModel getMissionModel(int mission) {
        switch (mission) {
            case MISSION_1 -> {
                return GameModel.initMissionOneModel();
            }
            case MISSION_2 -> {
                return GameModel.initMissionTwoModel();
            }
            case MISSION_3 -> {
                return GameModel.initMissionThreeModel();
            }
            default -> {
                return null;
            }
        }
    }

    public void checkIfGameOver(UserPlane userPlane, BossPlane bossPlane) {
        if (userPlane.destroyed) {
            this.scene.loseMission();
        }
        if (bossPlane != null) {
            if (bossPlane.destroyed) {
                int mission = this.model.mission + 1;
                startMission(mission);
            }
        } else if (shootDownCount >= this.model.shootDownCount) {
            int mission = this.model.mission + 1;
            startMission(mission);
        }
    }

    public void shootBullet(int planeType, Bullet bullet) {
        if (planeType == USER_PLANE) {
            this.userPlaneBullets.add(bullet);
        } else {
            this.enemyPlaneBullets.add(bullet);
        }
    }

    /**
     * plane-to-plane collision handling
     *
     * @param userPlane user
     */
    public void planeWithPlaneCollisions(UserPlane userPlane, BossPlane bossPlane) {
        for (EnemyPlane enemyPlane : enemyPlanes) {
            if (!userPlane.destroyed && userPlane.getBoundsInParent().intersects(enemyPlane.getBoundsInParent())) {
                userPlane.looseLife();
                enemyPlane.looseLife();
            }
        }
        if (!userPlane.destroyed && bossPlane != null && userPlane.getBoundsInParent().intersects(bossPlane.getBoundsInParent())) {
            userPlane.looseLife();
            bossPlane.looseLife();
        }
    }

    /**
     * user plane bullets and enemy plane collision handling
     */
    public void userBulletWithPlaneCollisions() {
        for (AbstractPlane plane : enemyPlanes) {
            if (plane.destroyed) continue;
            for (Bullet bullet : userPlaneBullets) {
                if (!bullet.destroyed && bullet.getBoundsInParent().intersects(plane.getBoundsInParent())) {
                    bullet.destroy(); // 子弹销毁
                    plane.looseLife();
                    if (plane.destroyed) {
                        this.shootDownCount++;
                    }
                }
            }
        }
    }

    /**
     * user plane bullets and enemy plane collision handling
     */
    public void userBulletWithPlaneCollisions(BossPlane bossPlane) {
        if (bossPlane == null) {
            return;
        }
        for (Bullet bullet : userPlaneBullets) {
            if (!bullet.destroyed && bullet.getBoundsInParent().intersects(bossPlane.getBoundsInParent())) {
                bullet.destroy(); // 子弹销毁
                bossPlane.looseLife();
            }
        }
    }

    /**
     * user plane bullets and enemy plane collision handling
     *
     * @param userPlane user
     */
    public void enemyBulletWithPlaneCollisions(UserPlane userPlane) {
        for (Bullet bullet : enemyPlaneBullets) {
            if (!bullet.destroyed && bullet.getBoundsInParent().intersects(userPlane.getBoundsInParent())) {
                bullet.destroy(); // 子弹销毁
                userPlane.looseLife();
            }
        }
    }

    /**
     * Updated plane bullet view
     */
    public void updatePlaneBullet() {
        updatePlaneBullet(userPlaneBullets);
        updatePlaneBullet(enemyPlaneBullets);
    }

    /**
     * Updated plane bullet view
     */
    private void updatePlaneBullet(List<Bullet> bullets) {
        for (var iterator = bullets.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            bullet.updatePosition();
            bullet.destroy(stage.getWidth());
            if (bullet.destroyed) {
                this.root.getChildren().remove(bullet);
                iterator.remove();
            }
        }
    }
}
