package com.quadx.dungeons;

/**
 * Created by Tom isLive 11/7/2015.
 */
public class Cell {
    boolean hasPlayer=false;
    boolean isLive = false;
    boolean isEmpty =true;
    boolean hasWater = false;
    boolean hasCrate=false;
    boolean hasLoot = false;
    boolean hasMon = false;
    boolean isWarp=false;
    boolean hasShop=false;
    boolean agroPoint=false;
    boolean playerFront=false;
    boolean attArea=false;
    int monsterIndex;
    int x;
    int y;


    public Cell() {
    }
    Cell(boolean activate)
    {
        isLive =activate;
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
    public boolean isPlayerFront(){return playerFront;}
    public void setAttArea(boolean a){attArea=a;}
    public void setCords(int a, int b){x=a;y=b;}
    public void setAgro(boolean b, int a){agroPoint=b; monsterIndex =a;}
    public void setShop(boolean set){hasShop=set;}
    public void setMon(boolean set){hasMon=set;}
    public void setWarp(boolean set){isWarp=set;}
    public void setState(boolean set)
    {
        isLive =set;
    }
    public void setCrate(boolean set){hasCrate=set;}
    public void setWater(boolean set){hasWater=set;}
    public void setHasLoot(boolean set) {
        hasLoot =set;
        if(set){
            isEmpty =false;}
    }
    public void setPlayer(boolean set){hasPlayer=set;}
    public void setFront(boolean set){playerFront=set;}
    public void setMonsterIndex(int set){monsterIndex=set;}
    public boolean hasWarp(){return isWarp;}
    public boolean hasMon(){return hasMon;}
    public boolean hasPlayer(){return hasPlayer;}
    public String getSummary(int denx, int deny) {
        /*String s=Main.field.getText();
        String s1=s+"\n["+denx+","+deny+"]";
        s1+="\nLive:" + isLive;
        if(hasLoot) {s1+="\nLoot:"+ hasLoot;}
        if(hasMon) {s1+="\nMonster:"+ hasMon;}
        else{s1+="\nEmpty:"+ isEmpty;}
        return s1;*/
        return "";
    }
    public String exportCell() {
        String s =isLive+","+ hasLoot +","+ isEmpty;
        return s;
    }
    public boolean hasLoot() {return hasLoot;}

    public boolean hasCrate() {return hasCrate;}

    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
}
