package com.quadx.dungeons.items.modItems;

import com.quadx.dungeons.items.Item;

/**
 * Created by Chris Cavazos on 5/25/2018.
 */
public abstract class ModItem extends Item {
    boolean canCluster=false;
    public boolean canCluster() {
        return canCluster;
    }
    public abstract int[] runMod();

}
