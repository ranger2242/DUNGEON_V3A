package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class FullHeal extends Attack {
    public FullHeal()  {
        costGold=1;
        type=2;
        powerA = new int[]{100,100,100,100,100};
        costA =new int[]{0,0,0,0,0};
        name="Full Heal";
        power=100;
        cost=0;
        mod=2;
        spread=0;
        range=0;
        description="DEBUG";
    }
}
