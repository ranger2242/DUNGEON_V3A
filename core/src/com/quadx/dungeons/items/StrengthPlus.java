package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.tools.FilePaths;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class StrengthPlus extends Item {
    public StrengthPlus(){
        ptColor= Color.RED;
        name="Strength+";
        strmod =1;
        cost=3500;
        setIcon(new Texture(FilePaths.getPath("images\\icons\\items\\icAtt+.png")));

    }
}
