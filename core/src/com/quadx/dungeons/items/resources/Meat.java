package com.quadx.dungeons.items.resources;

import static com.quadx.dungeons.Game.rn;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Meat extends Resource {
    public Meat(){
        int size=1;
        float r=rn.nextFloat();
        if(r<.1){
            size=3;
        }else if(r< .4){
            size=2;
        }
        init(size);
    }

    public Meat(int s){
        init(s);
    }
    public void init(int s){
        fileName="mods\\icMeat"+s+".png";
        String m="";
        switch (s){
            case 1:{
                m="SMALL";
                break;
            }
            case 2:{
                m="MEDIUM";
                break;
            }
            case 3:{
                m="LARGE";
                break;
            }
        }
        name=m+" MEAT";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
