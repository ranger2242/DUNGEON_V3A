package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Blood extends Resource {

    public Blood(){
        fileName="mods\\icBlood.png";
        name="Blood";
        gINIT(1,"icClaw");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
