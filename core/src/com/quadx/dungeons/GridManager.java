package com.quadx.dungeons;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.*;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.Timer;
import com.quadx.dungeons.tools.WallPattern;
import com.quadx.dungeons.tools.heightmap.HeightMap;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.ImageLoader.a;
import static com.quadx.dungeons.tools.gui.HUD.out;

public class GridManager {
    public static final Random rn = new Random();
    public static final ArrayList<Cell> liveCellList = new ArrayList<>();
    public static final ArrayList<Cell> drawList = new ArrayList<>();
    public static final ArrayList<Monster> monsterList = new ArrayList<>();
    public static ArrayList<Monster> monsOnScreen = new ArrayList<>();
    public static HeightMap hm;

    private static int liveCount=0;
    public static Cell[][] dispArray;
    public static final int res = Map2State.res;
    public static Timer mapLoadTime;

    public GridManager(){
        initializeGrid();
    }
    public static ArrayList<Cell> getSurroundingCells(int x, int y, int r){
        ArrayList<Cell> list=new ArrayList<>();
        if(isInBounds(new Vector2(x,y))) {
            list.add(dispArray[x][y]);
            for (int i = x - r; i < x + r + 1; i++) {
                for (int j = y - r; j < y + r + 1; j++) {
                    if (i >= 0 && i < res && j >= 0 && j < res) {
                        list.add(dispArray[i][j]);
                    }
                }
            }
        }
        return list;
    }
    private static ArrayList<Vector2> getSurroundingCellsPos(float x, float y){
        ArrayList<Vector2> list=new ArrayList<>();
        boolean a =x+1<res;
        boolean b=x-1>=0;
        boolean c=y+1<res;
        boolean d=y-1>=0;
      //  list.add(new Vector2(x,y));
        if(b && c)  list.add(new Vector2(x - 1,y + 1));
        if(c)       list.add(new Vector2(x,y+1));//dispArray[x][y + 1]);
        if(a && c)  list.add(new Vector2(x+1,y+1));//dispArray[x + 1][y + 1]);
        if(a &&b && d)list.add(new Vector2(x+1,y));//dispArray[x + 1][y]);
        if(a && d)  list.add(new Vector2(x+1,y-1));//dispArray[x + 1][y - 1]);
        if(d)       list.add(new Vector2(x,y-1));//dispArray[x][y - 1]);
        if(b && d)  list.add(new Vector2(x-1,y-1));//dispArray[x - 1][y - 1]);
        if(b)       list.add(new Vector2(x-1,y));//dispArray[x - 1][y]);
        return list;
    }
    private static void splitMapDataToList() {
        liveCellList.clear();
        liveCount=0;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (dispArray[i][j].getState()) {
                    liveCount++;
                }
                liveCellList.add(dispArray[i][j]);
            }
        }
    }
    public static void loadDrawList() {
        drawList.clear();
        monsOnScreen.clear();
        int x = (int) (viewX / cell.x);
        int y = (int) (viewY/ cell.y);
        int endx = (int) ((viewX + Game.WIDTH) / cell.x);
        int endy = (int) ((viewY + Game.HEIGHT) / cell.y);
        int scale=200;
        Rectangle screen= new Rectangle(viewX-scale,viewY-scale,(viewX + Game.WIDTH)+scale,(viewY + Game.HEIGHT)+scale);
        for(Monster m : monsterList){
            if(m.getHitBox().overlaps(screen)){
                m.setDrawable(true);
            }else{
                m.setDrawable(false);
            }
        }
        for (int i = x - 3; i < endx + 3; i++) {
            for (int j = y - 3; j < endy + 3; j++) {
                Cell c;
                    if(i>=0 &&i<res && j>=0 &&j<res) {
                        c = dispArray[i][j];
                        c.updateVariables();
                        if (!c.getState() || c.getWater()) {
                            c = loadTiles(c);

                        }
                        drawList.add(c);
                        //load particle effects
                        c.updateParticles();
                        //check for monster
                        if (c.getMonster() != null) {
                            monsOnScreen.add(c.getMonster());
                        }
                    }

            }
        }
    }
    public static void loadLiveCells(){
        liveCellList.clear();
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                dispArray[i][j].setCords(i,j);
                liveCellList.add(dispArray[i][j]);
            }
        }
    }
    public static void plotMonsters() {
        if (player.getFloor() == 1)
            splitMapDataToList();
        int temp = rn.nextInt(40)+20;//calculate number of monsters
        while (temp > 0) {
            int listSize = monsterList.size();
            int point = rn.nextInt(liveCellList.size());
            Cell c = liveCellList.get(point);
            if(c.getState()) {
                Monster m = Monster.getNew();
                m.setAbsPos(c.getAbsPos());
                m.setPos(c.getPos());
                m.setLiveCellIndex(point);
                monsterList.add(m);
                c.setMonsterIndex(listSize + 1);
                liveCellList.set(point, c);
                temp--;
            }

        }
        Monster.reindexMons=true;
    }
    public static boolean isInBounds(Vector2 pos){
        return pos.x >= 0 && pos.y >= 0 && pos.x < res && pos.y < res;
    }
    public static float fixHeight(Vector2 v){//get vector in absolute pos
        int gx=Math.round(v.x/cellW);//find grid pos
        int gy=Math.round(v.y/cellW);
        float pery=(v.y-(gy*cellW))/cellW;
        try {
            float y= (dispArray[gx][gy].getHeight()*cell.y)+(gy*cell.y)+(pery*cell.y);
            return y;
        }catch (NullPointerException| ArrayIndexOutOfBoundsException e) {
            return v.y;
        }
    }
    private void clearMonsterList() {
        monsterList.clear();
    }
    private void createMap() {
        for(ParticleEffect e :MapStateExt.effects){
            e.dispose();
        }
        MapStateExt.effects.clear();
        Map2State.updateVars();
        dispArray = Map2State.generateMap2();
        //splitMapDataToList();
        loadLiveCells();
    }
    private void plotWarps() {
        boolean placed = false;
        int index = rn.nextInt(liveCellList.size());
        Cell c = liveCellList.get(index);
        while (!placed) {
            while (c.getWater()) {

                index = rn.nextInt(liveCellList.size());
                c = liveCellList.get(index);

            }
            liveCellList.get(index).setState();
            int x = c.getX();
            int y = c.getY();
            int count = 0;
            for(Cell c1: getSurroundingCells(x,y,3)){
                if(c1.hasWater) count++;
            }
            if (count < 7) {
                placed = true;
                warp.set(x,y);
            }else index = rn.nextInt(liveCellList.size());
        }
        liveCellList.get(index).setWarp();
    }
    private void plotShop() {
        int index = rn.nextInt(liveCellList.size());
        Vector2 v=liveCellList.get(index).getPos();
        shop.set(v);
        liveCellList.get(index).setState();
        liveCellList.get(index).setShop(true);
    }
    private void plotLoot() {
        int seeds= 20+rn.nextInt(10);
        ArrayList<Vector2> points= new ArrayList<>();
        for(int i=0;i<seeds;i++){
            points.add(new Vector2(rn.nextInt(res),rn.nextInt(res)));
        }
        for(int i=0;i<seeds;i++) {
            int seeds2= rn.nextInt(13);
            for(int j=0;j<seeds2;j++) {
                int x = (int) (points.get(i).x + (4*rn.nextGaussian()));
                int y = (int) (points.get(i).y + (4*rn.nextGaussian()));
                x=setInBounds(x);
                y=setInBounds(y);
                dispArray[x][y].setHasLoot(true);
                dispArray[x][y].setItem(new Gold());
            }
        }
    }
    public static int setInBounds(int x){
        if(x<0)return 0;
        else if(x>res-1) return  res-1;
        else return x;
    }
    private void plotItems() {
        int seeds =10 + rn.nextInt(20);
        ArrayList<Vector2> points = new ArrayList<>();
        for (int i = 0; i < seeds; i++) {
            points.add(new Vector2(rn.nextInt(res), rn.nextInt(res)));
        }
        for (int i = 0; i < seeds; i++) {
            Item item = Item.generate();
            if (!item.isEquip && !(item.getClass().equals(SpellBook.class) || item.getClass().equals(Gold.class)
                    || item.getClass().equals(Potion.class) || item.getClass().equals(ManaPlus.class)
                    || item.getClass().equals(EnergyPlus.class))) {
                int seeds2 = rn.nextInt(8);
                for (int j = 0; j < seeds2; j++) {
                    int x = (int) (points.get(i).x + (4 * rn.nextGaussian()));
                    int y = (int) (points.get(i).y + (4 * rn.nextGaussian()));
                    x = setInBounds(x);
                    y = setInBounds(y);
                    dispArray[x][y].setItem(item);
                }
            }

        }

        float fillPercent = .005f;
        int crates = (int) (liveCellList.size() * fillPercent);
        for (int a = 0; a < crates; a++) {
            int x=rn.nextInt(res);
            int y=rn.nextInt(res);

            if(rn.nextInt(8)<7){
                dispArray[x][y].setBoosterItem(rn.nextInt(3));
            }
            else{
                Item item = Equipment.generate();
                dispArray[x][y].setItem(item);
            }
        }

    }
    private void plotPlayer() {
        int index = rn.nextInt(liveCellList.size());
        Cell c = liveCellList.get(index);
        while (!(!c.getWater() && c.getState())) {
            index = rn.nextInt(liveCellList.size());
            c = liveCellList.get(index);
        }
        player.setPos(c.getPos());
        player.setAbsPos(new Vector2(c.getAbsPos()));
        int range = 100;
        Rectangle rect = new Rectangle(player.getAbsPos().x - (cell.x * (range / 2)), player.getAbsPos().y - (cell.y * (range / 2)), range * cell.x, range * cell.y);
        for (int p = monsterList.size() - 1; p >= 0; p--) {
            Monster m = monsterList.get(p);
            if (m.getHitBox().overlaps(rect)) {
                monsterList.remove(p);
            }
        }
    }
    private void clearDigPlusCells(int ii, int jj, int x, int y) {
        for (int i = 0; i < ii; i++) {
            for (int j = 0; j < jj; j++) {
                int nx = 0;
                int ny = 0;
                switch (player.facing) {
                    case North:
                    case Northwest:
                    case Northeast: {
                        nx = x - 1 + i;
                        ny = y + j;
                        break;
                    }
                    case West: {
                        nx = x - i;
                        ny = y - 1 + j;
                        break;
                    }
                    case Southwest:
                    case South:
                    case Southeast: {
                        nx = x - 1 + i;
                        ny = y - j;
                        break;
                    }
                    case East: {
                        nx = x + i;
                        ny = y - 1 + j;
                        break;
                    }
                }
                /*
                switch (MapState.lastPressed){
                    case 'w':{nx=x - 1 + i; ny=y + j;       break;}
                    case 'd':{nx=x + i;     ny=y - 1 + j;   break;}
                    case 's':{nx=x - 1 + i; ny=y - j;       break;}
                    case 'a':{nx=x - i;     ny=y - 1 + j;   break;}
                }*/
                if (nx >= 0 && nx < res && ny >= 0 && ny < res) {
                    if (!dispArray[nx][ny].getState()) {
                        dispArray[nx][ny].setState();
                    }
                }
            }
        }
    }
    public void initializeGrid(){
        mapLoadTime=new Timer("MapLoadTime");
        mapLoadTime.start();
        MapStateUpdater.spawnCount=1;
        MapStateUpdater.dtRespawn=0;
        player.setMana(player.getManaMax());
        player.setEnergy(player.getEnergyMax());
        Timer t=new Timer();
        t.start();
        clearMonsterList();
        createMap();
        hm=new HeightMap(dispArray);
        HeightMap.initColorList(HeightMap.n);
        dispArray=hm.getCells();
        plotLoot();
        plotItems();
        plotShop();
        plotWarps();
        if(!Tests.nospawn)
            plotMonsters();
        t.end();
        out("4:"+t.runtime());
        t.start();
        plotPlayer();
        t.end();
        MapStateRender.showCircle = true;

        if(Tests.clearmap)
            Tests.loadEmptyMap();

        //nothing below here
        mapLoadTime.end();
        Tests.mapLoadTimes.add(mapLoadTime.getElapsedD());
        out(GridManager.mapLoadTime.runtime());

    }
    public void clearArea(float x, float y, boolean isPlayer) {
        try {
            int r;
            if(isPlayer)r=3;
            else r=1;
            ArrayList<Cell> temp = getSurroundingCells(Math.round(x),Math.round( y), r);
            temp.stream().filter(c -> !c.getState()).forEach(Cell::setState);
            temp.clear();

            if (isPlayer) {//checks if players dig ability is active
                if (player.facing.equals(Direction.Facing.East) || player.facing.equals(Direction.Facing.West))
                    //if (MapState.lastPressed == 'd' ||MapState.lastPressed == 'a')
                    clearDigPlusCells(9, 3,Math.round( x), Math.round(y));
                if (player.facing.equals(Direction.Facing.North) || player.facing.equals(Direction.Facing.South)
                        || player.facing.equals(Direction.Facing.Northeast) || player.facing.equals(Direction.Facing.Southeast)
                        || player.facing.equals(Direction.Facing.Northwest) || player.facing.equals(Direction.Facing.Southwest)) {

                }
                clearDigPlusCells(3, 9,Math.round( x), Math.round(y));
            }
            splitMapDataToList();
        }catch (ArrayIndexOutOfBoundsException e){
            out("ArrayIndexOutOfBoundsException");
            out("clearArea()");

        }
    }
    static Cell loadTiles(Cell c){
        int x1 = c.getX();
        int y1 = c.getY();
        ArrayList<Cell> temp = getSurroundingCells(x1, y1,1);
        int count = 0;
        Texture t = c.getTile();
        if (temp.size() == 8) {
            for (int ii = 0; ii < 3; ii++) {
                for (int jj = 0; jj < 3; jj++) {
                    if (count < temp.size()) {
                        if (ii == 1 && jj == 1) {
                            WallPattern.p[ii][jj] = false;
                        } else {

                            if (c.getWater()) {
                                WallPattern.p[ii][jj] = !temp.get(count).getWater();
                            } else {
                                WallPattern.p[ii][jj] = temp.get(count).getState();
                            }
                            count++;
                        }
                    }
                }
            }
            int a = 0;
            if (c.getWater()) a = 1;
            Texture t1 = WallPattern.getTile(a);
            if (t1 != null && t != t1) {
                c.setTile(t1);
            }
        } else {
            if (!c.getTile().equals(a[0]))
                c.setTile(a[0]);
        }
        return c;
    }

}
