package com.example.demo.view;

import com.example.demo.controller.GameController;
import com.example.demo.model.GameModel;
import com.example.demo.model.PlaneModel;
import com.example.demo.view.control.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Game interface
 */
public class GameScene extends Scene {
    private static final String GAME_OVER_IMAGE_LOCATION = "/com/example/demo/images/gameover.png";
    private static final int GAME_OVER_IMAGE_X_POSITION = 350;
    private static final int GAME_OVER_IMAGE_Y_POSITION = -50;
    private static final int GAME_OVER_IMAGE_WIDTH = 600;
    private static final int GAME_OVER_IMAGE_HEIGHT = 600;
    private static final String GAME_WIN_IMAGE_LOCATION = "/com/example/demo/images/youwin.png";
    private static final int GAME_WIN_IMAGE_X_POSITION = 355;
    private static final int GAME_WIN_IMAGE_Y_POSITION = 125;
    private static final int GAME_WIN_IMAGE_WIDTH = 600;
    private static final int GAME_WIN_IMAGE_HEIGHT = 500;
    private static final int TITLE_FONT_SIZE = 30; // 字体大小
    private static final int MILLISECOND_DELAY = 50;
    private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
    /**
     * window
     */
    private final Stage stage;
    /**
     * Game controllers
     */
    private final GameController gameController;
    private GameModel model;
    /**
     * user plane
     */
    private UserPlane userPlane;
    /**
     * Boss plane
     */
    private BossPlane bossPlane;
    private final Group root;
    private final Timeline timeline;
    private Label pauseLabel1;
    private Label pauseLabel2;
    private Label gameOverLabel;

    /**
     * Pause Game
     */
    private boolean pause;
    /**
     * Whether to return to the main menu
     */
    private boolean back;

    /**
     * Constructor
     *
     * @param stage window
     */
    public GameScene(Stage stage, GameController controller) {
        super(new Group(), stage.getWidth(), stage.getHeight());
        this.stage = stage;
        this.root = (Group) this.getRoot();
        this.gameController = controller;

        this.timeline = new Timeline();
        initializeTimeline();
        initializeGamePauseTips();
        initializeGameOverTips();
        initializeKeyListeners();
    }

