package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Investor extends Ability {
    //protected static ArrayList<String> output=new ArrayList<>();

    public Investor(){
        details();
    }
    @Override

    public void onActivate() {
        Game.player.setHpRegen(Game.player.getHpRegen()*2);
        Game.player.setIntel(Game.player.getIntel()*2);
        Game.player.setGold(100);
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the INVESTOR ability!");
        MapState.out("HP Regen doubled!");
        MapState.out("INT doubled!");
        MapState.out("Gold is being generated!");
        MapStateRender.setHoverText("INVESTOR!",1.5f, Color.WHITE);
    }

    public ArrayList<String> details() {
        output.clear();

        output.add("-INVESTOR-");
        output.add("Gold increases by .1% often");
        output.add("Start with 100g");
        output.add("INT x2");
        output.add("HP Regen x2");
        output.add("!Gold can go negative");
        output.add("if you don't save enough!");
        return output;
    }
    public static void generatePlayerGold(){
        Game.player.setGold((float)(Game.player.getGold()*1.01));
    }
}
