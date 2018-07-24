package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Claw extends Resource {
    public Claw(){
        name="CLAW";
        gINIT(1,"icClaw");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
