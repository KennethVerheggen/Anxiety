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
import be.carrotcats.anxiety.model.entity.obstacle.Wall;
import be.carrotcats.anxiety.model.section.Section;
import be.carrotcats.anxiety.view.AbstractGame;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Tunnel extends Section {

    /**
     * The left wall of the tunnel
     */
    private Wall leftWall;
    /**
     * The right wall of the tunnel
     */
    private Wall rightWall;
    /**
     * The x location of the tunnel's center
     */
    private int tunnelX;
    /**
     * The width of the tunnel
     */
    private int tunnelWidth;

    /**
     * Constructor for a tunnel
     *
     * @param game The parent game
     * @param length The length of the tunnel
     * @param scrollingSpeed The speed the tunnel migrates with
     * @param tunnelX The location of the tunnel's center
     * @param tunnelWidth The width of the tunnel
     */
    public Tunnel(Anxiety game, int length, int scrollingSpeed, int tunnelX, int tunnelWidth, double angle) {
        super(game, length, scrollingSpeed);
        createTunnel(tunnelX, tunnelWidth, angle);
    }

    /**
     * Constructor for a tunnel
     *
     * @param game The parent game
     * @param length The length of the tunnel
     * @param scrollingSpeed The speed the tunnel migrates with
     * @param tunnelX The location of the tunnel's center
     * @param tunnelWidth The width of the tunnel
     */
    public Tunnel(Anxiety game, int length, int scrollingSpeed, int tunnelX, int tunnelWidth) {
        super(game, length, scrollingSpeed);
        createTunnel(tunnelX, tunnelWidth, 0);
    }

    /**
     * Creates a tunnel with given parameters
     *
     * @param tunnelX The location of the tunnel's center
     * @param tunnelWidth The width of the tunnel
     */
    private void createTunnel(int tunnelX, int tunnelWidth, double angle) {
        this.tunnelX = tunnelX;
        this.tunnelWidth = tunnelWidth;
        //calculate the sections of the wall
        int leftWallStart = 0;
        int leftWallEnd = tunnelX - tunnelWidth / 2;
        int leftWallWidth = leftWallEnd - leftWallStart;

        int rightWallStart = tunnelX + tunnelWidth / 2;
        int rightWallEnd = AbstractGame.APP_WIDTH;
        int rightWallWidth = rightWallEnd - rightWallStart;

        //make walls
        leftWall = new Wall(getWorld(),leftWallStart, (int) getY(), leftWallWidth, length, 0);
        rightWall = new Wall(getWorld(),rightWallStart, (int) getY(), rightWallWidth, length, 0);

        //
        if (angle != 0) {
            Rotate rotate = new Rotate(angle);
            leftWall.getMyShape().getTransforms().add(rotate);
            rightWall.getMyShape().getTransforms().add(rotate);
        }
//add to the section list of objects
        getSectionObjects().add(leftWall);
        getSectionObjects().add(rightWall);
    }

    public int getTunnelX() {
        return tunnelX;
    }

    public void setTunnelX(int tunnelX) {
        this.tunnelX = tunnelX;
    }

    public int getTunnelWidth() {
        return tunnelWidth;
    }

    public void setTunnelWidth(int tunnelWidth) {
        this.tunnelWidth = tunnelWidth;
    }

}
