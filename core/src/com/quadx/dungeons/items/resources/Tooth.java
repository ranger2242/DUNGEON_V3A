package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Tooth extends Resource {
    public Tooth(){
        fileName="mods/icTooth.png";
        name="TOOTH";
        loadIcon();
    }

    @Override
    public int[] runMod() {
        return new int[0];
    }
}
