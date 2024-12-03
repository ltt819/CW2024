package com.example.demo;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;

    private final Boss boss;
    private int enemySpawnCounter = 0;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);

        // 初始化 Boss
        this.boss = new Boss(getRoot());
        this.boss.setLayoutX(1000); // 设置 Boss 的初始位置
        this.boss.setLayoutY(400);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame(); // 玩家失败
        } else if (boss.isDestroyed()) {
            // 通关后通知切换到下一关卡（如无下一关则显示通关）
            winGame();
        }
    }

    @Override
    protected void spawnEnemyUnits() {
        // 确保 Boss 已经添加到场景中
        if (!getRoot().getChildren().contains(boss)) {
            addEnemyUnit(boss); // 将 Boss 添加到场景中
        }

        // 控制敌机生成频率
        if (enemySpawnCounter % 100 == 0) {  // 每隔一定数量的敌机生成周期
            double spawnXPosition = getScreenWidth();  // 从屏幕最右侧生成
            double spawnYPosition = Math.random() * (getScreenHeight() - 150);  // 随机 Y 位置
            EnemyPlane enemyPlane = new EnemyPlane(spawnXPosition, spawnYPosition);
            addEnemyUnit(enemyPlane);  // 添加敌机到场景中
        }

        // 增加敌机生成计数器
        enemySpawnCounter++;
    }

    @Override
    protected LevelView instantiateLevelView() {
        return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH);
    }

}
