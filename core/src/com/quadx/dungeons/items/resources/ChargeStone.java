package com.quadx.dungeons.items.resources;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class ChargeStone extends Resource {
    public ChargeStone(){
        name="ChargeStone";
        gINIT(1,"icChargeStone");
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
