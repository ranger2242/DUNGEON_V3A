package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/19/2016.
 */
public class Anortih extends Monster {
    public Anortih(){
        sight=6;
        moveSpeedMin=.18f;
        moveSpeed=.18f;
        moveSpeedMax =.14f;
        body.setIcons(ImageLoader.en4);
        load("Anorith",new int[]{40,50,20,20,20,40});
    }
}
