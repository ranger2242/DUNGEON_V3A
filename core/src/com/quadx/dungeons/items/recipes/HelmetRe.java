package com.quadx.dungeons.items.recipes;

import com.quadx.dungeons.items.equipment.Helmet;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class HelmetRe extends Recipe {
    public HelmetRe(){
        name="Helmet";
        loadEquip(new Helmet());
        fileName="icRecipe.png";
        loadIcon();
    }
}
