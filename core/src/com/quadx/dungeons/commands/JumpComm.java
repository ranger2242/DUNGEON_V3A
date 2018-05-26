package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class JumpComm extends Command{
    public JumpComm(){
        name="Jump";
        keyboard= Input.Keys.SHIFT_RIGHT;
        contB= Xbox360Pad.BUTTON_A;
    }
    @Override
    public void execute() {
        if (pressed())
            if (cls.equals(MapState.class)) {
                if (player.getEnergy() > 30 && !player.jumping) {
                    player.jumping = true;
                    player.setEnergy(player.getEnergy()-30);
                }else if(player.getEnergy() < 30) {
                    new HoverText("-!-",SECOND, Color.YELLOW,player.abs(),true);
                }
            }
    }
}
