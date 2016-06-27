package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Dodrio extends Monster {
    public Dodrio(){
        power=90;
        hpBase =100;
        attBase =120;
        defBase =60;
        intBase =80;
        spdBase =90;
        sight=8;
        moveSpeedMin=.10f;
        moveSpeed=.08f;
        moveSpeedMax =.07f;
        name= "Dodrio";
        icons= ImageLoader.en6;
        icon=icons[0];
        genLevel();
        genStats();
        loadIcon();
    }
}
