package com.quadx.dungeons.tools;

import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.attacks.Heal;

/**
 * Created by range_000 on 1/5/2017.
 */
public class HealthBar {
    public float x=0;
    public float y=0;
    public float w=0;
    public float h=0;
    public HealthBar(){
        new HealthBar(0,0,0,0);
    }
    public HealthBar(float x, float y, float w,float h){
        this.x=x;
        this.y=y;
        this.h=h;
        this.w=w;
    }
    public void update(float x, float y, float w,float h){
        this.x=x;
        this.y=y;
        this.h=h;
        this.w=w;
    }
}
