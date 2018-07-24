package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/20/2016.
 */
public class Ponyta extends Monster {
    public Ponyta(){
        sight=9;
        moveSpeedMin=.11f;
        moveSpeed=.09f;
        moveSpeedMax =.05f;
        load("Ponyta","en3", new int[]{90,50,30,10,20,100});
    }
}
