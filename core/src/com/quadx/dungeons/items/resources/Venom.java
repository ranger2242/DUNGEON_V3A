package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Venom extends Resource {
    public Venom(){
        fileName="mods/icVenom.png";
        name="VENOM";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
