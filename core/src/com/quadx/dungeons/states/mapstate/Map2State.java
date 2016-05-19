package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
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
public class Map2State extends State {
    public static ShapeRenderer shapeR= new ShapeRenderer();
    public static Random rn= new Random();


    ////////////////////////////////////////////////////////////////////////////////////////
    //Map Variables
    ////////////////////////////////////////////////////////////////////////////////////////
    public static int res =200;
    static int seedPoints=20;
    static int secondaryPoints=100;
    static int triPoints=50;
    static int firstRunDepth=5;
    static int secRunDepth=3;
    static int triRunDepth=6;
    static int stdDvX=res/2;
    static int stdDvY=res/2;
    static float fillPercent = .5f;
    ////////////////////////////////////////////////////////////////////////////////////////
    public static Cell[][] dispArray  = new Cell[res][res];
    public static Cell[][] buffArray  = new Cell[res][res];
    boolean[][] screenArr = new boolean[res][res];
    boolean[][] randBools;
    boolean a= true;
    boolean mainroom=false;
    long exeCount = 0;
    float dtChange=0;





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
        firstRunDepth=rn.nextInt(6)+1;
        secRunDepth=rn.nextInt(4)+1;
        triRunDepth=rn.nextInt(3)+1;

        while(firstRunDepth==secRunDepth||secRunDepth==triRunDepth || triRunDepth==firstRunDepth)
        {
            firstRunDepth=rn.nextInt(6)+1;
            secRunDepth=rn.nextInt(4)+1;
            triRunDepth=rn.nextInt(3)+1;
        }

