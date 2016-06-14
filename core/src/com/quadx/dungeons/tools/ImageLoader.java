package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;


/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class ImageLoader {
    public Random rn=new Random();
    public static Texture[] floors=new Texture[8];
    public static Texture[] waters=new Texture[1];
    public static Texture[] walls=new Texture[3];
    public static Texture[] gold=new Texture[3];
    public static Texture potion = new Texture("images\\icons\\items\\icPotion.png");
    public static Texture mana = new Texture("images\\icons\\items\\icMPlus.png");
    public static Texture crate=new Texture("images\\icons\\items\\icCrate.png");


    public ImageLoader(){
        for(int i=0;i<8;i++){
            floors[i]=new Texture("images\\tiles\\f"+(i)+".png");
        }
        for(int i=0;i<3;i++){
            walls[i]=new Texture("images\\tiles\\n"+i+".png");
        }
        gold[0]=new Texture("images\\icons\\items\\icCoinS.png");
        gold[1]=new Texture("images\\icons\\items\\icCoinM.png");
        gold[2]=new Texture("images\\icons\\items\\icCoinL.png");

        waters[0]=new Texture("images\\tiles\\w0.png");
    }
}
