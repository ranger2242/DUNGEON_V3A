package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class AltAttackComm extends Command {
    public AltAttackComm(){
        name="Alt Attack";
        keyboard= Input.Keys.ALT_LEFT;
        contB = Xbox360Pad.BUTTON_RB;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                //MapStateUpdater.useAltAtt();
            }
        }
    }
}
