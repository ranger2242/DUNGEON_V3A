package com.quadx.dungeons.items.recipes.equipRecipes;


import com.quadx.dungeons.items.equipment.Arms;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class ArmRe extends Recipe {
    public ArmRe(){
        super();
        name="Arms";
        loadEquip(new Arms());
        isEquipRecipe=true;

    }
}
