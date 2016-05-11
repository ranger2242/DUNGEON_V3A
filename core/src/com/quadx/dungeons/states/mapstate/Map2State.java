package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.State;

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
    static int seedPoints=50;
    static int secondaryPoints=50;
    static int triPoints=50;
    static int firstRunDepth=5;
    static int secRunDepth=3;
    static int triRunDepth=6;
    static int stdDvX=res/4;
    static int stdDvY=res/4;
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
        generateMap();
    }
    protected void handleInput() {

    }
    ////////////////////////////////////////
    //UPDATE FUNCTIONS
    public void update(float dt) {
        dtChange+=dt;
        if(dtChange>2){
            updateVars();
            generateMap();
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
        Gdx.gl.glClearColor(1,1,1,0);

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
        initArray(buffArray);
        setSeed();
        automataAlgorithm(firstRunDepth);

        for(int i=0;i<secondaryPoints; i++) setSeed();
        automataAlgorithm(secRunDepth);

        for(int i=0;i<triPoints; i++) setSeed();
        automataAlgorithm(triRunDepth);
        checkForSingles();
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


    public void dispose() {

    }
}
