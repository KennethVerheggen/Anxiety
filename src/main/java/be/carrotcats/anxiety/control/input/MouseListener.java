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
package be.carrotcats.anxiety.control.input;

import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class MouseListener implements EventHandler<MouseEvent> {

    /**
     * The basic player that is active
     */
    private final BasicPlayer player;
    /**
     * boolean indicating the state of the primary (left) button
     */
    private boolean primaryButtonUsed = false;
    /**
     * boolean indicating if shot was charging
     */
    private boolean charging = false;
    /**
     * the time for the end of the charge
     */
    private long chargeEnd;
    /**
     * the time for the start of the charge
     */
    private long chargeStart;

    /**
     * Constructor for mouse listeners
     *
     * @param player the active player
     */
    public MouseListener(BasicPlayer player) {
        this.player = player;
    }

    @Override
    public void handle(MouseEvent me) {
        primaryButtonUsed = me.getButton() == MouseButton.PRIMARY;
        if (me.getEventType().equals(MouseEvent.MOUSE_RELEASED) && me.getButton() == MouseButton.PRIMARY) {
            chargeEnd = System.currentTimeMillis() - chargeStart;
            charging = false;
        } else if (me.getEventType().equals(MouseEvent.MOUSE_PRESSED) && !charging && me.getButton() == MouseButton.PRIMARY) {
            charging = true;
            chargeStart = System.currentTimeMillis();
        }
        player.setX((float) me.getX());
        player.setY((float) me.getY());
    }

    public boolean wasPrimaryButton() {
        return primaryButtonUsed;
    }

    public long getChargeTime() {
        return chargeEnd;
    }

    public void setChargeTime(long i) {
        this.chargeEnd = i;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public boolean isCharging() {
        return charging;
    }

}
