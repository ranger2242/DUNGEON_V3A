package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;


/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class ImageLoader {
    public Random rn=new Random();
    public static Texture[] floors=new Texture[8];
    public static Texture[] w =new Texture[21];
    public static Texture[] walls=new Texture[3];
    public static Texture[] gold=new Texture[3];
    public static Texture[] a=new Texture[8];
    public static Texture[] b=new Texture[7];
    public static Texture[] c=new Texture[8];
    public static Texture potion = new Texture("images\\icons\\items\\icPotion.png");
    public static Texture mana = new Texture("images\\icons\\items\\icMPlus.png");
    public static Texture crate=new Texture("images\\icons\\items\\icCrate.png");
    public static Texture warp=new Texture("images\\tiles\\icWarp.png");

    public ImageLoader(){
        for(int i=0;i<8;i++){
            floors[i]=new Texture("images\\tiles\\f"+(i)+".png");
        }
        for(int i=0;i<8;i++){
            a[i]=new Texture("images\\tiles\\a"+(i)+".png");
        }
        for(int i=0;i<7;i++){
            b[i]=new Texture("images\\tiles\\b"+(i)+".png");
        }
        for(int i=0;i<8;i++){
            c[i]=new Texture("images\\tiles\\c"+(i)+".png");
        }
        for(int i=0;i<3;i++){
            walls[i]=new Texture("images\\tiles\\n"+i+".png");
        }
        gold[0]=new Texture("images\\icons\\items\\icCoinS.png");
        gold[1]=new Texture("images\\icons\\items\\icCoinM.png");
        gold[2]=new Texture("images\\icons\\items\\icCoinL.png");
        for(int i=0;i<21;i++){
           // try {
                w[i] = new Texture("images\\tiles\\water\\w" +i + ".png");
           // }catch (GdxRuntimeException r){}

        }
    }
}
