package com.quadx.dungeons.items.resources;

import com.quadx.dungeons.items.Item;

/**
 * Created by Chris Cavazos on 5/25/2018.
 */
public abstract class Resource extends Item {
    boolean canCluster=false;
    public boolean canCluster() {
        return canCluster;
    }
    public abstract int[] runMod();

}
