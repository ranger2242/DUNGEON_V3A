package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class ScrollRightComm extends Command {
    public ScrollRightComm(){
        name="Scroll Right";
        keyboard= Input.Keys.E;
        contD=Xbox360Pad.BUTTON_DPAD_RIGHT;

    }
    @Override
    public void execute() {
        if(pressed()) {
            if(cls.equals(MapState.class))
                Inventory.scrollItems(true);
        }
    }
}
