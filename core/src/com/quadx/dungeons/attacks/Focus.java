package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/21/2016.
 */
public class Focus extends Attack {
    public Focus()  {
        costGold=2510;
        type=1;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{20,30,60,100,120};
        name="Focus";
        power=0;
        cost=0;
        mod=9;
        description="Player focuses E and restores M.";
        spread=0;
        range=0;
        setIcon(ImageLoader.attacks.get(3));
    }
}
