package com.quadx.dungeons.items.resources;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.Random;

/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class Gold extends Item {
    float lvl= 1;

    public Gold(int lvl){
        this.lvl=lvl;
        genValue();
    }
    public Gold(){
       genValue();
    }
    void genValue(){
        Random rn = new Random();
        float f = rn.nextFloat();
        while (f < .05) {
            f = rn.nextFloat();
        }
        gold = (int) Math.abs((( 50) *lvl)*rn.nextGaussian());
        if (gold < 0) gold = 1;
        icon= ImageLoader.gold[0];
        value= (gold);

    }
    public int getValue(){
        return value;
    }
}
