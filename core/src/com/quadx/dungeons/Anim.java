package com.quadx.dungeons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.EMath;

/**
 * Created by range_000 on 1/10/2017.
 */
public class Anim {
    Texture texture= null;
    Vector2 pos= null;
    float vel= 0;
    Vector2 dest=null;
    boolean end= false;

    public Anim(){

    }
    public Anim(Texture t, Vector2 p, float v, Vector2 d){
        texture=t;
        pos=p;
        vel=v;
        dest=d;
    }
    public void update(){
        float dx= EMath.dx(pos,dest);
        float dy= EMath.dy(pos,dest);
        double theta=Math.atan2(dy,dx);
        Vector2 velcomp= new Vector2((float) Math.cos(theta),(float)Math.sin(theta));
        pos.x+=velcomp.x;
        pos.y+=velcomp.y;
        if((int) dest.x ==(int) pos.x &&(int) dest.y==(int) pos.y )end=true;
    }

    public Vector2 getPos() {
        return pos;
    }
    public boolean isEnd(){
        return end;
    }

    public Texture getTexture() {
        return texture;
    }
}
