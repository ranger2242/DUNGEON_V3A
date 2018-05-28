package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class LeftComm extends Command {
    public LeftComm(){
        name="Left";
        axis=-1;

        keyboard= Input.Keys.A;
        contA = Xbox360Pad.AXIS_LEFT_X;

    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MapState.class)) {
                if (player.canMove) {
                    player.move(new Vector2(-1, 0));
                }
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.movePointer(-1,0);
            }
        }
    }
}
