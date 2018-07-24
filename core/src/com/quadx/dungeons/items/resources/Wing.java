package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Wing extends Resource {
    public Wing(){
        name="WING";
        gINIT(1,"icWings");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
