package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.potions.StatPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.resources.Blood;
import com.quadx.dungeons.items.resources.Bone;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class StatPotionRe extends Recipe {
    public StatPotionRe(){
        super();
        //fast regen for time and full health
        output=new StatPotion();
        name=output.getName();
        isPotionRecipe =true;
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(2,new Blood());
        c[1] = new Pair<>(2,new Bone());
        setCosts(c);
        itemLimit=5;
    }
}
