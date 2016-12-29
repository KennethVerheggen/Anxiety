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
package be.carrotcats.anxiety.model.entity.player;

import be.carrotcats.anxiety.control.util.audio.AudioClipPlayer;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.Ball;
import be.carrotcats.anxiety.model.entity.enemy.Enemy;
import be.carrotcats.anxiety.model.entity.obstacle.PolygonObstacle;
import be.carrotcats.anxiety.view.AbstractGame;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class BasicPlayer extends Ball {

    /**
     * The maximal radius a player can have
     */
    public static final int MAX_RADIUS = 15;
    /**
     * The minimal radius a player can have
     */
    public static final int MIN_RADIUS = 5;
    /**
     * Boolean indicating whether this player is alive
     */
    private boolean dead = false;
    /**
     * The entity that killed this player
     */
    private GameEntity myKiller;

    /**
     * Constructor for a player
     *
     * @param world the active game world
     */
    public BasicPlayer(AbstractGame world) {
        super(world);
        defineCircle();
        objectProperties.setProperty("illuminated", "true");
    }

    private void defineCircle() {
        myCircle = new Circle(getRadius());
        myCircle.setFill(Color.WHITE);
        myCircle.setStroke(Color.WHITE);
        myCircle.setCenterX(getX());
        myCircle.setCenterY(getY());
        myShape = myCircle;
    }

    @Override
    public void update() {
        myCircle.setRadius(radius);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

    @Override
    public Shape getMyShape() {
        return myCircle;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        if (PolygonObstacle.class.isAssignableFrom(otherObject.getClass())) {
            radius = 800;
            dead = true;
            myKiller = otherObject;
            new AudioClipPlayer(AudioClipPlayer.EXPLODE).start();
        } else if (otherObject instanceof Enemy) {
            double penalty = getRadius() * 0.25;
            setRadius(getRadius() - penalty);
            new AudioClipPlayer(AudioClipPlayer.SPARK).start();
        }
    }

    public boolean isDead() {
        return dead;
    }

    public GameEntity getMyKiller() {
        return myKiller;
    }

}
