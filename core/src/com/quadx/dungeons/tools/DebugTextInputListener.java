package com.quadx.dungeons.tools;

import com.badlogic.gdx.Input;

public class DebugTextInputListener implements Input.TextInputListener {
    @Override
    public void input (String text) {
        Tests.parseCommand(text);
    }

    @Override
    public void canceled () {
    }
}