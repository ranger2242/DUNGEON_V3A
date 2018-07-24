package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Bone extends Resource {
    public Bone(){
        fileName="mods/icBone.png";
        name="BONE";
        gINIT(1,"icBone");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}