package com.example.demo.view.control;

import com.example.demo.model.BulletModel;

/**
 * bullet
 */
public class Bullet extends AbstractBaseView<BulletModel> {

    public Bullet(BulletModel model) {
        super(model);
    }

    @Override
    public void updatePosition() {
        moveHorizontally(this.model.horizontallyVelocity);
    }

    /**
     * Determine whether the bullet has left the area
     */
    public void destroy(double width) {
        if (getLayoutX() > width || getLayoutX() < 0) {
            this.destroy();
        }
    }
}
