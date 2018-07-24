package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Gengar extends Monster {
    public Gengar(){
        sight=4;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.08f;
        load("Gengar","en7", new int[]{100,100,10,100,100,60});

    }
}
