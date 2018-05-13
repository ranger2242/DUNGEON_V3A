package com.quadx.dungeons;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.Timer;
import com.quadx.dungeons.tools.WallPattern;
import com.quadx.dungeons.tools.heightmap.HeightMap;
import com.quadx.dungeons.tools.heightmap.Matrix;

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
    static Vector2 rotateCords(boolean left,Vector2 pos){
        Vector2 v=new Vector2();
        if(left){
            v.x=res-pos.y-1;
            v.y=pos.x;
        }else{
            v.x=pos.y;
            v.y=res-pos.x-1;
        }
        return v;
    }

    public static void rotateMap(boolean left){
        if(MapStateUpdater.dtf>.15) {
            noLerp=true;
            Matrix<Integer> rotator = new Matrix<>(Integer.class);
            dispArray = rotator.rotateMatrix(dispArray, res, left);
            player.setPos(rotateCords(left,player.getPos()));
            warp.set ( rotateCords(left,warp));
            shop.set(rotateCords(left,shop));
            player.setAbsPos(new Vector2(player.getPos().x*cellW,player.getPos().y*cellW));
            for(Monster m:monsterList){
                m.setPos(rotateCords(left,m.getPos()));
                m.setAbsPos(new Vector2(m.getPos().x*cellW,m.getPos().y*cellW));
            }
            hm.calcCorners(dispArray);
            hm.getCells();
            loadLiveCells();
            loadDrawList();
            MapStateUpdater.dtf=0;
        }
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
    private static void splitMapDataToList() {
        liveCellList.clear();
        liveCount=0;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (dispArray[i][j].isClear()) {
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
        Monster.mdrawList.clear();
        for(Monster m : monsterList){
                Monster.mdrawList.add(m);
                m.setDrawable(true);
                monsOnScreen.add(m);
        }
/*        for(Cell[] a: dispArray){
            for(Cell c: a){
                c.updateVariables();
                c.updateParticles();
                drawList.add(c);
            }
        }*/

        int ext=8;
        for ( int i = x - ext; i < endx + ext; i++) {
            for (int j = y - ext; j < endy +ext; j++) {
                Cell c=dispArray[(i+res)%res][(j+res)%res];
                c.updateVariables();
                c.updateParticles();
                drawList.add(c);










                //old and broken

               /* Cell c;
                    if(i>=0 &&i<res && j>=0 &&j<res) {
                        c = dispArray[i][j];
                        c.updateVariables();
                        if (!c.isClear() || c.isWater()) {
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
*/
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
        while (temp > 0 && Tests.spawn) {
            int listSize = monsterList.size();
            int point = rn.nextInt(liveCellList.size());
            Cell c = liveCellList.get(point);
            if(!Monster.isNearPlayer(c.getPos())) {
                if (c.isClear()) {
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

        }
        Monster.reindexMons=true;
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
/*        for(ParticleEffect e :MapStateExt.effects){
            e.dispose();
        }
        MapStateExt.effects.clear();*/
        Map2State.updateVars();
        dispArray = Map2State.generateMap2();
        //splitMapDataToList();
        loadLiveCells();
    }

    private void plotWarps() {
        int index = rn.nextInt(liveCellList.size());
        Cell c = liveCellList.get(index);
        liveCellList.get(index).setCleared();
        int x = c.getX();
        int y = c.getY();
        warp.set(x, y);
        liveCellList.get(index).setWarp();
    }
    private void plotShop() {
        int index = rn.nextInt(liveCellList.size());
        Vector2 v=liveCellList.get(index).getPos();
        shop.set(v);
        liveCellList.get(index).setCleared();
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
    public static boolean isInBounds(Vector2 v){
        return (int)v.x>=0 &&(int)v.y>=0 && (int)v.x<res &&(int)v.y<res;
    }
    private void plotItems() {
        int seeds =10 + rn.nextInt(20);
        ArrayList<Vector2> points = new ArrayList<>();
        for (int i = 0; i < seeds; i++) {
            points.add(new Vector2(rn.nextInt(res), rn.nextInt(res)));
        }
        for (int i = 0; i < seeds; i++) {
            Item item = Item.generate();
            Class c =item.getClass();
            boolean useItem = c.equals(Potion.class)
                    || c.equals(ManaPlus.class)
                    || c.equals(EnergyPlus.class);
            if (!(item.isEquip || (c.equals(SpellBook.class) || c.equals(Gold.class)))) {
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
        while (!(!c.isWater() && c.isClear())) {
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
                    if (!dispArray[nx][ny].isClear()) {
                        dispArray[nx][ny].setCleared();
                    }
                }
            }
        }
    }
    public void initializeGrid(){
        mapLoadTime=new Timer("MapLoadTime");
        mapLoadTime.start();
        player.fullHeal();
        clearMonsterList();
        createMap();
        hm=new HeightMap(dispArray);
        dispArray=hm.getCells();
        plotLoot();
        plotItems();
        plotShop();
        plotWarps();
        plotPlayer();
        plotMonsters();
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
            temp.stream().filter(c -> !c.isClear()).forEach(Cell::setCleared);
            temp.clear();

         /*   if (isPlayer) {//checks if players dig ability is active
                if (player.facing.equals(Direction.Facing.East) || player.facing.equals(Direction.Facing.West))
                    //if (MapState.lastPressed == 'd' ||MapState.lastPressed == 'a')
                    clearDigPlusCells(9, 3,Math.round( x), Math.round(y));
                if (player.facing.equals(Direction.Facing.North) || player.facing.equals(Direction.Facing.South)
                        || player.facing.equals(Direction.Facing.Northeast) || player.facing.equals(Direction.Facing.Southeast)
                        || player.facing.equals(Direction.Facing.Northwest) || player.facing.equals(Direction.Facing.Southwest)) {

                }
                clearDigPlusCells(3, 9,Math.round( x), Math.round(y));
            }*/
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

                            if (c.isWater()) {
                                WallPattern.p[ii][jj] = !temp.get(count).isWater();
                            } else {
                                WallPattern.p[ii][jj] = temp.get(count).isClear();
                            }
                            count++;
                        }
                    }
                }
            }
            int a = 0;
            if (c.isWater()) a = 1;
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
