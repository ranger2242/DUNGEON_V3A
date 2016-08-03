package com.quadx.dungeons.tools;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Game;

public class MyTextInputListener implements Input.TextInputListener {
    @Override
    public void input (String text) {
        Game.player.setName(text);
    }

    @Override
    public void canceled () {
    }
}
