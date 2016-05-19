package com.quadx.dungeons;


import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;
import java.util.Random;

public class GridManager {
    static ArrayList<Cell> warpList = new ArrayList();
    public static ArrayList<Monster> monsterList = new ArrayList<>();

    public static int res= 200;
    public static int monsters= 10;
    public  static int width=0;
    public static int height=0;
    public static Cell[][] dispArray;
    public static ArrayList<Cell> liveCellList = new ArrayList<>();
    static Random rn = new Random();

    public void initializeGrid(){
        clearMonsterList();
        createMap();
        plotLoot();
        plotCrates();
        plotShop();
        plotWarps();
        plotMonsters();
        plotPlayer();
    }
    public void clearArea() {
        int x= Game.player.getX()-1;
        int y= Game.player.getY()-1;
        unNullWallCells();
        try {
            dispArray[x + 1][y + 1].setState(true);
            dispArray[x + 1][y].setState(true);
            dispArray[x + 1][y - 1].setState(true);
            dispArray[x][y + 1].setState(true);
            dispArray[x][y - 1].setState(true);
            dispArray[x - 1][y + 1].setState(true);
            dispArray[x - 1][y].setState(true);
            dispArray[x - 1][y - 1].setState(true);
            splitMapDataToList();
        }catch(ArrayIndexOutOfBoundsException e){}
    }
    public void clearMonsterPositions(){
        for(Cell c:liveCellList)
            c.setMon(false);
    }
    private void unNullWallCells(){
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                if(dispArray[i][j]==null)dispArray[i][j]=new Cell(false);
            }
        }
    }
    private void createMap() {
            Map2State.updateVars();
            dispArray= Map2State.generateMap2();
            splitMapDataToList();
        }
    private void splitMapDataToList(){
        liveCellList.clear();
        for(int i=0;i<res;i++) {
            for(int j=0;j<res;j++)
                //Trim wall Cells from array to make memory taken smaller
                if(dispArray[i][j].getState()==false){
                    dispArray[i][j]=null;
                }
                else{
                    //set live cells into list
                    dispArray[i][j].setCords(i+1,j+1);
                    liveCellList.add(dispArray[i][j]);
                }
        }
        try{

        }
        catch (ArrayIndexOutOfBoundsException e){

        }
    }
    private void clearMonsterList(){
            monsterList.clear();
        }
    private void plotWarps() {
            //int points=3;
            warpList.clear();
            int index=rn.nextInt(liveCellList.size());
            liveCellList.get(index).setWarp(true);
            warpList.add(liveCellList.get(index));

        }
    private void plotMonsters() {
            double temp=liveCellList.size()*.005;

            //double temp=rng.nextGaussian(10,6);
            while(temp<=0){
                temp=rn.nextGaussian()*20;
            }
            while(temp>0){
                Monster m = new Monster();
                int index=rn.nextInt(liveCellList.size());
                Cell c =liveCellList.get(index);
                m.setCords(c.getX(),c.getY());
                monsterList.add(m);
                liveCellList.get(index).setMon(true);
                liveCellList.get(index).setMonsterIndex(monsterList.indexOf(m));
                temp--;
            }
        }
    private void plotShop(){

            int index= rn.nextInt(liveCellList.size());
            liveCellList.get(index).setShop(true);
        }
    private void plotLoot() {
            float fillPercent=.01f;
            int loot=(int)(liveCellList.size()*fillPercent);
            while(loot>0){
                int index = rn.nextInt(liveCellList.size());
                liveCellList.get(index).setHasLoot(true);
                loot--;
            }
        }
    private void plotCrates(){
            float fillPercent=.001f;
            int crates=(int)(liveCellList.size()*fillPercent);
            while(crates>0){
                int index = rn.nextInt(liveCellList.size());
                liveCellList.get(index).setCrate(true);
                crates--;
            }
        }
    private void plotPlayer() {
            int index = rn.nextInt(liveCellList.size());
            Game.player.setLiveListIndex(index);
            Cell c=liveCellList.get(index);
            int w= MapState.cellW;
            Game.player.setCordsPX(c.getX()*w,c.getY()*w);
        }

}
