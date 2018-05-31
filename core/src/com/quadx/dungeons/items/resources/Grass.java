package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Grass extends Resource {
    public Grass(){
        fileName="mods/icGrass.png";
        name="Grass";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}