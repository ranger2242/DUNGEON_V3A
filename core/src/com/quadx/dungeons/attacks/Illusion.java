package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Illusion extends Attack {
    public Illusion()  {
        costGold=5000;
        type=2;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{45,50,60,65,70};
        name="Illusion";
        power=0;
        cost=45;
        mod=5;
        spread=1;
        range=3;
        spread=3;
        range=4;
        description="Lowers INT by increasing amounts.";
        setIcon(ImageLoader.attacks.get(5));

    }
}
