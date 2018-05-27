package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Dragonair extends Monster {
    public Dragonair(){
        sight=8;
        moveSpeedMin=.9f;
        moveSpeed=.08f;
        moveSpeedMax =.07f;
        body.setIcons(ImageLoader.en9);
        load("Dragonair", new int[]{80,130,110,40,80,150});
    }
}
