package com.quadx.dungeons.items.resources;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class DefPlus extends Resource {
    public DefPlus(){
        name="Defense+";

        ptColor= Color.BLUE;
        defensemod+=1;
        cost=3500;
        canCluster=true;
        isUsable=true;
        hasEffect=true;
        gINIT(1,"icDefense+");

    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,0,defensemod,0,0};
    }
}
