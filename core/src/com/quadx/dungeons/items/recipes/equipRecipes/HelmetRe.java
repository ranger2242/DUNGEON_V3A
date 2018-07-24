package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Helmet;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class HelmetRe extends Recipe {
    public HelmetRe(){
        super();
        name="Helmet";
        loadEquip(new Helmet());
        isEquipRecipe=true;

    }
}
