package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/20/2016.
 */
class Krabby extends  Monster {
    public Krabby(){
        power=40;
        hpBase =20;
        attBase =90;
        defBase =30;
        intBase =30;
        spdBase =70;
        sight=4;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.10f;
        name= "Krabby";
        icons= ImageLoader.en5;
        icon=icons[0];
        //iconSet=1;
        genLevel();
        genStats();
        loadIcon();
    }
}
