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
package be.carrotcats.anxiety.model.entity.enemy;

import be.carrotcats.anxiety.control.util.audio.AudioClipPlayer;
import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.Ball;
import be.carrotcats.anxiety.model.entity.player.LightShot;
import be.carrotcats.anxiety.view.AbstractGame;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Enemy extends Ball {

    /**
     * boolean indicating if an enemy has died
     */
    boolean destroyed;
    /**
     * the target the enemy will be following
     */
    GameEntity target;
    /**
     * The movement that happened in the X direction
     */
    public float amountToMoveX;
    /**
     * The movement that happened in the Y direction
     */
    public float amountToMoveY;
    public float accelerationX = 0;
    private float accelerationY = 0;
   
    public float aggressionDistance=Anxiety.APP_WIDTH;
    /**
     * Generates an enemy
     *
     * @param game the active game
     */
    public Enemy(AbstractGame game) {
        super(game);
        innerColor = Color.RED;
        myCircle = new Circle(15);
        myCircle.setFill(innerColor);
        myCircle.setCenterX(x);
        myCircle.setCenterY(y);
        myShape = myCircle;
        this.speed = 0.03f;
        this.target=((Anxiety)game).getBasicPlayer();
    }

    /**
     * Generates an enemy
     *
     * @param target the target this enemy will be following
     * @param game the active game
     */
    public Enemy(AbstractGame game, GameEntity target) {
        super(game);
        innerColor = Color.RED;
        myCircle = new Circle(15);
        myCircle.setFill(innerColor);
        myCircle.setCenterX(x);
        myCircle.setCenterY(y);
        myShape = myCircle;
        this.target = target;
        this.speed = 0.03f;
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(5);
        myCircle.setEffect(blur);
    }

    @Override
    public void update() {
        if (radius <= 5 && !destroyed) {
            destroyed = true;
            new AudioClipPlayer(AudioClipPlayer.getRandomEnemyDeadSound()).start();
        } else if (target != null) {
            moveToTarget();
        }
        if (!destroyed) {
            //   setY(getY() + speed * 0.05f);
            myCircle.setRadius(radius);
        }
    }

    public boolean moveToTarget() {
        float easingAmount = 0.15f;
        float xDistance = ((Anxiety) getWorld()).getBasicPlayer().getX() - x;
        float yDistance = ((Anxiety) getWorld()).getBasicPlayer().getY() - y;
        float distance = (float) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        if (distance < aggressionDistance) {
            setX(x + (xDistance * easingAmount) * 0.05f);
            setY(y + (yDistance * easingAmount) * 0.05f);
            return true;
        }
        return false;
    }

    public void moveRandomly() {
   
    }

    @Override
    public Shape getMyShape() {
        return myShape;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        if (!destroyed) {
            if (otherObject instanceof LightShot) {
                /*  double penalty = 1 - ((Circle) otherObject.getMyShape()).getRadius() / BasicPlayer.maxRadius;
                double tempRadius = radius * penalty;
                setRadius(Math.max(0, tempRadius));*/
                radius--;
                if(radius<2){
                    setDestroyed(true);
                }
            } else if (otherObject instanceof Enemy) {
                Enemy otherEnemy = (Enemy) otherObject;
                //check if part of snake...
                if (!otherEnemy.objectProperties.containsKey("chained") && !objectProperties.containsKey("chained")) {
                    //largest survives and eats the smaller ones
                    if (otherEnemy.getRadius() > getRadius() && otherEnemy.getRadius() <= 100) {
                        destroyed = true;
                        otherEnemy.setRadius(otherEnemy.getRadius() + getRadius());
                        setRadius(0);
                    }
                }
            }
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public GameEntity getTarget() {
        return target;
    }

    public void setTarget(GameEntity target) {
        this.target = target;
    }

    public float getAmountToMoveX() {
        return amountToMoveX;
    }

    public void setAmountToMoveX(float amountToMoveX) {
        this.amountToMoveX = amountToMoveX;
    }

    public float getAmountToMoveY() {
        return amountToMoveY;
    }

    public void setAmountToMoveY(float amountToMoveY) {
        this.amountToMoveY = amountToMoveY;
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

}
