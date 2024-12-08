package com.example.demo.controller;

import com.example.demo.model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        // 初始化 GameController，但不涉及 UI（不传入 Stage）
        gameController = new GameController(null);
        // 手动初始化必要的列表，避免 null
        gameController.getUserPlaneBullets().clear();
        gameController.getEnemyPlaneBullets().clear();
        gameController.getEnemyPlanes().clear();
    }

    @Test
    void testGetMissionModel() {
        GameModel mission1 = gameController.getMissionModel(GameController.MISSION_1);
        assertNotNull(mission1);
        assertEquals(1, mission1.mission);

        GameModel invalidMission = gameController.getMissionModel(99);
        assertNull(invalidMission);
    }
}
