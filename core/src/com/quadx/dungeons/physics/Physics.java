package com.quadx.dungeons.physics;

import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.shapes1_5.EMath;

/**
 * Created by Chris Cavazos on 1/11/2017.
 */
public class Physics {
    public static double getAngleRad(Vector2 start, Vector2 end){
        float dx= EMath.dx(end,start);
        float dy= EMath.dy(start,end);
        double a=Math.atan2(dy,dx);
        if(a<0)
            a+=(Math.PI*2);
        return a;
    }
    public static Vector2 getVector(double vel, Vector2 start, Vector2 end){
        float dx= EMath.dx(end,start);
        float dy= EMath.dy(start,end);
        double theta=Math.atan2(dy,dx);
        float vx=(float)(vel* Math.cos(theta));
        float vy=(float)(vel*Math.sin(theta));
        return new Vector2(vx,vy);
    }
}
