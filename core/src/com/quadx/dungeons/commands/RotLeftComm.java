package com.quadx.dungeons.commands;

import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

/**
 * Created by Chris Cavazos on 1/22/2017.
 */
public class RotLeftComm extends Command {
    public RotLeftComm(){
        name="Rotate Left";
        //keyboard= Input.Keys.SHIFT_RIGHT;
        contB= Xbox360Pad.BUTTON_Y;
    }
    @Override
    public void execute() {
        if(cls.equals(MapState.class)){
            if(pressed())
                GridManager.rotateMap(true);
        }
    }
}
