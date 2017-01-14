package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SpeedPlus extends Item {
    public SpeedPlus(){
        name="Speed+";
        ptColor= Color.GREEN;
        speedmod+=1;
        cost=1000 ;
        setIcon(new Texture("images\\icons\\items\\icSpd+.png"));
    }
}
