package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Drain extends Attack {
    public Drain()  {
        costGold=50;
        type=2;
        powerA = new int[]{25,35,45,50,70};
        costA =new int[]{15,25,35,50,60};
        name="Drain";
        power=powerA[level];
        cost=costA[level];
        mod=1;
        spread=1;
        range=10;
        description="Heals user the same amount as damage done to opponent.";
        setIcon(ImageLoader.attacks.get(1));

    }
}

