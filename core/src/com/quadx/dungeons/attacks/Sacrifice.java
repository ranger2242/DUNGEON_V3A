package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Sacrifice extends Attack {
    public Sacrifice()  {
        costGold=200;
        type=2;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{10,15,20,25,30};
        name="Sacrifice";
        power=0;
        cost=10;
        mod=7;
        spread=0;
        range=0;
        description="Converts M to HP";
    }
}
