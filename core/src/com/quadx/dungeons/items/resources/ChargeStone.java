package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class ChargeStone extends Resource {
    public ChargeStone(){
        fileName="mods/icChargeStone.png";
        name="ChargeStone";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
