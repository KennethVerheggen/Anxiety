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
package be.carrotcats.anxiety.control.factory.gameobjects;

import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.entity.enemy.Enemy;
import be.carrotcats.anxiety.model.entity.enemy.snake.Snake;
import be.carrotcats.anxiety.model.entity.enemy.snake.SnakeBodyPart;
import java.util.Random;
import static be.carrotcats.anxiety.view.AbstractGame.APP_WIDTH;
import static be.carrotcats.anxiety.view.AbstractGame.APP_HEIGHT;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class EnemyFactory {

    /**
     * A shared instance for a random generator
     */
    private static final Random RANDOM = new Random();
    /**
     * the maximal amount of enemies allowed on screen
     */
    public static final int MAX_ENEMIES = 50;

    /**
     * Spawn enemies in game
     *
     * @param game the active game
     * @param maxAmount the maximal amountt of spawnable enemies
     */
    public static void spawnEnemies(Anxiety game, int maxAmount) {
        //add a random amount of enemies enemy
        int amount = Math.min(maxAmount, RANDOM.nextInt(5 + SectionFactory.getSectionsPassed()));
        //spawn either a snake or something else?
         if (RANDOM.nextBoolean()) {
            spawnBallEnemies(game, amount);
        } else {
        spawnSnakeEnemy(game, amount);
           }
    }

    public static void spawnBallEnemies(Anxiety game, int amount) {
        for (int i = 0; i < amount; i++) {
            Enemy enemy = new Enemy(game, game.getBasicPlayer());
            float radius = 5 + RANDOM.nextFloat() * 15;
            enemy.setX(RANDOM.nextInt(APP_WIDTH));
            enemy.setY(RANDOM.nextInt(APP_HEIGHT));
            enemy.setRadius(radius);
            game.addEnemy(enemy);
        }
    }

    public static void spawnSnakeEnemy(Anxiety game, int amount) {
        Snake snake = new Snake(game, game.getBasicPlayer(),
                3 + (SectionFactory.getSectionsPassed() + amount),
                15 + RANDOM.nextInt(APP_WIDTH - 15),
                APP_HEIGHT / 4 + RANDOM.nextInt(APP_HEIGHT / 4));
        game.addObject(snake);
        for (SnakeBodyPart enemy : snake.getTail()) {
            game.addEnemy(enemy);
        }
    }

}
