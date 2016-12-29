/*
 * Copyright 2016 Kenneth Verheggen <kenneth.verheggen@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.carrotcats.anxiety.view;

import be.carrotcats.anxiety.control.factory.scenes.SceneFactory;
import be.carrotcats.anxiety.control.util.StartupCheck;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.enemy.snake.Snake;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public abstract class AbstractGame extends Application {

    /**
     * the background color of the screen
     */
    protected static Color appBackground = Color.CORNFLOWERBLUE;
    /**
     * the foreground color of the screen
     */
    protected static Color appForeGround = Color.BLACK;
    /**
     * the title of the game
     */
    protected String appTitle = "CarrotCat Engine V 1.0";
    /**
     * The width of the application
     */
    public static final int APP_WIDTH = 800;
    /**
     * The height of thte application
     */
    public static final int APP_HEIGHT = 600;
    /**
     * The canvas to draw to
     */
    private Canvas canvas;
    /**
     * The graphics context
     */
    private GraphicsContext gc;
    /**
     * The gameLoop
     */
    private GameLoop gameLoop;

    /**
     * Collection of objects to render
     */
    protected LinkedList<GameEntity> objectsToRender = new LinkedList<>();
    /**
     * The root application
     */
    protected Group root;
    /**
     * The stage
     */
    protected Stage stage;

    private boolean gameActive;
    /**
     * The scene to play the game
     */
    protected Scene gameScene;
    /**
     * The scene to show the menu
     */
    private Scene menuScene;
    /**
     * The scene that shows a game over screen
     */
    private Scene gameOverScene;
    /**
     * The scene for information
     */
    private Scene gameStoryScene;
    /**
     * Boolean stating the gameOverScreen is activated
     */
    private boolean gameOver = false;
    /**
     * The collection of snake enemies
     */
    ArrayList<Snake> snakes = new ArrayList<>();
    //try to multithread?
    protected ExecutorService mtService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void startGame(String[] args) {
        if (StartupCheck.isCorrectJavaInstalled()) {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "This game requires at least JDK" + StartupCheck.TARGET_JAVA_VERSION + ". This machine is currently running the " + StartupCheck.getVersion() + ".",
                    "Java version too low",
                    JOptionPane.OK_OPTION);
        } else {
            launch(args);
        }
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        canvas = new Canvas(APP_WIDTH, APP_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root = new Group();
        root.getChildren().add(canvas);
        gameScene = new Scene(root);
        menuScene = constructMenu();
        gameStoryScene = constructGameStoryScene();
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);
        stage.setTitle(appTitle);
        stage.setScene(menuScene);
        stage.setResizable(false);
        initialise(stage);
        stage.show();
        gameLoop = new GameLoop();
        gameLoop.start();
    }

    public void reset() {
        root.getChildren().clear();
        objectsToRender.clear();
        initialise(stage);
    }

    protected Scene constructMenu() {
        return SceneFactory.createMenuScreen(this);
    }

    protected Scene constructGameOverScene() {
        return SceneFactory.createGameOverScreen(this);
    }

    protected Scene constructGameStoryScene() {
        try {
            return SceneFactory.createInformationScreen(this);
        } catch (IOException ex) {
            //ignore for now
        }
        return null;
    }

    public static Color getAppBackground() {
        return appBackground;
    }

    public static Color getAppForeGround() {
        return appForeGround;
    }

    public static int getAppWidth() {
        return APP_WIDTH;
    }

    public static int getAppHeight() {
        return APP_HEIGHT;
    }

    public void activateMenu() {
        stage.setScene(menuScene);
        ObservableList<Node> childrenUnmodifiable = menuScene.getRoot().getChildrenUnmodifiable();
        //find the start button
        for (Node node : childrenUnmodifiable) {
            if (node instanceof Button) {
                Button temp = (Button) node;
                if (temp.getText().equalsIgnoreCase("play") && isGameActive()) {
                    temp.setText("Resume");
                } else if (temp.getText().equalsIgnoreCase("resume") && !isGameActive()) {
                    temp.setText("Play");
                }
            }
        }
        gameActive = false;
        toggleCursor(true);
    }

    public void activateStory() {
        stage.setScene(gameStoryScene);
        gameActive = false;
        toggleCursor(true);
    }

    public void activateGameOver() {
        //create the screen here, not before or there will be nullpointers!
        //stop all the rest?
        mtService.shutdownNow();
        gameOverScene = constructGameOverScene();
        gameOver = true;
        stage.setScene(gameOverScene);
        gameActive = false;
        toggleCursor(true);
    }

    public void activateGame() {
        stage.setScene(gameScene);
        gameActive = true;
        toggleCursor(false);
    }

    public void toggleCursor(boolean on) {
        Cursor activeCursor;
        if (on) {
            activeCursor = Cursor.HAND;
        } else {
            activeCursor = Cursor.NONE;
        }
        menuScene.getRoot().setCursor(activeCursor);
        gameScene.getRoot().setCursor(activeCursor);
        gameStoryScene.getRoot().setCursor(activeCursor);
        if (gameOver) {
            gameOverScene.getRoot().setCursor(activeCursor);
        }
    }

    private class GameLoop extends AnimationTimer {

        private long before = System.nanoTime();
        private float delta;

        @Override
        public void handle(long now) {

            if (gameActive) {
                delta = (float) ((now - before) / 1E9);

                handleInput(delta);

                updateObjects(delta);

                clearScreen(gc);

                render(gc);

                before = now;
            }
        }
    }

    protected void clearScreen(GraphicsContext gc) {
        gc.clearRect(0, 0, APP_WIDTH, APP_HEIGHT);
        gc.setFill(appBackground);
        gc.fillRect(0, 0, APP_WIDTH, APP_HEIGHT);
    }

    protected abstract void initialise(Stage stage);

    protected abstract void handleInput(float dt);

    protected void updateObjects(float dt) {

        for (GameEntity object : objectsToRender) {
            if (!(object instanceof Snake)) {
                object.checkCollisions(objectsToRender);
                object.update();
            } else {
                snakes.add((Snake) object);
            }
        }
        //do this seperately so snakes can destroy themselves
        for(Snake aSnake:snakes){
            aSnake.update();
        }
    }

    protected abstract void render(GraphicsContext gc);

    public Group getRoot() {
        return root;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isGameActive() {
        return gameActive;
    }

}
