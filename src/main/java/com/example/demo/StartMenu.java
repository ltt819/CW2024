package com.example.demo;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import com.example.demo.controller.Controller;

public class StartMenu {

    private static final String BACKGROUND_IMAGE_PATH = "/com/example/demo/images/background1.jpg";
    private static final String TITLE = "Sky Battle";
    private static final String FONT_FAMILY = "Arial";

    private final Group root;
    private final Scene scene;
    private final Stage stage;

    private int selectedOption = 0; // 0 for "Start Game", 1 for "Exit"
    private final Label startGameLabel;
    private final Label exitLabel;

    public StartMenu(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.scene = new Scene(root, 1300, 750); // 固定屏幕宽高

        // 初始化背景
        ImageView background = initializeBackground();

        // 初始化标题
        Label titleLabel = initializeTitleLabel();

        // 初始化选项
        startGameLabel = initializeMenuOption("Start Game", 350);
        exitLabel = initializeMenuOption("Exit", 450);

        // 设置默认选中效果
        updateSelection();

        // 添加按键事件
        addKeyListeners();

        // 将元素添加到根节点
        root.getChildren().addAll(background, titleLabel, startGameLabel, exitLabel);
    }

    private ImageView initializeBackground() {
        ImageView background = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE_PATH).toExternalForm()));
        background.setFitWidth(1300);
        background.setFitHeight(750);
        return background;
    }

    private Label initializeTitleLabel() {
        Label titleLabel = new Label(TITLE);
        titleLabel.setFont(new Font(FONT_FAMILY, 80));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setLayoutX(500); // 居中标题
        titleLabel.setLayoutY(150);
        return titleLabel;
    }

    private Label initializeMenuOption(String text, int yPosition) {
        Label label = new Label(text);
        label.setFont(new Font(FONT_FAMILY, 40));
        label.setTextFill(Color.WHITE);
        label.setLayoutX(550); // 菜单选项居中
        label.setLayoutY(yPosition);
        return label;
    }

    private void addKeyListeners() {
        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();

            switch (key) {
                case W:
                    selectedOption = Math.max(0, selectedOption - 1);
                    break;
                case S:
                    selectedOption = Math.min(1, selectedOption + 1);
                    break;
                case ENTER:
                    handleSelection();
                    break;
                default:
                    break;
            }

            // 更新选中效果
            updateSelection();
        });
    }

    private void updateSelection() {
        // 根据当前选项更新颜色
        startGameLabel.setTextFill(selectedOption == 0 ? Color.YELLOW : Color.WHITE);
        exitLabel.setTextFill(selectedOption == 1 ? Color.YELLOW : Color.WHITE);
    }

    private void handleSelection() {
        if (selectedOption == 0) {
            // 启动游戏
            launchGame();
        } else if (selectedOption == 1) {
            // 退出游戏
            Platform.exit();
        }
    }

    private void launchGame() {
        try {
            Controller controller = new Controller(stage);
            controller.launchGame(); // 假设 Controller 类有启动游戏的方法
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }
}
