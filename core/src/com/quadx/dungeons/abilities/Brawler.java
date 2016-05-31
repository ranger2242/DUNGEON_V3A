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
public class Brawler extends Ability {
    //protected static ArrayList<String> output=new ArrayList<>();

    public Brawler(){
        details();
    }

    @Override
    public  void onActivate() {
        Game.player.setEnergy(Game.player.getEnergy()*2);
        Game.player.setEnergyMax(Game.player.getEnergyMax()*2);
        Game.player.setEnergyRegen(Game.player.getEnergyRegen()*2);
        Game.player.setMana(Game.player.getMana()/2);
        Game.player.setManaMax(Game.player.getManaMax()/2);
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the BRAWLER ability!");
        MapState.out("E Max doubled!");
        MapState.out("E Regen doubled!");
        MapState.out("M Max was halved!");
        MapState.out("1.2x DMG for E Attacks");
        MapStateRender.setHoverText("BRAWLER!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);

        //super.onActivate();
    }

    @Override
    public int getMod() {
        return 2;
    }

    public ArrayList<String> details() {
        //ArrayList<String> output=new ArrayList<>();
        output.clear();
        output.add("-BRAWLER-");
        output.add("E Max x2");
        output.add("E Regen x2");
        output.add("M Max x0.5");
        output.add("E DMG x1.2");
        return output;
    }

    @Override
    public String getName() {
        return "BRAWLER";
    }
}
