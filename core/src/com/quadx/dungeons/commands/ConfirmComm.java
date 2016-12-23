package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.MainMenuState;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class ConfirmComm extends Command {
    public ConfirmComm(){
        name="Confirm";
        keyboard= Input.Keys.ENTER;
        contB = Xbox360Pad.BUTTON_A;
    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.selectOption();
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.selectAbiltiy();
            }
        }
    }
}
