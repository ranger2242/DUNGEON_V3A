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

import static com.quadx.dungeons.GridManager.setInBounds;
import static com.quadx.dungeons.tools.gui.HUD.out;



@SuppressWarnings("DefaultFileTemplate")
public class
Map2State extends State {
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
        //initBools();
        generateMap2();
    }
    protected void handleInput() {}
    ////////////////////////////////////////
    //UPDATE FUNCTIONS
    public void update(float dt) {
        dtChange+=dt;
        if(dtChange>2){
            generateMap2();
            dtChange=0;
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
                Map2State.buffArray[i][j].setCleared();
            }
        }
        dispArray=buffArray;
    }
    private void drawGrid(){
        //int cellW=1;
        int cellW= Game.HEIGHT/res;
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++) {
                if(dispArray[i][j].isClear()){
                    shapeR.setColor(Color.GREEN);
                }
                else{
                    shapeR.setColor(Color.BLACK);
                }
                shapeR.rect((i*cellW),(j*cellW),cellW,cellW);
            }
        }
    }

    static void createRooms(ArrayList<Room> rooms){
        int runs = rn.nextInt(10) + 5;
        for (int i = 0; i < runs; i++) {
            rooms.add(new Room());
        }
        for (Room r : rooms) {
            int x1 = (r.x - r.w / 2),
                    x2 = r.x + r.w / 2,
                    y1 = (r.y - r.h / 2),
                    y2 = r.y + r.h / 2;

            for (int i = x1; i < x2; i++) {
                for (int j = y1; j < y2; j++) {
                    int x = setInBounds(i),
                            y = setInBounds(j);
                    buffArray[x][y].setCleared();
                    live.add(buffArray[x][y]);
                }
            }
        }
    }
    static void createHalls(ArrayList<Room> rooms){
        for (int i = 0; i < rooms.size(); i++) {
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
            if (a.equals(b))
                b = rooms.get(rn.nextInt(rooms.size()));
            int x1 = 0;
            int y1 = 0;
            int x2 = 0;
            if (a.dx < b.dx) {
                x1 = a.dx;
                x2 = b.dx;
            } else if (a.dx < b.dx) {
                x1 = b.dx;
                x2 = a.dx;
            }

            int y2 = 0;
            if (a.dy < b.dy) {
                y1 = a.dy;
                y2 = b.dy;
            } else if (a.dy < b.dy) {
                y1 = b.dy;
                y2 = a.dy;

            }

            for (int l = x1; l < x2; l++) {
                int x=setInBounds(l),
                        y=setInBounds(y1);
                buffArray[x][y].setCleared();
            }
            for (int z = y1; z < y2; z++) {
                int x=setInBounds(x1),
                        y=setInBounds(z);
                buffArray[x][y].setCleared();
            }
        }
    }

    public static Cell[][] generateMap2() {
        live.clear();
        initArray();
        ArrayList<Room> rooms = new ArrayList<>();

        createRooms(rooms);
        createHalls(rooms);

        fillBits();
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
            buffArray[x][y].setCleared();
            buffArray[x][y].setWater();
        }
        for (int i = 0; i < grow; i++) {
            for (int x = 0; x < res; x++) {
                for (int y = 0; y < res; y++) {
                    if (buffArray[x][y].isWater()) {
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

    private static void fillBits() {
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                try {
                    int count = 0;
                    if (buffArray[i - 1][j].isClear()) count++;
                    if (buffArray[i + 1][j].isClear()) count++;
                    if (buffArray[i][j - 1].isClear()) count++;
                    if (buffArray[i][j + 1].isClear()) count++;
                    if (count >= 1 && rn.nextFloat() < .5 || (count == 4 && rn.nextBoolean())) {
                        buffArray[i][j].setCleared();
                        buffArray[i][j].setCords(i, j);
                        live.add(buffArray[i][j]);
                    }

                    int counta = 0;
                    if (buffArray[res - 1 - i - 1][res - 1 - j].isClear()) counta++;
                    if (buffArray[res - 1 - i + 1][res - 1 - j].isClear()) counta++;
                    if (buffArray[res - 1 - i][res - 1 - j - 1].isClear()) counta++;
                    if (buffArray[res - 1 - i][res - 1 - j + 1].isClear()) counta++;
                    if (counta >= 1 && rn.nextFloat() < .5 || (counta == 4 && rn.nextBoolean())) {
                        buffArray[res - 1 - i][res - 1 - j].setCleared();
                        buffArray[res - 1 - i][res - 1 - j].setCords(res - 1 - i, res - 1 - j);
                        live.add(buffArray[res - 1 - i][res - 1 - j]);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }
    private static void plotInitPoints(ArrayList<Cell> endpointList, int count){
        //create initial growth points
        endpointListSize=count;
        for(int i=0; i<count;i++) {
            int xs = rn.nextInt(res);
            int ys = rn.nextInt(res);
            buffArray[xs][ys].setCleared();
            buffArray[xs][ys].setCords(xs, ys);
            endpointList.add(buffArray[xs][ys]);
            live.add(buffArray[xs][ys]);
        }
    }

    private static ArrayList<Cell> updateLiveList(ArrayList<Cell> liveList){
        liveList.clear();
        for(int i=0; i<res; i++){
            for(int j=0;j<res;j++){
                if(buffArray[i][j].isClear()){
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
                if (buffArray[x - 1][y].isClear()) count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {

                if(buffArray[x+1][y].isClear())count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {

                if(buffArray[x][y-1].isClear())count++;
            }catch (ArrayIndexOutOfBoundsException ignored){}
            try {

                if(buffArray[x][y+1].isClear())count++;
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

    static boolean growBranchProb(){
        return rn.nextFloat() <= .1 && rn.nextBoolean();
    }

    private static void addBranch(int x, int y) {
        for (int i = 0; i < endpointListSize; i++) {
            int factor = 30;
            int growth = rn.nextInt(factor) + 10;
            boolean u = growBranchProb(),
                    d = growBranchProb(),
                    l = growBranchProb(),
                    r = growBranchProb();

            for (int j = 0; j < growth; j++) {
                int x1 = x,
                        y1 = y;
                if (u) {
                    x1 = x;
                    y1 = y + j < res ? y + j : y;
                }
                if (d) {
                    x1 = x;
                    y1 = y - j >= 0 ? y - j : y;

                }
                if (l) {
                    x1 = x - j >= 0 ? x - j : x;
                    y1 = y;
                }
                if (r) {
                    x1 = x + j < res ? x + j : x;
                    y1 = y;
                }
                buffArray[x1][y1].setCleared();
                live.add(buffArray[x1][y1]);
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
