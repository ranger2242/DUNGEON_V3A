package com.quadx.dungeons.monsters;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Chris Cavazos on 6/19/2016.
 */
public class Porygon extends Monster {
    public Porygon(){
        sight=6;
        moveSpeedMin=.11f;
        moveSpeed=.11f;
        moveSpeedMax =.08f;
        body.setIcons(ImageLoader.en1);
        load("Porygon", new int[]{50,95,75,90,60,100});
        //load("", new int[]{,,,,,});

    }
}
