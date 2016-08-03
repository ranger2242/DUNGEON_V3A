package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Torment extends Attack {
    public Torment()  {
        costGold=6000;
        type=3;
        powerA= new int[]{0,0,0,0,0};
        costA= new int[]{0,0,0,0,0};
        name="Torment";
        description="INT x0.5 and ATT x2 for 30 time";
        power= 0;
        cost= 0;
        range= 0;
        spread= 0;
        mod=4;
        setIcon(ImageLoader.attacks.get(11));
    }
}
