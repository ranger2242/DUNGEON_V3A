package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Boots;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class BootsRe extends Recipe {
    public BootsRe(){
        super();
        name="Boots";
        loadEquip(new Boots());
        isEquipRecipe=true;

    }
}