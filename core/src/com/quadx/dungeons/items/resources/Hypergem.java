package com.quadx.dungeons.items.resources;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Chris Cavazos on 5/28/2018.
 */
public class Hypergem extends Resource {
    public Hypergem(){
        name="Hypergem";

        ptColor= Color.GOLD;
        defensemod+=1;
        cost=3500;
        canCluster=true;
        isUsable=true;
        hasEffect=true;
        gINIT(1,"icHypergem");
    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,0,0,0,0,0};
    }
}
