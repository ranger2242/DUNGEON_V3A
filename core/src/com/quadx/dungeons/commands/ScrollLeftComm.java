package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class ScrollLeftComm extends Command {
    public ScrollLeftComm(){
        name="Scroll Left";
        keyboard= Input.Keys.Q;
        contA = Xbox360Pad.AXIS_LEFT_X;
        axis=-1;
    }
    @Override
    public void execute() {
        if(pressed()) {
            if(cls.equals(MapState.class))
                MapStateUpdater.scrollItems(false);
        }
    }
}
