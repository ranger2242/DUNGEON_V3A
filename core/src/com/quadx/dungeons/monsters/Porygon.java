package com.quadx.dungeons.monsters;

/**
 * Created by Chris Cavazos on 6/19/2016.
 */
public class Porygon extends Monster {
    public Porygon(){
        sight=6;
        moveSpeedMin=.11f;
        moveSpeed=.11f;
        moveSpeedMax =.08f;
        load("Porygon","en1", new int[]{50,95,75,90,60,100});
    }
}
