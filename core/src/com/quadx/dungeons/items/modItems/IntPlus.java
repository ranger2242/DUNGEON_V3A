package com.quadx.dungeons.items.modItems;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class IntPlus extends ModItem {
    public IntPlus(){
        name="Intel+";

        ptColor= Color.PURPLE;
        intelmod+=1;
        cost=3500;
        canCluster=true;
        fileName="mods/icIntel+.png";
        isUsable=true;

    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,0,0,intelmod,0};
    }
}
