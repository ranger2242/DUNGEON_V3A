package com.quadx.dungeons.items.resources;

import static com.quadx.dungeons.Game.rn;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Grass extends Resource {
    public Grass(){
        int size=1;
        float r=rn.nextFloat();
        if(r<.333f){
            size=3;
        }else if(r<.666f){
            size=2;
        }
        init(size);
    }

    public Grass(int s){
        init(s);
    }
    public void init(int s){
        hasEffect=false;
        name="GRASS";
        gINIT(1,"icGrass"+s);
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}