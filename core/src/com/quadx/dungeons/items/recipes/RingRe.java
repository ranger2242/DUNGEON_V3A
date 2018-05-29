package com.quadx.dungeons.items.recipes;

import com.quadx.dungeons.items.equipment.Ring;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class RingRe extends Recipe {
    public RingRe(){
        name="Ring";
        loadEquip(new Ring());
        fileName="icRecipe.png";
        loadIcon();
    }
}
