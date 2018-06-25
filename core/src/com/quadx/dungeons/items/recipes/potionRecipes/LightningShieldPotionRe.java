package com.quadx.dungeons.items.recipes.potionRecipes;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.potions.LightningShieldPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.resources.*;
import javafx.util.Pair;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class LightningShieldPotionRe extends Recipe {
    public LightningShieldPotionRe(){
        //fast regen for time and full health
        output=new LightningShieldPotion();
        name=output.getName();
        fileName="icRecipe.png";
        isPotionRecipe =true;
        craftFileName=output.getFileName();
        loadIcon();
        Pair<Integer,Item>[] c = new Pair[5];
        c[0] = new Pair<>(2,new ChargeStone());
        c[1] = new Pair<>(1,new Blood());
        c[2] = new Pair<>(1,new Heart());
        c[3] = new Pair<>(3,new Crystal(1));
        c[4] = new Pair<>(100,new Gold());

        setCosts(c);
        itemLimit=4;
    }
}
