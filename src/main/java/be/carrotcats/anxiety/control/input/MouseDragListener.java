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
import javafx.scene.input.MouseDragEvent;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class MouseDragListener implements EventHandler<MouseDragEvent> {

    /**
     * The active player
     */
    private final BasicPlayer player;

    /**
     * Constructor for mouse drag event listeners
     *
     * @param player the active player
     */
    public MouseDragListener(BasicPlayer player) {
        this.player = player;
    }

    @Override
    public void handle(MouseDragEvent me) {
        System.out.println(me.getButton());
        player.setX((float) me.getX());
        player.setY((float) me.getY());
    }
}
