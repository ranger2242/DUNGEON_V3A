package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Protect extends Attack {
    public Protect()  {
        costGold=90;
        type=2;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{80,85,90,95,100};
        name="Protect";
        power=0;
        cost=45;
        mod=6;
        spread=0;
        range=0;
        description="Has a chance to protect the user from damage.";
    }
}
