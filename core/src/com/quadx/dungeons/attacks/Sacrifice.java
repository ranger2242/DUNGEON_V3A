package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Sacrifice extends Attack {
    public Sacrifice() {
        costGold = 9069;
        type = 4;
        powerA = new int[]{0, 0, 0, 0, 0};
        //costA = new int[]{0, 0, 0, 0};
        costA = new int[]{50, 50, 50, 50,50};

        name = "Sacrifice";
        power = 0;
        cost = 0;
        mod = 7;
        spread = 4;
        range = 4;
        description = "Costs half max HP for  instakill.";
        setIcon(ImageLoader.attacks.get(8));
        hitBoxShape = HitBoxShape.None;
    }

    public void runAttackMod() {
        player.setHpMax((int) (player.getHpMax()/2));
        player.setIntel((int) (player.getIntel()*1.5f));
        player.setStrength((int) (player.getStrength()*1.5f));
        int index=-1;
        for(Attack a: player.attackList){
            if(a.getClass().equals(Sacrifice.class)){
                index=player.attackList.indexOf(a);
            }
        }
        //MapStateUpdater.shakeScreen(.2f,1);
        new HoverText("--SACRIFICE--",2, Color.MAGENTA,player.getAbsPos().x,player.getAbsPos().y+100,false);
        new HoverText("STR x1.5",2, Color.GREEN,player.getAbsPos().x,player.getAbsPos().y+80,false);
        new HoverText("INT x1.5",2, Color.GREEN,player.getAbsPos().x,player.getAbsPos().y+60,false);
        new HoverText("HP x0.5",2, Color.RED,player.getAbsPos().x,player.getAbsPos().y+40,false);


        if(index!=-1){
            player.attackList.remove(index);
        }

    }
}
