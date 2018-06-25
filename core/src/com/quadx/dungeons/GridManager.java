package com.quadx.dungeons;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.Mine;
import com.quadx.dungeons.items.resources.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.EMath;
import com.quadx.dungeons.shapes1_5.Ngon;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.WallPattern;
import com.quadx.dungeons.tools.heightmap.HeightMap;
import com.quadx.dungeons.tools.heightmap.Matrix;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Timer;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.ImageLoader.a;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.timers.Time.ft;

public class GridManager {
    public static final Random rn = new Random();
    public static final ArrayList<Cell> liveCellList = new ArrayList<>();
    public static final ArrayList<Cell> drawList = new ArrayList<>();
    public static final ArrayList<Monster> monsterList = new ArrayList<>();
    private static ArrayList<Monster> monsOnScreen = new ArrayList<>();
    private static HeightMap hm;

    private static int liveCount=0;
    public static Cell[][] dispArray;
    public static final int res =150;
    private static Timer mapLoadTime;

    static Delta dRotate = new Delta(10*ft);

    public GridManager(){
        mapLoadTime=new Timer("MapLoadTime");
        initializeGrid();
    }

    public void initializeGrid(){
        mapLoadTime.start();

        dispArray = Map2State.generateMap2();
        loadLiveCells();
        hm=new HeightMap(dispArray);
        dispArray=hm.getCells();
        plotLoot();
        plotMines();
        plotGrass();
        plotShrooms();
        plotShop();
        plotWarps();
        plotPlayer();
        plotMonsters();
        if(Tests.clearmap)
            Tests.loadEmptyMap();
        //nothing below here
        mapLoadTime.end();
        Tests.mapLoadTimes.add(mapLoadTime.getElapsedD());
        String s=GridManager.mapLoadTime.runtime();
        System.out.println(s);
        out(s);
    }

