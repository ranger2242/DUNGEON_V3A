package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.ControlState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.ShopState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public class UpComm extends Command {
    public UpComm(){
        name="Up";
        keyboard= Input.Keys.W;
        contD = Xbox360Pad.BUTTON_DPAD_UP;
        contA=Xbox360Pad.AXIS_LEFT_Y;
        axis=-1;

    }
    @Override
    public void execute() {
        if(pressed()){
            if (cls.equals(MainMenuState.class)) {
                MainMenuState.incrementSelector();
            }
            if(cls.equals(AbilitySelectState.class)){
                AbilitySelectState.movePointer(0,-1);
            }
            if (cls.equals(MapState.class)) {
                if (player.canMove) {
                    player.move(new Vector2(0, 1));
                }
            }
            if(cls.equals(ControlState.class)){
                ControlState.selector.upSelection();
            }
            if(cls.equals(ShopState.class)){
                player.addShopInvOffset(-1);
            }
        }

    }
}
