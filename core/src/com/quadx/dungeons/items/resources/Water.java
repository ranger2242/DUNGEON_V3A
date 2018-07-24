package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Water extends Resource {
    public Water(){
        name="WATER";
        gINIT(1,"icWater");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
