package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Kabuto extends Monster {
    public Kabuto(){
        sight=3;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.11f;
        body.setIcons(ImageLoader.en0);

        iconSet=0;
        load("Kabuto", new int[]{40,60,40,20,70,40});
    }
}
