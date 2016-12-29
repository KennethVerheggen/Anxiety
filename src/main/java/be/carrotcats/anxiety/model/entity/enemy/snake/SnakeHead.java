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
import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.enemy.Enemy;
import be.carrotcats.anxiety.model.entity.obstacle.PolygonObstacle;
import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import be.carrotcats.anxiety.model.entity.player.LightShot;
import be.carrotcats.anxiety.view.AbstractGame;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class SnakeHead extends SnakeBodyPart {

    private float vx = (float) ((Math.random() * speed));
    private float vy = (float) ((Math.random() * speed));

    public SnakeHead(AbstractGame game, Snake motherSnake, GameEntity target) {
        super(game, motherSnake, target);
        aggressionDistance=(float) (5*radius);
    }

    public SnakeHead(SnakeBodyPart part) {
        super(part.getWorld(), part.getMotherSnake(), part.getTarget());
                aggressionDistance=(float) (5*radius);
    }

    @Override
    public void move() {
        //don't move if just the head is there, make the head explode?
        if (getMotherSnake().getTail().size() == 1||getRadius()<10) {
            this.setDestroyed(true);
                   new AudioClipPlayer(AudioClipPlayer.getRandomEnemyDeadSound()).start();
        } else if (!super.moveToTarget()) {
            if (vx > speed) {
                vx = speed * vx / Math.abs(vx);
            }
            if (vy > speed) {
                vy = speed * vy / Math.abs(vy);
            }

            if (x <= radius) {
                setX((float) radius);
                vx = (float) ((Math.random() * speed));
            }
            if (x >= Anxiety.APP_WIDTH - radius) {
                x = (int) (Anxiety.APP_WIDTH - radius);
                vx = -(float) ((Math.random() * speed));
            }
            setX(x + vx);

            if (y <= radius) {
                setY((float) radius);
                vy = (float) ((Math.random() * speed));;
            }
            if (y >= Anxiety.APP_HEIGHT - radius) {
                y = (int) (Anxiety.APP_HEIGHT - radius);
                vy = -(float) ((Math.random() * speed));
            }
            setY(y + vy);
        }
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        if (otherObject instanceof BasicPlayer) {
            //try to make snake bite animation
            if (vx > 0) {
                setX((float) (x - (4 * radius)));

            } else {
                setX((float) (x + (4 * radius)));

            }
            if (vy > 0) {
                setY((float) (y - (4 * radius)));

            } else {
                setY((float) (y + (4 * radius)));

            }
        } else if (PolygonObstacle.class.isAssignableFrom(otherObject.getClass())) {
            vx = -vx;
            vy = -vy;
        } else if (otherObject instanceof LightShot) {
            for (SnakeBodyPart bodyPart : getMotherSnake().getTail()) {
                bodyPart.setRadius(bodyPart.getRadius() - 1);
                if (bodyPart.getRadius() < 2) {
                    bodyPart.setDestroyed(true);
                }
            }
        }
    }
}
