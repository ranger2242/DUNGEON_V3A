package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/20/2016.
 */
public class Ponyta extends Monster {
    public Ponyta(){
        power=90;
        hpBase =50;
        attBase =30;
        defBase =10;
        intBase =20;
        spdBase =100;
        sight=9;
        moveSpeedMin=.11f;
        moveSpeed=.09f;
        moveSpeedMax =.05f;
        name= "Ponyta";
        icons= ImageLoader.en3;
        icon=icons[0];
        //iconSet=1;
        genLevel();
        genStats();
        loadIcon();
    }
}
