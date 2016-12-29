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
import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import be.carrotcats.anxiety.model.section.Section;
import be.carrotcats.anxiety.model.section.impl.BladedFence;
import be.carrotcats.anxiety.model.section.impl.Maze;
import be.carrotcats.anxiety.model.section.impl.ObstacleField;
import be.carrotcats.anxiety.model.section.impl.Spinner;
import be.carrotcats.anxiety.model.section.impl.Tunnel;
import java.util.Random;
import static be.carrotcats.anxiety.view.AbstractGame.APP_WIDTH;

/**
 * This class generates the sections to use in the game
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class SectionFactory {

    /**
     * The amount of sections that were finished
     */
    private static int sectionsPassed = 0;
    /**
     * The minimal length of a section
     */
    public static final int MIN_LEN = 500;
    /**
     * Tthe maximal length of a section
     */
    public static final int MAX_LEN = 1000;
    /**
     * The minimal section speed
     */
    public static final int MIN_SPEED = 5;
    /**
     * The minimal amount of objects in a "storm"
     */
    public static final int MIN_ENEMIES = 10;
    /**
     * A shared instance for a random generator
     */
    private static final Random RANDOM = new Random();
    /**
     * Boolean indicating whether the next section is clear or obstacle filled
     */
    private static boolean calm = false;

    /**
     * Generates a section
     *
     * @param game the game to return a section to
     * @return a new game section
     */
    public static Section generateSection(Anxiety game) {
        sectionsPassed++;
        //check the difficulty...up it every 5 sections?
        //    int difficulty = 1 + sectionsPassed / 5;
        if (calm) {
            calm = false;
            //int pick = 1 + RANDOM.nextInt(Math.min(5, difficulty));
            int pick = 1 + RANDOM.nextInt(4);
            switch (pick) {
                case 1:
                    System.out.println("Incoming tunnel !!!");
                    return generateMaze(game);
                case 2:
                    System.out.println("Incoming blade !!!");
                    return generateBlade(game);
                case 3:
                    System.out.println("Incoming rain !!!");
                    return generateRain(game);
                default:
                    System.out.println("Incoming Gate !!!");
                    return generateBlades(game);
            }
        } else {
            calm = true;
            System.out.println("Phew, calming down...");
            return generateSpace(game);
        }
    }

    /* //@ToDo check this lenght later
    private static Tunnel generateTunnel(Anxiety game) {
        int tunnelLength = 450;
        int tunnelWidth = (10 + RANDOM.nextInt(10)) * BasicPlayer.MIN_RADIUS + RANDOM.nextInt(BasicPlayer.MAX_RADIUS - BasicPlayer.MIN_RADIUS);
        int newX = RANDOM.nextInt((appWidth - tunnelWidth / 2 - tunnelWidth / 2) + 1) + tunnelWidth / 2;
        Tunnel tunnel = new Tunnel(game, tunnelLength, (int) (MIN_SPEED + (sectionsPassed * 0.5)), newX, tunnelWidth, 35);
        tunnel.setScrollingSpeed((int) Math.max(MIN_SPEED, (tunnel.getSpeed() + (sectionsPassed / 3))));
        return tunnel;
    }*/
    private static Maze generateMaze(Anxiety game) {
        int tunnelLength = (int) (1.5 * Anxiety.APP_HEIGHT) + RANDOM.nextInt(MAX_LEN / 2) + MAX_LEN / 2;
        int tunnelWidth = (int) (3 * (BasicPlayer.MAX_RADIUS));
        int amountOfCorridors = 3 + RANDOM.nextInt(2);
        int tunnelX[] = new int[amountOfCorridors];

        for (int i = 0; i < amountOfCorridors; i++) {
            tunnelX[i] = tunnelWidth / 2 + RANDOM.nextInt((APP_WIDTH - tunnelWidth / 2));
        }
        Maze maze = new Maze(game, tunnelLength, (int) (MIN_SPEED + (sectionsPassed * 0.5)), tunnelWidth, tunnelX);
        int tempSpeed = (int) Math.max(MIN_SPEED, (maze.getSpeed() + (sectionsPassed / 3)));
        if (tempSpeed > 3 * MIN_SPEED) {
            tempSpeed = 3 * MIN_SPEED;
        }
        maze.setScrollingSpeed(tempSpeed);

        return maze;
    }

    private static Section generateBlade(Anxiety game) {
        int tunnelLength = (int) (1.5 * Anxiety.APP_HEIGHT) + MIN_LEN + Math.max(0, RANDOM.nextInt(MAX_LEN / 2 + MAX_LEN / (2 * sectionsPassed)));
        Spinner section = new Spinner(game, tunnelLength, 3);
        section.createSpinner(MIN_SPEED, MIN_SPEED, 0, APP_WIDTH);
        section.setSpeed((int) (2 + (sectionsPassed * 0.5)));
        section.setScrollingSpeed((int) section.getSpeed());
        return section;
    }

    private static Section generateBlades(Anxiety game) {
        int tunnelLength = (int) (1.5 * Anxiety.APP_HEIGHT) + MIN_LEN + Math.max(0, RANDOM.nextInt(MAX_LEN / 2 + MAX_LEN / (2 * sectionsPassed)));
        BladedFence section = new BladedFence(game, tunnelLength, 3);
        section.createGate(MIN_SPEED, MIN_SPEED, 0, APP_WIDTH);
        section.setSpeed((int) (2 + (sectionsPassed * 0.5)));
        section.setScrollingSpeed((int) section.getSpeed());
        return section;
    }

    private static Section generateSpace(Anxiety game) {
        int tunnelLength = (int) (Anxiety.APP_HEIGHT) + MIN_LEN + Math.max(0, RANDOM.nextInt(MAX_LEN / 2 + MAX_LEN / (2 * sectionsPassed)));
        Section section = new Section(game, tunnelLength, MIN_SPEED);
        section.setScrollingSpeed((int) (MIN_SPEED + (sectionsPassed * 0.5)));
        section.objectProperties.setProperty("zen", "true");
        return section;
    }

    private static Section generateRain(Anxiety game) {
        int fieldLength = (int) (1.5 * Anxiety.APP_HEIGHT) + MIN_LEN + Math.max(0, RANDOM.nextInt(MAX_LEN / 2 + MAX_LEN / (2 * sectionsPassed)));
        ObstacleField section = new ObstacleField(game, fieldLength, MIN_SPEED);
        int scrollingSpeed = (int) (MIN_SPEED + (sectionsPassed * 0.5));
        section.setScrollingSpeed(scrollingSpeed);
        section.createRain(RANDOM.nextInt(MIN_ENEMIES), scrollingSpeed, (scrollingSpeed + RANDOM.nextInt(MIN_ENEMIES)));
        return section;
    }

    public static int getSectionsPassed() {
        return sectionsPassed;
    }

    public static boolean isCalm() {
        return calm;
    }

    public static void reset() {
        sectionsPassed = 0;
    }

}
