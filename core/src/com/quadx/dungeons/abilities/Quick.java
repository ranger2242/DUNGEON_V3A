package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quick extends Ability {
    public Quick(){
        name="Quick";
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
        MapStateRender.setHoverText("QUICK!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);
    }

    @Override
    public int getMod() {
        return 4;
    }

    @Override
    public void l1() {
        level=1;
        Game.player.setxMoveSpeed(.3);
        Game.player.setxSpeed(2);
        Game.player.setxEnergyRegen(1.5);
        Game.player.setxManaRegen(1.5);
        Game.player.setxDefense(.75);
        Game.player.setxIntel(.75);
    }

    @Override
    public void l2() {
        player.setxMoveSpeed(.8);
        player.setxSpeed(1.3);
        player.setxManaRegen(1.1);
        player.addItemToInventory(equipSets.ref[3].get(0));

    }

    @Override
    public void l3() {
        player.setxMoveSpeed(.7);
        player.setxSpeed(1.5);
        player.setxHpRegen(1.2);
        player.setxEnergyRegen(1.2);
        player.addItemToInventory(equipSets.ref[3].get(3));
    }

    @Override
    public void l4() {
        player.setxHpRegen(1.4);
        player.setxManaRegen(1.3);
        player.setxMoveSpeed(.5);
        player.setxSpeed(1.6);
        player.addItemToInventory(equipSets.ref[3].get(2));
    }

    @Override
    public void l5() {
        player.setxSpeed(2);
        player.setxMoveSpeed(.4);
        player.setxHpMax(1.6);
        player.setxManaRegen(1.4);
        player.setxEnergyRegen(1.4);
        player.addItemToInventory(equipSets.ref[3].get(1));
        player.maxSec=3;
    }

    public  ArrayList<String> details() {
        output.clear();
        output.add("-"+name+" "+(level+1) +"-");
        switch (this.level+1){
            case 1:{
                output.add("1.7x Move Speed");
                output.add("2x SPD");
                output.add("1.5x E Regen");
                output.add("1.5x M Regen");
                output.add("0.75x DEF");
                output.add("0.75x INT");
                output.add("");
                output.add("");
                output.add("");

                break;
            }case 2:{
                output.add("1.3x SPD");
                output.add("1.1x M Regen");
                output.add("1.2x Move Speed");
                output.add(equipSets.ref[3].get(0).getName());
                break;
            }case 3:{
                output.add("1.3x Move Speed");
                output.add("1.5x SPD");
                output.add("1.2x HP Regen");
                output.add("1.2x E Regen");
                output.add(equipSets.ref[3].get(3).getName());
                break;
            }case 4:{
                output.add("1.4x HP Regen");
                output.add("1.3x M Regen");
                output.add("2x Move Speed");
                output.add("1.6x SPD");
                output.add(equipSets.ref[3].get(2).getName());
                break;
            }case 5:{
                output.add("2x SPD");
                output.add("1.6x Move Speed");
                output.add("1.6x HP");
                output.add("1.4x M Regen");
                output.add("1.4x E Regen");
                output.add(equipSets.ref[3].get(1).getName());
                break;
            }
        }
        if(level<5)
            output.add("Upgrade cost: "+upCost[level]+" AP");
        return output;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
