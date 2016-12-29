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
package be.carrotcats.anxiety.model.entity;

import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.view.AbstractGame;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Ball extends GameEntity {

    /**
     * The speed the ball moves with
     */
    public float speed = 0.05f;
    /**
     * the radius of the ball
     */
    public double radius = 15;

    /**
     * The inner shape of the circle, this is a utility objectt
     */
    protected Circle myCircle;

    /**
     * Constructor for a spheroid
     * @param world the active game world
     */
    public Ball(AbstractGame world) {
        super(world);
        innerColor = Color.WHITE;
        myCircle = new Circle(15);
        myCircle.setFill(innerColor);
        myCircle.setCenterX(x);
        myCircle.setCenterY(y);
        myShape = myCircle;
    }

    @Override
    public void update() {
        myCircle.setRadius(radius);
        setX(getX() + getDx());
        setY(getY() + getDy());
    }

 
    @Override
    public Shape getMyShape() {
        return myShape;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
        double tempRadius = radius + 1 - (2 * Math.random());
        myCircle.setRadius(tempRadius);
    }

    @Override
    public void handleCollision(GameEntity otherObject) {

    }

}
