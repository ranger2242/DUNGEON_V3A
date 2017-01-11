package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by range_000 on 1/5/2017.
 */
public class Text {
    public int size =1;
    public Color c=Color.GRAY;
    public Vector2 pos= new Vector2();
    public String text = "";

    public Text(String s,Vector2 v,Color c, int size){
        text=s;
        pos=v;
        this.c=c;
        this.size=size;
    }

}
