package com.example.demo.model;

/**
 * shield data
 */
public class ShieldModel extends BaseModel {
    /**
     * open rate
     */
    public double openRate;
    /**
     * Maximum protection times
     */
    public int maxShield;
    /**
     * The maximum number of frames to maintain, the default is 500 frames
     */
    public int maxFrames = 500;
}
