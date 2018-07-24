package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Cape;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class CapeRe extends Recipe {
    public CapeRe(){
        super();
        name="Cape";
        loadEquip(new Cape());
        isEquipRecipe=true;

    }
}
