package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.tools.gui.HUD.out;

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
        player.setEnergyMax((int) (player.getEnergyMax()*2));
        //player.setxEnergyRegen(2);
        player.setMana(player.getMana()/2);
        player.setManaMax((int) (player.getManaMax()/2));
        player.setAttack(player.getAttack()*2);
        out("----------------------------------");
        out(player.getName()+" activated the BRAWLER ability!");
        out("E Max doubled!");
        out("E Regen doubled!");
        out("M Max was halved!");
        out("1.2x DMG for E Attacks");
        new HoverText("BRAWLER!",1.5f, Color.WHITE, player.getAbsPos().x, player.getAbsPos().y,false);

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
