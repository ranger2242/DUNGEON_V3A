package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Drain extends Attack {
    public Drain()  {
        costGold=50;
        type=2;
        powerA = new int[]{15,25,35,45,60};
        costA =new int[]{30,40,50,60,80};
        name="Drain";
        power=powerA[level];
        cost=costA[level];
        range=5;
        spread=0;
        mod=1;
        spread=1;
        range=6;
        description="Heals user the same amount as damage done to opponent.";
    }
}

