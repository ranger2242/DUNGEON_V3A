package com.quadx.dungeons.items.recipes;


import com.quadx.dungeons.items.equipment.Arms;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class ArmRe extends Recipe {
    public ArmRe(){
        name="Arms";
        loadEquip(new Arms());
        fileName="icRecipe.png";
        loadIcon();
    }
}
