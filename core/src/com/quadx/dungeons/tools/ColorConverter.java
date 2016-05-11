package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 1/5/2016.
 */
public class ColorConverter {
    int redI=0;
    int greenI=0;
    int blueI=0;
    float redF=0;
    float greenF=0;
    float blueF=0;
     Color color;

    public ColorConverter(int r, int g, int b,int a){
        redI=r;
        greenI=g;
        blueI=b;
        convert(redI,blueI,greenI);
        color=new Color(redF,greenF,blueF,a);
        printValues();
    }
    public ColorConverter(float r, float g, float b, int a){
        redF=r;
        greenF=g;
        blueF=b;
        convert(redF,blueF,greenF);
        color=new Color(greenF,greenF,blueF,a);
        printValues();
    }
    public void printValues(){
        System.out.println(redF+" "+greenF+" "+blueF);
    }
    public  Color getLIBGDXColor(){
        return color;
    }
    void convert(int r, int g, int b){
        redF=(float)r/255;
        greenF=(float)g/255;
        blueF=(float)b/255;
    }
    void convert(float r, float g, float b){
        redI=(int)r*255;
        greenF=(int)g*255;
        blueF=(int)b*255;

    }


}
