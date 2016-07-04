package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/19/2016.
 */
class Porygon extends Monster {
    public Porygon(){
        power=50;
        hpBase =95;
        attBase =75;
        defBase =90;
        intBase =60;
        spdBase =100;
        sight=6;
        moveSpeedMin=.11f;
        moveSpeed=.11f;
        moveSpeedMax =.08f;
        name= "Porygon";
        icons= ImageLoader.en1;
        icon=icons[0];
        iconSet=1;
        genLevel();
        genStats();
        loadIcon();
    }
}
