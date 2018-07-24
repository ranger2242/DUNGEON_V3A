package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/30/2018.
 */
public class Heart extends Resource {
    public Heart(){
        name="Monster Heart";
        gINIT(1,"icHeart");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
