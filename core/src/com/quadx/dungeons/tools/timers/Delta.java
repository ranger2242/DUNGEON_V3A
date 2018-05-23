package com.quadx.dungeons.tools.timers;

/**
 * Created by Chris Cavazos on 9/29/2017.
 */
public class Delta {
    protected float dtPassed = 0;
    protected float limit;
    public Delta(float lim) {
        limit=lim;
    }
    public void update(float dt) {
        if(dtPassed<=limit) {
            dtPassed += dt;
        }
    }
    public boolean isDone(){
        return dtPassed>limit;
    }
    public void reset(){
        dtPassed=0;
    }


}

