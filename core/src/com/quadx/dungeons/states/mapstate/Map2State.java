package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.State;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tom on 1/18/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Map2State extends State {
    private static ShapeRenderer shapeR= new ShapeRenderer();
    private static Random rn= new Random();
    public static int res =200;
    private static Cell[][] dispArray  = new Cell[res][res];
    private static Cell[][] buffArray  = new Cell[res][res];
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

        while(firstRunDepth == secRunDepth || secRunDepth == triRunDepth || triRunDepth == firstRunDepth)
        {
            firstRunDepth =rn.nextInt(6)+1;
            secRunDepth =rn.nextInt(4)+1;
            triRunDepth =rn.nextInt(3)+1;
        }

        int seedPoints = rn.nextInt(15) + 2;
        int secondaryPoints = rn.nextInt(15) + 2;
        int triPoints = rn.nextInt(15) + 2;
        //fillPercent=(float) rn.nextGaussian();
        //stdDvX=res/(rn.nextInt(20)+1);
        //stdDvY=res/(rn.nextInt(20)+1);
    }
    private void initBools(){
        boolean mainroom = rn.nextBoolean();
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
    private static void initArray(Cell[][] arr){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                arr[i][j]=new Cell();
            }
        }
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
//New maze algorithm
    public static Cell[][] generateMapTEST(){
        initArray(buffArray);
        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                buffArray[i][j].setState();
            }
        }
        dispArray=buffArray;
        return buffArray;
    }
    public static Cell[][] generateMap2(){

        ArrayList<Cell> endpointList = new ArrayList<>();
        ArrayList<Cell> liveList=new ArrayList<>();
        int runs = rn.nextInt(80)+30;
        int initPoints =rn.nextInt(8)+1;

        initArray(buffArray);//buffers the buffArray
        plotInitPoints(endpointList,initPoints);
        growPaths(endpointList);
        //make sure there grid is more than 1 size
        liveList=updateLiveList(liveList);
        while(liveList.size()<4){
            growPaths(endpointList);
            liveList=updateLiveList(liveList);
        }
        for(int i=0;i<runs;i++){
            liveList= updateLiveList(liveList);
            endpointList=updateEndpoints(liveList,endpointList);
            growPaths(endpointList);
        }
        //search for single off bits
        for(int i=0;i<rn.nextInt(3)+1;i++){
            if(rn.nextBoolean())
                liveList= fillBits(rn.nextInt(4)+1,liveList);
        }
        plotWater(liveList);
        dispArray=buffArray;
        return dispArray;
    }
    private static void plotWater(ArrayList<Cell> liveList){
        int cycles = rn.nextInt(20);
        int grow=rn.nextInt(2)+1;
        for(int i=0;i<cycles;i++) {
            int x=rn.nextInt(liveList.size());
            int x1=liveList.get(x).getX();
            int y1=liveList.get(x).getY();
            liveList.get(x).setState();
            liveList.get(x).setWater();
            buffArray[x1][y1].setState();
            buffArray[x1][y1].setWater();
        }
        for(int i=0;i<grow;i++) {
            for (Cell c : liveList) {
                if (c.getWater()) {
                    int x = c.getX();
                    int y = c.getY();
                    try {
                        if (rn.nextBoolean()) buffArray[x - 1][y - 1].setWater();
                        if (rn.nextBoolean()) buffArray[x - 1][y].setWater();
                        if (rn.nextBoolean()) buffArray[x - 1][y + 1].setWater();
                        if (rn.nextBoolean()) buffArray[x][y + 1].setWater();
                        if (rn.nextBoolean()) buffArray[x + 1][y + 1].setWater();
                        if (rn.nextBoolean()) buffArray[x + 1][y].setWater();
                        if (rn.nextBoolean()) buffArray[x + 1][y - 1].setWater();
                        if (rn.nextBoolean()) buffArray[x][y - 1].setWater();
                    }
                    catch (ArrayIndexOutOfBoundsException e){}

                }
            }
        }
    }
    private static ArrayList<Cell> fillBits(int factor, ArrayList<Cell> liveList){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                try{
                    int count=0;
                    if(buffArray[i-1][j].getState())count++;
                    if(buffArray[i+1][j].getState())count++;
                    if(buffArray[i][j-1].getState())count++;
                    if(buffArray[i][j+1].getState())count++;
                    if(count>=factor && rn.nextFloat()>.4){
                        buffArray[i][j].setState();
                        buffArray[i][j].setCords(i,j);
                        liveList.add(buffArray[i][j]);
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){}
            }
        }
        return liveList;
    }
    private static void plotInitPoints(ArrayList<Cell> endpointList, int count){
        //create initial growth points
        for(int i=0; i<count;i++) {
            int xs = rn.nextInt(res);
            int ys = rn.nextInt(res);
            buffArray[xs][ys].setState();
            buffArray[xs][ys].setCords(xs, ys);
            endpointList.add(buffArray[xs][ys]);
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
    private static ArrayList<Cell> updateEndpoints(ArrayList<Cell> liveList, ArrayList<Cell> endpointList){
        endpointList.clear();
        for(Cell c: liveList){
            int x=c.getX();
            int y=c.getY();
            int count =0;
            try {
                if (buffArray[x - 1][y].getState()) count++;
            }catch (ArrayIndexOutOfBoundsException e){}
            try {

                if(buffArray[x+1][y].getState())count++;
            }catch (ArrayIndexOutOfBoundsException e){}
            try {

                if(buffArray[x][y-1].getState())count++;
            }catch (ArrayIndexOutOfBoundsException e){}
            try {

                if(buffArray[x][y+1].getState())count++;
            }catch (ArrayIndexOutOfBoundsException e){}
            if(count==1){
                endpointList.add(c);
            }
        }
        return endpointList;
    }
    private static void growPaths(ArrayList<Cell> endpointList){
        for(Cell c:endpointList){//check boundries
            try {//left bound
                //System.out.println("-----");
                int a1=c.getX() - 1;
                int b1=c.getX() - 2;
                int a2=c.getY();
                boolean a=!buffArray[a1][a2].getState() && !buffArray[b1][a2].getState();
                addBranch(a,a1,a2,b1,a2);
            }
            catch (ArrayIndexOutOfBoundsException e){
                //System.out.println("Err:0001");
            }
            try {//right bound
                //System.out.println("-----");
                int a1=c.getX() + 1;
                int b1=c.getX() + 2;
                int a2=c.getY();
                boolean a=!buffArray[a1][a2].getState() && !buffArray[b1][a2].getState();
                addBranch(a,a1,a2,b1,a2);
            }
            catch (ArrayIndexOutOfBoundsException e){
              //  System.out.println("Err:0001");
            }
            try {//top bound
 //               System.out.println("-----");
                int a1=c.getY() + 1;
                int b1=c.getY() + 2;
                int a2=c.getX();
                boolean a=!buffArray[a1][a2].getState() && !buffArray[b1][a2].getState();
                addBranch(a,a2,a1,a2,b1);
            }
            catch (ArrayIndexOutOfBoundsException e){
            //    System.out.println("Err:0001");
            }
            try {//low bound
   //             System.out.println("-----");
                int a1=c.getY() - 1;
                int b1=c.getY() - 2;
                int a2=c.getX();
                boolean a=!buffArray[a1][a2].getState() && !buffArray[b1][a2].getState();
                addBranch(a,a2,a1,a2,b1);
            }
            catch (ArrayIndexOutOfBoundsException e){
                //System.out.println("Err:0001");
            }
        }
    }
    private static void addBranch(boolean a, int a1, int a2, int b1, int b2){
        if (a && rn.nextBoolean() && rn.nextFloat() <.6f) {
            buffArray[a1][a2].setState();
            buffArray[a1][a2].setCords(a1,a2);
            buffArray[b1][b2].setState();
            buffArray[b1][b2].setCords(b1,b2);
        }
    }
    public void dispose() {

    }
}
