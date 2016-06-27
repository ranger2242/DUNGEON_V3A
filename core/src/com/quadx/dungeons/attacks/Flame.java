package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Flame extends Attack {
    public Flame()  {
        costGold=100;
        type=3;
        powerA = new int[]{20,30,60,100,120};
        costA =new int[]{10,20,30,50,70};
        name="Flame";
        power=40;
        cost=30;
        mod=0;
        description="Player creates a burst of fire.";
        spread=3;
        range=6;
        setIcon(ImageLoader.attacks.get(2));
    }
}
