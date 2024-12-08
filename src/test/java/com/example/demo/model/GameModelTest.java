package com.example.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    @Test
    void testInitMissionOneModel() {
        GameModel model = GameModel.initMissionOneModel();
        assertEquals(1, model.mission);
        assertEquals("/com/example/demo/images/background1.jpg", model.background);
        assertNotNull(model.userPlaneModel);
        assertNotNull(model.enemyPlaneModel);
    }

    @Test
    void testInitMissionTwoModel() {
        GameModel model = GameModel.initMissionTwoModel();
        assertEquals(2, model.mission);
        assertEquals("/com/example/demo/images/background2.jpg", model.background);
        assertNotNull(model.userPlaneModel);
        assertNotNull(model.bossPlaneModel);
    }

    @Test
    void testInitMissionThreeModel() {
        GameModel model = GameModel.initMissionThreeModel();
        assertEquals(3, model.mission);
        assertEquals("/com/example/demo/images/background1.jpg", model.background);
        assertNotNull(model.userPlaneModel);
        assertNotNull(model.bossPlaneModel);
    }
}
