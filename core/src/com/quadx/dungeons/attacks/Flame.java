package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Flame extends Attack {
    public Flame()  {
        costGold=100;
        type=2;
        powerA = new int[]{40,60,80,100,120};
        costA =new int[]{30,45,60,80,90};
        name="Flame";
        power=40;
        cost=30;
        mod=0;
        description="Player creates a burst of fire.";
        spread=5;
        range=10;
    }
}
