package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Gloves;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class GlovesRe extends Recipe {
    public GlovesRe(){
        super();
        name="Gloves";
        loadEquip(new Gloves());
        isEquipRecipe=true;

    }
}
