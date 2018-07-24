package com.quadx.dungeons.items.resources;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.items.Item;

import java.util.Random;

/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class Gold extends Item {
    float lvl= 1;

    public Gold(int lvl){
        this.lvl=lvl;
        init();
    }
    public Gold(){
       init();
    }
    void init(){
        name="GOLD";

        Random rn = new Random();
        float f = rn.nextFloat();
        while (f < .05) {
            f = rn.nextFloat();
        }
        gold = (int) Math.abs((( 50) *lvl)*rn.nextGaussian());
        if (gold < 0) gold = 1;
        value= (gold);
        tileColor= new Color(1f, .647f, 0f, 1);
        gINIT(1,"icCoinS");
    }
    public int getValue(){
        return value;
    }
}
