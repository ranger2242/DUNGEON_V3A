package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Dragonair extends Monster {
    public Dragonair(){
        sight=8;
        moveSpeedMin=.9f;
        moveSpeed=.08f;
        moveSpeedMax =.07f;
        load("Dragonair","en9", new int[]{80,130,110,40,80,150});
    }
}