    private void plotGrass() {
        for(int x=0;x<res;x++){
            for(int y=0;y<res;y++){
                try {
                    int c=0;
                    ArrayList<Cell> cells= getSurroundingCells(x,y,2);
                    for(int i=0;i< cells.size();i++){
                        if(cells.get(i).isWater()){
                            c++;
                        }
                    }
                    if(c>0 && !dispArray[x][y].isWater())
                        dispArray[x][y].setItem(new Grass());
                    if(c==16 && !dispArray[x][y].isWater() )
                        dispArray[x][y].setItem(new Leaf());
                    if(c==18 && !dispArray[x][y].isWater())
                        dispArray[x][y].setItem(new Flower());
                    if(rn.nextFloat()< 1f/3000f && !dispArray[x][y].isWater() )
                        dispArray[x][y].setItem(new Bone());
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }


    public static void update(float dt){
        dRotate.update(dt);
        loadDrawList();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private static Vector2 rotateCords(boolean left, Vector2 pos){
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

    public static void rotateMap(boolean left) { // refactor this
        if (dRotate.isDone()) {
            State.camController.setSnapCam(true);
            Matrix<Integer> rotator = new Matrix<>(Integer.class);
            dispArray = rotator.rotateMatrix(dispArray, res, left);
            //player.body.setPos(rotateCords(left, player.pos()));
            warp.set(rotateCords(left, warp));
            shop.set(rotateCords(left, shop));
            player.body.setAbs(new Vector2(player.pos().x * cellW, player.pos().y * cellW));
            for (Monster m : monsterList) {
               /* m.body.setPos(rotateCords(left, m.pos()));
                m.body.setPos(new Vector2(m.pos().x * cellW, m.pos().y * cellW));*/
            }
            hm.calcCorners(dispArray);
            hm.getCells();
            loadLiveCells();
            loadDrawList();
            dRotate.reset();
        }
    }
    public static ArrayList<Cell> getSurroundingCells(Vector2 pos, int i) {
        return getSurroundingCells((int)pos.x,(int)pos.y,i);
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
        Vector2 view = State.getView();
        int x = (int) (view.x / cell.x);
        int y = (int) (view.y/ cell.y);
        int endx = (int) ((view.x + Game.WIDTH) / cell.x);
        int endy = (int) ((view.y + Game.HEIGHT) / cell.y);
        int scale=200;
        Rectangle screen= new Rectangle(view.x-scale,view.y-scale,(view.x + Game.WIDTH)+scale,(view.y + Game.HEIGHT)+scale);
        Monster.mdrawList.clear();
        for(Monster m : monsterList){
                Monster.mdrawList.add(m);
                m.setDrawable(true);
        }

        int ext=8;
        for ( int i = x - ext; i < endx + ext; i++) {
            for (int j = y - ext; j < endy +ext; j++) {
                int p= Math.abs((i+res))%res;
                int q= Math.abs((j+res))%res;

                Cell c=dispArray[p][q];
                c.updateVariables();
                c.updateParticles();
                drawList.add(c);
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

    public static boolean isNearPlayer(Vector2 pos, int gridUnits) {
        return EMath.pathag(player.pos(), pos) <= gridUnits;
    }

    private void clearMonsterList() {
        monsterList.clear();
    }

    Vector2[] seeds(int base, int var){
        int seeds= base+rn.nextInt(var);
        Vector2[] points = new Vector2[seeds];
        for(int i=0;i<seeds;i++){
            points[i]=new Vector2(resInt(),resInt());
        }
        return points;
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
        Cell c=liveCellList.get(index);
        Vector2 v=c.pos();
        shop.set(v);
        c.setCleared();
        c.setShop(true);
    }
    private void plotLoot() {
        Vector2[] points =seeds(35,20);
        for(int i=0;i<10;i++) {
            int cluster= rn.nextInt(10)+10;
            for(int j=0;j<cluster;j++) {
                points[i].add(spreadv(4));
                dispArray(points[i]).setItem(new Gold());
            }
        }
    }

    private void plotShrooms() {
        Vector2[] points =seeds(2,4);
        for(int i=0;i<points.length;i++) {
            int cluster= rn.nextInt(5)+3;
            for(int j=0;j<cluster;j++) {
                points[i].add(spreadv(2));
                dispArray(points[i]).setItem(new Mushroom());
            }
        }
    }

    private void plotMines() {
        Vector2[] points = seeds(20, 20);
        for (int i = 0; i < points.length; i++) {
            Cell c = dispArray(points[i]);
            Mine m= new Mine(c.abs());
            m.genItems();
            c.setItem(m);
        }
      /*  int crates = (int) (liveCellList.size() * .0025f);
        for (int a = 0; a < crates; a++) {
            dispArray[resInt()][resInt()].setItem(Item.generateSpecial());
        }*/

    }

    private static void plotMonsters() {
        monsterList.clear();
        int mtotal = rn.nextInt(20) + 10;
        for (int i = 0; i < mtotal && Tests.spawn; i++) {
            Cell cell = dispArray[resInt()][resInt()];
            Vector2 v = new Vector2(cell.abs());


            int r = 8 * cellW;
            int n = rn.nextInt(6) + 2;
            Ngon pts = new Ngon(v, r, n, 0);
            float[] p = pts.getVerticies();
            for (int j = 0; j < p.length; j += 2) {
                if(Monster.canSpawn()) {
                    Vector2 a = new Vector2(p[j], p[j + 1]);
                    Monster m = Monster.getNew(a);
                    dispArray(a).setMonster(m);
                }
            }
        }
    }
    private void plotPlayer() {
        Cell c = getAnyCell();
        for(Cell cell:  getSurroundingCells(c.pos(),5)){
            cell.setWater(false);
        }
        player.body.setAbs(new Vector2(c.abs()));
        for(Cell cell: getSurroundingCells(player.pos(),15)){
            cell.removeMon();
        }
    }

    public static Vector2 fixYv(Vector2 v){//get vector in absolute pos
        return new Vector2(v.x, fixY(v));
    }

    public static float fixY(Vector2 v) {//get vector in absolute pos
        int gx = Math.round(v.x / cellW);//find grid pos
        int gy = Math.round(v.y / cellW);
        float pery = (v.y - (gy * cellW)) / cellW;
        try {
            float y = (dispArray[gx][gy].getHeight() * cell.y) + (gy * cell.y) + (pery * cell.y);
            return y;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return v.y;
        }
    }

    float spreadf(double scl){//normal distributed number with mean 0
        return (float) (scl*rn.nextGaussian());
    }
    public Vector2 spreadv(double scl){
        return new Vector2(spreadf(scl),spreadf(scl));
    }

    public static float boundW(float ind){
        return boundW((int)ind,res);
    }
    public static int boundW(int ind, int bound) {
        if (ind < 0) return bound - 1;
        else if (ind > bound - 1) return 0;
        else return ind;
    }
    public static float boundW(float ind, float bound, float sep) {
        if (ind < 0) return bound - sep;
        else if (ind > bound - sep) return sep;
        else return ind;
    }
   /* public static float boundW(float ind, float min, float max) {
        if (ind < min) return max - 1;
        else if (ind > max) return min;
        else return ind;

    }*/
    public static int bound(int ind, int bound) {
        return bound(ind,0,bound);
    }
    public static int bound(int ind, int min, int max) {
        if (ind < min)
            return min;
        else if (ind >= max)
            return max-1;
        else
            return  ind;
    }
    public static float bound(float ind, float min, float max) {
        if (ind < min)
            return min;
        else if (ind >= max)
            return max-Float.MIN_VALUE;
        else
            return  ind;

    }
    public static float bound(float ind){
        return bound(ind,(float)res);
    }
    public static float bound(float ind, float bound){
        return bound(ind,0f,bound);
    }
    public static Vector2 boundI(Vector2 p) {
        Vector2 t= new Vector2(p);
        t.x= bound((int) t.x);
        t.y= bound((int)t.y);
        return t;
    }
    public static Vector2 bound(Vector2 p) {
        return new Vector2(bound(p.x),bound(p.y));

    }
   /* public static Vector2 boundW(Vector2 p, float min, float max){


    }*/
    public static int bound(int ind) {
        return bound(ind, res);
    }
    public static boolean isInBounds(Vector2 v){
        return (int)v.x>=0 &&(int)v.y>=0 && (int)v.x<res &&(int)v.y<res;
    }

    public static int resInt(){
        return rn.nextInt(res);
    }
    private void clearDigPlusCells(int ii, int jj, int x, int y) {
        for (int i = 0; i < ii; i++) {
            for (int j = 0; j < jj; j++) {
                int nx = 0;
                int ny = 0;
                switch (player.body.getFacing()) {
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
    public void clearArea(Vector2 pos, boolean b) {
        clearArea(pos.x,pos.y,b);
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
    public Cell getAnyCell() {
        return dispArray[resInt()][resInt()];
    }
    public static Cell dispArray(Vector2 p){
        p= boundI(p);
        return dispArray[(int) p.x][(int) p.y];
    }

}
