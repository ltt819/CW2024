package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	
	private int currentNumberOfEnemies;
	private LevelView levelView;

	private static final int TITLE_FONT_SIZE = 30; // 字体大小
	private Label levelTitle; // 用于显示关卡名称的标签
	private Label hintLabel;

	private ProgressBar bossHealthBar;
	private ActiveActorDestructible boss; // 通用的 Boss 引用
	private double bossMaxHealth;

	private boolean isPaused = false;
	private ImageView pauseBackground;
	private Label pauseLabel1;
	private Label pauseLabel2;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		initializeKeyListeners(); // 初始化键盘监听（用来暂停和恢复游戏）
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	// 添加暂停和恢复的功能
	public void togglePause() {
		if (isPaused) {
			resumeGame();  // 恢复游戏
		} else {
			pauseGame();  // 暂停游戏
		}
	}

	// 暂停游戏
	private void pauseGame() {
		isPaused = true;
		timeline.pause();  // 暂停时间线
		showPauseText();  // 显示暂停文字
		disableGameInput();  // 禁用游戏输入
	}

	// 恢复游戏
	private void resumeGame() {
		isPaused = false;
		timeline.play();  // 恢复时间线
		removePauseText();  // 移除暂停文字
		enableGameInput();  // 恢复游戏输入
	}

	private void disableGameInput() {
		// 仅禁用除 ESC 键外的其他输入
		scene.setOnKeyPressed(event -> {
			if (event.getCode() != KeyCode.ESCAPE) {
				event.consume();  // 忽略其他键盘输入
			} else {
			// 让 ESC 键处理暂停和恢复
			togglePause();
			}
		});
	}

	private void enableGameInput() {
		// 恢复游戏输入（除了 ESC）
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				togglePause();  // 切换暂停状态
			}
		});
	}

	private void showPauseText() {
		// 创建暂停文字
		pauseLabel1 = new Label("The game is paused");
		pauseLabel1.setFont(new Font("Arial", 40));
		pauseLabel1.setStyle("-fx-text-fill: white;");
		pauseLabel1.setTextAlignment(TextAlignment.CENTER);
		pauseLabel1.setLayoutX((screenWidth - 400) / 2);  // 水平居中
		pauseLabel1.setLayoutY(screenHeight / 2 - 50);  // 位于屏幕中央

		pauseLabel2 = new Label("Press ESC to return to the game");
		pauseLabel2.setFont(new Font("Arial", 20));
		pauseLabel2.setStyle("-fx-text-fill: white;");
		pauseLabel2.setTextAlignment(TextAlignment.CENTER);
		pauseLabel2.setLayoutX((screenWidth - 400) / 2);  // 水平居中
		pauseLabel2.setLayoutY(screenHeight / 2 + 20);  // 位于第一行下方

		// 将暂停文字添加到 root 中
		root.getChildren().addAll(pauseLabel1, pauseLabel2);
	}

	private void removePauseText() {
		if (pauseLabel1 != null) {
			root.getChildren().remove(pauseLabel1);
		}
		if (pauseLabel2 != null) {
			root.getChildren().remove(pauseLabel2);
		}

		// 调试输出：检查当前根节点中的所有元素
		System.out.println("Current root children after removing pause text: " + root.getChildren());
	}

	// 监听键盘事件，切换暂停状态
	private void initializeKeyListeners() {
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				togglePause();
			}
		});
	}


	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
	}

	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.SPACE) fireProjectile();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			}
		});
		root.getChildren().add(0,background);
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	protected void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
		// 更新 Boss 的血条状态
		updateBossHealth();
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> projectiles, List<ActiveActorDestructible> targets) {
		for (ActiveActorDestructible target : targets) {
			for (ActiveActorDestructible projectile : projectiles) {
				if (projectile.getBoundsInParent().intersects(target.getBoundsInParent())) {
					projectile.takeDamage(); // 子弹销毁
					target.takeDamage(); // Boss 或敌机受到伤害
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
		// 添加提示信息
		showReturnToStartMenuHint();

		// 捕捉 R 键事件
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.R) {
				returnToStartMenu();
			}
		});
	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
		// 添加提示信息
		showReturnToStartMenuHint();

		// 捕捉 R 键事件
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.R) {
				returnToStartMenu();
			}
		});
	}

	// 返回主菜单的方法
	private void returnToStartMenu() {
		setChanged();
		notifyObservers("com.example.demo.StartMenu");
	}

	// 显示提示信息
	public void showReturnToStartMenuHint() {
		Label hintLabel = new Label("Press R to return to the start menu");
		hintLabel.setFont(new Font("Arial", 20));
		hintLabel.setStyle("-fx-text-fill: red;");
		hintLabel.setTextAlignment(TextAlignment.CENTER);
		hintLabel.setLayoutX((screenWidth - 300) / 2); // 水平居中
		hintLabel.setLayoutY(screenHeight - 150); // 位于屏幕底部

		root.getChildren().add(hintLabel);
		// 调试输出
		System.out.println("Hint Label added to root: " + root.getChildren().contains(hintLabel));
		System.out.println("Hint Label Position: X=" + hintLabel.getLayoutX() + ", Y=" + hintLabel.getLayoutY());
	}

	public void cleanUp() {
		timeline.stop();
		root.getChildren().clear();
		friendlyUnits.clear();
		enemyUnits.clear();
		userProjectiles.clear();
		enemyProjectiles.clear();
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}

	protected void notifyObservers(String nextLevelClassName) {
		setChanged();
		super.notifyObservers(nextLevelClassName); // 通知观察者
	}

	// 添加方法：初始化顶部关卡名称
	protected void initializeLevelTitle(String levelName) {
		levelTitle = new Label(levelName);
		levelTitle.setFont(new Font("Arial", TITLE_FONT_SIZE)); // 设置字体和大小
		levelTitle.setTextAlignment(TextAlignment.CENTER);
		levelTitle.setStyle("-fx-text-fill: white;"); // 设置文字颜色
		levelTitle.setLayoutX((screenWidth - levelTitle.prefWidth(-1)) / 2); // 水平居中
		levelTitle.setLayoutY(20); // 设置距离顶部的偏移
		root.getChildren().add(levelTitle); // 添加到根节点
	}

	// 初始化 Boss 血条
	protected void initializeBossHealthBar(ActiveActorDestructible boss, double initialHealth) {
		this.boss = boss;
		bossMaxHealth = initialHealth; // 记录 Boss 最大血量
		bossHealthBar = new ProgressBar(1.0); // 初始进度为 100%
		bossHealthBar.setPrefWidth(200); // 设置血条宽度
		bossHealthBar.setLayoutX(screenWidth - 220); // 右上角布局
		bossHealthBar.setLayoutY(20); // 距离顶部的偏移
		root.getChildren().add(bossHealthBar); // 添加到根节点
	}

	// 更新 Boss 血条
	private void updateBossHealth() {
		if (boss != null && bossHealthBar != null) {
			double currentHealth = ((FighterPlane) boss).getHealth();
			double progress = Math.max(0, currentHealth / bossMaxHealth);
			bossHealthBar.setProgress(progress);
			// 根据血量比例设置血条颜色
			if (progress > 0.5) {
				bossHealthBar.setStyle("-fx-accent: green;");  // 绿色
			} else if (progress > 0.2) {
				bossHealthBar.setStyle("-fx-accent: yellow;");  // 黄色
			} else {
				bossHealthBar.setStyle("-fx-accent: red;");  // 红色
			}
		}
	}
}
