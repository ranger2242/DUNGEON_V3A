package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Mushroom extends Resource {
    public Mushroom(){
        name="Mushroom";
        gINIT(1,"icMushroom");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}