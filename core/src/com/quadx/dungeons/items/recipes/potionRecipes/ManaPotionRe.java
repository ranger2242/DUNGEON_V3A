package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.potions.ManaPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.resources.Blood;
import com.quadx.dungeons.items.resources.Crystal;
import com.quadx.dungeons.items.resources.Water;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class ManaPotionRe extends Recipe {
    public ManaPotionRe(){
        //fast regen for time and full health
        output=new ManaPotion();
        name=output.getName();
        fileName="icRecipe.png";
        isPotionRecipe =true;
        craftFileName=output.getFileName();
        loadIcon();
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(2,new Water());
        c[1] = new Pair<>(2,new Blood());
        c[2] = new Pair<>(2,new Crystal(1));
        setCosts(c);
        itemLimit=3;
    }
}
