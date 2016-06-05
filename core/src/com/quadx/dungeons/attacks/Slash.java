package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Slash extends Attack {
    public Slash(){
        costGold=35;
        type=1;
        powerA = new int[]{80,90,110,130,150};
        costA =new int[]{30,40,50,70,80};
        name="Slash";
        power=30;
        cost=30;
        mod=-1;
        spread=5;
        range=2;
        description="Slashes the opponent.";
    }
}
