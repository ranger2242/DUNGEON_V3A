package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

/**
 * Created by range on 5/20/2016.
 */
public class Quick extends Ability {
   // protected ArrayList<String> output=new ArrayList<>();

    public Quick(){
        details();

    }

    @Override
    public void onActivate() {
        Game.player.setMoveSpeed(Game.player.getMoveSpeed() *2);
        Game.player.setSpeed(Game.player.getSpeed()*2);
        Game.player.setEnergyRegen(Game.player.getEnergyRegen()*2);
        Game.player.setManaRegen(Game.player.getManaRegenRate()*2);
        Game.player.setDefense(Game.player.getDefense()/2);
        Game.player.setIntel((int)(Game.player.getIntel()*.75));
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the QUICK ability!");
        MapState.out("Move Speed doubled");
        MapState.out("Speed doubled!");
        MapState.out("M Regen doubled!");
        MapState.out("E Regen doubled!");
        MapState.out("DEF halved!");
        MapState.out("INT lowered by 25%");
        MapStateRender.setHoverText("QUICK!",1.5f, Color.WHITE);

    }
    public  ArrayList<String> details() {
        output.clear();

        output.add("-QUICK-");
        output.add("Move Speed x2");
        output.add("SPD x2");
        output.add("M Regen x2");
        output.add("E Regen x2");
        output.add("DEF x0.5");
        output.add("INT x0.75");
        return output;
    }
}
