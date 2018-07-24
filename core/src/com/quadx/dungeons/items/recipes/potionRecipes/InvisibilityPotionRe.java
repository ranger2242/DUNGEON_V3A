package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.potions.InvisibilityPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.resources.Brain;
import com.quadx.dungeons.items.resources.Hypergem;
import com.quadx.dungeons.items.resources.Leaf;
import com.quadx.dungeons.items.resources.Water;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class InvisibilityPotionRe extends Recipe {
    public InvisibilityPotionRe(){
        super();
        //fast regen for time and full health
        output=new InvisibilityPotion();
        name=output.getName();
        isPotionRecipe =true;
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(1,new Hypergem());
        c[1] = new Pair<>(3,new Water());
        c[2] = new Pair<>(3,new Leaf());
        c[3] = new Pair<>(1,new Brain());
        setCosts(c);
        itemLimit=4;
    }
}
