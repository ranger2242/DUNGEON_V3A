package com.quadx.dungeons.items;

import com.quadx.dungeons.tools.ImageLoader;

import java.util.Random;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class Gold extends Item{
    private int value=0;
    public Gold(){
        Random rn = new Random();
        float f = rn.nextFloat();
        while (f < .05) {
            f = rn.nextFloat();
        }
        gold = (int) ((f) * 100) * player.getLevel();
        if (gold < 0) gold = 1;
        if (gold > 1000) {
            gold = 1000;
        }
        if(gold>=1 && gold<333){
            icon= ImageLoader.gold[0];
        }
        if(gold>=333 && gold<666){
            icon=ImageLoader.gold[1];
        }
        if(gold>=666){
            icon=ImageLoader.gold[2];
        }
        value=gold;
    }
    public int getValue(){
        return value;
    }
}
