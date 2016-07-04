package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/19/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Protect extends Attack {
   // public static float[] time={1f,1.2f,1.5f,2f,2.4f};

    public Protect()  {
        costGold=14000;
        type=3;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{80,85,90,95,100};
        name="Protect";
        power=0;
        cost=80;
        mod=6;
        spread=0;
        range=0;
        description="Protects the user from damage.";
        setIcon(ImageLoader.attacks.get(6));
    }
    public int getLevel(){
        return level;
    }
}
