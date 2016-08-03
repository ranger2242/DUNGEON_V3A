package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Brawler extends Ability {
    //protected static ArrayList<String> output=new ArrayList<>();

    public Brawler(){
        icon= ImageLoader.abilities.get(4);
        details();
    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override
    public  void onActivate() {
        player.setAbilityMod(4);
        player.setEnergy(player.getEnergy()*2);
        player.setEnergyMax(player.getEnergyMax()*2);
        player.setEnergyRegen(player.getEnergyRegen()*2);
        player.setMana(player.getMana()/2);
        player.setManaMax(player.getManaMax()/2);
        player.setAttack(player.getAttack()*2);
        MapState.out("----------------------------------");
        MapState.out(player.getName()+" activated the BRAWLER ability!");
        MapState.out("E Max doubled!");
        MapState.out("E Regen doubled!");
        MapState.out("M Max was halved!");
        MapState.out("1.2x DMG for E Attacks");
        MapStateRender.setHoverText("BRAWLER!",1.5f, Color.WHITE, player.getPX(), player.getPY(),false);

        //super.onActivate();
    }

    @Override
    public int getMod() {
        return 2;
    }

    @Override
    public void l1() {

    }

    @Override
    public void l2() {

    }

    @Override
    public void l3() {

    }

    @Override
    public void l4() {

    }

    @Override
    public void l5() {

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
