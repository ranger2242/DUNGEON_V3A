package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Muk extends Monster {
    public Muk(){
        power=70;
        hpBase =200;
        attBase =30;
        defBase =40;
        intBase =40;
        spdBase =30;
        sight=8;
        moveSpeedMin=.18f;
        moveSpeed=.12f;
        moveSpeedMax =.1f;
        name= "Muk";
        icons= ImageLoader.en8;
        icon=icons[0];
        genLevel();
        genStats();
        loadIcon();
    }
}
