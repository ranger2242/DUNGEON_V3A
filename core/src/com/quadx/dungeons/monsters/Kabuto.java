package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Kabuto extends Monster {
    public Kabuto(){
        power=20;
        hpBase =60;
        attBase =40;
        defBase =40;
        intBase =40;
        spdBase =40;
        sight=3;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.11f;
        name= "Kabuto";
        icons= ImageLoader.en0;
        icon=icons[0];
        iconSet=0;
        genLevel();
        genStats();
        loadIcon();
    }
}
