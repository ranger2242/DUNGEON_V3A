package com.quadx.dungeons.attacks;

/**
 * Created by Tom on 11/21/2015.
 */
public class Stab extends Attack {
    public Stab(){
        costGold=30;
        type=1;
        powerA = new int[]{30,45,55,70,90};
        costA =new int[]{0,0,0,0,0};
        name="Stab";
        power=30;
        cost=0;
        mod=-1;
        spread=1;
        range=2;
        description="Stabs the opponent.";
    }
}
