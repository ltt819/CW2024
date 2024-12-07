package com.example.demo.model;

import java.util.Arrays;

/**
 * game mission data
 */
public class GameModel {
    /**
     * mission
     */
    public int mission;

    /**
     * Game mission background path
     */
    public String background;

    /**
     * Conditions for winning the game
     */
    public int shootDownCount;

    /**
     * plane data
     */
    public PlaneModel userPlaneModel;

    /**
     * enemy boss plane data
     */
    public PlaneModel bossPlaneModel;

    /**
     * Total number of enemy plane
     */
    public int totalEnemies;
    /**
     * Probability of enemy plane appearing
     */
    public double enemyCreateRate;
    /**
     * enemy plane data
     */
    public PlaneModel enemyPlaneModel;

    /**
     * Configuration only small enemy plane
     *
     * @return Data model for mission 1
     */
    public static GameModel initMissionOneModel() {
        GameModel model = new GameModel();
        model.mission = 1;
        model.background = "/com/example/demo/images/background1.jpg";
        model.totalEnemies = 5; // 出现的小飞机处理
        model.shootDownCount = 10; // 击落的飞机数量，用来批判的输赢
        model.enemyCreateRate = .10; // 随机创建飞机的概率
        // 友机数据
        model.userPlaneModel = buildUserPlaneModel();
        // 敌机数据
        model.enemyPlaneModel = buildEnemyPlaneModel();
        return model;
    }

    /**
     * Configuration only Boss enemy plane
     *
     * @return Data model for mission 2
     */
    public static GameModel initMissionTwoModel() {
        GameModel model = new GameModel();
        model.mission = 2;
        model.background = "/com/example/demo/images/background2.jpg";
        model.totalEnemies = 0; // 出现的小飞机处理
        model.shootDownCount = 10; // 击落的飞机数量，用来批判的输赢
        model.enemyCreateRate = .20; // 随机创建飞机的概率
        // 友机数据
        model.userPlaneModel = buildUserPlaneModel();
        // 敌机数据
        model.bossPlaneModel = buildBossPlaneModel();
        return model;
    }


    public static GameModel initMissionThreeModel() {
        GameModel model = new GameModel();
        model.mission = 3;
        model.background = "/com/example/demo/images/background1.jpg";
        model.totalEnemies = 2; // 出现的小飞机处理
        model.shootDownCount = 1; // 击落的飞机数量，用来批判的输赢
        model.enemyCreateRate = .10; // 随机创建飞机的概率
        // 友机数据
        model.userPlaneModel = buildUserPlaneModel();
        // 敌机数据
        model.bossPlaneModel = buildBossPlaneModel();
        model.enemyPlaneModel = buildEnemyPlaneModel();
        return model;
    }

    private static PlaneModel buildUserPlaneModel() {
        PlaneModel model = new PlaneModel();
        model.location = "/com/example/demo/images/userplane.png";
        model.height = 150;
        model.xPosition = 5.0;
        model.yPosition = 300.0;
        model.horizontallyVelocity = 0;
        model.verticalVelocity = 8;
        model.blood = 5; // 血槽

        BulletModel bulletModel = new BulletModel();
        bulletModel.location = "/com/example/demo/images/userfire.png";
        bulletModel.height = 125;
        bulletModel.xPosition = 110.0;
        bulletModel.yPosition = 0.0;
        bulletModel.verticalVelocity = 0;
        bulletModel.horizontallyVelocity = 15;
        model.bulletModel = bulletModel;

        return model;
    }

    private static PlaneModel buildBossPlaneModel() {
        PlaneModel model = new PlaneModel();
        model.location = "/com/example/demo/images/bossplane.png";
        model.width = 360;
        model.height = 300;
        model.xPosition = 1000.0;
        model.yPosition = 400.0;
        model.yPositions = Arrays.asList(-8, 0, 8, -8, 0, 8, -8, 0, 8, -8, 0, 8, -8, 0, 8);
        model.blood = 100;

        BulletModel bulletModel = new BulletModel();
        bulletModel.location = "/com/example/demo/images/fireball.png";
        bulletModel.height = 75;
        bulletModel.xPosition = 0.0;
        bulletModel.yPosition = 0.0;
        bulletModel.horizontallyVelocity = -15;
        bulletModel.fireRate = .04;
        model.bulletModel = bulletModel;

        ShieldModel shieldModel = new ShieldModel();
        shieldModel.location = "/com/example/demo/images/shield.png";
        shieldModel.height = 150;
        shieldModel.openRate = .001;
        shieldModel.maxShield = 1;
        shieldModel.maxFrames = 500;
        model.shieldModel = shieldModel;

        return model;
    }

    private static PlaneModel buildEnemyPlaneModel() {
        PlaneModel model = new PlaneModel();
        model.location = "/com/example/demo/images/enemyplane.png";
        model.height = 150;
        model.xPosition = 0.0;
        model.yPosition = 0.0;
        model.horizontallyVelocity = -6;
        model.blood = 1;

        BulletModel bulletModel = new BulletModel();
        bulletModel.location = "/com/example/demo/images/enemyFire.png";
        bulletModel.height = 50;
        bulletModel.xPosition = 0.0;
        bulletModel.yPosition = 0.0;
        bulletModel.horizontallyVelocity = -10;
        bulletModel.fireRate = .01;
        model.bulletModel = bulletModel;

        return model;
    }
}
