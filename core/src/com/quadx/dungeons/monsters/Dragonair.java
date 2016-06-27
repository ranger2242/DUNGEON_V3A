package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Dragonair extends Monster {
    public Dragonair(){
        power=80;
        hpBase =130;
        attBase =110;
        defBase =40;
        intBase =80;
        spdBase =150;
        sight=8;
        moveSpeedMin=.9f;
        moveSpeed=.08f;
        moveSpeedMax =.07f;
        name= "Dragonair";
        icons= ImageLoader.en9;
        icon=icons[0];
        genLevel();
        genStats();
        loadIcon();
    }
}
