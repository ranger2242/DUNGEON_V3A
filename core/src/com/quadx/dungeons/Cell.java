package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.Random;

import static com.quadx.dungeons.states.mapstate.MapStateRender.dtWaterEffect;

/**
 * Created by Tom isLive 11/7/2015.
 */
@SuppressWarnings({"ALL", "unused"})
public class Cell {
    private final Random rn=new Random();
    private boolean isLive = false;
    boolean hasWater = false;
    private boolean hasCrate=false;
    private boolean hasLoot = false;
    private boolean hasMon = false;
    private boolean isWarp=false;
    private boolean hasShop=false;
    private boolean attArea=false;
    private Item item;
    private int gold;
    int monsterIndex=-1;
    private int x;
    private int y;
    private int boosterItem=-1;
    private Color color = null;
    private Texture tile;
    private Monster monster=null;

    public Cell() {
        setTile(ImageLoader.a[0]);
    }
    //GETTERS---------------------------------------------------------------------------------
    public Color getColor(){
        if(getState()){
            color=new Color(.235f, .235f, .196f, 1);
            if(hasLoot()) color=new Color(1f, .647f, 0f, 1);
            if(hasCrate() && boosterItem ==0)color=new Color(.627f, .322f, .176f, 1);
            if(getShop()) color=new Color(1f, 0f, 1f, 1);
            if(hasWarp()) color=new Color(0f, 1f, 0f, 1);
            if(getAttArea())color=new Color(.7f,0,0f,1);
        }
        else color=new Color(0f, 0f, 0f, 1);
        if(getWater()){
            color=new Color(.07f, .07f, .8f, 1f);
        }
        if( dtWaterEffect>Game.frame*60){
            boolean blink = false;
            if (blink && rn.nextBoolean())
                color= new Color(.12f,.12f,.8f,1f);
            else if(blink)
                color=new Color(0f,0f,.8f,1f);
            dtWaterEffect=0;
        }
        else dtWaterEffect+= Gdx.graphics.getDeltaTime();

        return color;
    }
    public Texture getTile(){
        return tile;
    }
    public Item getItem() {
        return item;
    }
    public Monster getMonster(){return monster;}
    public boolean getAttArea(){return  attArea;}
    public boolean getWater(){return hasWater;}
    public boolean getShop(){return hasShop;}
    public boolean getState()
    {
        return isLive;
    }
    public boolean hasWarp(){return isWarp;}
    public boolean hasMon(){return hasMon;}
    public boolean hasLoot() {return hasLoot;}
    public boolean hasCrate() {return hasCrate;}
    public int getBoosterItem(){
        return boosterItem;
    }
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
    public int getMonsterIndex() {
        return monsterIndex;
    }
    public int getGold() {
        return gold;
    }
//SETTERS---------------------------------------------------------------------------------
    public void setMonster(Monster m){
        monster=m;
        monster.setCords(x,y);
        setMon(true);
    }
    public void setBoosterItem(int i){
        boosterItem=i;
        if(boosterItem==1){
            item=new Potion();
            item.setIcon(ImageLoader.potion);
        }if(boosterItem==2){
            item=new ManaPlus();
            item.setIcon(ImageLoader.mana);
        }if(boosterItem==0){
            item=new EnergyPlus();
            item.setIcon(ImageLoader.energy[0]);
        }
    }
    public void setAttArea(boolean a){attArea=a;}
    public void setCords(int a, int b){x=a;y=b;}
    public void setShop(boolean set){hasShop=set;}
    public void setMon(boolean set){hasMon=set;}
    public void setWarp(){isWarp= true;}
    public void setState() {
        if(rn.nextFloat()>.2){

            setTile(ImageLoader.floors[0]);
        }
        else{
            setTile(ImageLoader.floors[rn.nextInt(13)]);

        }
        isLive = true;
    }
    public void setTile(Texture t) {
        tile=t;
    }
    public void setCrate(boolean set){hasCrate=set;}
    public void setWater(){
        tile= ImageLoader.w[0];
        hasWater= true;
    }
    public void setColor(Color c){
        color=c;
    }
    public void setHasLoot(boolean set) {
        hasLoot =set;
        if(set){
            boolean isEmpty = false;
        }
    }
    public void setMonsterIndex(int set){monsterIndex=set;}
    public void setItem(Item item) {
        this.item = item;
    }

//OTHER----------------------------------------------------------------------------------
    public void clearMonster(){
        monster=null;
        hasMon=false;
    }
}
