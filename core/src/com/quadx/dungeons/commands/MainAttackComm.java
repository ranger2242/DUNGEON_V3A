package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class MainAttackComm extends Command {
    public MainAttackComm(){
        name="Main Attack";
        keyboard= Input.Keys.SPACE;
        contB= Xbox360Pad.BUTTON_RB;
    }
    @Override
    public void execute() {
        if(pressed()){
            if(cls.equals(MapState.class)){
                player.getAttack().use();
            }
        }
    }
}
