package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.tools.Timer;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.states.mapstate.MapState.out;

/**
 * Created by Tom on 1/18/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Map2State extends State {
    private static final ShapeRenderer shapeR= new ShapeRenderer();
    private static final Random rn= new Random();
    public static final int res =150;
    private static Cell[][] dispArray  = new Cell[res][res];
    private static final Cell[][] buffArray  = new Cell[res][res];
    private static int endpointListSize=0;
    static ArrayList<Cell> live=new ArrayList<>();
    private float dtChange=0;

    public Map2State(GameStateManager gsm) {
        super(gsm);
        initBools();
        generateMap2();
    }

    protected void handleInput() {

    }
    ////////////////////////////////////////
    //UPDATE FUNCTIONS
    public void update(float dt) {
        dtChange+=dt;
        if(dtChange>2){
            updateVars();
            generateMap2();
            dtChange=0;
        }
    }
    public static void updateVars(){
        int firstRunDepth = rn.nextInt(6) + 1;
        int secRunDepth = rn.nextInt(4) + 1;
        int triRunDepth = rn.nextInt(3) + 1;

        if(firstRunDepth == secRunDepth || secRunDepth == triRunDepth || triRunDepth == firstRunDepth)
        {
            firstRunDepth =rn.nextInt(6)+1;
            secRunDepth =rn.nextInt(4)+1;
            triRunDepth =rn.nextInt(3)+1;
        }
    }
    private void initBools(){
        boolean[][] randBools = new boolean[5][9];
        for(long i=0;i<5; i++) {
            for (int j = 0; j < 9; j++) {
                randBools[(int) i][j] = rn.nextBoolean();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //RENDER FUNCTIONS
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(1,0,0,1);

        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        //calculateCells(0,0, Game.HEIGHT,5);
        drawGrid();
        shapeR.setColor(0,0,0,1);
        shapeR.end();
    }
    ////////////////////////////////////////
    private static void initArray(){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                Map2State.buffArray[i][j]=new Cell();
            }
        }
    }
    public static void fillArray(){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                Map2State.buffArray[i][j].setState();
            }
        }
        dispArray=buffArray;
    }
    private void drawGrid(){
        //int cellW=1;

        int cellW= Game.HEIGHT/res;
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++) {
                if(dispArray[i][j].getState()){
                    shapeR.setColor(Color.GREEN);
                }
                else{
                    shapeR.setColor(Color.BLACK);
                }
                shapeR.rect((i*cellW),(j*cellW),cellW,cellW);
            }
        }
    }

    public static Cell[][] generateMap2() {
        live.clear();
        initArray();//buffers the buffArray
        ArrayList<Room> rooms = new ArrayList<>();
        int runs = rn.nextInt(10) + 5;
        for (int i = 0; i < runs; i++) {//initialize rooms
            rooms.add(new Room());
        }

        ArrayList<Cell> edges = new ArrayList<>();
        for (Room r : rooms) {//builds rooms and shit
            for (int i = (r.x - r.w / 2); i < r.x + r.w / 2; i++) {
                for (int j = (r.y - r.h / 2); j < r.y + r.h / 2; j++) {
                    try {
                        buffArray[i][j].setState();
                        if (i == (r.x - r.w / 2) || i == (r.x + r.w / 2) - 1 || j == (r.y - r.h / 2) || j == (r.y + r.h / 2) - 1)
                            edges.add(buffArray[i][j]);
                        live.add(buffArray[i][j]);
                    } catch (Exception e) {
                    }
                }
            }
        }
        for (int i = 0; i < rooms.size(); i++) {//crash starts here
            Room a = rooms.get(i);
            Room b;
            if (i == rooms.size() - 1) {
                b = rooms.get(0);
            } else {
                try {
                    b = rooms.get(i + 1);
                } catch (Exception e) {
                    out("Fuck");
                    b = a;
                }
            }
            if (a.equals(b)) b = rooms.get(rn.nextInt(rooms.size()));
            int q = 0;
            int w = 0;
            int endpointX = 0;
            if (a.dx < b.dx) {
                q = a.dx;
                endpointX = b.dx;
            } else if (a.dx < b.dx) {
                q = b.dx;
                endpointX = a.dx;
            }

            int endpointY = 0;
            if (a.dy < b.dy) {
                w = a.dy;
                endpointY = b.dy;
            } else if (a.dy < b.dy) {
                w = b.dy;
                endpointY = a.dy;

            }

            for (int l = q; l < endpointX; l++) {
                try {
                    buffArray[l][w].setState();
                } catch (Exception e) {
                }
            }
            for (int z = w; z < endpointY; z++) {
                try {
                    buffArray[q][z].setState();
                } catch (Exception e) {
                }
            }
        }
        fillBits(1, true);
        plotWater();
        dispArray = buffArray;
        return dispArray;
    }
    private static void plotWater(){
        int cycles = rn.nextInt(13);
        if(cycles<3)cycles=rn.nextInt(13);
        int grow=3;
        for (int i = 0; i < cycles; i++) {
            int x = rn.nextInt(res);
            int y = rn.nextInt(res);
            buffArray[x][y].setState();
            buffArray[x][y].setWater();
        }
        for (int i = 0; i < grow; i++) {
            for (int x = 0; x < res; x++) {
                for (int y = 0; y < res; y++) {
                    if (buffArray[x][y].getWater()) {
                        try {
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x - 1][y - 1].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x - 1][y].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x - 1][y + 1].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x][y + 1].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x + 1][y + 1].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x + 1][y].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x + 1][y - 1].setWater();
                            if (rn.nextBoolean() && rn.nextBoolean()) buffArray[x][y - 1].setWater();
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                        }

                    }
                }
            }
        }
    }
    private static void fillBits(int factor, boolean b){
        if(b) {
            for (int i = 0; i < res; i++) {
                for (int j = 0; j < res; j++) {
                    try {
                        int count = 0;
                        if (buffArray[i - 1][j].getState()) count++;
                        if (buffArray[i + 1][j].getState()) count++;
                        if (buffArray[i][j - 1].getState()) count++;
                        if (buffArray[i][j + 1].getState()) count++;
                        if (count >= factor && rn.nextFloat() < .5 || (count==4 && rn.nextBoolean()) ) {
                            buffArray[i][j].setState();
                            buffArray[i][j].setCords(i, j);
                            live.add(buffArray[i][j]);
                        }

                        int counta = 0;
                        if (buffArray[res-1-i - 1][res-1-j].getState()) counta++;
                        if (buffArray[res-1-i + 1][res-1-j].getState()) counta++;
                        if (buffArray[res-1-i][res-1-j - 1].getState()) counta++;
                        if (buffArray[res-1-i][res-1-j + 1].getState()) counta++;
                        if (counta >= factor && rn.nextFloat() < .5|| (counta==4 && rn.nextBoolean()) ) {
                            buffArray[res-1-i][res-1-j].setState();
                            buffArray[res-1-i][res-1-j].setCords(res-1-i, res-1-j);
                            live.add(buffArray[res-1-i][res-1-j]);
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }/*
        else{
            for (int i = res-1; i >= 0; i--) {
                for (int j = res-1; j >= 0; j--) {
                    try {
                        int count = 0;
                        if (buffArray[i - 1][j].getState()) count++;
                        if (buffArray[i + 1][j].getState()) count++;
                        if (buffArray[i][j - 1].getState()) count++;
                        if (buffArray[i][j + 1].getState()) count++;
                        if (count >= factor && rn.nextFloat() > .4) {
                            buffArray[i][j].setState();
                            buffArray[i][j].setCords(i, j);
                            live.add(buffArray[i][j]);
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                    }
                }
            }
        }*/
    }
    private static void plotInitPoints(ArrayList<Cell> endpointList, int count){
        //create initial growth points
        endpointListSize=count;
        for(int i=0; i<count;i++) {
            int xs = rn.nextInt(res);
            int ys = rn.nextInt(res);
            buffArray[xs][ys].setState();
            buffArray[xs][ys].setCords(xs, ys);
            endpointList.add(buffArray[xs][ys]);
            live.add(buffArray[xs][ys]);
        }
    }

    private static ArrayList<Cell> updateLiveList(ArrayList<Cell> liveList){
        liveList.clear();
        for(int i=0; i<res; i++){
            for(int j=0;j<res;j++){
                if(buffArray[i][j].getState()){
                    liveList.add(buffArray[i][j]);
                }
            }
        }
        return liveList;
    }
    private static ArrayList<Cell> updateEndpoints(){
        ArrayList<Cell> ends=new ArrayList<>();
        for(Cell c: live){
            int x=c.getX();
            int y=c.getY();
            int count =0;
            try {
                if (buffArray[x - 1][y].getState()) count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {

                if(buffArray[x+1][y].getState())count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {

                if(buffArray[x][y-1].getState())count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {

                if(buffArray[x][y+1].getState())count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            if(count==1){
                ends.add(c);
            }
        }
        endpointListSize=ends.size();
        out("Ends:"+endpointListSize);
        return ends;
    }
    private static void growPaths(ArrayList<Cell> endpointList){
        Timer as = new Timer("Grow Paths");
        as.start();
        for(Cell c:endpointList){//check boundries
            addBranch(c.getX(),c.getY());
        }

        as.end();
        Game.console(as.runtime());
    }
    private static void addBranch(int x, int y){
            for (int i = 0; i < endpointListSize; i++) {
                int factor=30;
                int a = rn.nextInt(factor)+10;
                boolean z=rn.nextFloat()<=.1 && rn.nextBoolean(),s=rn.nextFloat()<=.1 && rn.nextBoolean(),c=rn.nextFloat()<=.1 && rn.nextBoolean(),v=rn.nextFloat()<=.1 && rn.nextBoolean();

                for (int j = 0; j < a; j++) {
                    try {
                        if(z) {
                            buffArray[x][y + j].setState();
                            live.add(buffArray[x][y + j]);
                        }if(s) {
                            buffArray[x][y - j].setState();
                            live.add(buffArray[x][y - j]);
                        }if(c) {
                            buffArray[x - j][y].setState();
                            live.add(buffArray[x - j][y]);
                        }if(v){
                            buffArray[x+j][y].setState();}
                            live.add(buffArray[x+j][y]);
                    } catch (Exception e) {
                    }
                }
            }
    }
    public void dispose() {

    }
    private static class Room{
        protected int w;
        protected int h;
        protected int y;
        protected int x;
        protected int dx;
        protected int dy;
        public Room(){
            w=rn.nextInt(40);
            h=rn.nextInt(40);
            y=rn.nextInt(res);
            x=rn.nextInt(res);
            if(w-x>0)
                dx=x+rn.nextInt(w-x);
            else
                dx=x;
            if(h-y>0)
                 dx=y+rn.nextInt(h-y);
            else
                dy=y;

        }

        public int getX() {
            return x;
        }
    }
}
