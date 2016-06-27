package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Stab extends Attack {
    public Stab(){
        costGold=2000;
        type=1;
        powerA = new int[]{50,60,70,80,90};
        costA =new int[]{20,25,35,40,50};
        name="Stab";
        power=50;
        cost=20;
        mod=-1;
        spread=2;
        range=4;
        description="Stabs the opponent.";
        setIcon(ImageLoader.attacks.get(10));
    }
}
