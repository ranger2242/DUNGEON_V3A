package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Brain extends Resource {
    public Brain(){
        fileName="mods/icBrain.png";
        name="BRAIN";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
