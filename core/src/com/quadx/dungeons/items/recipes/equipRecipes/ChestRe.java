package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Chest;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class ChestRe extends Recipe {
    public ChestRe(){
        name="Chest";
        loadEquip(new Chest());
        fileName="icRecipe.png";
        loadIcon();
        isEquipRecipe=true;

    }
}
