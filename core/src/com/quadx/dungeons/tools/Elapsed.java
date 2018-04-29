package com.quadx.dungeons.tools;

/**
 * Created by Chris Cavazos on 4/29/2018.
 */
public class Elapsed {
    double start=0;
    double time= 0;
   /* Elapsed(){
        start
    }*/
    public void update(float dt){
        time+=dt;
    }

    public double getTime() {
        return time;
    }
}
