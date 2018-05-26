package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.Direction;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class RightComm extends Command {
    public RightComm(){
        name="Right";
        keyboard= Input.Keys.D;
        contA = Xbox360Pad.AXIS_LEFT_X;
        axis=1;

    }
    @Override
    public void execute() {
        if (pressed()) {
            if (cls.equals(MapState.class)) {
                if (player.canMove) {
                    player.setAim(Direction.Facing.East);
                    player.move(new Vector2(1, 0));
                }
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.movePointer(1,0);
            }
        }
    }
}
