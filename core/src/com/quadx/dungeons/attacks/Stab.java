package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Stab extends Attack {
    public Stab(){
        costGold=30;
        type=1;
        powerA = new int[]{50,60,70,80,90};
        costA =new int[]{20,25,35,40,50};
        name="Stab";
        power=30;
        cost=20;
        mod=-1;
        spread=2;
        range=3;
        description="Stabs the opponent.";
    }
}
