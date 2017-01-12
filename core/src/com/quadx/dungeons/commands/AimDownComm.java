package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.Direction;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class AimDownComm extends Command {
    public AimDownComm(){
        name="Aim Down";
        axis=1;
        contA= Xbox360Pad.AXIS_RIGHT_Y;
        keyboard= Input.Keys.DOWN;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                MapStateUpdater.setAim(Direction.Facing.South);
            }
        }
    }
}
