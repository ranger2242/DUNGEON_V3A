package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Sacrifice extends Attack {
    public Sacrifice()  {
        costGold=9069;
        type=4;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{0,0,0,0};
        name="Sacrifice";
        power=0;
        cost=0;
        mod=7;
        spread=4;
        range=4;
        description="Costs half max HP for  instakill.";
        setIcon(ImageLoader.attacks.get(8));

    }
}
