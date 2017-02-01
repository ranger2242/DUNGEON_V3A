package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.states.*;

/**
 * Created by Chris Cavazos on 8/12/2016.
 */
public class BackComm extends Command {
    public BackComm(){
        name="Back";
        keyboard= Input.Keys.TAB;
        contB = Xbox360Pad.BUTTON_B;
    }
    @Override
    public void execute() {
        if(pressed()) {
            if (cls.equals(AbilitySelectState.class)) {
                AbilitySelectState.exit();
            }
            if (cls.equals(ControlState.class)) {
                ControlState.exit();
            }
            if(cls.equals(HighScoreState.class)){
                HighScoreState.exit();
            }
            if(cls.equals(OptionState.class)){
                OptionState.exit();
            }
            if(cls.equals(ShopState.class)){
                ShopState.exit();
            }
        }
    }
}
