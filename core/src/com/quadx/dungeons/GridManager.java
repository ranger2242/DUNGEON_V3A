package com.quadx.dungeons;


import com.quadx.dungeons.abilities.DigPlus;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GridManager {
    private static ArrayList<Cell> warpList = new ArrayList<>();
    public static ArrayList<Monster> monsterList = new ArrayList<>();

    public static int res = Map2State.res;
    public static Cell[][] dispArray;
    public static ArrayList<Cell> liveCellList = new ArrayList<>();
    private static Random rn = new Random();

    public void initializeGrid() {
        clearMonsterList();
        createMap();
        plotLoot();
        plotCrates();
        plotShop();
        plotWarps();
        plotMonsters();
        plotPlayer();
        MapStateRender.showCircle = true;
        MapStateUpdater.dtCircle = MapStateRender.circleTime;
    }
    public void clearArea(int x, int y, boolean player) {
        x -= 1;
        y -= 1;
        unNullWallCells();
        try {dispArray[x + 1][y + 1].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x + 1][y].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x + 1][y - 1].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x][y - 1].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x][y + 1].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x - 1][y + 1].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x - 1][y].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
        try {dispArray[x - 1][y - 1].setState();}
        catch (ArrayIndexOutOfBoundsException e) {}
            if (player && AbilityMod.modifier==1) {//checks if players dig ability is active
                if (MapState.lastPressed == 'd')
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 3; j++) {
                            try{dispArray[x + i][y - 1 + j].setState();}
                            catch (ArrayIndexOutOfBoundsException e) {}
                        }
                    }
                if (MapState.lastPressed == 'a')
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 3; j++) {
                            try{dispArray[x - i][y - 1 + j].setState();}
                            catch (ArrayIndexOutOfBoundsException e) {}
                        }
                    }
                if (MapState.lastPressed == 's')
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {
                            try{dispArray[x - 1 + i][y - j].setState();}
                            catch (ArrayIndexOutOfBoundsException e) {}
                        }
                    }
                if (MapState.lastPressed == 'w')
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {
                            try{dispArray[x - 1 + i][y + j].setState();}
                            catch (ArrayIndexOutOfBoundsException e) {}
                        }
                    }
            }
            splitMapDataToList();
        }
    public void clearMonsterPositions() {
        for (Cell c : liveCellList)
            c.setMon(false);
    }
    private void unNullWallCells() {
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (dispArray[i][j] == null) dispArray[i][j] = new Cell();
            }
        }
    }
    private void createMap() {
        Map2State.updateVars();
        dispArray = Map2State.generateMap2();
        //splitMapDataToList();
        loadLiveCells();
    }
    public static void loadLiveCells(){
        liveCellList.clear();
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                try {
                    if (dispArray[i][j].getState())
                        liveCellList.add(dispArray[i][j]);
                }catch (NullPointerException e){}
            }
        }
    }
    private void splitMapDataToList() {
        liveCellList.clear();
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++)
                //Trim wall Cells from array to make memory taken smaller
                if (!dispArray[i][j].getState()) {
                    dispArray[i][j] = null;
                } else {
                    //set live cells into list
                    dispArray[i][j].setCords(i + 1, j + 1);
                    liveCellList.add(dispArray[i][j]);
                }
        }
        try {

        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }
    private void clearMonsterList() {
        monsterList.clear();
    }
    private void plotWarps() {
        boolean placed = false;
        int index=0;
        while(!placed){
            index = rn.nextInt(liveCellList.size());

            Cell c = liveCellList.get(index);
            if(!c.hasWater){
                int x=c.getX();
                int y=c.getY();
                int count=0;
                if(x-1>=0 && x+1<res &&y-1>=0 && y+1<res) {
                    if (dispArray[x + 1][y].hasWater) count++;
                    if (dispArray[x - 1][y].hasWater) count++;
                    if (dispArray[x][y + 1].hasWater) count++;
                    if (dispArray[x][y - 1].hasWater) count++;
                    if (count < 4) placed = true;
                }
                else index = rn.nextInt(liveCellList.size());
            }
        }
        liveCellList.get(index).setWarp();
        Cell c = liveCellList.get(index);
        int x=c.getX();
        int y=c.getY();
        for(int i=0;i<4;i++){
            if(y + i + 1<res)
                dispArray[x][y + i + 1].setState();
        }
    }
    private void plotMonsters() {
        double temp = liveCellList.size() * .005;

        //double temp=rng.nextGaussian(10,6);
        while (temp <= 0) {
            temp = rn.nextGaussian() * 20;
        }
        while (temp > 0) {
            Monster m = new Monster();
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater) {

                Cell c = liveCellList.get(index);
                m.setCords(c.getX(), c.getY());
                monsterList.add(m);
                c.setMon(true);
                c.setMonsterIndex(monsterList.indexOf(m));
                liveCellList.set(index,c);
                //liveCellList.get(index).setMon(true);
                //liveCellList.get(index).setMonsterIndex(monsterList.indexOf(m));
                Game.console("MList:" + monsterList.indexOf(m));
                temp--;
            }
        }
    }
    private void plotShop() {

        int index = rn.nextInt(liveCellList.size());
        if (!liveCellList.get(index).hasWater)
            liveCellList.get(index).setShop(true);
    }
    private void plotLoot() {
        float fillPercent = .01f;
        int loot = (int) (liveCellList.size() * fillPercent);
        while (loot > 0) {
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater)
                liveCellList.get(index).setHasLoot(true);
            loot--;
        }
    }
    private void plotCrates() {
        float fillPercent = .01f;
        int crates = (int) (liveCellList.size() * fillPercent);
        while (crates > 0) {
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater)
                liveCellList.get(index).setCrate(true);
            crates--;
        }
    }
    private void plotPlayer() {
        boolean placed =false;
        int index = rn.nextInt(liveCellList.size());

        while(!placed){
            Cell c = liveCellList.get(index);
            if(!c.hasWater){
                int x=c.getX();
                int y=c.getY();
                int count=0;
                if(x-1>=0 && x+1<res &&y-1>=0 && y+1<res) {
                    if (dispArray[x + 1][y].hasWater) count++;
                    if (dispArray[x - 1][y].hasWater) count++;
                    if (dispArray[x][y + 1].hasWater) count++;
                    if (dispArray[x][y - 1].hasWater) count++;
                    if (count < 4) placed = true;
                }
                else index = rn.nextInt(liveCellList.size());
            }
            else index = rn.nextInt(liveCellList.size());
        }

        Game.player.setLiveListIndex(index);
        Cell c = liveCellList.get(index);
        int w = MapState.cellW;
        Game.player.setCordsPX(c.getX() * w, c.getY() * w);
        int range = 20;
        ArrayList<Integer> monsFound = new ArrayList<>();
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                int x = c.getX() - (range / 2) + i;
                int y = c.getY() - range / 2 + j;
                int monIndex;

                if (x < 0) x = 0;
                if (x > res - 1) x = res - 1;
                if (y < 0) y = 0;
                if (y > res - 1) y = res - 1;
                for (Cell cell : liveCellList) {
                    if (cell.hasMon() && cell.getX() == x && cell.getY() == y) {
                        monIndex = cell.monsterIndex;
                        monsFound.add(monIndex);

                    }
                }
            }
        }
        Collections.sort(monsFound,Collections.<Integer>reverseOrder());
        for (Integer aMonsFound : monsFound) {
            try {
                Monster m = monsterList.get(aMonsFound);
                //MapState.out("Fucking monster:" + monsFound.get(i));
                dispArray[m.getX()][m.getY()].setMon(false);
                monsterList.remove(m);
            } catch (IndexOutOfBoundsException e) {
            }
        }
        loadLiveCells();
    }
}