    private void initializeTimeline() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateView());
        timeline.getKeyFrames().add(frame);
    }

    private void initializeGamePauseTips() {
        pauseLabel1 = new Label("The game is paused");
        pauseLabel1.setFont(new Font("Arial", 40));
        pauseLabel1.setStyle("-fx-text-fill: white;");
        pauseLabel1.setTextAlignment(TextAlignment.CENTER);
        pauseLabel1.setLayoutX((stage.getWidth() - 400) / 2);  // 水平居中
        pauseLabel1.setLayoutY(stage.getHeight() / 2 - 50);  // 位于屏幕中央

        pauseLabel2 = new Label("Press ESC to return to the game");
        pauseLabel2.setFont(new Font("Arial", 20));
        pauseLabel2.setStyle("-fx-text-fill: white;");
        pauseLabel2.setTextAlignment(TextAlignment.CENTER);
        pauseLabel2.setLayoutX((stage.getWidth() - 320) / 2);  // 水平居中
        pauseLabel2.setLayoutY(stage.getHeight() / 2 + 20);  // 位于第一行下方
    }

    private void initializeGameOverTips() {
        gameOverLabel = new Label("Press R to return to the start menu");
        gameOverLabel.setFont(new Font("Arial", 20));
        gameOverLabel.setStyle("-fx-text-fill: red;");
        gameOverLabel.setTextAlignment(TextAlignment.CENTER);
        gameOverLabel.setLayoutX((stage.getWidth() - 300) / 2); // 水平居中
        gameOverLabel.setLayoutY(stage.getHeight() - 150); // 位于屏幕底部
    }

    /**
     * Event Listening
     */
    private void initializeKeyListeners() {
        this.setOnKeyPressed(event -> {
            // 如果游戏暂停，其他按键无效
            if (this.pause || userPlane == null) {
                return;
            }
            if (event.getCode() == KeyCode.UP) userPlane.moveUp();
            if (event.getCode() == KeyCode.DOWN) userPlane.moveDown();
            if (event.getCode() == KeyCode.LEFT) userPlane.moveLeft();
            if (event.getCode() == KeyCode.RIGHT) userPlane.moveRight();
            if (event.getCode() == KeyCode.SPACE) {
                Bullet bullet = userPlane.shoot();
                gameController.shootBullet(GameController.USER_PLANE, bullet);
                this.root.getChildren().add(bullet);
            }
            // 当游戏失败，按键R返回主菜单
            if (back && event.getCode() == KeyCode.R) {
                stage.setScene(new MainScene(stage));
            }
        });

        this.setOnKeyReleased(event -> {
            if (userPlane == null) {
                return;
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                this.pause = !this.pause;
                if (this.pause) {
                    this.root.getChildren().addAll(pauseLabel1, pauseLabel2);
                    this.timeline.pause();
                } else {
                    this.root.getChildren().removeAll(pauseLabel1, pauseLabel2);
                    this.timeline.play();
                }
            }
            // 如果游戏暂停，其他按键无效
            if (this.pause) {
                return;
            }
            if (event.getCode() == KeyCode.UP
                    || event.getCode() == KeyCode.DOWN
                    || event.getCode() == KeyCode.LEFT
                    || event.getCode() == KeyCode.RIGHT)
                userPlane.stopMove();
        });
    }

    /**
     * start mission
     *
     * @param model mission Data
     */
    public void startMission(GameModel model) {
        this.model = model;
        this.pause = false;
        this.root.getChildren().clear();
        // 初始化背景
        initializeBackground(this.model.background);
        // 初始化标题
        initializeMissionTitle("Mission " + this.model.mission);
        // 初始化飞机
        userPlane = new UserPlane(this.model.userPlaneModel, this.stage);
        this.root.getChildren().addAll(userPlane.getUserBlood(), userPlane);
        // 初始化 Boss
        if (this.model.bossPlaneModel != null) {
            PlaneModel planeModel = this.model.bossPlaneModel;
            bossPlane = new BossPlane(planeModel, this.stage);
            this.root.getChildren().addAll(bossPlane.getBossBlood(), bossPlane);
            Shield shield = bossPlane.shield;
            if (shield != null) {
                root.getChildren().add(shield);
            }
        } else {
            bossPlane = null;
        }
        this.timeline.play();
    }

    /**
     * Lose the mission
     */
    public void loseMission() {
        this.timeline.stop();
        initializeImageView(GAME_OVER_IMAGE_LOCATION, GAME_OVER_IMAGE_X_POSITION, GAME_OVER_IMAGE_Y_POSITION, GAME_OVER_IMAGE_WIDTH, GAME_OVER_IMAGE_HEIGHT);
        root.getChildren().add(gameOverLabel);
        this.back = true;
    }

    /**
     * win game
     */
    public void winGame() {
        timeline.stop();
        initializeImageView(GAME_WIN_IMAGE_LOCATION, GAME_WIN_IMAGE_X_POSITION, GAME_WIN_IMAGE_Y_POSITION, GAME_WIN_IMAGE_WIDTH, GAME_WIN_IMAGE_HEIGHT);
        root.getChildren().add(gameOverLabel);
        this.back = true;
    }

    /**
     * Initialize the mission background
     *
     * @param image Background image path
     */
    private void initializeBackground(String image) {
        ImageView background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(image)).toExternalForm()));
        background.setFocusTraversable(true);
        background.setFitWidth(stage.getWidth());
        background.setFitHeight(stage.getHeight());
        this.root.getChildren().add(background);
    }

    /**
     * initialize Image
     *
     * @param image image
     * @param xPoint x-coordinate
     * @param yPoint y-coordinate
     * @param width width
     * @param height height
     */
    private void initializeImageView(String image, int xPoint, int yPoint, double width, double height) {
        ImageView background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(image)).toExternalForm()));
        background.setFocusTraversable(true);
        background.setFitWidth(width);
        background.setFitHeight(height);
        background.setLayoutX(xPoint);
        background.setLayoutY(yPoint);
        this.root.getChildren().add(background);
    }

    /**
     * Initialize the level name
     *
     * @param name name
     */
    private void initializeMissionTitle(String name) {
        Label label = new Label(name);
        label.setFont(new Font("Arial", TITLE_FONT_SIZE)); // 设置字体和大小
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-text-fill: white;"); // 设置文字颜色
        label.setLayoutX((stage.getWidth() - label.prefWidth(-1)) / 2); // 水平居中
        label.setLayoutY(20); // 设置距离顶部的偏移
        root.getChildren().add(label); // 添加到根节点
    }

    /**
     * update view
     */
    public void updateView() {
        if (this.model == null) return;
        createEnemyPlane(); // 创建敌方飞机
        gameController.userBulletWithPlaneCollisions();
        gameController.userBulletWithPlaneCollisions(bossPlane);
        gameController.enemyBulletWithPlaneCollisions(userPlane);
        gameController.planeWithPlaneCollisions(userPlane, bossPlane);
        gameController.updatePlaneBullet();
        updatePlane(); // 更新飞机视图
        gameController.checkIfGameOver(userPlane, bossPlane);
    }

    /**
     * Randomly create enemy plane
     */
    protected void createEnemyPlane() {
        if (this.model.enemyPlaneModel == null) return;
        for (int i = 0; i < this.model.totalEnemies - gameController.getEnemyPlanes().size(); i++) {
            if (Math.random() < this.model.enemyCreateRate) {
                PlaneModel model = this.model.enemyPlaneModel;
                model.xPosition = stage.getWidth();
                model.yPosition = Math.random() * (stage.getHeight() - SCREEN_HEIGHT_ADJUSTMENT);
                EnemyPlane plane = new EnemyPlane(model, stage);
                gameController.getEnemyPlanes().add(plane);
                Shield shield = plane.shield;
                if (shield != null) {
                    root.getChildren().add(shield);
                }
                root.getChildren().add(plane);
                Bullet bullet = plane.shoot();
                if (bullet != null) {
                    this.root.getChildren().add(bullet);
                }
            }
        }
    }

    /**
     * update Plane view
     */
    protected void updatePlane() {
        userPlane.updatePosition();
        if (bossPlane != null) {
            bossPlane.updatePosition();
            Bullet bullet = bossPlane.shoot();
            if (bullet != null) {
                gameController.getEnemyPlaneBullets().add(bullet);
                this.root.getChildren().add(bullet);
            }
        }
        for (var iterator = gameController.getEnemyPlanes().iterator(); iterator.hasNext(); ) {
            EnemyPlane plane = iterator.next();
            plane.updatePosition();
            if (plane.isOutOfArea(stage.getWidth())) {
                userPlane.looseLife();
            } else {
                Bullet bullet = plane.shoot();
                if (bullet != null) {
                    gameController.getEnemyPlaneBullets().add(bullet);
                    this.root.getChildren().add(bullet);
                }
            }
            if (plane.destroyed) {
                root.getChildren().remove(plane);
                iterator.remove();
            }
        }
    }
}
