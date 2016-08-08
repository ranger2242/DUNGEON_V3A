package com.quadx.dungeons.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.MainMenuState;

import static com.quadx.dungeons.Game.controllerMode;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class UpComm extends Command {
    public UpComm(Class c){
        cls=c;
        name="Up";
        keyboard= Input.Keys.W;
        controller= Xbox360Pad.AXIS_LEFT_Y;
    }
    @Override
    public void execute() {
        if (Gdx.input.isKeyPressed(keyboard) || (controllerMode && Xbox360Pad.getLUp())) {
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.incrementSelector();
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
