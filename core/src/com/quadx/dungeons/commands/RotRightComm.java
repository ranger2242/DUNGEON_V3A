package com.quadx.dungeons.commands;

import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;

import static com.quadx.dungeons.states.mapstate.MapStateUpdater.rotateMap;

/**
 * Created by Chris Cavazos on 1/22/2017.
 */
public class RotRightComm extends Command {
    public RotRightComm(){
        name="Rotate Left";
        //keyboard= Input.Keys.SHIFT_RIGHT;
        contB= Xbox360Pad.BUTTON_B;
    }
    @Override
    public void execute() {
        if(cls.equals(MapState.class)){
            if(pressed())
                rotateMap(true);
        }
    }
}