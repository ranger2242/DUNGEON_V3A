package com.quadx.dungeons.attacks;


/**
 * Created by Tom on 11/18/2015.
 */
public class Blind extends Attack {
    public Blind()  {
        costGold=30;
        type=2;
        powerA = new int[]{20,20,20,20,20};
        costA =new int[]{25,25,25,25,25};
        name="Blind";
        power=20;
        cost=25;
        mod=3;
        range=5;
        spread=1;
        description="Lowers the opponents accuracy.";

    }
}
