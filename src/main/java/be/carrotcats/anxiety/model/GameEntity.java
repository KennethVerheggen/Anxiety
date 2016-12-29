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
package be.carrotcats.anxiety.model;

import be.carrotcats.anxiety.model.entity.obstacle.PolygonObstacle;
import be.carrotcats.anxiety.view.AbstractGame;
import java.util.LinkedList;
import java.util.Properties;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public abstract class GameEntity {

    /**
     * the x coordinate
     */
    public float x;
    /**
     * the y coordinate
     */
    public float y;
    /**
     * the movement on x-axis
     */
    private float dx;
    /**
     * the movement on y-axis
     */
    private float dy;
    /**
     * the movement speed
     */
    private float speed;
    /**
     * the inner collor of the entity
     */
    public Color innerColor = Color.WHITE;
    /**
     * the outer color of the entity
     */
    private Color outerColor = Color.BLACK;
    /**
     * the shape of the entity
     */
    public Shape myShape;
    /**
     * The active game world
     */
    private final AbstractGame world;

    /**
     * Constructor for a game eniy
     *
     * @param world the active game world
     */
    public GameEntity(AbstractGame world) {
        this.world = world;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        if (myShape != null) {
            this.myShape.setLayoutX(x);
        }
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        if (myShape != null) {
            this.myShape.setLayoutY(y);
        }
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public Color getInnerColor() {
        return innerColor;
    }

    public void setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
    }

    public Color getOuterColor() {
        return outerColor;
    }

    public void setOuterColor(Color outerColor) {
        this.outerColor = outerColor;
    }

    public Shape getMyShape() {
        return myShape;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public AbstractGame getWorld() {
        return world;
    }

    public Properties objectProperties = new Properties();

    /**
     * Updaets the position and meta of this object
     */
    public abstract void update();

    /**
     * Handles a collision with a given object
     *
     * @param otherObject the colliding entity
     */
    public abstract void handleCollision(GameEntity otherObject);

    /**
     * Checks any collisions with logic objects
     *
     * @param objectsToRender the objects that can potentially collide
     */
    public void checkCollisions(LinkedList<GameEntity> objectsToRender) {
        for (GameEntity anObject : objectsToRender) {
            if (anObject != this && !PolygonObstacle.class.isAssignableFrom(getClass())) {
                //eliminate all offscreen objects
//                if (anObject.getY() < 0 && anObject.getY() < world.getAppHeight()) {
                if (getMyShape() != null && anObject.getMyShape() != null) {
                    if (intersects(anObject)) {
                        handleCollision(anObject);
                    }
                }
                //               }
            } else if (PolygonObstacle.class.isAssignableFrom(getClass())) {
                System.out.println("Hmm");
            }
        }
    }

    protected boolean intersects(GameEntity object) {
        return Shape.intersect(getMyShape(), object.getMyShape()).getBoundsInLocal().getWidth() != -1;
    }

}
