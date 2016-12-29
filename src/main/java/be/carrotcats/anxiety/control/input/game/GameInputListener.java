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
package be.carrotcats.anxiety.control.input.game;

import be.carrotcats.anxiety.control.input.KeyboardListener;
import be.carrotcats.anxiety.control.input.MouseDragListener;
import be.carrotcats.anxiety.control.input.MouseListener;
import be.carrotcats.anxiety.game.Anxiety;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class GameInputListener {

    /**
     * The active keyboard listener
     */
    private KeyboardListener keyboardListener;
    /**
     * The active mouse movement listener
     */
    private MouseListener mouseListener;
    /**
     * The active mouse drag listener
     */
    private MouseDragListener mouseDragListener;

    /**
     * Constructor for the input listener wrapper
     *
     * @param game the active game
     */
    public GameInputListener(Anxiety game) {
        this.keyboardListener = new KeyboardListener(game);
        this.mouseListener = new MouseListener(game.getBasicPlayer());
        this.mouseDragListener = new MouseDragListener(game.getBasicPlayer());
    }

    /**
     * Constructor for the input listener wrapper
     *
     * @param keyboardListener active keyboard listener
     * @param mouseListener active mouse movement listener
     * @param mouseDragListener active mouse drag listener
     */
    public GameInputListener(
            KeyboardListener keyboardListener,
            MouseListener mouseListener,
            MouseDragListener mouseDragListener
    ) {
        this.keyboardListener = keyboardListener;
        this.mouseListener = mouseListener;
        this.mouseDragListener = mouseDragListener;
    }

    public KeyboardListener getKeyboardListener() {
        return keyboardListener;
    }

    public void setKeyboardListener(KeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public MouseDragListener getMouseDragListener() {
        return mouseDragListener;
    }

    public void setMouseDragListener(MouseDragListener mouseDragListener) {
        this.mouseDragListener = mouseDragListener;
    }

}
