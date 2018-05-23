package com.quadx.dungeons.commands;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 2/4/2017.
 */
public class ChangeAttackComm extends Command {
    public ChangeAttackComm(){
        name="Change Attack";
        keyboard= Input.Keys.G;
        contB=Xbox360Pad.BUTTON_LB;

    }
    @Override
    public void execute() {
        if(pressed()) {
            if(cls.equals(MapState.class)) {
                if(Inventory.dtItem>.1)
                Attack.incPos();
                if(Attack.pos>=player.attackList.size())
                    Attack.changePos(0);
                Inventory.dtItem=0;
            }
        }
    }
}
