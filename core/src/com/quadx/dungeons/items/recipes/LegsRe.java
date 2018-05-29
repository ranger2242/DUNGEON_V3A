package com.quadx.dungeons.items.recipes;

import com.quadx.dungeons.items.equipment.Legs;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class LegsRe extends Recipe {
    public LegsRe(){
        name="Legs";
        loadEquip(new Legs());
        fileName="icRecipe.png";
        loadIcon();
    }
}
