package com.quadx.dungeons.tools.buttons;

import com.badlogic.gdx.Input;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.GridManager.rotateMap;

/**
 * Created by Chris Cavazos on 5/23/2018.
 */
public class MapStateButtonHandler extends ButtonHandler {
    private static Delta dDebugKey = new Delta(10 * Game.ft);


    private void numButtonFunctions(int x) {
        if (prsd(Input.Keys.MINUS) && Inventory.dtItem > .15) {
            Inventory.unequip(x);
        } else if (prsd(Input.Keys.EQUALS)) {
            Attack.showDescription(x);
        } else
            Attack.changePos(x);

    }

    @Override
    public void update(float dt) {
        runCommands();

        dDebugKey.update(dt);
        for (int i = 0; i < 8; i++) {//number keys
            if (prsd(Input.Keys.NUM_1 + i)) {
                numButtonFunctions(i);
            }
        }

        if (dDebugKey.isDone() && MapState.debug) {//fkeys
            for (int i = 0; i < 12; i++) {
                if (prsd(Input.Keys.F1 + i)) {
                    db.execute(i);
                    dDebugKey.reset();
                }
            }
        }
        if (prsd(Input.Keys.CONTROL_LEFT)
                || prsd(Input.Keys.CONTROL_RIGHT)) {
            rotateMap(false);
        }
    }
}
