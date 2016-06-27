package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Sacrifice extends Attack {
    public Sacrifice()  {
        costGold=12000;
        type=4;
        int a = player.getHpMax()/2;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{a,a,a,a,a};
        name="Sacrifice";
        power=0;
        cost=10;
        mod=7;
        spread=0;
        range=0;
        description="Cuts HP in half and temp x2-INT x2-ATT buff";
        setIcon(ImageLoader.attacks.get(8));

    }
}
