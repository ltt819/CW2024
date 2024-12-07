package com.example.demo.view;

import com.example.demo.controller.MainController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main Scene
 */
public class MainScene extends Scene {

    private static final String BACKGROUND_IMAGE_PATH = "/com/example/demo/images/background1.jpg";
    private static final String FONT_FAMILY = "Arial";

    /**
     * window
     */
    private final Stage stage;
    /**
     * Main Scene controller
     */
    private final MainController mainController;
    /**
     * start game
     */
    private Label startGameLabel;
    /**
     * exit game
     */
    private Label exitGameLabel;
    /**
     * selected menu
     */
    private int selectedMenu = 0; // 0 for "Start Game", 1 for "Exit"

    /**
     * Constructor
     *
     * @param stage window
     */
    public MainScene(Stage stage) {
        super(new Group(), stage.getWidth(), stage.getHeight());
        this.stage = stage;
        this.mainController = new MainController(stage);
        // 初始化背景
        ImageView background = initializeBackground();
        // 初始化标题
        Label title = initializeTitleLabel();
        // 初始化选项
        startGameLabel = initializeMenuOption("Start Game", 350);
        exitGameLabel = initializeMenuOption("Exit Game", 450);
        // 设置默认选中效果
        updateSelectedMenu();
        // 添加组件
        ((Group) this.getRoot()).getChildren().addAll(background, title, startGameLabel, exitGameLabel);
        // 添加按键事件
        addKeyListeners();
    }

    /**
     * initialize Background
     */
    private ImageView initializeBackground() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE_PATH)).toExternalForm()));
        imageView.setFitWidth(this.stage.getWidth());
        imageView.setFitHeight(this.stage.getHeight());
        return imageView;
    }

    /**
     * initialize TitleLabel
     *
     * @return Label
     */
    private Label initializeTitleLabel() {
        Label label = new Label(this.stage.getTitle());
        label.setFont(new Font(FONT_FAMILY, 80));
        label.setTextFill(Color.WHITE);
        label.setLayoutX(500); // 居中标题
        label.setLayoutY(150);
        return label;
    }

    /**
     * initialize Menu Option
     *
     * @param text      name
     * @param yPosition Y coordinate position
     * @return Label
     */
    private Label initializeMenuOption(String text, int yPosition) {
        Label label = new Label(text);
        label.setFont(new Font(FONT_FAMILY, 40));
        label.setTextFill(Color.WHITE);
        label.setLayoutX(550); // 菜单选项居中
        label.setLayoutY(yPosition);
        return label;
    }

    /**
     * Update menu option colors
     */
    private void updateSelectedMenu() {
        // 根据当前选项更新颜色
        startGameLabel.setTextFill(selectedMenu == 0 ? Color.YELLOW : Color.WHITE);
        exitGameLabel.setTextFill(selectedMenu == 1 ? Color.YELLOW : Color.WHITE);
    }

    private void addKeyListeners() {
        this.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case W:
                    selectedMenu = Math.max(0, selectedMenu - 1);
                    // 更新选中效果
                    updateSelectedMenu();
                    break;
                case S:
                    selectedMenu = Math.min(1, selectedMenu + 1);
                    // 更新选中效果
                    updateSelectedMenu();
                    break;
                case ENTER:
                    this.mainController.executeMenuEvent(selectedMenu);
                    break;
                default:
                    break;
            }
        });
    }
}
