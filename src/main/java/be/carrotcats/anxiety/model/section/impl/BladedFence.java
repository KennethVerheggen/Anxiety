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
import be.carrotcats.anxiety.model.entity.obstacle.BladeObstacle;
import be.carrotcats.anxiety.model.section.Section;
import java.util.Random;
import javafx.geometry.Bounds;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class BladedFence extends Section {

    private static final Random RANDOM = new Random();

    /**
     * Constructor for a fence
     *
     * @param game the active game world
     * @param length the length of the fence
     * @param scrollingSpeed the speed the fence migrates with
     */
    public BladedFence(Anxiety game, int length, int scrollingSpeed) {
        super(game, length, scrollingSpeed);
    }

    /**
     * Creates a gate
     *
     * @param minSpeed the minimal speed
     * @param maxSpeed the maximal speed
     * @param xBegin the beginning x position
     * @param xEnd the ending x position
     */
    public void createGate(float minSpeed, float maxSpeed, int xBegin, int xEnd) {
        //a gate is a sequence of normal plus signs side by side
        //get a plus
        BladeObstacle singlePlus = new BladeObstacle(getWorld(), minSpeed);
        Bounds bounds = singlePlus.getMyShape().getBoundsInLocal();
        double width = bounds.getWidth();
        //check how many plusses we need to cover the with of the gap (for now this is the app width)
        int gapSize = xEnd - xBegin;
        double scaleFactor = 1;
        //if the gapSize is smaller, we need to downscale the polygon?
        if (gapSize < width) {
            scaleFactor = gapSize / width;
        }
        int plussesNeeded = (int) (gapSize / width);
        float tempX = 0;
        int spawnableZone = (int) (sectionRectangle.getHeight() - (2 * Anxiety.APP_HEIGHT));
        float tempY = (float) (sectionRectangle.getY() + Anxiety.APP_HEIGHT) + RANDOM.nextInt((int) spawnableZone);

        for (int i = 0; i < plussesNeeded; i++) {
            BladeObstacle temp = new BladeObstacle(getWorld(), (0.95 * scaleFactor));
            tempX += temp.getMyShape().getBoundsInLocal().getWidth();
            temp.setX(tempX);
            temp.setY(tempY);
            temp.setClockWise(i % 2 == 0);
            sectionObjects.add(temp);
        }

    }

}
