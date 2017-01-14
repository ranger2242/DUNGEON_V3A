package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class IntPlus extends Item {
    public IntPlus(){
        name="Intel+";
        ptColor= Color.PURPLE;
        intelmod+=1;
        cost=1000;
        setIcon(new Texture("images\\icons\\items\\icInt+.png"));

    }
}
