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
package be.carrotcats.anxiety.model.section.impl;

import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.entity.obstacle.PolygonObstacle;
import be.carrotcats.anxiety.model.section.Section;
import java.util.Random;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class ObstacleField extends Section {

    private static final Random RANDOM = new Random();

    /**
     * Constructor for an obstacle field
     *
     * @param game the active game world
     * @param length the length of the fence
     * @param scrollingSpeed the speed the fence migrates with
     */
    public ObstacleField(Anxiety game, int length, int scrollingSpeed) {
        super(game, length, scrollingSpeed);
    }

    /**
     * Creates a gate
     *
     * @param minSpeed the minimal speed
     * @param maxSpeed the maximal speed
     * @param amount the beginning x position
     */
    public void createRain(int amount, float minSpeed, float maxSpeed) {
        for (int i = 0; i < amount; i++) {
            float tempX = RANDOM.nextInt(Anxiety.APP_WIDTH);
            //must be within the bottom of the sectionrectangle and the head minus the screenheight (so it all finishes properly
            //100 is the bufferspace above 

            int spawnableZone = Math.abs((int) ((0.75 * sectionRectangle.getHeight()) + Anxiety.APP_HEIGHT));
            if (spawnableZone > 100) {
                float tempY = RANDOM.nextInt(Anxiety.APP_HEIGHT) - Anxiety.APP_HEIGHT;
                float scale = 0.5f + 2 * RANDOM.nextFloat();
                PolygonObstacle hexagon = new PolygonObstacle(getWorld(), scale).applyRandomRotation();
                hexagon.setX(tempX);
                hexagon.setY(tempY);
                sectionObjects.add(hexagon);
            }
        }
    }

}
