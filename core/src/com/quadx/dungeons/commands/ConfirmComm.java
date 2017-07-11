package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.ControlState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import static com.quadx.dungeons.Game.commandList;

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
            if(cls.equals(ControlState.class)){
                if(!commandList.get(ControlState.selector.getPos()).getClass().equals(PauseComm.class)){
                    ControlState.selector.flipState(cls);
                }
            }
        }
    }
}
