package com.quadx.dungeons.items.recipes;

import com.quadx.dungeons.items.equipment.Gloves;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class GlovesRe extends Recipe {
    public GlovesRe(){
        name="Gloves";
        loadEquip(new Gloves());
        fileName="icRecipe.png";
        loadIcon();
    }
}
