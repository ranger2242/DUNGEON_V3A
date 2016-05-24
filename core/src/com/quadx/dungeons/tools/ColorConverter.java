package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom on 1/5/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ColorConverter {
    private int redI=0;
    private float redF=0;
    private float greenF=0;
    private float blueF=0;
     private Color color;

    public ColorConverter(int r, int g, int b,int a){
        redI=r;
        convert(redI, g, b);
        color=new Color(redF,greenF,blueF,a);
       // printValues();
    }
    public ColorConverter(float r, float g, float b, int a){
        redF=r;
        greenF=g;
        blueF=b;
        convert(redF,greenF, blueF);
        color=new Color(greenF,greenF,blueF,a);
        //printValues();
    }
    public void printValues(){
        System.out.println(redF+" "+greenF+" "+blueF);
    }
    public  Color getLIBGDXColor(){
        return color;
    }
    private void convert(int r, int g, int b){
        redF=(float)r/255;
        greenF=(float)g/255;
        blueF=(float)b/255;
    }
    private void convert(float r, float g, float b){
        redI=(int)r*255;
        greenF=(int)g*255;
        blueF=(int)b*255;

    }


}
