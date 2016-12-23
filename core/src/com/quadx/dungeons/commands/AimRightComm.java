package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class AimRightComm extends Command {
    public AimRightComm(){
        name="Aim Right";
        axis=1;
        contA= Xbox360Pad.AXIS_RIGHT_X;
        keyboard= Input.Keys.L;

    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                MapStateUpdater.setAim('d');

            }
        }
    }
}
