package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Dodrio extends Monster {
    public Dodrio(){
        sight=8;
        moveSpeedMin=.10f;
        moveSpeed=.08f;
        moveSpeedMax =.07f;
        body.setIcons(ImageLoader.en6);
        load("Dodrio",new int[]{90,100,120,60,80,90});
    }
}
