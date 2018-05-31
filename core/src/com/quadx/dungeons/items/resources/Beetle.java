package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Beetle extends Resource {
    public Beetle(){
        fileName="mods/icBeetle.png";
        name="BEETLE";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}