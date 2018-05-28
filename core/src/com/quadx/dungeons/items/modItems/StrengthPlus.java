package com.quadx.dungeons.items.modItems;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class StrengthPlus extends ModItem {
    public StrengthPlus(){
        ptColor= Color.RED;
        name="Strength+";
        strmod =1;
        cost=3500;
        canCluster=true;
        fileName="mods/icStrength+.png";

    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,strmod,0,0,0};
    }
}
