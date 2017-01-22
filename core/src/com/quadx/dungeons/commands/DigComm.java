package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class DigComm extends Command{
    public DigComm(){
        name="Dig";
        keyboard= Input.Keys.SHIFT_RIGHT;
        contB=Xbox360Pad.BUTTON_LB;
        contA= Xbox360Pad.AXIS_LEFT_TRIGGER;
        if(Xbox360Pad.win10)
             axis=0;
        else axis=1;
    }
    @Override
    public void execute() {
        if (pressed())
            if (cls.equals(MapState.class)) {
                if (player.getEnergy() > 30 && !player.jumping) {
                    player.jumping = true;
                    player.setEnergy(player.getEnergy()-30);
                }
            }
    }
}
