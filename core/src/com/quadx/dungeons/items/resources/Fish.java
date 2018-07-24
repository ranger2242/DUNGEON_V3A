package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Fish extends Resource {
    public Fish(){
        name="Fish";
        gINIT(1,"icFish");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}