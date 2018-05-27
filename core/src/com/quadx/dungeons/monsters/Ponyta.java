package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/20/2016.
 */
public class Ponyta extends Monster {
    public Ponyta(){
        sight=9;
        moveSpeedMin=.11f;
        moveSpeed=.09f;
        moveSpeedMax =.05f;
        body.setIcons(ImageLoader.en3);
        load("Ponyta", new int[]{90,50,30,10,20,100});
    }
}
