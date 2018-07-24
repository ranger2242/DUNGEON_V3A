package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.Mine;
import com.quadx.dungeons.items.resources.Grass;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.EMath;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.shapes1_5.Triangle;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.heightmap.HeightMap;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.rn;
import static com.quadx.dungeons.GridManager.fixY;
import static com.quadx.dungeons.GridManager.monsterList;
import static com.quadx.dungeons.states.mapstate.MapState.cell;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Tom isClear 11/7/2015.
 */
@SuppressWarnings({"ALL", "unused"})
public class Cell {
    static Color[] colors = new Color[]{
            new Color(.1f, .1f, .8f, 1f),
            new Color(.3f, .3f, .8f, 1f),
            new Color(0f, 0f, .8f, 1f)};

    public ArrayList<Command> commandQueue = new ArrayList<>();

    private ArrayList<Triangle> tris = new ArrayList<>();
    private Rectangle bounds = null;
    private Monster monster = null;
    private Polygon corners = new Polygon();
    private Texture tile;
    private Color color = null;
    private Delta dWater = new Delta(SECOND/2);
    private State state = State.BLOCKED;
    private Item item;

    private Vector2 pos = new Vector2();    //<- position relative to grid
    private Vector2 absPos = new Vector2(); //<- for use with cellw shift
    private Vector2 fixedPos = new Vector2();

    private boolean hasLoot = false;
    private boolean hasMon = false;
    private boolean attArea = false;
    private boolean hasItem = false;
    private boolean effectLoaded = false;
    private boolean hasPlayer = false;

    private int effect = -1;
    private int gold;
    private int x;
    private int y;
    private int boosterItem = -1;
    private int monsterIndex = -1;
    private int height = 0;

