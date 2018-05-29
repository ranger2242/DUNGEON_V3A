package com.quadx.dungeons.items.recipes;

import com.quadx.dungeons.items.equipment.Cape;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class CapeRe extends Recipe {
    public CapeRe(){
        name="Cape";
        loadEquip(new Cape());
        fileName="icRecipe.png";
        loadIcon();
    }
}
