package com.quadx.dungeons.abilities;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

/**
 * Created by range on 5/20/2016.
 */
public class Mage extends Ability {
   // protected static ArrayList<String> output=new ArrayList<>();

    public Mage(){
        details();

    }

    @Override
    public void onActivate() {
        Game.player.setMana(Game.player.getMana()*2);
        Game.player.setManaMax(Game.player.getManaMax()*2);
        Game.player.setManaRegen(Game.player.getManaRegenRate()*2);
        Game.player.setEnergy(Game.player.getEnergy()/2);
        Game.player.setEnergyMax(Game.player.getEnergyMax()/2);
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the MAGE ability!");
        MapState.out("M Max doubled!");
        MapState.out("M Regen doubled!");
        MapState.out("E Max was halved!");
        MapState.out("1.2x DMG for M Attacks");
        MapStateRender.hovTextS="MAGE!";
        MapStateRender.hovText=true;
    }
    public ArrayList<String> details() {
        output.clear();

        output.add("-MAGE-");
        output.add("M Max x2");
        output.add("M Regen x2");
        output.add("E Max x0.5");
        output.add("M DMG x1.2");
        return output;
    }
}