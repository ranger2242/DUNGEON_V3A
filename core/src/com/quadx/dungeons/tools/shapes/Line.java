package com.quadx.dungeons.tools.shapes;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 2/4/2017.
 */
public class Line {
    public Vector2 a=new Vector2();
    public Vector2 b=new Vector2();
    public Line(){

    }
    public Line(Vector2 a,Vector2 b){
        this.a=a;
        this.b=b;
    }
}
