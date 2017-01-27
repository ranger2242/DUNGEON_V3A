package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
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
public class Investor extends Ability {
    static float dtGold= 0;
    public Investor(){
        icon=  ImageLoader.abilities.get(1);
        name="Investor";
        details();
    }

    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public void onActivate() {
        player.setAbilityMod(1);
        l1();
        new HoverText("INVESTOR!",1.5f, Color.WHITE, player.getAbsPos().x,player.getAbsPos().y,false);
    }

    @Override
    public int getMod() {
        return 5;
    }

    @Override
    public void l1() {
        level=1;
        player.setxHpRegen(2);
        //player.setxEnergyRegen(2);
    }

    @Override
    public void l2() {
            player.setxManaRegen(1.1);
        player.setxMoveSpeed(.85);
        player.setxAttack(1.1);
        player.addItemToInventory(equipSets.ref[1].get(7));

    }

    @Override
    public void l3() {
        player.setxMoveSpeed(.75);
        //.setxEnergyRegen(2);
        player.setxAttack(1.2);
        player.setxIntel(1.2);
        player.addItemToInventory(equipSets.ref[1].get(0));

    }

    @Override
    public void l4() {
        player.setxDefense(1.3);
        player.setxIntel(1.2);
        player.setxAttack(1.25);
        player.setxSpeed(1.2);
        player.addItemToInventory(equipSets.ref[1].get(2));
    }

    @Override
    public void l5() {
        player.setxHpRegen(1.5);
        player.setxManaRegen(1.4);
        player.setxSpeed(1.5);
        player.setxAttack(1.6);
        player.addItemToInventory(equipSets.ref[1].get(3));
        player.maxSec=3;
    }

    public ArrayList<String> details() {
        output.clear();
        output.add("-"+name+" "+(level+1) +"-");
        switch (this.level+1){
            case 1:{
                output.add("x2 HP Regen");
                output.add("x2 E Regen");
                output.add("Generate Money");
                break;
            }case 2:{
                output.add("1.1x Mana Regen");
                output.add("1.15x Move Speed");
                output.add("1.1x ATT");
                output.add(equipSets.ref[1].get(0).getName());
                break;
            }case 3:{
                output.add("1.25x Move Speed");
                output.add("2x E Regen  ");
                output.add("1.2x ATT");
                output.add("1.2x INT");
                output.add(equipSets.ref[1].get(2).getName());
                break;
            }case 4:{
                output.add("1.3x DEF");
                output.add("1.2x INT");
                output.add("1.25x ATT");
                output.add("1.2x SPD");
                output.add(equipSets.ref[1].get(2).getName());
                break;
            }case 5:{
                output.add("1.5x HP Regen");
                output.add("1.4x M Regen");
                output.add("1.5x SPD");
                output.add("1.6x ATT");
                output.add(equipSets.ref[1].get(3).getName());
                output.add("Unlock Investor Special");
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
        return this.name;
    }

    public static void generatePlayerGold() {
        dtGold += Gdx.graphics.getDeltaTime();
        if (dtGold > 1) {
            Game.player.addGold((int) (Game.player.getGold() * .0001));
            dtGold = 0;
        }
    }

}
