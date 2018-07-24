package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Spider extends Resource {
    public Spider(){
        name="SPIDER";
        gINIT(1,"icSpider");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}