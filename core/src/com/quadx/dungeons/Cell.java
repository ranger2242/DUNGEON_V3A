package com.quadx.dungeons;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tom isLive 11/7/2015.
 */
public class Cell {
    private boolean hasPlayer=false;
    private boolean isLive = false;
    boolean hasWater = false;
    private boolean hasCrate=false;
    private boolean hasLoot = false;
    private boolean hasMon = false;
    private boolean isWarp=false;
    private boolean hasShop=false;
    private boolean agroPoint=false;
    private boolean attArea=false;
    int monsterIndex;
    public int LLIndex;
    private int x;
    private int y;
    Color color = null;

    public Cell() {
    }

    public boolean getAttArea(){return  attArea;}
    public boolean getWater(){return hasWater;}
    public boolean getShop(){return hasShop;}
    public boolean getAgro(){return agroPoint;}
    public boolean getState()
    {
        return isLive;
    }
    public boolean getLootState()
    {
        return hasLoot;
    }
    public boolean getCrate(){return hasCrate;}
    public int     getIndex(){return monsterIndex;}
    public int getLLIndex(){
        return LLIndex;
    }
    public Color getColor(){
        if(getState()){
            color=new Color(.235f, .235f, .196f, 1);
            if(hasLoot()) color=new Color(1f, .647f, 0f, 1);
            if(hasCrate())color=new Color(.627f, .322f, .176f, 1);
            if(getShop()) color=new Color(1f, 0f, 1f, 1);
            if(hasWarp()) color=new Color(0f, 1f, 0f, 1);
            if(getAttArea())color=new Color(.7f,0,0f,1);
        }
        else            color=new Color(0f, 0f, 0f, 1);

        return color;
    }
    public void setAttArea(boolean a){attArea=a;}
    public void setCords(int a, int b){x=a;y=b;}
    public void setAgro(boolean b, int a){agroPoint=b; monsterIndex =a;}
    public void setShop(boolean set){hasShop=set;}
    public void setMon(boolean set){hasMon=set;}
    public void setWarp(){isWarp= true;}
    public void setState()
    {
        isLive = true;
    }
    public void setCrate(boolean set){hasCrate=set;}
    public void setWater(){hasWater= true;}
    public void setColor(Color c){
        color=c;
    }
    public void setLLIndex(int i){LLIndex=i;}
    public void setHasLoot(boolean set) {
        hasLoot =set;
        if(set){
            boolean isEmpty = false;
        }
    }
    public void setPlayer(boolean set){hasPlayer=set;}
    public void setMonsterIndex(int set){monsterIndex=set;}
    public boolean hasWarp(){return isWarp;}
    public boolean hasMon(){return hasMon;}
    public boolean hasPlayer(){return hasPlayer;}
    public boolean hasLoot() {return hasLoot;}

    public boolean hasCrate() {return hasCrate;}

    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }

    public int getMonsterIndex() {
        return monsterIndex;
    }
}
