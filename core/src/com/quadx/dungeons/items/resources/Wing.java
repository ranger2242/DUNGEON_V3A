package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Wing extends Resource {
    public Wing(){
        fileName="mods/icWing.png";
        name="WING";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
