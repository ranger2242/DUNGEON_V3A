package com.quadx.dungeons.items.modItems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.tools.FilePaths;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class DefPlus extends ModItem {
    public DefPlus(){
        name="Defense+";
        ptColor= Color.BLUE;
        defensemod+=1;
        cost=3500;
        setIcon(new Texture(FilePaths.getPath("images\\icons\\items\\icDef+.png")));
        canCluster=true;
    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,0,defensemod,0,0};
    }
}
