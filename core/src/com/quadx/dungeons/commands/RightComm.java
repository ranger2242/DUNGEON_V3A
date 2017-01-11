package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.mapstate.MapState;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class RightComm extends Command {
    public RightComm(){
        name="Right";
        keyboard= Input.Keys.D;
        contD = Xbox360Pad.BUTTON_DPAD_RIGHT;

    }
    @Override
    public void execute() {
        if (pressed()) {
            if (cls.equals(MapState.class)) {
                if (player.canMove) {
                    player.move2(new Vector2(1, 0));
                    player.dtMove = 0;
                }
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.movePointer(1,0);
            }
        }
    }
}
