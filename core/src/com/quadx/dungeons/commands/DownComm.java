package com.quadx.dungeons.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.MainMenuState;

import static com.quadx.dungeons.Game.controllerMode;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class DownComm extends Command {
    public DownComm(Class c){
        cls=c;
        name="Down";
        keyboard= Input.Keys.S;
        controller= Xbox360Pad.AXIS_LEFT_Y;
    }
    @Override
    public void execute() {
        if (Gdx.input.isKeyPressed(keyboard) || (controllerMode && Xbox360Pad.getLDown())) {
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.decrementSelector();
            }
        }
    }

    @Override
    public void changeKey(int k) {

    }

    @Override
    public void changeCont(int c) {

    }
}
