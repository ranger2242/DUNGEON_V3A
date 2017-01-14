package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class DefPlus extends  Item {
    public DefPlus(){
        name="Defense+";
        ptColor= Color.BLUE;
        defensemod+=1;
        cost=1000;
        setIcon(new Texture("images\\icons\\items\\icDef+.png"));
    }
}
