package com.quadx.dungeons.items.resources;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class StrengthPlus extends Resource {
    public StrengthPlus(){
        ptColor= Color.RED;
        name="Strength+";
        strmod =1;
        cost=3500;
        canCluster=true;
        isUsable=true;
        hasEffect=true;
        gINIT(1,"icStrength+");
    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,strmod,0,0,0};
    }
}
