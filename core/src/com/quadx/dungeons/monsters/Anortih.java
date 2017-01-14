package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/19/2016.
 */
class Anortih extends Monster {
    public Anortih(){
        power=40;
        hpBase =50;
        attBase =20;
        defBase =20;
        intBase =20;
        spdBase =40;
        sight=6;
        moveSpeedMin=.18f;
        moveSpeed=.18f;
        moveSpeedMax =.14f;
        name= "Anorith";
        icons= ImageLoader.en4;
        icon=icons[0];
        //iconSet=0;
        genLevel();
        genStats();
        loadIcon();
    }
}
