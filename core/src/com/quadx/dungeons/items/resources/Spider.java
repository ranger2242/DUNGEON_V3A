package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Spider extends Resource {
    public Spider(){
        fileName="mods/icSpider.png";
        name="SPIDER";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}