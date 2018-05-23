package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.State;

import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.states.MainMenuState.gl;

/**
 * Created by range_000 on 1/5/2017.
 */
public class Text {
    public int size;
    public Color c;
    public Vector2 pos;
    public String text;

    public Text(String s,Vector2 v,Color c, int size){
        text=s;
        pos=v;
        this.c=c;
        this.size=size;
    }
    public static float strWidth(String s){
        CharSequence cs=s;
        gl.setText(Game.getFont(),cs);
        return gl.width;
    }
    public static float centerString(String s){
        return (State.getView().x+WIDTH/2)- (strWidth(s)/2);
    }

    public static float[] fitLineToWord(String s){
        //x1,y1,x2,y2
        float[] arr=new float[8];
        arr[0]= -10;
        arr[1]=-15;
        arr[2]=strWidth(s)+20;
        arr[3]=-15;
        arr[4]= -10;
        arr[5]=-12;
        arr[6]=strWidth(s)+35;
        arr[7]=-12;
        return arr;
    }
}
