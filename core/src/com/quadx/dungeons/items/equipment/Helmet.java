package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Helmet extends Equipment {
    public Helmet(){
        type=Type.Helmet;
        craftCost=new int[]{12,8,2000};
        init("icHelmet");
    }
}
