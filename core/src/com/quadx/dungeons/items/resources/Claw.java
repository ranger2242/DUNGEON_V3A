package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Claw extends Resource {
    public Claw(){
        fileName="mods/icClaw.png";
        name="CLAW";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
