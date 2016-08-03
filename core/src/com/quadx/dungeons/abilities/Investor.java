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
public class Investor extends Ability {
    //protected static ArrayList<String> output=new ArrayList<>();

    public Investor(){
        icon=  ImageLoader.abilities.get(1);
        details();
    }

    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public void onActivate() {
        player.setAbilityMod(1);

        Game.player.setHpRegen(Game.player.getHpRegen()*2);
      //  Game.player.setIntel(Game.player.getIntel()*2);
     //   Game.player.setGold(100);
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the INVESTOR ability!");
        MapState.out("HP Regen doubled!");
        MapState.out("INT doubled!");
        MapState.out("Gold is being generated!");
        MapStateRender.setHoverText("INVESTOR!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);
    }

    @Override
    public int getMod() {
        return 5;
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
        output.clear();

        output.add("-INVESTOR-");
        output.add("Gold increases by .1% often");
    //    output.add("Start with 100g");
  //   output.add("INT x2");
        output.add("HP Regen x2");
   //     output.add("!Gold can go negative");
  //      output.add("if you don't save enough!");
        return output;
    }

    @Override
    public String getName() {
        return "INVESTOR";
    }

    public static void generatePlayerGold(){
        Game.player.setGold((float)(Game.player.getGold()*1.001));
    }
}
