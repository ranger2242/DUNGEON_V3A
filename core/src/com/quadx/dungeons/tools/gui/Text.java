package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;

import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.states.MainMenuState.gl;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;

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
    public static float strWidth(String s){
        CharSequence cs=s;
        gl.setText(Game.getFont(),cs);
        return gl.width;
    }
    public static float centerString(String s){
        return (viewX+WIDTH/2)- (strWidth(s)/2);
    }

}
