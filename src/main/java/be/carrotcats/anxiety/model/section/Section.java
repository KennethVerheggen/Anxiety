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
package be.carrotcats.anxiety.model.section;

import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.view.AbstractGame;
import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Section extends GameEntity {

    /**
     * the section length
     */
    protected int length;
    /**
     * the shape of the rectangle
     */
    protected final Rectangle sectionRectangle;
    /**
     * boolean indicating the section was completed
     */
    protected boolean isDone = false;
    /**
     * the scrolling speed
     */
    protected int scrollingSpeed;
    /**
     * the collection of section objects
     */
    protected ArrayList<GameEntity> sectionObjects = new ArrayList<>();
    /**
     * The distance the section has travelled (to determine the finished state)
     */
    protected int distanceTravelled = 0;
    /**
     * the initial Y coordinatet
     */
    protected double initialY = 0;


    /**
     * Constructor for a section
     *
     * @param game the active game
     * @param length he section length
     * @param scrollingSpeed the speed of the section (should increase
     * gradually?)
     */
    public Section(AbstractGame game, int length, int scrollingSpeed) {
        super(game);
        this.length = length;
        this.scrollingSpeed = scrollingSpeed;
        this.sectionRectangle = new Rectangle(0, -Anxiety.APP_HEIGHT - length, Anxiety.APP_WIDTH, length);
        setY((float) sectionRectangle.getY());
        setX((float) sectionRectangle.getX());
        initialY = sectionRectangle.getY();
    }

    private boolean finishedScrolling() {
        return distanceTravelled > (Anxiety.APP_HEIGHT) + sectionRectangle.getHeight();
    }

    @Override
    public void update() {
        if (finishedScrolling()) {
            isDone = true;
            System.out.println("Section completed");
        } else {
            //scroll down
            setY(getY() + scrollingSpeed);
            distanceTravelled += scrollingSpeed;
            sectionRectangle.setY(getY());
            for (GameEntity object : sectionObjects) {
                //make sure they retain the same speed where needed?
                object.setSpeed(scrollingSpeed);
            }
        }
    }

    public ArrayList<GameEntity> getSectionObjects() {
        return sectionObjects;
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        //collision detection completely depends
    }

    public void setScrollingSpeed(int scrollingSpeed) {
        this.scrollingSpeed = scrollingSpeed;
    }

}
