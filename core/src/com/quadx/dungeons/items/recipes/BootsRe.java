package com.quadx.dungeons.items.recipes;

import com.quadx.dungeons.items.equipment.Boots;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class BootsRe extends Recipe {
    public BootsRe(){
        name="Boots";
        loadEquip(new Boots());
        fileName="icRecipe.png";
        loadIcon();
    }
}