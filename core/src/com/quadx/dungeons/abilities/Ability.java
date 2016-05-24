package com.quadx.dungeons.abilities;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Ability {
    ArrayList<String> output=new ArrayList<>();

    Ability(){

    }
    public void onActivate(){

    }
    public ArrayList<String> details(){

        return  output;
    }
}
