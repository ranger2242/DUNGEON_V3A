package com.quadx.dungeons.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.MainMenuState;

import static com.quadx.dungeons.Game.controllerMode;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class ConfirmComm extends Command {
    public ConfirmComm(Class o){
        name="Confirm";
        cls=o;
        keyboard= Input.Keys.ENTER;
        controller= Xbox360Pad.BUTTON_A;
    }
    @Override
    public void execute() {
        if (Gdx.input.isKeyPressed(keyboard) || (controllerMode && MainMenuState.controller.getButton(controller))) {
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.selectOption();
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
