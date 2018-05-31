package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Mushroom extends Resource {
    public Mushroom(){
        fileName="mods/icMushroom.png";
        name="Mushroom";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}