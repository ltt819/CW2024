package com.example.demo.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class MainControllerTest {

    private MainController mainController;
    private Stage stage;
    private GameController mockGameController;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // 创建模拟的 Stage 和 GameController
        stage = mock(Stage.class);
        mockGameController = mock(GameController.class);

        // 使用模拟对象初始化 MainController
        mainController = new MainController(stage);

        // 通过反射访问 private 字段并注入 mockGameController
        Field gameControllerField = MainController.class.getDeclaredField("gameController");
        gameControllerField.setAccessible(true);  // 设置为可访问
        gameControllerField.set(mainController, mockGameController);  // 设置 gameController 为 mock 对象
    }

    @Test
    void testExecuteMenuEvent_startGame() {
        // 模拟选择开始游戏的菜单项 (选项 0)
        mainController.executeMenuEvent(0);

        // 验证 GameController 的 startGame() 方法被调用
        verify(mockGameController, times(1)).startGame();
    }

    @Test
    void testExecuteMenuEvent_exitGame() {
        // 使用 Mockito.mockStatic 来模拟 Platform.exit() 静态方法的调用
        try (var platformExitMock = mockStatic(Platform.class)) {
            // 模拟选择退出游戏的菜单项 (选项 1)
            mainController.executeMenuEvent(1);

            // 验证 Platform.exit() 被调用
            platformExitMock.verify(() -> Platform.exit(), times(1));
        }
    }
}

