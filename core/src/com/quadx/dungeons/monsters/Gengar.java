package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Gengar extends Monster {
    public Gengar(){
        sight=4;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.08f;
        body.setIcons(ImageLoader.en7);
        load("Gengar", new int[]{100,100,10,100,100,60});
    }
}
