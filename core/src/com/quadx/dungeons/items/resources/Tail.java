package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Tail extends Resource {
    public Tail(){
        fileName="mods/icTail.png";
        name="TAIL";
        loadIcon();
    }

    @Override
    public int[] runMod() {
        return new int[0];
    }
}
