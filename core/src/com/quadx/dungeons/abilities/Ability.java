package com.quadx.dungeons.abilities;

import java.util.ArrayList;

/**
 * Created by range on 5/20/2016.
 */
public class Ability {
    protected boolean primary;
    protected ArrayList<String> output=new ArrayList<>();

    public Ability(){

    }
    public void onActivate(){

    }
    public ArrayList<String> details(){

        return  output;
    }
}
