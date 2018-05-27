package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Muk extends Monster {
    public Muk(){
        sight=8;
        moveSpeedMin=.18f;
        moveSpeed=.12f;
        moveSpeedMax =.1f;
        body.setIcons(ImageLoader.en8);
        load("Muk",new int[]{70,200,30,40,40,30});
    }
}
