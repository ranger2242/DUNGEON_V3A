package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.potions.EnergyPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.resources.Meat;
import com.quadx.dungeons.items.resources.Mushroom;
import com.quadx.dungeons.items.resources.Water;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class EnergyPotionRe extends Recipe {
    public EnergyPotionRe(){
        //fast regen for time and full health
        output=new EnergyPotion();
        name=output.getName();
        fileName="icRecipe.png";
        isPotionRecipe =true;
        craftFileName=output.getFileName();
        loadIcon();
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(2,new Water());
        c[1] = new Pair<>(2,new Mushroom());
        c[2] = new Pair<>(2,new Meat(1));
        setCosts(c);
        itemLimit=3;
    }
}
