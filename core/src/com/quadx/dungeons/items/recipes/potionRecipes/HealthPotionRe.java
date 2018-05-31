package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.resources.Blood;
import com.quadx.dungeons.items.resources.Heart;
import com.quadx.dungeons.items.potions.HealthPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class HealthPotionRe extends Recipe {
    public HealthPotionRe(){
        //fast regen for time and full health
        output=new HealthPotion();
        name=output.getName();
        fileName="icRecipe.png";
        isPotionRecipe =true;
        craftFileName=output.getFileName();
        loadIcon();
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(3,new Blood());
        c[1] = new Pair<>(3,new Heart());
        setCosts(c);
        itemLimit=2;
    }
}
