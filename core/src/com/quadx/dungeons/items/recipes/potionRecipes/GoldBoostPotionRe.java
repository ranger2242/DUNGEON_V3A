package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.potions.GoldBoostPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.resources.*;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class GoldBoostPotionRe extends Recipe {
    public GoldBoostPotionRe(){
        //fast regen for time and full health
        output=new GoldBoostPotion();
        name=output.getName();
        fileName="icRecipe.png";
        isPotionRecipe =true;
        craftFileName=output.getFileName();
        loadIcon();
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(500,new Gold());
        c[1] = new Pair<>(2,new Blood());
        c[2] = new Pair<>(1,new Flower());
        c[3] = new Pair<>(1,new Heart());
        c[4] = new Pair<>(1,new Bone());

        setCosts(c);
        itemLimit=2;
    }
}
