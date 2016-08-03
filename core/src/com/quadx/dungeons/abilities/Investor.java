package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
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
        MapStateRender.setHoverText("INVESTOR!",1.5f, Color.WHITE, player.getPX(),player.getPY(),false);
    }

    @Override
    public int getMod() {
        return 5;
    }

    @Override
    public void l1() {
        player.setxHpRegen(2);
    }

    @Override
    public void l2() {
        player.setxManaRegen(1.2);
        player.setxMoveSpeed(.7);
        player.setxAttack(1.1);
    }

    @Override
    public void l3() {
        player.setxMoveSpeed(.5);
        player.setxAttack(1.4);
        player.setxIntel(1.4);
    }

    @Override
    public void l4() {
        player.setxDefense(1.6);
        player.setxIntel(1.4);
        player.setxAttack(1.5);
        player.setxSpeed(1.4);
    }

    @Override
    public void l5() {
        player.setxHpRegen(1.5);
        player.setxManaRegen(1.4);
        player.setxSpeed(1.5);
        player.setxAttack(1.6);
    }

    public ArrayList<String> details() {
        output.clear();
        output.add("-"+name+" "+level +"-");
        switch (this.level){
            case 1:{
                output.add("x2 HP Regen");
                output.add("Generate Money");
                break;
            }case 2:{
                output.add("1.3x Move Speed");
                output.add("1.2x Mana Regen");
                output.add("1.1x ATT");
                output.add("Standard+ ATT Gloves");
                break;
            }case 3:{
                output.add("1.5x Move Speed");
                output.add("1.4x ATT");
                output.add("1.2x INT");
                output.add("Standard+ INT Helmet");
                break;
            }case 4:{
                output.add("1.6x DEF");
                output.add("1.4x INT");
                output.add("1.5x ATT");
                output.add("1.4x SPD");
                output.add("Standard+ DEF Chest");
                break;
            }case 5:{
                output.add("1.5x HP Regen");
                output.add("1.4x M Regen");
                output.add("1.5x SPD");
                output.add("1.6x ATT");
                output.add("Legendary ATT Arms");
                output.add("Unlock Investor Special");
                output.add("Unlock Extra Ability Slot");
                break;
            }
        }
        if(level-1<4)
            output.add("Upgrade cost: "+upCost[level-1]+" AP");
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
