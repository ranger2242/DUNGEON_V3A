package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class AimUpComm extends Command {
    public AimUpComm(){
        name="Aim Up";
        axis=-1;
        contA= Xbox360Pad.AXIS_RIGHT_Y;
        keyboard= Input.Keys.I;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                MapStateUpdater.setAim('w');

            }
        }
    }
}
