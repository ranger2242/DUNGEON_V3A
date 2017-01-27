package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quake extends Attack {
    public Quake(){
        costGold=35000;
        type=1;
        int a= (int) (player.getEnergyMax()/2);
        powerA = new int[]{80,90,110,130,150};
        costA =new int[]{a,a,a,a,a};
        name="Quake";
        power=80;
        cost=a;
        mod=10;
        spread=0;
        range=0;
        description="EARTHQUAKE.";
        setIcon(ImageLoader.attacks.get(9));

    }
}
