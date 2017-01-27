package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.HoverText;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Mage extends Ability {
    public Mage(){
        name="Mage";
        icon=  ImageLoader.abilities.get(3);
        details();

    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public  void onActivate() {
        player.setAbilityMod(2);
        l1();
        new HoverText("MAGE!",1.5f, Color.WHITE, Game.player.getAbsPos().x,Game.player.getAbsPos().y,false);

    }

    @Override
    public int getMod() {
        return 3;
    }

    @Override
    public void l1() {
        level=1;
        Game.player.setxManaMax(2);
        Game.player.setxManaRegen(2);
        Game.player.setxEnergyMax(.5);
        Game.player.setxIntel(2);
    }

    @Override
    public void l2() {
        player.setxManaRegen(1.2);
        player.setxDefense(1.1);
        player.setxIntel(1.2);
        player.addItemToInventory(equipSets.ref[2].get(1));
    }

    @Override
    public void l3() {
        player.setxIntel(1.3);
        player.setxHpRegen(1.1);
        player.setxMoveSpeed(.7);
        player.addItemToInventory(equipSets.ref[2].get(2));
    }

    @Override
    public void l4() {
        player.setxDefense(1.3);
        player.setxSpeed(1.3);
        player.setxIntel(1.5);
        player.setxHpRegen(1.3);
        player.addItemToInventory(equipSets.ref[2].get(6));
    }

    @Override
    public void l5() {
        player.setxManaRegen(1.5);
        player.setxIntel(1.7);
        player.setxSpeed(1.3);
        player.setxDefense(1.1);
        player.addItemToInventory(equipSets.ref[2].get(3));
        player.maxSec=3;

    }

    public ArrayList<String> details() {
        output.clear();
        output.add("-"+name+" "+(level+1) +"-");
        switch (this.level+1){
            case 1:{
                output.add("2x M");
                output.add("2x M Regen");
                output.add("0.5x E");
                output.add("2x INT");
                break;
            }case 2:{
                output.add("1.2x M Regen");
                output.add("1.1x DEF");
                output.add("1.2x INT");
                output.add(equipSets.ref[2].get(1).getName());
                break;
            }case 3:{
                output.add("1.3x Move Speed");
                output.add("1.1x HP Regen");
                output.add("1.3x INT");
                output.add(equipSets.ref[2].get(2).getName());
                break;
            }case 4:{
                output.add("1.3x DEF");
                output.add("1.3x SPD");
                output.add("1.5x INT");
                output.add("1.3x HP Regen");
                output.add(equipSets.ref[2].get(6).getName());

                break;
            }case 5:{
                output.add("1.5x M Regen");
                output.add("1.7x INT");
                output.add("1.1x DEF");
                output.add("1.3x SPD");
                output.add(equipSets.ref[2].get(3).getName());
                break;
            }
        }
        if(level<5)
            output.add("Upgrade cost: "+upCost[level]+" AP");
        return output;
    }

    @Override
    public String getName() {
        return "MAGE";
    }
}