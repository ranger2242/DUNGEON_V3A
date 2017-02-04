package com.quadx.dungeons.tools.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris Cavazos on 2/4/2017.
 */
public class Circle {
    public Vector2 center=new Vector2();
    public float radius=0;

    public Circle() {
        new Circle(new Vector2(0,0),0);
    }

    public Circle(Vector2 c, float r) {
        center=c;
        radius=r;
    }
    public boolean overlaps(Rectangle rect){
        float distX = Math.abs(center.x - rect.x-rect.width/2);
        float  distY = Math.abs(center.y - rect.y-rect.height/2);

        if (distX > (rect.width/2 + radius)) { return false; }
        if (distY > (rect.height/2 + radius)) { return false; }

        if (distX <= (rect.width/2)) { return true; }
        if (distY <= (rect.height/2)) { return true; }

        float dx=distX-rect.width/2;
        float dy=distY-rect.height/2;
        return (dx*dx+dy*dy<=(radius*radius));
    }
}
