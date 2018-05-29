package com.quadx.dungeons.tools.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.dungeons.states.CraftState;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class CraftStateButtonHandler extends ButtonHandler {
    @Override
    public void update(float dt) {
        runCommands();
        updateMousePos();

    }
}
