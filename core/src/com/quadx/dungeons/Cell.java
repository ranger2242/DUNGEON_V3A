package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.EnergyPlus;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.ManaPlus;
import com.quadx.dungeons.items.Potion;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapStateExt;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.Triangle;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.GridManager.drawList;
import static com.quadx.dungeons.GridManager.fixHeight;
import static com.quadx.dungeons.states.mapstate.MapState.cell;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;
import static com.quadx.dungeons.states.mapstate.MapStateExt.effects;

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
    Rectangle bounds=null;
    Vector2 pos = new Vector2();    //<- position relative to grid
    Vector2 absPos = new Vector2(); //<- for use with cellw shift
    Vector2 fixedPos= new Vector2();
    private boolean hasItem = false;
    public ArrayList<Command> commandQueue= new ArrayList<>();
    boolean effectLoaded=false;
    ParticleEffect effect=null;
    int height=0;
    Polygon corners=new Polygon();
    float dtwater=0;

     static Color[] colors= new Color[]{new Color(.1f, .1f, .8f, 1f),new Color(.3f, .3f, .8f, 1f), new Color(0f, 0f, .8f, 1f)};

    ArrayList<Triangle> tris= new ArrayList<>();

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
           // if(hasMon()) color=new Color(1f, .2f, .2f, 1);
            if(getAttArea())color=new Color(.7f,0,0f,1);
        }
       // else color=new Color(0f, 0f, 0f, 1);

        if(getWater()) {
            if (dtwater > .5) {
                int r = rn.nextInt(3);
                color = colors[r];
                dtwater=0;
            }else{
                color=colors[0];
            }
        }
        return color;
    }
    public Texture getTile(){
        return tile;
    }
    public Item getItem() {
        return item;
    }
    public Monster getMonster(){return monster;}
    public Vector2 getPos(){return pos;}
    public Vector2 getAbsPos(){return absPos;}
    public Vector2 getFixedPos(){return fixedPos;}

    public boolean getAttArea(){return  attArea;}
    public boolean getWater(){return hasWater;}
    public boolean getShop(){return hasShop;}
    public boolean getState()
    {
        return isLive;
    }
    public Rectangle getBounds(){return bounds;}
    public boolean hasWarp(){return isWarp;}
    public boolean hasMon(){return hasMon;}
    public boolean hasLoot() {return hasLoot;}
    public boolean hasCrate() {return hasCrate;}
    public boolean hasItem(){
        return hasItem;
    }
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
    public void setCorners(Polygon c){
        corners=c;
        float[] f=c.getVertices();
        tris.clear();
        tris.add(new Triangle(new float[]{f[0], f[1], f[2], f[3], f[8], f[9]}));
        tris.add(new Triangle(new float[]{f[2], f[3], f[4], f[5], f[8], f[9]}));
        tris.add(new Triangle(new float[]{f[4], f[5], f[6], f[7], f[8], f[9]}));
        tris.add(new Triangle(new float[]{f[6], f[7], f[0], f[1], f[8], f[9]}));
    }
    public ArrayList<Triangle> getTris(){
        return tris;
    }
    public Polygon getCorners() {
        return corners;
    }
    public void setHeight(int h){height=h;}
    public int getHeight(){return height;}
//SETTERS---------------------------------------------------------------------------------
    public void setMonster(Monster m){
        monster=m;
//        monster.setCords(x,y);
        setMon(true);
    }
    public void setBoosterItem(int i){
        boosterItem=i;
        if(boosterItem==1){
            item=new Potion();
        }if(boosterItem==2){
            item=new ManaPlus();
        }if(boosterItem==0){
            item=new EnergyPlus();
        }
    }
    public void setAttArea(boolean a){attArea=a;}

    public void setCords(int a, int b) {
        x = a;
        y = b;
        pos=new Vector2(a,b);
        absPos=new Vector2(a*cellW,b*cellW);
        bounds = new Rectangle(absPos.x,fixHeight(absPos),cell.x,cell.y);
    }
    public void setShop(boolean set){hasShop=set;}
    public void setMon(boolean set){hasMon=set;}
    public void setWarp(){isWarp= true;}
    public void setState() {
        if(rn.nextFloat()>.2){

            setTile(ImageLoader.floors[0]);
        }
        else{
            //setTile(ImageLoader.floors[rn.nextInt(17)]);
            setTile(ImageLoader.floors[0]);

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
        color=colors[0];
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
        if(item !=null){
            this.item.setTexturePos(new Vector2(absPos.x, GridManager.fixHeight(absPos)));
            this.item.setHitBox(new Rectangle(absPos.x, GridManager.fixHeight(absPos), item.getIcon().getWidth(), item.getIcon().getHeight()));
        }
    }

//OTHER----------------------------------------------------------------------------------
    public void addCommand(Command c){
        commandQueue.add(c);
    }
    public void updateParticles(){
        if(this.item!= null) {
            hasItem = true;
            if(!effectLoaded && getState() && drawList.contains(this)){
                if(item.hasEffect()) {
                    effect = MapStateExt.loadParticles("ptItem", getAbsPos().x +(item.getIcon().getWidth()/2), getAbsPos().y ,item.getPtColor(), 2);
                    effectLoaded = true;
                    MapStateExt.addEffect(effect);
                }
            }
        }
        else{
            hasItem=false;
            if(effectLoaded){
                try {
                    effects.remove(effect);
                    effect.dispose();
                    effect=null;
                }catch (Exception e){

                }
            }
        }
    }
    public void updateVariables(){
        float dt= Gdx.graphics.getDeltaTime();
        fixedPos.set(new Vector2(absPos.x, fixHeight(absPos)));
        if(getWater()){
            dtwater+=dt;
        }
    }
    public void clearMonster(){
        monster=null;
        hasMon=false;
    }
}
