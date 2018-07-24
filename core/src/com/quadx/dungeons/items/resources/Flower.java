package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Flower extends Resource {
    public Flower(){
        name="FLOWER";
        gINIT(1,"icFlower");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}