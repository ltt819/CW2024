package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.LevelParent;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private static final String LEVEL_THREE_CLASS_NAME = "com.example.demo.LevelThree";
	private static final String START_MENU_CLASS_NAME = "com.example.demo.StartMenu"; // 添加 StartMenu 类名
	private final Stage stage;
	private LevelParent currentLevel;

	public Controller(Stage stage) {
		this.stage = stage;
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {

			stage.show();
		    try {
				goToLevel(LEVEL_ONE_CLASS_NAME);
		    } catch (Exception e) {
				handleException(e);
		    }
	}

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		try {
			if (currentLevel != null) {
				currentLevel.cleanUp();
			}
			if (START_MENU_CLASS_NAME.equals(className)) {
				// 加载 StartMenu
				Class<?> startMenuClass = Class.forName(className);
				Constructor<?> startMenuConstructor = startMenuClass.getConstructor(Stage.class);
				Object startMenuInstance = startMenuConstructor.newInstance(stage);
				stage.setScene((Scene) startMenuClass.getMethod("getScene").invoke(startMenuInstance));
			} else {
				// 加载普通关卡
				Class<?> myClass = Class.forName(className);
				Constructor<?> constructor = myClass.getConstructor(double.class, double.class);
				currentLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
				currentLevel.addObserver(this);
				Scene scene = currentLevel.initializeScene();
				stage.setScene(scene);
				currentLevel.startGame();
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			String nextLevel = (String) arg1;

			if (START_MENU_CLASS_NAME.equals(nextLevel)) {
				goToLevel(START_MENU_CLASS_NAME);
			} else {
				goToLevel((String) arg1);
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		if (e instanceof InvocationTargetException) {
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
				System.out.println("Cause of InvocationTargetException: " + cause.getMessage());
			}
		} else {
			e.printStackTrace();
		}
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(e.getClass().toString());
		alert.show();
	}
}
