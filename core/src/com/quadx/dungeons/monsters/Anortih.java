package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/19/2016.
 */
public class Anortih extends Monster {
    public Anortih(){
        sight=6;
        moveSpeedMin=.18f;
        moveSpeed=.18f;
        moveSpeedMax =.14f;
        load("Anorith","en4",new int[]{40,50,20,20,20,40});

    }


}
