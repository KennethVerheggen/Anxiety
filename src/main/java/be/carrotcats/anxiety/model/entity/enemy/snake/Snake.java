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

import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.Ball;
import be.carrotcats.anxiety.model.entity.enemy.Enemy;
import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Snake extends GameEntity {

    private final LinkedList<SnakeBodyPart> tail = new LinkedList<>();
    private int radius = 15;
    private float snakeSpeed = 0.03f;
    private final BasicPlayer mainTarget;
    private Anxiety game;

    public Snake(Anxiety world, Ball target, int length, int x, int y) {
        super(world);
        this.x = x;
        this.y = y;
        this.game = world;
        this.mainTarget = world.getBasicPlayer();
        initSnake(length);
    }

    public Snake(Anxiety world, Ball target, LinkedList<SnakeBodyPart> segment, int x, int y) {
        super(world);
        this.x = x;
        this.y = y;
        this.mainTarget = world.getBasicPlayer();
        initSnake(segment);
    }

    private void initSnake(LinkedList<SnakeBodyPart> segment) {
        objectProperties.setProperty("chained", "true");
        GameEntity tempTarget = mainTarget;
        segment.set(0, segment.getFirst().convertToHead());
        for (SnakeBodyPart enemy : segment) {
            enemy.setSpeed(snakeSpeed);
            enemy.objectProperties.setProperty("chained", "true");
            enemy.setX((float) (getX() - tail.size() * radius));
            enemy.setY((float) (getY() - tail.size() * radius));
            //enemy.setX(spawnX);
            enemy.setTarget(tempTarget);
            tempTarget = enemy;
            tail.add(enemy);
        }
    }

    private void initSnake(int length) {
        objectProperties.setProperty("chained", "true");
        GameEntity tempTarget = mainTarget;
        for (int i = 0; i < length; i++) {
            SnakeBodyPart enemy;
            if (i != 0) {
                enemy = new SnakeBodyPart(getWorld(), this, tempTarget);
            } else {
                enemy = new SnakeHead(getWorld(), this, tempTarget);
            }
            enemy.setSpeed(snakeSpeed);
            enemy.setRadius(Math.max(5, radius - i));
            enemy.objectProperties.setProperty("chained", "true");
            enemy.setX((float) (getX() - i * radius));
            enemy.setY((float) (getY() - i * radius));
            //enemy.setX(spawnX);
            enemy.setTarget(tempTarget);
            tempTarget = enemy;
            tail.add(enemy);
        }
        tail.getFirst().setRadius(1.1*radius);
    }

    @Override
    public void update() {
        //check if snake needs to be updated or destroyed
        if (tail.size() == 1) {
            tail.getFirst().setRadius(0);
            tail.getFirst().setDestroyed(true);
            tail.clear();
            game.removeObject(this);
        } else if (tail.size() > 1 && (tail.getFirst().isDestroyed())) {
            ArrayList<SnakeBodyPart> toRemove =new ArrayList<>();
            for (SnakeBodyPart enemy : tail) {
                enemy.setDestroyed(true);
                toRemove.add(enemy);
            }
            tail.removeAll(toRemove);
          } else if (tail.size() > 0) {
            //update the objects in the snake?
            move();
        }
    }

    private void move() {
        SnakeHead snakeHead = (SnakeHead) tail.getFirst();
        int snakeLength = tail.size() - 1;
        float previousX = snakeHead.getX();
        float previousY = snakeHead.getY();
        snakeHead.move();
        //    if (previousX != snakeHead.getX() && previousY != snakeHead.getY()) {
        for (int i = 1; i <= snakeLength; i++) {

            SnakeBodyPart partBefore = tail.get(i - 1);
            SnakeBodyPart thisPart = tail.get(i);

            float distance = (float) ((partBefore.getRadius() + thisPart.getRadius()) / 2);

            float xChange = partBefore.getX() - thisPart.getX();

            float yChange = partBefore.getY() - thisPart.getY();

            float angle = (float) Math.atan2(yChange, xChange);

            thisPart.setX((float) (partBefore.getX() - (float) Math.cos(angle) * distance));
            thisPart.setY((float) (partBefore.getY() - (float) Math.sin(angle) * distance));
        }
        //     }

    }

    public LinkedList<SnakeBodyPart> getTail() {
        return tail;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {

    }

}
