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
package be.carrotcats.anxiety.game;

import be.carrotcats.anxiety.control.factory.gameobjects.BulletFactory;
import be.carrotcats.anxiety.control.factory.gameobjects.EnemyFactory;
import be.carrotcats.anxiety.control.util.audio.BackgroundMusicPlayer;
import be.carrotcats.anxiety.control.util.audio.HeartBeatPlayer;
import be.carrotcats.anxiety.control.factory.gameobjects.SectionFactory;
import be.carrotcats.anxiety.control.input.MouseListener;
import be.carrotcats.anxiety.control.input.game.GameInputListener;
import be.carrotcats.anxiety.control.util.QuoteLibrary;
import be.carrotcats.anxiety.control.util.ScoreCalculator;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.Ball;
import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import be.carrotcats.anxiety.model.entity.enemy.Enemy;
import be.carrotcats.anxiety.model.entity.player.LightShot;
import be.carrotcats.anxiety.model.section.Section;
import be.carrotcats.anxiety.view.AbstractGame;
import com.sun.javafx.tk.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Anxiety extends AbstractGame {

    public static void main(String[] args) {
        launch(args);
    }

    private BasicPlayer basicPlayer;
    private WritableImage writableImage;
    private PixelWriter pixelWriter;
    private ImageView destImageView;

    private Light.Point lightPoint;
    private Lighting lighting;

    private MouseListener defaultMouseListener;
    private long cooldown;

    private final ArrayList<LightShot> lights = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private Section currentSection;

    private HeartBeatPlayer hbp;

    //check to optimise this later?
    private Color[][] lightColorMap;
    private BackgroundMusicPlayer backgroundMusic;
    private Label messageLabel;
    private QuoteLibrary quoteLib;
    private long renderDelay;
    private Label scoreLabel;
    private boolean metaDataActive;
    private GameInputListener gameInput;

    @Override
    protected void initialise(Stage stage) {
        //make a player
        basicPlayer = new BasicPlayer(this);
        //set background
        initBackground();
        //init the lighting
        initLight();
        //set audio
        initBGM();
        //set a title
        appTitle = "Anxiety - beta version";
        //set input
        initInput();
        //init level
        initLevel();
        //set cursor
        toggleCursor(true);
        try {
            //init message Label
            quoteLib = new QuoteLibrary();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //message label
        messageLabel = new Label("");
        messageLabel.setFont(Font.font("Copperplate", 18));
        messageLabel.setWrapText(true);
        root.getChildren().add(messageLabel);
        displayMessage("Use the [MOUSE] to navigate and shine your light with the [LEFT MOUSE BUTTON]");
        //score and progress label
        scoreLabel = new Label("");
        scoreLabel.setFont(Font.font("Copperplate", 16));
        scoreLabel.setWrapText(true);
        root.getChildren().add(scoreLabel);

        //disable cursor
        toggleCursor(true);
        //set the app title
        stage.setTitle(appTitle);
    }

    public void displayMetaData() {
        StringBuilder message = new StringBuilder();
        //fill the message
        //section info
        message.append("Section\t ").append(SectionFactory.getSectionsPassed()).append("\n");
        //heartbeat
        message.append("Heartrate\t ").append(hbp.getBPM()).append("\n");
        //score info
        message.append("Score\t ").append(ScoreCalculator.getScore()).append("\n");

        //finalize
        scoreLabel.setText(message.toString());
        int x = 50;
        int y = 20;
        scoreLabel.setLayoutX(x);
        scoreLabel.setLayoutY(y);
        scoreLabel.setTextFill(Color.WHITE);
        Glow effect1 = new Glow(1.0);
        GaussianBlur effect2 = new GaussianBlur(1.5);
        effect2.setInput(effect1);
        scoreLabel.setEffect(effect2);
    }

    public void displayMessage(String message) {
        messageLabel.setText(message);
        int x = (int) ((APP_WIDTH - Toolkit.getToolkit().getFontLoader().computeStringWidth(messageLabel.getText(), messageLabel.getFont())) / 2);
        int y = APP_HEIGHT - 100;
        messageLabel.setLayoutX(x);
        messageLabel.setLayoutY(y);
        messageLabel.setTextFill(Color.WHITE);
        Glow effect1 = new Glow(1.0);
        GaussianBlur effect2 = new GaussianBlur(1.5);
        effect2.setInput(effect1);
        messageLabel.setEffect(effect2);
        FadeTransition fadeIn = createFader(messageLabel, 3, true);
        FadeTransition fadeOut = createFader(messageLabel, 7, false);
        SequentialTransition fading = new SequentialTransition(messageLabel, fadeIn, fadeOut);
        fading.playFromStart();
    }

    private FadeTransition createFader(Node node, int time, boolean fadeIn) {
        FadeTransition fade = new FadeTransition(Duration.seconds(time), node);
        if (fadeIn) {
            fade.setFromValue(0);
            fade.setToValue(1);
        } else {
            fade.setFromValue(1);
            fade.setToValue(0);
        }
        return fade;
    }

    private void initLevel() {
        //add objects to render
        addObject(basicPlayer);
        //add a tunnel
        currentSection = SectionFactory.generateSection(this);
        addSection(currentSection);
    }

    private void spawnEnemies() {
        if (enemies.size() < EnemyFactory.MAX_ENEMIES) {
            EnemyFactory.spawnEnemies(this, EnemyFactory.MAX_ENEMIES - enemies.size());
        }
    }

    private void initBackground() {
        lightColorMap = new Color[APP_WIDTH][APP_HEIGHT];
        appBackground = Color.WHITE;
        appForeGround = Color.BLACK;
        writableImage = new WritableImage(APP_WIDTH, APP_HEIGHT);
        destImageView = new ImageView();
        destImageView.setImage(writableImage);
        pixelWriter = writableImage.getPixelWriter();
        root.getChildren().add(destImageView);
    }

    private void initInput() {

//add the eventHandler to this scene for keyboard input
        gameInput = new GameInputListener(this);
        defaultMouseListener = gameInput.getMouseListener();
        gameScene.addEventHandler(KeyEvent.KEY_PRESSED, gameInput.getKeyboardListener());
        gameScene.addEventHandler(KeyEvent.KEY_RELEASED, gameInput.getKeyboardListener());
        //add the eventHandler to this scene for mouse input
        gameScene.addEventHandler(MouseEvent.MOUSE_PRESSED, gameInput.getMouseListener());
        gameScene.addEventHandler(MouseEvent.MOUSE_RELEASED, gameInput.getMouseListener());
        gameScene.addEventHandler(MouseEvent.MOUSE_MOVED, gameInput.getMouseListener());
        /*
        //add the eventHandler to this scene for drag input (if needed)
        scene.addEventHandler(MouseEvent.DRAG_DETECTED, new DefaultMouseListener());
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, new DefaultMouseListener());
         */
    }

    private void initLight() {
        //Light.Point: Represents a light source at a given position in 3D space.
        lightPoint = new Light.Point();
        lightPoint.setX(0);
        lightPoint.setY(0);
        lightPoint.setZ(0);
        lightPoint.setColor(Color.GOLD);
        lighting = new Lighting();
        lighting.setLight(lightPoint);
        lighting.setSurfaceScale(5.0);
        lighting.setDiffuseConstant(10.0);
        //other side
        Light.Point tempLightPoint = new Light.Point();
        tempLightPoint.setX(0);
        tempLightPoint.setY(0);
        tempLightPoint.setZ(0);
        tempLightPoint.setColor(Color.GOLD);
        Lighting tempLighting = new Lighting();
        tempLighting.setLight(tempLightPoint);
        tempLighting.setSurfaceScale(5.0);
        tempLighting.setDiffuseConstant(10.0);
        tempLighting.setBumpInput(lighting);
        lighting = tempLighting;
    }

    private void initBGM() {
        backgroundMusic = new BackgroundMusicPlayer(this, BackgroundMusicPlayer.BACKGROUND_MUSIC);
        backgroundMusic.start();

        //   BackgroundMusicPlayer rain = new BackgroundMusicPlayer(this,BackgroundMusicPlayer.BACKGROUND_RAIN);
        //   rain.start();
        hbp = new HeartBeatPlayer(this);
        hbp.start();
        //make sure the heartbeat closes down in a neat fashion...
        stage.setOnCloseRequest((WindowEvent event) -> {
            hbp.stop();
            Platform.exit();
        });
    }

    private void createBullet(MouseListener listener, double tempRadius) {
        BulletFactory.createLightShot(this, listener, tempRadius);
    }

    private void updateHeartBeat() {
        hbp.setAnxiety((enemies.size()));
    }

    private void updatePlayerLighting(float dt) {

        //update the radius
        double tempRadius = basicPlayer.getRadius();
        long now = System.currentTimeMillis();
        boolean onCooldown = (now - cooldown) <= 3000;
        if (!onCooldown) {
            if (tempRadius <= 0 && defaultMouseListener.isCharging()) {
                cooldown = System.currentTimeMillis();
                defaultMouseListener.setCharging(false);
            } else if (defaultMouseListener.isCharging()) {
                tempRadius--;
            } else if (defaultMouseListener.getChargeTime() > 0) {
                createBullet(defaultMouseListener, tempRadius);
            } else if (!defaultMouseListener.isCharging() && tempRadius < BasicPlayer.MAX_RADIUS) {
                tempRadius++;
            }
        }
        basicPlayer.setRadius(Math.max(0, tempRadius));
    }

    private void checkObjectLighting(Ball entity, int x, int y, double dim) {
        int entityX = (int) entity.getX();
        int entityY = (int) entity.getY();
        int entityRadius = (int) entity.getRadius();
        double distance = Math.sqrt((x - entityX) * (x - entityX) + (y - entityY) * (y - entityY)) - entityRadius;
        Color temp = lightColorMap[x][y];
        if (temp == null) {
            temp = appForeGround;
            lightColorMap[x][y] = temp;
        }
        if (distance >= 0) {
            double transparancy = Math.min(1, Math.max(0, (1 - entityRadius / distance)));
            temp = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), transparancy);
            lightColorMap[x][y] = temp;
        }

    }

    protected void addSection(Section section) {
        addObject(section);
        for (GameEntity object : section.getSectionObjects()) {
            addObject(object);
        }
    }

    public void addObject(GameEntity object) {
        objectsToRender.add(object);
        Shape shape = object.getMyShape();
        if (shape != null) {
            root.getChildren().add(shape);
            if (!(object instanceof BasicPlayer)) {
                object.getMyShape().setEffect(lighting);
            }
        }
    }

    public void addEnemy(Enemy enemy) {
        addObject(enemy);
        enemies.add(enemy);
    }

    public void addLightShot(LightShot shot) {
        lights.add(shot);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public void reset() {
        hbp.stop();
        super.reset();
        backgroundMusic.reset();
        SectionFactory.reset();
        ScoreCalculator.reset();
    }

    @Override
    public void stop() {
        hbp.stop();
        backgroundMusic.stop();
        Platform.exit();
    }

    @Override
    public void activateGame() {
        super.activateGame();
        basicPlayer.setX(APP_WIDTH / 2);
        basicPlayer.setY(APP_HEIGHT / 2);
    }

    @Override
    protected void handleInput(float dt) {
        //input is handled by the listeners for now ?
    }

    @Override
    public void start(Stage primaryStage) {
        //      primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle(appTitle);
        super.start(primaryStage);
    }

    @Override
    protected void updateObjects(float dt) {
        if (!basicPlayer.isDead()) {
            super.updateObjects(dt);
            if (enemies.isEmpty()) {
                spawnEnemies();
            }

            updatePlayerLighting(dt);

        } else if (basicPlayer.getRadius() > 1) {
            basicPlayer.setRadius(basicPlayer.getRadius() * 0.85f);
            ((Circle) basicPlayer.getMyShape()).setRadius(basicPlayer.getRadius());
            updateHeartBeat();
        } else {
            activateGameOver();
        }
    }

    @Override
    protected void render(GraphicsContext gc) {
        //check hbp
        if (hbp == null) {
            hbp = new HeartBeatPlayer(this);
        }
        hbp.start();
        //display score
        if (metaDataActive) {
            displayMetaData();
        }
        //check the current section
        if (currentSection.isDone()) {
            if ((System.currentTimeMillis() - renderDelay) > 5000) {
                //there is lag in the rendering...give it 5 seconds
                objectsToRender.remove(currentSection);
                for (GameEntity object : currentSection.getSectionObjects()) {
                    objectsToRender.remove(object);
                    root.getChildren().remove(object.getMyShape());
                }
                //instantly load next section?
                currentSection = SectionFactory.generateSection(this);
                //Display a message
                if (SectionFactory.isCalm()) {
                    ScoreCalculator.finishSection(hbp.getBPM());
                    displayMessage(Math.max(1, SectionFactory.getSectionsPassed() / 2) + ".\n" + quoteLib.getRandomQuote());
                    spawnEnemies();
                }
                //add next section
                addSection(currentSection);
                renderDelay = System.currentTimeMillis();
            }

        }

        //check for dead enemies       
        List<Enemy> temp = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.isDestroyed()) {
                ScoreCalculator.killEnemy(hbp.getBPM());
                objectsToRender.remove(enemy);
                root.getChildren().remove(enemy.getMyShape());
                temp.add(enemy);
            }
        }
        enemies.removeAll(temp);

        updateHeartBeat();
        //move the light
        lightPoint.setX(basicPlayer.getX());
        lightPoint.setY(APP_HEIGHT - basicPlayer.getY());
        //the background should be transparent where the player is?
        for (int y = 0; y < APP_HEIGHT; y++) {
            for (int x = 0; x < APP_WIDTH; x++) {
                checkObjectLighting(basicPlayer, x, y, 0.3);
                pixelWriter.setColor(x, y, lightColorMap[x][y]);
            }
        }

        root.getChildren().remove(destImageView);
        root.getChildren().add(root.getChildren().size(), destImageView);
        root.getChildren().remove(basicPlayer.getMyShape());
        root.getChildren().add(root.getChildren().size(), basicPlayer.getMyShape());
        //place bullets on top (maybe add light later, not really needed now)
        ArrayList<LightShot> expiredLights = new ArrayList<>();
        for (LightShot light : lights) {
            //remove all the lights that are expired               
            if (objectsToRender.contains(light)) {
                objectsToRender.remove(light);
            }
            if (root.getChildren().contains(light.getMyShape())) {
                root.getChildren().remove(light.getMyShape());
            }
            if (!basicPlayer.isDead() && !light.isDestroyed()) {
                objectsToRender.add(light);
                root.getChildren().add(light.getMyShape());
            }
            if (light.isDestroyed()) {
                expiredLights.add(light);
            }
        }
        lights.removeAll(expiredLights);
        //the messagelabels needs tto be absolutely on top
        root.getChildren().remove(messageLabel);
        root.getChildren().add(root.getChildren().size(), messageLabel);
        //metadata only on top if it's active
        if (metaDataActive) {
            root.getChildren().remove(scoreLabel);
            root.getChildren().add(root.getChildren().size(), scoreLabel);
        }
    }

    public BasicPlayer getBasicPlayer() {
        return basicPlayer;
    }

    public void setMetaDataActive(boolean b) {
        metaDataActive = b;
    }

    public boolean isMetaDataActive() {
        return metaDataActive;
    }

    public HeartBeatPlayer getHbp() {
        return hbp;
    }

    public void removeObject(GameEntity entity) {
        objectsToRender.remove(entity);
        root.getChildren().remove(entity.getMyShape());
    }

    public LinkedList<GameEntity> getObjectsToRender() {
       return objectsToRender;
    }

}
