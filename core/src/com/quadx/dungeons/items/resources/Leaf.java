package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Leaf extends Resource {
    public Leaf(){
        fileName="mods/icLeaf.png";
        name="Leaf";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}