    public Cell() {
    }
    //GETTERS---------------------------------------------------------------------------------
    public ArrayList<Triangle> getTris() {
        return tris;
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public Command getNextComm(){
        if(!commandQueue.isEmpty()) {
            Command c= commandQueue.get(0);
            commandQueue.remove(0);
            return c;
        }else{
            return null;
        }

    }
    public Polygon getCorners() {
        return corners;
    }
    public Texture getTile() {
        return tile;
    }
    public Monster getMonster() {
        return monster;
    }
    public Vector2 pos() {
        return pos;
    }
    public Vector2 abs() {
        return absPos;
    }
    public Vector2 fixed() {
        return fixedPos;
    }
    public Color getColor() {
        if (isClear()) {
            color = HeightMap.getColor(height);// new Color(.235f, .235f, .196f, 1);
            if (isWater()) {
                if (dWater.isDone()) {
                    int r = rn.nextInt(3);
                    color = colors[r];
                    dWater.reset();
                } else {
                    color = colors[0];
                }
            }
            if (isShop()) color = new Color(1f, 0f, 1f, 1);
            if (isWarp()) color = new Color(0f, 1f, 0f, 1);
            if (getAttArea()) color = new Color(.7f, 0, 0f, 1);
            if (hasMon()) color = new Color(1, 0, 0, 1);
            if (hasItem() && item != null) color = item.getTileColor();
            if(hasPlayer) color=Color.DARK_GRAY;
        }


        return color;
    }
    public Item getItem() {
        return item;
    }
    public boolean getAttArea() {
        return attArea;
    }
    public boolean isWater() {
        return state == State.WATER;
    }
    public boolean isShop() {
        return state == State.SHOP;
    }
    public boolean isClear() {
        return state == State.CLEAR;
    }
    public boolean isWall() {
        return state == State.BLOCKED;
    }
    public boolean isWarp() {
        return state == State.WARP;
    }
    public boolean hasMon() {
        return hasMon;
    }
    public boolean hasLoot() {
        return hasLoot;
    }
    public boolean hasItem() {
        return hasItem;
    }
    public boolean hasGrass() {
        return item instanceof Grass;
    }
    public boolean canPlaceItem() {
        return  !(isClear() || hasLoot() || isWarp() || isWater() || hasItem());
    }
    public int getBoosterItem() {
        return boosterItem;
    }
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
    public int getGold() {
        return gold;
    }
    public int getHeight() {
        return height;
    }
    //SETTERS---------------------------------------------------------------------------------
    public void setCorners(Polygon c) {
        corners = c;
        float[] f = c.getVertices();
        tris.clear();
        tris.add(new Triangle(new float[]{f[0], f[1], f[2], f[3], f[8], f[9]}));
        tris.add(new Triangle(new float[]{f[2], f[3], f[4], f[5], f[8], f[9]}));
        tris.add(new Triangle(new float[]{f[4], f[5], f[6], f[7], f[8], f[9]}));
        tris.add(new Triangle(new float[]{f[6], f[7], f[0], f[1], f[8], f[9]}));
    }
    public void setHeight(int h) {
        height = h;
    }
    public void setMonster(Monster m) {
        monster = m;
        monsterList.add(monster);
        setMon(true);
    }
    public void setAttArea(boolean a) {
        attArea = a;
    }
    public void setCords(int a, int b) {
        x = a;
        y = b;
        pos = new Vector2(a, b);
        absPos = new Vector2(a * cellW, b * cellW);
        bounds = new Rectangle(absPos.x, fixY(absPos), cell.x, cell.y);
    }
    public void setShop(boolean set) {
        state = State.SHOP;
    }
    public void setMon(boolean set) {
        hasMon = set;
    }
    public void setWarp() {
        state = State.WARP;
    }
    public void setCleared() {
        if (isWall())
            state = State.CLEAR;
    }
    public void setTile(Texture t) {
        tile = t;
    }
    public void setWater(boolean b) {
        //tile = ImageLoader.w[0];
        color = colors[0];
        if(b)
        state = State.WATER;
        else
            state = State.CLEAR;
    }
    public void setColor(Color c) {
        color = c;
    }
    public void setHasLoot(boolean set) {
        hasLoot = set;
        if (set) {
            boolean isEmpty = false;
        }
    }
    public void setItem(Item item) {

        if (item != null) {
            Vector2 p=new Vector2(fixed());
            Vector2 ic = new Vector2(item.getIconDim());
            item.setTexturePos(p);
            item.setHitBox(new Rectangle(p.x,p.y, ic.x,ic.y));
            this.item = item;
            hasItem=true;
            setHasLoot(true);
        }else{
            hasItem=false;
        }
    }
    public void setPlayer(boolean s){
        hasPlayer=s;
    }
    public boolean isNearPlayer() {
        return EMath.pathag(player.pos(), pos) < 10;
    }
    public boolean canPlaceMon(){
        return !isNearPlayer() && isClear();
    }
    public boolean canPlacePlayer(){
        return (!isWater());
    }
    //OTHER----------------------------------------------------------------------------------
    public void addCommand(Command c) {
        commandQueue.add(c);
    }
    public void updateParticles() {
        if (this.item != null) {
            if (!effectLoaded && isClear()) {
                if (item.hasEffect()) {
                    float x = abs().x - (item.getIcon().getRegionWidth() / 2);
                    float y = abs().y;
                    // effect = ParticleHandler.loadParticles("ptItem", x, y, item.getPtColor(), ParticleHandler.EffectType.FIELD);
                    effectLoaded = true;
                    item.setParticle(MapState.particleHandler.addEffect(fixed(), item.getPtColor()));
                }
            }
        }
    }
    public void updateVariables() {
        float dt = Gdx.graphics.getDeltaTime();
        fixedPos.set(new Vector2(absPos.x, fixY(absPos)));
        if (isWater()) {
            dWater.update(dt);
        }
        if(item instanceof Mine){
            Mine m=((Mine) item);
            if(m.kill)
                removeItem();
            else
                m.update(dt);
        }
    }
    public void clearMonster() {
        monster = null;
        hasMon = false;
    }
    public void generateSpecial() {
    }
    public void removeMon() {
        if(hasMon) {
            hasMon=false;
            monsterList.remove(monster);
            monster=null;
        }
    }
    public void removeItem() {
        if (hasItem()) {
            out("ITEM");
            item.colliion(pos());
            destroyEffect();
            boosterItem=-1;
            setItem(null);
            item=null;
            hasLoot=false;
        }
    }
    private void destroyEffect(){
        if(effect != -1) {
            MapState.particleHandler.remove(effect);
            effectLoaded = false;
        }
    }

    public void render(SpriteBatch sb) {
        Vector2 p = fixed();
        if (hasItem() && isClear()) {
            //System.out.println(item.getName());
            if(!item.getName().equals("ITEM"))
                sb.draw(item.getIcon(), p.x, p.y);
        }
        if (isWarp())
            sb.draw(ImageLoader.warp, p.x, p.y);
    }

    public void renderSR(ShapeRendererExt sr) {
        float[] f = corners.getVertices();
        Color c=new Color();
        if (isWater()) {
            c = getColor();//draw water
        } else {
            if (isClear())
                c = HeightMap.getColor(Math.round(f[10]));//draw clear
            else
                c = new Color(.1f, .1f, .1f, 1);//draw wall
        }
        sr.setColor(c);
        for (Triangle t : tris)
            sr.triangle(t);

    }

    enum State {
        CLEAR, WATER, BLOCKED, WARP, SHOP
    }
}
