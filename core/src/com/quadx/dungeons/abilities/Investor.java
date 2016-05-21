package com.quadx.dungeons.abilities;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;


/**
 * Created by range on 5/20/2016.
 */
public class Investor extends Ability {
    //protected static ArrayList<String> output=new ArrayList<>();

    public Investor(){
        details();
    }

    @Override
    public void onActivate() {
        Game.player.setHpRegen(Game.player.getHpRegen()*2);
        Game.player.setIntel(Game.player.getIntel()*2);
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the INVESTOR ability!");
        MapState.out("HP Regen doubled!");
        MapState.out("INT doubled!");
        MapState.out("Gold is being generated!");
        MapStateRender.hovTextS="INVESTOR!";
        MapStateRender.hovText=true;
    }

    public ArrayList<String> details() {
        output.clear();

        output.add("-INVESTOR-");
        output.add("Gold increases by 1% often");
        output.add("INT x2");
        output.add("HP Regen x2");
        return output;
    }
}
