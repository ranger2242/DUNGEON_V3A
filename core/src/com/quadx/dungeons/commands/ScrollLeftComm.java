package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class ScrollLeftComm extends Command {
    public ScrollLeftComm(){
        name="Scroll Left";
        keyboard= Input.Keys.Q;
        contD=Xbox360Pad.BUTTON_DPAD_LEFT;
    }
    @Override
    public void execute() {
        if(pressed()) {
            if(cls.equals(MapState.class))
                MapStateUpdater.scrollItems(false);
        }
    }
}
