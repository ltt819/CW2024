package com.example.demo.model;

import java.util.List;

/**
 * plana data
 */
public class PlaneModel extends BaseModel {
    /**
     * X coordinate option for movement, effective for boss configuration
     */
    public List<Integer> xPositions;
    /**
     * Y coordinate option for movement, effective for boss configuration
     */
    public List<Integer> yPositions;
    /**
     * blood bar
     */
    public int blood;
    /**
     * shield data
     */
    public ShieldModel shieldModel;
    /**
     * bullet data
     */
    public BulletModel bulletModel;
}
