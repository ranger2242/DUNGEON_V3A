package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/22/2016.
 */
public class Muk extends Monster {
    public Muk(){
        sight=8;
        moveSpeedMin=.18f;
        moveSpeed=.12f;
        moveSpeedMax =.1f;
        load("Muk","en8",new int[]{70,200,30,40,40,30});
    }
}
