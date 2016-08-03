package com.quadx.dungeons.attacks;


import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Blind extends Attack {
    public Blind()  {
        costGold=15309;
        type=4;
        powerA = new int[]{20,30,40,50,60};
        costA =new int[]{15,25,35,45,55};
        name="Blind";
        power=20;
        cost=25;
        mod=3;
        range=10;
        spread=10;
        description="Disables enemy vision.";
        setIcon( ImageLoader.attacks.get(0));
    }
}
