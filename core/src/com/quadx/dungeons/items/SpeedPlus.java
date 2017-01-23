package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.tools.FilePaths;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SpeedPlus extends Item {
    public SpeedPlus(){
        name="Speed+";
        ptColor= Color.GREEN;
        speedmod+=1;
        cost=3500 ;
        setIcon(new Texture(FilePaths.getPath("images\\icons\\items\\icSpd+.png")));
    }
}
