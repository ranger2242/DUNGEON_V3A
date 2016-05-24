package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Slash extends Attack {
    public Slash(){
        costGold=35;
        type=1;
        powerA = new int[]{20,40,60,80,100};
        costA =new int[]{0,0,0,0,0};
        name="Slash";
        power=20;
        cost=0;
        mod=-1;
        spread=3;
        range=3;
        description="Slashes the opponent.";
    }
}
