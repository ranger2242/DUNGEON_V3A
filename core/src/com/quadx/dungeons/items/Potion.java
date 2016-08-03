package com.quadx.dungeons.items;

import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Potion extends Item {
    public Potion  (){
        name="Potion+";
        int size= (int) (3*((double)player.getHpMax()/(double)500));
        if(size==0){
            icon= ImageLoader.potion[0];
            hpmod=60;
            cost=100;
        }
        else if(size==1){
            icon=ImageLoader.potion[1];
            hpmod=200;
            cost=100;
        }
        else if(size>1){
            icon=ImageLoader.potion[2];
            hpmod=1000;
            cost=100;
        }
    }
}
