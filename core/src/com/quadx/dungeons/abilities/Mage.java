package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Mage extends Ability {
   // protected static ArrayList<String> output=new ArrayList<>();

    public Mage(){
        icon= loadIcon("images/icons/abilities/icMage.png");
        details();

    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public  void onActivate() {
        player.setAbility(2);
        Game.player.setMana(Game.player.getMana()*2);
        Game.player.setManaMax(Game.player.getManaMax()*2);
        Game.player.setManaRegen(Game.player.getManaRegenRate()*2);
        Game.player.setEnergy(Game.player.getEnergy()/2);
        Game.player.setEnergyMax(Game.player.getEnergyMax()/2);
        Game.player.setIntel(Game.player.getIntel()*2);
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the MAGE ability!");
        MapState.out("M Max x4!");
        MapState.out("M Regen x3!");
        MapState.out("E Max was halved!");
        MapState.out("2x INT");
        MapStateRender.setHoverText("MAGE!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);

    }

    @Override
    public int getMod() {
        return 3;
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

    @Override
    public String getName() {
        return "MAGE";
    }
}