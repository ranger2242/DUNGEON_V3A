package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.Direction;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class AimUpComm extends Command {
    public AimUpComm(){
        name="Aim Up";
        axis=-1;
        contA= Xbox360Pad.AXIS_RIGHT_Y;
        keyboard= Input.Keys.UP;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                MapStateUpdater.setAim(Direction.Facing.North);

            }
        }
    }
}
