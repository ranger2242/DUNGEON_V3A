package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Legs;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class LegsRe extends Recipe {
    public LegsRe(){
        super();
        name="Legs";
        loadEquip(new Legs());
        isEquipRecipe=true;

    }
}
