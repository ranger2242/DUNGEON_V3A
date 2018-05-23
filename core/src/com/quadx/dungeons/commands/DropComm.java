package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class DropComm extends Command {
    public DropComm(){
        name="Drop";
        keyboard= Input.Keys.X;
        contD= Xbox360Pad.BUTTON_DPAD_DOWN;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class))
                Inventory.discard(player.pos(),true,null);
        }
    }
}
