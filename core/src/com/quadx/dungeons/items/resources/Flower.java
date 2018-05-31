package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Flower extends Resource {
    public Flower(){
        fileName="mods/icFlower.png";
        name="FLOWER";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}