        seedPoints=rn.nextInt(15)+2;
        secondaryPoints=rn.nextInt(15)+2;
        triPoints=rn.nextInt(15)+2;
        //fillPercent=(float) rn.nextGaussian();
        //stdDvX=res/(rn.nextInt(20)+1);
        //stdDvY=res/(rn.nextInt(20)+1);
    }
    void initBools(){
        mainroom=rn.nextBoolean();
        randBools = new boolean[5][9];
        for(long i=0;i<5; i++) {
            for (int j = 0; j < 9; j++) {
                randBools[(int) i][j] = rn.nextBoolean();
            }
        }
    }
    public void calculateCells(int x, int y, int w, int c){
        int sub =w/3;
        if(c==5 && mainroom) drawSquare(x+sub,y+sub,sub);
        else if(c==5){}
        else drawSquare(x+sub,y+sub,sub);
        if(c>0){
            c--;
            if(randBools[c][0])calculateCells(x,y,sub,c);
            if(randBools[c][1])calculateCells(x+sub,y,sub,c);
            if(randBools[c][2])calculateCells(x+sub*2,y,sub,c);
            if(randBools[c][3])calculateCells(x,y+sub,sub,c);
            if(randBools[c][4])calculateCells(x+sub,y+sub,sub,c);
            if(randBools[c][5])calculateCells(x+sub*2,y+sub,sub,c);
            if(randBools[c][6])calculateCells(x,y+sub*2,sub,c);
            if(randBools[c][7])calculateCells(x+sub,y+sub*2,sub,c);
            if(randBools[c][8])calculateCells(x+sub*2,y+sub*2,sub,c);
            //initArray(buffArray);
            //drawSquare(x,y+h,w*2,h*2);
            //drawSquare(x+w*2,y+h*2,w*3,h*3);
            //dispArray=buffArray;
        }
        else{
            exeCount=0;
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
    public void drawSquare(int x, int y, int side){
        shapeR.rect(x,y,side,side);
    }
    ////////////////////////////////////////

    static void setSeed(){
        for(int ii=0; ii<seedPoints;ii++) {
            int x=(int)(rn.nextGaussian()*stdDvX);
            int y=(int)(rn.nextGaussian()*stdDvY);
            if(x<0)x*=-1;
            if(y<0)y*=-1;


            System.out.println(x+" "+y);

            for (int i = 0; i < 1; i++)
                try{

                    if(rn.nextBoolean() && rn.nextFloat()<fillPercent)buffArray[x][y + i].setState(true);

                }
                catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Fail");

                }
        }
        dispArray=buffArray;

    }
    static void automataAlgorithm(int passes){
        while (passes>0){
            for(int i=0;i<res;i++){
                for(int j=0;j<res;j++){
                    if(buffArray[i][j].getState() && rn.nextBoolean()){
                        try {
                            if (rn.nextBoolean()) buffArray[i - 1][j - 1].setState(true);
                            if (rn.nextBoolean()) buffArray[i][j - 1].setState(true);
                            if (rn.nextBoolean()) buffArray[i + 1][j - 1].setState(true);
                            if (rn.nextBoolean()) buffArray[i + 1][j].setState(true);
                            if (rn.nextBoolean()) buffArray[i + 1][j + 1].setState(true);
                            if (rn.nextBoolean()) buffArray[i][j + 1].setState(true);
                            if (rn.nextBoolean()) buffArray[i - 1][j + 1].setState(true);
                            if (rn.nextBoolean()) buffArray[i - 1][j].setState(true);

                        }
                        catch (ArrayIndexOutOfBoundsException e){

                        }
                    }
                }
            }
            dispArray=buffArray;
            passes--;
        }
    }
    static void checkForSingles(){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                int emptyCount=0;
                try {
                    if (dispArray[i - 1][j - 1].getState()) emptyCount++;
                    if (dispArray[i][j - 1].getState()) emptyCount++;
                    if (dispArray[i + 1][j - 1].getState()) emptyCount++;
                    if (dispArray[i + 1][j].getState()) emptyCount++;
                    if (dispArray[i + 1][j + 1].getState()) emptyCount++;
                    if (dispArray[i][j + 1].getState()) emptyCount++;
                    if (dispArray[i - 1][j + 1].getState()) emptyCount++;
                    if (dispArray[i - 1][j].getState()) emptyCount++;
                    if (emptyCount == 0) dispArray[i][j].setState(false);
                }
                catch (ArrayIndexOutOfBoundsException e){

                }
            }
        }
}
    public static Cell[][] generateMap(){
        /*
        initArray(buffArray);
        setSeed();
        automataAlgorithm(firstRunDepth);

        for(int i=0;i<secondaryPoints; i++) setSeed();
        automataAlgorithm(secRunDepth);

        for(int i=0;i<triPoints; i++) setSeed();
        automataAlgorithm(triRunDepth);
        checkForSingles();*/
        return dispArray;
    }
    public static void initArray(Cell[][] arr){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                arr[i][j]=new Cell();
            }
        }
    }
    public void drawGrid(){
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
    public static Cell[][] generateMap2(){

        ArrayList<Cell> endpointList = new ArrayList<>();
        ArrayList<Cell> liveList=new ArrayList<>();
        int runs = 60;
        int initPoints =rn.nextInt(5)+1;

        initArray(buffArray);//buffers the buffArray
        plotInitPoints(endpointList,initPoints);
        growPaths(endpointList);
        //make sure there grid is more than 1 size
        liveList=updateLiveList(liveList);
        while(liveList.size()<2){
            growPaths(endpointList);
            liveList=updateLiveList(liveList);
        }
        for(int i=0;i<runs;i++){
            liveList= updateLiveList(liveList);
            endpointList=updateEndpoints(liveList,endpointList);
            growPaths(endpointList);
        }

        ArrayList<Cell> small = new ArrayList<>();
        //search for single off bits
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                try{
                    int count=0;
                    if(buffArray[i-1][j].getState())count++;
                    if(buffArray[i+1][j].getState())count++;
                    if(buffArray[i][j-1].getState())count++;
                    if(buffArray[i][j+1].getState())count++;
                    if(count>2 && rn.nextFloat()>.2){
                        buffArray[i][j].setState(true);
                        buffArray[i][j].setCords(i,j);
                        liveList.add(buffArray[i][j]);
                    }

                }
                catch (ArrayIndexOutOfBoundsException e){

                }
            }

        }
        dispArray=buffArray;
        return dispArray;
    }

    static ArrayList<Cell> plotInitPoints(ArrayList<Cell> endpointList, int count){
        //create initial growth points
        for(int i=0; i<count;i++) {
            int xs = rn.nextInt(res);
            int ys = rn.nextInt(res);
            buffArray[xs][ys].setState(true);
            buffArray[xs][ys].setCords(xs, ys);
            endpointList.add(buffArray[xs][ys]);
        }
        return endpointList;
    }

    static ArrayList<Cell> updateLiveList(ArrayList<Cell> liveList){
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
    static ArrayList<Cell> updateEndpoints(ArrayList<Cell> liveList, ArrayList<Cell> endpointList){
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
    static void growPaths(ArrayList<Cell> endpointList){
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
    static void addBranch(boolean a, int a1, int a2, int b1, int b2){
        if (a && rn.nextBoolean() && rn.nextFloat() <.6f) {
            buffArray[a1][a2].setState(true);
            buffArray[a1][a2].setCords(a1,a2);
            buffArray[b1][b2].setState(true);
            buffArray[b1][b2].setCords(b1,b2);
        }
    }
    public void dispose() {

    }
}
