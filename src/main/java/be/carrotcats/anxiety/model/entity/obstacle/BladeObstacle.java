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
package be.carrotcats.anxiety.model.entity.obstacle;

import be.carrotcats.anxiety.control.util.ShapeLibrary;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.view.AbstractGame;
import java.util.LinkedList;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class BladeObstacle extends PolygonObstacle {

    /**
     * Boolean indicating the direction of the blade
     */
    private boolean clockWise;
    /**
     * the speed the blades spin with
     */
    private double angularSpeed;

    /**
     * Constructor for blade obstacles
     *
     * @param world the active game world
     * @param angularSpeed the speed blades spin with
     * @param d the scale of the blade
     */
    public BladeObstacle(AbstractGame world, double angularSpeed, double d) {
        super(world, d, ShapeLibrary.PLUS);
        myPolygon.getTransforms().add(rotate);
        this.angularSpeed = angularSpeed;
    }

    /**
     * Constructor for blade obstacles
     *
     * @param world the active game world
     * @param angularSpeed the speed blades spin with
     */
    public BladeObstacle(AbstractGame world, double angularSpeed) {
        super(world, ShapeLibrary.PLUS);
        myPolygon.getTransforms().add(rotate);
        this.angularSpeed = angularSpeed;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        //don't influence spinning blades
    }

    public boolean isClockWise() {
        return clockWise;
    }

    public void setClockWise(boolean clockWise) {
        this.clockWise = clockWise;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public void setAngularSpeed(double angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    @Override
    public void update() {
        if (clockWise) {
            angle += angularSpeed;
        } else {
            angle -= angularSpeed;
        }
        setX(getX() + getDx());
        setY(getY() + getDy() + speed);
        rotate.setAngle(angle);
    }

    @Override
    public void checkCollisions(LinkedList<GameEntity> objectsToRender) {

    }
}
