package com.quadx.dungeons.items.recipes.equipRecipes;

import com.quadx.dungeons.items.equipment.Ring;
import com.quadx.dungeons.items.recipes.Recipe;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class RingRe extends Recipe {
    public RingRe(){
        super();
        name="Ring";
        loadEquip(new Ring());
        isEquipRecipe=true;
    }
}
