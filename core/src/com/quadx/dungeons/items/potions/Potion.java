package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.items.Item;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Potion extends Item {
    protected ArrayList<String> effects = new ArrayList<>();
    public ArrayList<String> getEffects() {
        return effects;
    }
}
