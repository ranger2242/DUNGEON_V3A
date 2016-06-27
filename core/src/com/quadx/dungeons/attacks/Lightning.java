package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 6/25/2016.
 */
public class Lightning extends Attack {
    public Lightning(){
        costGold=30000;
        type=3;
        int a=player.getManaMax();
        powerA = new int[]{80,90,110,130,150};
        costA =new int[]{a,a,a,a,a};
        name="Lightning";
        power=80;
        cost=a;
        mod=10;
        description="Summons lighning.";
        spread=0;
        range=0;
        setIcon(ImageLoader.attacks.get(12));
    }
}
