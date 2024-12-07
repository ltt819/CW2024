> GitHub：https://github.com/ltt819/CW2024.git

- Directory Structure
<img src="doc/image-1.png">

- Class file description

> Program entry：<code>com.example.demo.App</code>



> Functional interface：
> 
> view：<code>com.example.demo.view.MainScene</code>
> 
> - Rendering background, function options
> 
> Logical Processing：<code>com.example.demo.controller.MainController</code>
> 
> - The implementation of keyboard events is completed here
> 
> 



> Game interface：
>
> view：<code>com.example.demo.view.GameScene</code>
> 
> - Render background, user plane, enemy plane, bullets and other game elements according to the mission
> 
> Logical Processing：<code>com.example.demo.controller.GameController</code>
> 
> - Launch the game
> - Setting Up the Level
> - 
> 
> After selecting Start Game in the menu interface, call the<code>com.example.demo.controller.GameController.startGame</code>function to initialize the game interface
> 
> After initialization is completed, the first mission of the game task is enabled by default
> 
 ```java
    /**
     * 启动游戏界面
     */
    public void startGame() {
        this.scene = new GameScene(stage, this);
        this.root = (Group) scene.getRoot();
        stage.setScene(scene);
        startMission(MISSION_1); // 默认开始第一关
    }
```
>
>

Modified content before refactoring

- Fixed several bugs
- Added a game start menu, including start game and exit, use W and S to switch options(add:StartMenu.java, modify:Main.java)
- Added mission 3 (add:LevelThree.java, modify:LevelParent.java, EnemyPlane.java, Boss.java, LevelTwo.java, Controller.java)
- Added the function of checking the current level number, added the boss health bar, and the health bar will change color according to the remaining health(modify:Boss.java, FighterPlane.java, LevelParent.javaLevelOne.java, LevelTwo.java, LevelThree.java, Controller.java)
- Added settlement interface. Press R to return to the start menu on the win or lose interface(modify:GameOverImage.java, LevelParent.java, LevelView.java, Controller.java)
- Added pause function, press esc to pause, press esc again to return to the game(modify:LevelParent.java)



Refactoring Instructions

- The original com.example.demo.controller.Main class is renamed to App and moved to the demo package.
- Create model, view, and controller packages, corresponding to data, view, and logic processing respectively
- The original com.example.demo.StartMenu class is moved to view and renamed to com.example.demo.view.MainScene, which inherits Scene and moves the event handling logic to the com.example.demo.controller.MainController class. 
- The game startup logic in com.example.demo.controller.MainController is optimized. Instead of recreating the view every time you enter a different level, the game interface is started in com.example.demo.controller.GameController and the view is created only once. The game interface of each level is re-rendered by updating the level data.
- The rendering functions of the game interface in the previous com.example.demo.Destructible, com.example.demo.ActiveActor, com.example.demo.ActiveActorDestructible, and com.example.demo.FighterPlane classes have been merged into various classes under the com.example.demo.view.GameScene and com.example.demo.view.control packages. According to the game elements, they are divided into abstract plane classes, user plane, enemy plane, Boss, bullets, health bars, and other classes. The attributes of these elements have been extracted and encapsulated in model data classes, including GameModel for game level data, PlaneModel for plane data in the game, BulletModel for bullet data, ShieldModel for shield data, and BaseModel as the base class.
- 






