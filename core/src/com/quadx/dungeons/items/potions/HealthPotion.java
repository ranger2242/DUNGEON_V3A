package com.quadx.dungeons.items.potions;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class HealthPotion extends Potion {
    public HealthPotion(){
        name="Health Potion";
        fileName="potions\\pRed.png";
        loadIcon();
        effects.add("MAX HP");
        effects.add("2x HP REGEN");
    }

}
