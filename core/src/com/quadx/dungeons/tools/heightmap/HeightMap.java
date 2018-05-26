package com.quadx.dungeons.tools.heightmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.shapes1_5.EMath;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.GridManager.res;
import static com.quadx.dungeons.states.State.cam;
import static com.quadx.dungeons.states.mapstate.MapState.cell;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;

/**
 * Created by Chris Cavazos on 1/16/2017.
 */
public class HeightMap{
    static float viewX;
    static float viewY;
    private static Random rn= new Random();
    float dtf=0;
    public static int n = 10;
    int[][] grid = new int[res][res];
    private ShapeRenderer shapeR = new ShapeRenderer();
    static ArrayList<Color> colors = new ArrayList<>();
    Vector2 centerCamPos = new Vector2(Game.WIDTH / 2, Game.HEIGHT / 2);
    Vector2 playerpos=new Vector2(100,100);
    Cell[][] cells;
    static int maxHeight=0;

    public HeightMap(Cell[][] g) {
        cells=g;
        initGrid();
        initColorList(n);
    }
    public static void initColorList(int n){
        colors.clear();
        float d =(3f/4f);
        Color c = new Color(rn.nextFloat()*d,rn.nextFloat()*d,rn.nextFloat()*d,1);
        for (int i = 0; i < n + 3; i++) {
            float a = 1 * ((float) i / (float) n)*.7f;
            colors.add(new Color(c.r*a, c.g*a, c.b*a, 1));
        }
        colors.remove(0);
    }
    public static ArrayList<Color> getColors(){return colors;}
    float[][] addToMap(float[][] f,float[][]f2) {
        float[][] f3=new float[res][res];
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                f3[i][j]=(f[i][j]+f2[i][j])/2;
            }
        }
        return f3;
    }
    float scanAverageHeight(float[][] f) {
        float a=0;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                a+=f[i][j];
            }
        }
        a/=(res*res);
        return a;
    }
    void setMaxHeight() {
        maxHeight = 0;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if(grid[i][j]>maxHeight)maxHeight=  grid[i][j];

            }
        }
    }
    public static float getMaxHeight(){
        return maxHeight;
    }
    protected void handleInput() {
        float rate = 20;
        if(Gdx.input.isKeyPressed(Input.Keys.F1)){
            initGrid();
        }/*
        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            if (dtf > .15) {
                if (cellW > 4) {
                    cellW--;
                    calcCorners();
                dtf=0;
                   }
            }

        }
        if(Gdx.input.isKeyPressed(Input.Keys.O)) {
            if (dtf > .15) {
                if (cellW < 40) {
                    cellW++;
                    calcCorners();
                    dtf = 0;
                }
            }
        }*/

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            centerCamPos.set(centerCamPos.x, centerCamPos.y - rate);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            centerCamPos.set(centerCamPos.x - rate, centerCamPos.y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            centerCamPos.set(centerCamPos.x, centerCamPos.y + rate);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            centerCamPos.set(centerCamPos.x + rate, centerCamPos.y);
        }
        if(dtf>.1) {

            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                if (playerpos.y > 0)
                    playerpos.y -= 1;
                dtf=0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                if (playerpos.x > 0)
                    playerpos.x -= 1;
                dtf=0;

            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                if (playerpos.y < res - 1)
                    playerpos.y += 1;
                dtf=0;

            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                if (playerpos.x < res - 1)
                    playerpos.x += 1;
                dtf=0;

            }
        }
    }
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, .4f, 0, 1);
        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        Game.setFontSize(0);



        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        float[] f= cells[(int) playerpos.x][(int) playerpos.y].getCorners().getVertices();
        shapeR.setColor(Color.BLUE);
        shapeR.rect(f[8],f[9],cellW,cellW);
        shapeR.end();
    }
    public void dispose() {

    }
    void initGrid() {
        float[][] f3=new float[res][res];
        float check =0;
        for(int i=0;check<.1f ;i++) {
            Noise noisemap = new Noise(null, .5f, 200, 200);
            noisemap.initialise();
            float[][] f = noisemap.getGrid();
            f3 = addToMap(f3,f);
            check=scanAverageHeight(f3);
        }
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                grid[i][j] = Math.round(f3[i][j] * n);
                if (grid[i][j] < 0) grid[i][j] = 0;
                if (grid[i][j] > n - 1) grid[i][j] = n - 1;
            }
        }
        calcCorners(cells);
        setMaxHeight();
    }
    public void calcCorners(Cell[][] cc){
        cells=cc;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                //cells[i][j]=new Cell(new Vector2(i,j),grid[i][j]);
                Polygon polygon=new Polygon();

                int     q,w,e,
                        a,s,d,
                        z,x,c;
                s=grid[i][j];
                try{
                    q =grid[i-1][j+1];
                }catch (ArrayIndexOutOfBoundsException p){
                    q =grid[i][j];
                }
                try{
                    w =grid[i][j+1];
                }catch (ArrayIndexOutOfBoundsException p){
                    w =grid[i][j];
                }
                try{
                   e  =grid[i+1][j+1];
                }catch (ArrayIndexOutOfBoundsException p){
                   e  =grid[i][j];
                }
                try{
                    d =grid[i+1][j];
                }catch (ArrayIndexOutOfBoundsException p){
                   d  =grid[i][j];
                }
                try{
                  c   =grid[i+1][j-1];
                }catch (ArrayIndexOutOfBoundsException p){
                   c  =grid[i][j];
                }
                try{
                  x   =grid[i][j-1];
                }catch (ArrayIndexOutOfBoundsException p){
                   x  =grid[i][j];
                }
                try{
                   z  =grid[i-1][j-1];
                }catch (ArrayIndexOutOfBoundsException p){
                  z   =grid[i][j];
                }
                try{
                  a   =grid[i-1][j];
                }catch (ArrayIndexOutOfBoundsException p){
                  a   =grid[i][j];
                }
                float ul,ur,dl,dr;
                ul=  EMath.average(new float[]{q,w,a,s});
                ur=  EMath.average(new float[]{w,e,s,d});
                dl=  EMath.average(new float[]{a,s,z,x});
                dr=  EMath.average(new float[]{s,d,x,c});
                float heightmx=20;
                float px0=(i*cell.x);
                float px1=((i+1)*cell.x);
                float py0=(heightmx*ul)+((j+1)*cell.y);
                float py1=(heightmx*ur)+((j+1)*cell.y);
                float py2=(j*cell.y)+(heightmx*dl);
                float py3=(j*cell.y)+(heightmx*dr);
                float xavg=(px0+px1)/2;
                float yavg=(py0+py1+py2+py3)/4;
                polygon.setVertices(new float[]{
                        px0,py0,//0,1 top left
                        px1,py1,//2,3 top right
                        px1,py3,//4,5 bot right
                        px0,py2,//6,7 bot left
                        xavg,yavg,//8,9 center
                        ul,ur,
                        dl,dr
                });
                cells[i][j].setHeight(grid[i][j]);
                cells[i][j].setCorners(polygon);
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }
}
