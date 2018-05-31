package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Fish extends Resource {
    public Fish(){
        fileName="mods/icFish.png";
        name="Fish";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}