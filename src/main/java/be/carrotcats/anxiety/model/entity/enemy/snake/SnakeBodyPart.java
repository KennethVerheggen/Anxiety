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
package be.carrotcats.anxiety.model.entity.enemy.snake;

import be.carrotcats.anxiety.control.util.audio.AudioClipPlayer;
import be.carrotcats.anxiety.model.entity.enemy.*;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.player.LightShot;
import be.carrotcats.anxiety.view.AbstractGame;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class SnakeBodyPart extends Enemy {
    //boolean indicatitng if the bodypart is the head

    private Snake motherSnake;
    private Float[] lastMove = new Float[]{0f, 0f};

    /**
     * Generates a snake bodypart
     *
     * @param target the target this enemy will be following
     * @param motherSnake the parent snake
     * @param game the active game
     */
    public SnakeBodyPart(AbstractGame game, Snake motherSnake, GameEntity target) {
        super(game, target);
        this.speed = 0.05f;
        this.motherSnake = motherSnake;
    }

    @Override
    public void update() {
        //the head has the option to make all the others move with the same speed
        //don't automatically call movement update on a bodypart, let the snake object handle this
        if (radius <= 2 && !isDestroyed()) {
            setDestroyed(true);
            new AudioClipPlayer(AudioClipPlayer.getRandomEnemyDeadSound()).start();
        }
        if (!isDestroyed()) {
            //   setY(getY() + speed * 0.05f);
            myCircle.setRadius(radius);
        }
    }

    public void move() {
        super.update();
    }

    public boolean isHead() {
        int myIndex = motherSnake.getTail().indexOf(this);
        return motherSnake != null && myIndex <= 1;
    }

    public void setParent(Snake childSnake) {
        this.motherSnake = childSnake;
    }

    public Float[] getLastMove() {
        return lastMove;
    }

    public void setLastMove(Float[] lastMove) {
        this.lastMove = lastMove;
    }

    public Snake getMotherSnake() {
        return motherSnake;
    }

    public void setMotherSnake(Snake motherSnake) {
        this.motherSnake = motherSnake;
    }

    public SnakeHead convertToHead() {
        return new SnakeHead(this);
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        if (!isDestroyed()) {
            if (otherObject instanceof LightShot) {
                /*  double penalty = 1 - ((Circle) otherObject.getMyShape()).getRadius() / BasicPlayer.maxRadius;
                double tempRadius = radius * penalty;
                setRadius(Math.max(0, tempRadius));*/
                //my index
                int myIndex = getMotherSnake().getTail().indexOf(this);
                for (SnakeBodyPart bodyPart : getMotherSnake().getTail().subList(myIndex, getMotherSnake().getTail().size())) {
                    bodyPart.setRadius(bodyPart.getRadius() - 1);
                    if (bodyPart.getRadius() < 2) {
                        bodyPart.setDestroyed(true);
                    }
                }
            } else {
                super.handleCollision(otherObject);
            }
        }
    }
}
