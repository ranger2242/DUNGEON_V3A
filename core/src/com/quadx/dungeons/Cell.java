package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.ManaPlus;
import com.quadx.dungeons.items.Potion;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.Random;

import static com.quadx.dungeons.states.mapstate.MapStateRender.dtWaterEffect;

/**
 * Created by Tom isLive 11/7/2015.
 */
public class Cell {
    Random rn=new Random();
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
    Item item;
    int gold;
    int monsterIndex;
    public int LLIndex;
    private int x;
    private int y;
    boolean blink=false;
    public int boosterItem=0;
    Color color = null;
    Texture tile;

    public Cell() {
        setTile(ImageLoader.a[0]);
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
    public int getBoosterItem(){
        return boosterItem;
    }
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
            if (blink&& rn.nextBoolean())
                color= new Color(.12f,.12f,.8f,1f);
            else if(blink )
                color=new Color(0f,0f,.8f,1f);
            dtWaterEffect=0;
        }
        else dtWaterEffect+= Gdx.graphics.getDeltaTime();

        return color;
    }
    public void setBoosterItem(int i){
        boosterItem=i;
        if(boosterItem==1){
            item=new Potion();
            item.setIcon(ImageLoader.potion);
        }if(boosterItem==2){
            item=new ManaPlus();
            item.setIcon(ImageLoader.mana);
        }
    }
    public void setAttArea(boolean a){attArea=a;}
    public void setCords(int a, int b){x=a;y=b;}
    public void setAgro(boolean b, int a){agroPoint=b; monsterIndex =a;}
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
    public void setLLIndex(int i){LLIndex=i;}
    public void setTile(String s){
        try {
            tile = new Texture(Gdx.files.internal("images\\"+s));
        }catch (GdxRuntimeException | NullPointerException e ){
            tile=null;
        }
    }
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
    public Texture getTile(){
        return tile;
    }
    public void setGold(int i) {
        gold=i;
    }
    public void setItem(Item item) {
        this.item = item;
    }

    public int getGold() {
        return gold;
    }

    public Item getItem() {
        return item;
    }
}
