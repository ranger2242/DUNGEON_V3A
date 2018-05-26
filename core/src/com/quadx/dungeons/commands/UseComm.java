package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class UseComm extends Command{
    public UseComm(){
        name="Use";
        keyboard= Input.Keys.C;
        contB = Xbox360Pad.BUTTON_X;
    }
    @Override
    public void execute() {
        if(pressed()) {
            if(cls.equals(MapState.class)){
                Game.player.useItem();
            }
        }
    }
}
