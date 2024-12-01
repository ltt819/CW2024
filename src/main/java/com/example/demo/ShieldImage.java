package com.example.demo;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;

public class ShieldImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";
	public static final int SHIELD_SIZE = 200;
	private ImageView shieldImageView;
	
	public ShieldImage(double xPosition, double yPosition) {
		initializeShieldImage(xPosition, yPosition);
	}

	private void initializeShieldImage(double xPosition, double yPosition) {
		shieldImageView = new ImageView();
		URL imageUrl = getClass().getResource(IMAGE_NAME);
		System.out.println("Shield image URL: " + imageUrl);
		if (imageUrl != null) {
			shieldImageView.setImage(new Image(imageUrl.toExternalForm()));
		} else {
			System.err.println("Shield image not found at path: " + IMAGE_NAME);
		}
		shieldImageView.setLayoutX(xPosition);
		shieldImageView.setLayoutY(yPosition);
		shieldImageView.setFitHeight(SHIELD_SIZE);
		shieldImageView.setFitWidth(SHIELD_SIZE);
		shieldImageView.setOpacity(1.0);
		shieldImageView.setPreserveRatio(true);
		shieldImageView.setVisible(false);
	}

	public void setPosition(double x, double y) {
		Platform.runLater(() -> {
			shieldImageView.setLayoutX(x);
			shieldImageView.setLayoutY(y);
		});
	}


	public void showShield() {
		Platform.runLater(() -> {
			shieldImageView.setVisible(true);
			shieldImageView.toFront();
			System.out.println("Shield is now visible");
		});
	}
	
	public void hideShield() {
		Platform.runLater(() -> {
			shieldImageView.setVisible(false);
			System.out.println("Shield is now hidden");
		});
	}

	public ImageView getShieldImageView() {
		return shieldImageView;
	}
}
