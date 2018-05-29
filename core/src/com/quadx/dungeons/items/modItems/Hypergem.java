package com.quadx.dungeons.items.modItems;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Chris Cavazos on 5/28/2018.
 */
public class Hypergem extends ModItem {
    public Hypergem(){
        name="Hypergem";

        ptColor= Color.GOLD;
        defensemod+=1;
        cost=3500;
        canCluster=true;
        fileName="mods/icHypergem.png";
        isUsable=true;

    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,0,0,0,0};
    }
}