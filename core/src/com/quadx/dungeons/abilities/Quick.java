package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quick extends Ability {
   // protected ArrayList<String> output=new ArrayList<>();

    public Quick(){
        icon= ImageLoader.abilities.get(3);
        details();

    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public  void onActivate() {
        player.setAbilityMod(3);

        Game.player.setMoveSpeed((float) (Game.player.getMoveSpeed()/1.5));
        Game.player.setSpeed(Game.player.getSpeed()*2);
        Game.player.setEnergyRegen((int) (Game.player.getEnergyRegen()*1.5));
        Game.player.setManaRegen((int) (Game.player.getManaRegenRate()*1.5));
        Game.player.setDefense((int) (Game.player.getDefense()*.75));
        Game.player.setIntel((int)(Game.player.getIntel()*.75));
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the QUICK ability!");
        MapState.out("Move Speed x1.5");
        MapState.out("M Regen x1.5!");
        MapState.out("E Regen x1.5!");
        MapState.out("DEF x.75 !");
        MapState.out("INT x.75 !");
        MapState.out("EXP x1.25");
        MapState.out("SPD x2!");

        MapStateRender.setHoverText("QUICK!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);

    }

    @Override
    public int getMod() {
        return 4;
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

    public  ArrayList<String> details() {
        output.clear();

        output.add("-QUICK-");
        output.add("Move Speed x1.5");
        output.add("M Regen x1.5");
        output.add("E Regen x1.5");
        output.add("DEF x0.5");
        output.add("INT x0.75");
        output.add("EXP x1.25");
        output.add("SPD x2");

        return output;
    }

    @Override
    public String getName() {
        return "QUICK";
    }
}
