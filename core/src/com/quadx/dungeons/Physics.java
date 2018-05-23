package com.quadx.dungeons;

import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.EMath;

/**
 * Created by Chris Cavazos on 1/11/2017.
 */
public class Physics {
    public static Vector2 getVector(double vel, Vector2 start, Vector2 end){
        float dx= EMath.dx(start,end);
        float dy= EMath.dy(start,end);
        double theta=Math.atan2(dy,dx);
        float vx=(float)(vel* Math.cos(theta));
        float vy=(float)(vel*Math.sin(theta));
        return new Vector2(vx,vy);
    }
}
