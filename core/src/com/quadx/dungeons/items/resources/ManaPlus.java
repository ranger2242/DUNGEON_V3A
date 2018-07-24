package com.quadx.dungeons.items.resources;

import com.quadx.dungeons.tools.stats.PlayerStat;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/23/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ManaPlus extends Resource {
    public ManaPlus(){
        name="Mana+";
        PlayerStat s= player.st;
        isUsable=true;
        hasEffect=true;

        double m = s.getManaMax();

        int size= (int) (3*(m/(double)500));
        if(size==0){
            manamod=60;
            cost=2250;
        }
        else if(size==1){
            manamod=200;
            cost=2250;
        }
        else if(size>1){
            manamod=1000;
            cost=2250;
        }
        gINIT(1,"icCrystal"+(size+1));
    }

    @Override
    public int[] runMod() {

        return new int[]{0,manamod,0,0,0,0,0};
    }
}
