package com.quadx.dungeons.tools;

/**
 * Created by Chris Cavazos on 9/29/2017.
 */
public class Delta {
    private float dtPassed = 0;
    private float limit=0;
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

