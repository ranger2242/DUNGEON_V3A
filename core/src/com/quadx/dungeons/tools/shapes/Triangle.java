package com.quadx.dungeons.tools.shapes;

/**
 * Created by Chris Cavazos on 1/23/2017.
 */
public class Triangle {
    float[] points= new float[6];
    public Triangle(){
        this(new float[]{0,0,0,0,0,0});
    }
    public Triangle(float[] p){
        points=p;
    }
    public float[] getPoints(){
        return points;
    }
}
