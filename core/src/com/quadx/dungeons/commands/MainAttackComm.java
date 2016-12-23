package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class MainAttackComm extends Command {
    public MainAttackComm(){
        name="Main Attack";
        keyboard= Input.Keys.SPACE;
        contA = Xbox360Pad.AXIS_RIGHT_TRIGGER;
        axis=1;
        if(!Xbox360Pad.win10) axis=-1;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                MapStateUpdater.useMainAtt();
            }
        }
    }
}
