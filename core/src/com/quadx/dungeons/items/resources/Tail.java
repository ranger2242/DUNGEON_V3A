package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Tail extends Resource {
    public Tail(){
        name="TAIL";
        gINIT(1,"icTail");
    }

    @Override
    public int[] runMod() {
        return new int[0];
    }
}
