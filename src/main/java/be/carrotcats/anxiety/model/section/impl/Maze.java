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
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.obstacle.Wall;
import be.carrotcats.anxiety.model.section.Section;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Maze extends Section {

    /**
     * Constructor for a maze
     *
     * @param game The parent game
     * @param length The length of each tunnel
     * @param scrollingSpeed The speed the maze migrates with
     * @param tunnelCenters The location of the tunnel entries (center)
     * @param tunnelWidth The width of the tunnel
     */
    public Maze(Anxiety game, int length, int scrollingSpeed, int tunnelWidth, int... tunnelCenters) {
        super(game, length, scrollingSpeed);
        createMaze(game, length, scrollingSpeed, 2 * tunnelWidth, tunnelCenters);
    }

    /**
     * Creates a short maze with given parameters
     *
     * @param tunnelCenters The location of the tunnel centers
     * @param tunnelWidth The width of the tunnel
     */
    private void createMaze(Anxiety game, int length, int scrollingSpeed, int tunnelWidth, int... tunnelCenters) {
        //calculate the tunnel height, but leave room for players to move between 
        double tunnelHeight = (double)(tunnelWidth + (length / tunnelCenters.length)) / 2;
        for (int index = 0; index < tunnelCenters.length; index++) {
            Tunnel tunnel = new Tunnel(game, length, scrollingSpeed, tunnelCenters[index], tunnelWidth);
            //place tunnel in right spot
            for (GameEntity entity : tunnel.getSectionObjects()) {
                if (entity instanceof Wall) {
                    Rectangle shape = (Rectangle) entity.getMyShape();
                    //change the height
                    shape.setHeight(tunnelHeight);
                    //move the rectangle up with 2x the height
                    int newY =(int) (2 * (index+1) * tunnelHeight);
                    entity.setY(newY);
                 }
                getSectionObjects().add(entity);
            }
        }
    }

}
