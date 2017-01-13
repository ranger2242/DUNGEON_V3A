package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.Direction;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class DownComm extends Command {
    public DownComm(){
        name="Down";
        keyboard= Input.Keys.S;
        contD = Xbox360Pad.BUTTON_DPAD_DOWN;


    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.decrementSelector();
            }
            if (cls.equals(MapState.class)) {
                if (player.canMove) {
                    MapStateUpdater.setAim(Direction.Facing.South);
                    player.move(new Vector2(0, -1));
                    player.dtMove = 0;
                }
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.movePointer(0,1);
            }
        }
    }
}
