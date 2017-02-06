package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.attacks.Stab;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Tank extends Ability {
    public Tank() {
        name="Tank";
        icon= ImageLoader.abilities.get(0);
        details();
    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public void onActivate() {
        player.setAbilityMod(0);
        l1();
        new HoverText("Tank!!",1.5f, Color.WHITE, player.getAbsPos().x,player.getAbsPos().y,false);

    }

    @Override
    public int getMod() {
        return 6;
    }

    @Override
    public void l1() {
        level=1;
        player.attackList.add(new Stab());
        player.setxDefense(2);
        player.setxHpMax(1.5);
        player.setxHpRegen(2);
        player.setxMoveSpeed(1.005);
    }

    @Override
    public void l2() {
        player.setxDefense(1.2);
        player.setxMoveSpeed(.9);
        player.addItemToInventory(equipSets.ref[0].get(0));
    }

    @Override
    public void l3() {
        player.setxDefense(1.3);
        player.setxAttack(1.1);
        player.setxMoveSpeed(.8);
        player.addItemToInventory(equipSets.ref[0].get(6));
    }

    @Override
    public void l4() {
        player.setxDefense(1.3);
        player.setxAttack(1.1);
        player.setxMoveSpeed(.8);
        player.addItemToInventory(equipSets.ref[0].get(2));

    }

    @Override
    public void l5() {
        player.setxDefense(1.4);
        player.setxSpeed(1.3);
        player.setxIntel(1.1);
        player.setxHpRegen(1.5);
        player.setxMoveSpeed(.9);
        player.maxSec=3;
        player.addItemToInventory(equipSets.ref[0].get(3));

    }

    public ArrayList<String> details() {
        output.clear();
        output.add("-"+name+" "+(level+1) +"-");
        switch (this.level+1){
            case 1:{
                output.add("HP Max x1.5");
                output.add("HP Regen x2");
                output.add("DEF x2");
                output.add("Move Speed x0.75");
                break;
            }case 2:{
                output.add("1.2x DEF");
                output.add("1.1x Move Speed");
                output.add(equipSets.ref[0].get(0).getName());
                break;
            }case 3:{
                output.add("1.3x DEF");
                output.add("1.1x ATT");
                output.add("1.2x Move Speed");
                output.add(equipSets.ref[0].get(6).getName());
                break;
            }case 4:{
                output.add("1.4x DEF");
                output.add("1.3x SPD");
                output.add("1.1x INT");
                output.add("1.5x HP Regen");
                output.add(".9x Move Speed");
                output.add(equipSets.ref[0].get(2).getName());
                break;
            }case 5:{
                output.add("1.5x DEF");
                output.add("1.2x INT");
                output.add("1.2x SPD");
                output.add("1.2x Move Speed");
                output.add(equipSets.ref[0].get(3).getName());
               // output.add("Unlock Tank Special");
                output.add("Unlock Extra Ability Slot");
                break;
            }
        }
        if(level<5)
        output.add("Upgrade cost: "+upCost[level]+" AP");
        return output;
    }

    @Override
    public String getName() {
        return name;
    }
}
