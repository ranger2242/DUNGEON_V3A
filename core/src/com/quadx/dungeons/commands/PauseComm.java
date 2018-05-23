package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class PauseComm extends Command {
    public PauseComm(){
        name="Pause";
        keyboard= Input.Keys.ESCAPE;
        contB= Xbox360Pad.BUTTON_START;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class))
                MapState.pause();
        }
    }
}
