package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Water extends Resource {
    public Water(){
        fileName="mods/icWater.png";
        name="WATER";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
