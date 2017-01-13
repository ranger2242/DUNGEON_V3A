package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.Direction;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class LeftComm extends Command {
    public LeftComm(){
        name="Left";
        keyboard= Input.Keys.A;
        contD = Xbox360Pad.BUTTON_DPAD_LEFT;

    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MapState.class)) {
                if (player.canMove) {
                    MapStateUpdater.setAim(Direction.Facing.West);
                    player.move(new Vector2(-1, 0));
                    player.dtMove = 0;
                }
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.movePointer(-1,0);
            }
        }
    }
}
