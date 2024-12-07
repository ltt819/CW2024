package com.example.demo.view.control;

import javafx.scene.control.ProgressBar;

/**
 * plane health
 */
public class BossBlood extends ProgressBar {

    /**
     * Remaining health
     */
    private final double blood;

    public BossBlood(double width, double xPosition, double yPosition, int blood) {
        super(1.0);
        this.blood = blood;
        this.setPrefWidth(width); // 设置血条宽度
        this.setLayoutX(xPosition); // 右上角布局
        this.setLayoutY(yPosition); // 距离顶部的偏移
        this.setStyle("-fx-accent: green;");  // 绿色
    }

    /**
     * bleed
     */
    public void bleeding(double left) {
        double progress = Math.max(0, left / this.blood);
        this.setProgress(progress);
        // 根据血量比例设置血条颜色
        if (progress > 0.5) {
            this.setStyle("-fx-accent: green;");  // 绿色
        } else if (progress > 0.2) {
            this.setStyle("-fx-accent: yellow;");  // 黄色
        } else {
            this.setStyle("-fx-accent: red;");  // 红色
        }
    }
}
