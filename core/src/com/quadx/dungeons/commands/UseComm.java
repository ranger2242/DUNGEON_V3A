package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class UseComm extends Command{
    public UseComm(){
        name="Use";
        keyboard= Input.Keys.C;
        contB = Xbox360Pad.BUTTON_Y;
    }
    @Override
    public void execute() {
        if(pressed()) {
            if(cls.equals(MapState.class)){
                MapStateUpdater.selectItemFromInventory();
            }
        }
    }
}
