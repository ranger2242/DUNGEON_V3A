package com.quadx.dungeons;


import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class GridManager {
    static ArrayList<Cell> warpList = new ArrayList();
    public static ArrayList<Monster> monsterList = new ArrayList<>();

    public static int res = Map2State.res;
    public static int monsters = 10;
    public static int width = 0;
    public static int height = 0;
    public static Cell[][] dispArray;
    public static ArrayList<Cell> liveCellList = new ArrayList<>();
    static Random rn = new Random();

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
        try {
            dispArray[x + 1][y + 1].setState(true);
            dispArray[x + 1][y].setState(true);
            dispArray[x + 1][y - 1].setState(true);
            dispArray[x][y + 1].setState(true);
            dispArray[x][y - 1].setState(true);
            dispArray[x - 1][y + 1].setState(true);
            dispArray[x - 1][y].setState(true);
            dispArray[x - 1][y - 1].setState(true);
            if (player && AbilityMod.digPlus) {//checks if players dig ability is active
                if (MapState.lastPressed == 'd')
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 3; j++) {
                            dispArray[x + i][y - 1 + j].setState(true);
                        }
                    }
                if (MapState.lastPressed == 'a')
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 3; j++) {
                            dispArray[x - i][y - 1 + j].setState(true);
                        }
                    }
                if (MapState.lastPressed == 's')
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {
                            dispArray[x - 1 + i][y - j].setState(true);
                        }
                    }
                if (MapState.lastPressed == 'w')
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 9; j++) {
                            dispArray[x - 1 + i][y + j].setState(true);
                        }
                    }
            }
            splitMapDataToList();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public void clearMonsterPositions() {
        for (Cell c : liveCellList)
            c.setMon(false);
    }

    private void unNullWallCells() {
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (dispArray[i][j] == null) dispArray[i][j] = new Cell(false);
            }
        }
    }

    private void createMap() {
        Map2State.updateVars();
        dispArray = Map2State.generateMap2();
        //splitMapDataToList();
        loadLiveCells();
    }
    private void loadLiveCells(){
        liveCellList.clear();
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                if(dispArray[i][j].getState())
                liveCellList.add(dispArray[i][j]);
            }
        }
    }
    private void splitMapDataToList() {
        liveCellList.clear();
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++)
                //Trim wall Cells from array to make memory taken smaller
                if (dispArray[i][j].getState() == false) {
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
        //int points=3;
        warpList.clear();
        boolean plotted = false;
        while (!plotted) {
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater) {
                liveCellList.get(index).setWarp(true);
                warpList.add(liveCellList.get(index));
                plotted = true;
            }
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
        int index = rn.nextInt(liveCellList.size());
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
                int monIndex = -1;

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
        for(int i =0;i<monsFound.size();i++){
            try {
                Monster m = monsterList.get(monsFound.get(i));
                //MapState.out("Fucking monster:" + monsFound.get(i));
                dispArray[m.getX()][m.getY()].setMon(false);
                monsterList.remove(m);
            }catch (IndexOutOfBoundsException e){}
        }
        loadLiveCells();
    }
}
