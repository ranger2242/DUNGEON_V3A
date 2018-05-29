package com.quadx.dungeons.items.modItems;

import com.badlogic.gdx.graphics.Color;

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
        canCluster=true;
        fileName="mods/icDefense+.png";
        isUsable=true;

    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,0,defensemod,0,0};
    }
}
