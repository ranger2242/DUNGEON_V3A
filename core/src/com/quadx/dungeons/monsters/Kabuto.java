package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Kabuto extends Monster {
    public Kabuto(){
        power=40;
        hpBase =60;
        strBase =40;
        defBase =20;
        intBase =70;
        spdBase =40;
        sight=3;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.11f;
        name= "Eura";
        icons= ImageLoader.en0;
        icon=icons[0];
        iconSet=0;
        genLevel();
        genStats();
        loadIcon();
    }
}
