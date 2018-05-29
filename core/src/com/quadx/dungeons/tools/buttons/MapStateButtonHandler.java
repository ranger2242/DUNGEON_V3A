package com.quadx.dungeons.tools.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Time;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.rotateMap;
import static com.quadx.dungeons.tools.Tests.mouseAim;

/**
 * Created by Chris Cavazos on 5/23/2018.
 */
public class MapStateButtonHandler extends ButtonHandler {
    private static Delta dDebugKey = new Delta(10 * Time.ft);


    private void numButtonFunctions(int x) {
        if (prsd(Input.Keys.MINUS)) {
            Game.player.unequip(x);
        } else if (prsd(Input.Keys.EQUALS)) {
            Attack.showDescription(x);
        } else
            Attack.changePos(x);

    }

    void mouseAim(float dt){
        updateMousePos();
        if(mouseAim==true){
           player.setAimVector(getAimVector(), false);

        }
    }
    public Vector2 getAimVector(){
        return new Vector2(mpos);
    }

    @Override
    public void update(float dt) {
        mouseAim(dt);//needs to be before commands
        runCommands();
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            player.getAttack().use();

        }

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
