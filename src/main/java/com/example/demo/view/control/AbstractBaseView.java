package com.example.demo.view.control;

import com.example.demo.model.BaseModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * plane base class
 */
public abstract class AbstractBaseView<T extends BaseModel> extends ImageView {

    /**
     * plane data
     */
    public T model;

    /**
     * Whether it has been destroyed
     */
    public boolean destroyed;

    /**
     * Default Constructor
     */
    public AbstractBaseView(T model) {
        this(model.location, model.height, model.xPosition, model.yPosition);
        this.model = model;
    }

    /**
     * Constructor
     *
     * @param location  Image Path
     * @param height    Image height
     * @param xPosition x-coordinate
     * @param yPosition y-coordinate
     */
    public AbstractBaseView(String location, int height, double xPosition, double yPosition) {
        this.setImage(new Image(Objects.requireNonNull(getClass().getResource(location)).toExternalForm()));
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
        this.setFitHeight(height);
        this.setPreserveRatio(true);
    }

    /**
     * Update Position
     */
    public abstract void updatePosition();

    /**
     * Horizontal movement
     *
     * @param horizontalMove Moving distance
     */
    protected void moveHorizontally(double horizontalMove) {
        double currentTranslateX = getTranslateX();
        setTranslateX(currentTranslateX + horizontalMove);
    }

    /**
     * Vertical movement
     *
     * @param verticalMove Moving distance
     */
    protected void moveVertically(double verticalMove) {
        double currentTranslateY = getTranslateY();
        setTranslateY(currentTranslateY + verticalMove);
    }

    public void destroy() {
        this.destroyed = true;
    }
}
