package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/20/2016.
 */
public class Krabby extends  Monster {
    public Krabby(){
        sight=4;
        moveSpeedMin=.15f;
        moveSpeed=.12f;
        moveSpeedMax =.10f;
        load("Krabby","en5",new int[]{40,20,90,30,30,70});
    }
}
