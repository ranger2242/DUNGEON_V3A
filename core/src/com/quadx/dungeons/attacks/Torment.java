package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Torment extends Attack {
    public Torment()  {
        costGold=5000;
        type=2;
        powerA = new int[]{10,10,10,10,10};
        costA =new int[]{50,50,50,50,50};
        name="Torment";
        power=10;
        cost=50;
        mod=4;
        spread=3;
        range=4;
        description="Lowers the opponents ATT.";
        setIcon(ImageLoader.attacks.get(11));
    }
}
