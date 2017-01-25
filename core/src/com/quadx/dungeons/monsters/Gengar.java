package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
class Gengar extends Monster {
    public Gengar(){
        power=100;
        hpBase =100;
        attBase =10;
        defBase =100;
        intBase =100;
        spdBase =60;
        sight=4;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.08f;
        name= "Gengar";
        icons= ImageLoader.en7;
        icon=icons[0];
        genLevel();
        genStats();
        loadIcon();
    }
}
