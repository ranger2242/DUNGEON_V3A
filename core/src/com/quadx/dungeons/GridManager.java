package com.quadx.dungeons;


import com.quadx.dungeons.items.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.quadx.dungeons.states.mapstate.MapState.*;

public class GridManager {
    private static Random rn = new Random();
    public static ArrayList<Cell> liveCellList = new ArrayList<>();
    public static ArrayList<Cell> drawList = new ArrayList<>();
    public static ArrayList<Monster> monsterList = new ArrayList<>();
    public static int liveCount=0;
    public static Cell[][] dispArray;
    public static int res = Map2State.res;

    public void initializeGrid() {
        MapStateUpdater.spawnCount=1;
        MapStateUpdater.dtRespawn=0;
        Game.player.setMana(Game.player.manaMax);
        Game.player.setEnergy(Game.player.getEnergyMax());
        clearMonsterList();
        createMap();
        plotLoot();
        plotCrates();
        plotShop();
        plotWarps();
        plotMonsters();
        plotPlayer();
        MapStateRender.showCircle = true;
    }
    ArrayList<Cell> getSurroundingCells(int x, int y){
        ArrayList<Cell> list=new ArrayList<>();
        //x -= 1;
        //y -= 1;
        boolean a =x+1<res;
        boolean b=x-1>=0;
        boolean c=y+1<res;
        boolean d=y-1>=0;
        if(a && c)  list.add(dispArray[x + 1][y + 1]);
        if(a &&b && d)       list.add(dispArray[x + 1][y]);
        if(a && d)  list.add(dispArray[x + 1][y - 1]);
        if(c)       list.add(dispArray[x][y + 1]);
        if(b && c)  list.add(dispArray[x - 1][y + 1]);
        if(b)       list.add(dispArray[x - 1][y]);
        if(b && d)  list.add(dispArray[x - 1][y - 1]);
        if(d)       list.add(dispArray[x][y - 1]);

        return list;
    }
    void clearDigPlusCells(int ii, int jj, int x, int y){
    //    x -= 1;
    //    y -= 1;
        for (int i = 0; i < ii; i++) {
            for (int j = 0; j < jj; j++) {
                int nx=0;
                int ny=0;
                switch (MapState.lastPressed){
                    case 'w':{nx=x - 1 + i; ny=y + j;       break;}
                    case 'd':{nx=x + i;     ny=y - 1 + j;   break;}
                    case 's':{nx=x - 1 + i; ny=y - j;       break;}
                    case 'a':{nx=x - i;     ny=y - 1 + j;   break;}
                }
                try {
                    if (!dispArray[nx][ny].getState()) {
                        dispArray[nx][ny].setState();
                    }
                }
                catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
    }
    public void clearArea(int x, int y, boolean player) {
        //unNullWallCells();
        ArrayList<Cell> temp= getSurroundingCells(x,y);
        for(Cell c:temp){
            if(!c.getState()) {
                c.setState();
            }
        }
        temp.clear();
        if (player && AbilityMod.modifier==1) {//checks if players dig ability is active
            if (MapState.lastPressed == 'd' ||MapState.lastPressed == 'a')
                clearDigPlusCells(9,3,x,y);
            if (MapState.lastPressed == 's' ||MapState.lastPressed == 'w')
                clearDigPlusCells(3,9,x,y);
        }
            splitMapDataToList();
        }
    public static void loadDrawList(){
        drawList.clear();
        for(Cell c: liveCellList){
            int screenLeftBound= (int) viewX;
            int screenRightBound= (int) viewX+Game.WIDTH;
            int screenBotBound= (int) viewY;
            int screenTopBound= (int) viewY+Game.HEIGHT;
            int x=(c.getX()+1)*cellW;
            int y=(c.getY()+1)*cellW;


            if(x>=screenLeftBound &&x-cellW<=screenRightBound
                    && y>=screenBotBound && y-cellW<=screenTopBound){
                drawList.add(c);
            }
           // c.setAttArea(false);
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
        int count=0;
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
               // try {
                    //if (dispArray[i][j].getState())
                        dispArray[i][j].setCords(i,j);
                        dispArray[i][j].setLLIndex(count);
                        liveCellList.add(dispArray[i][j]);
                        count++;
            }
        }
    }
    private void splitMapDataToList() {
        liveCellList.clear();
        liveCount=0;
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                if (dispArray[i][j].getState()) {
                    liveCount++;
                }
                liveCellList.add(dispArray[i][j]);
            }
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
            if(!c.hasWater && c.getState()){
                int x=c.getX();
                int y=c.getY();
                int count=0;
                if(x-1>=0 && x+1<res &&y-1>=0 && y+1<res) {
                    if (dispArray[x + 1][y].hasWater) count++;
                    if (dispArray[x - 1][y].hasWater) count++;
                    if (dispArray[x][y + 1].hasWater) count++;
                    if (dispArray[x][y - 1].hasWater) count++;
                    if (count < 4) {
                        placed = true;
                        MapState.warpX=x;
                        MapState.warpY=y;
                    }
                }
                else index = rn.nextInt(liveCellList.size());
            }
        }
        liveCellList.get(index).setWarp();
    }
    private void plotMonsters() {
        double temp = liveCount * .005;
        while (temp <= 0) {
            temp = rn.nextGaussian() * 20;
        }
        while (temp > 0) {
            Monster m = new Monster();
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater&& liveCellList.get(index).getState()) {

                Cell c = liveCellList.get(index);
                m.setCords(c.getX(), c.getY());
                monsterList.add(m);
                c.setMon(true);
                c.setMonsterIndex(monsterList.indexOf(m));
                liveCellList.set(index,c);
                m.setMonListIndex(monsterList.indexOf(m));
                m.setLiveCellIndex(index);
                //liveCellList.get(index).setMon(true);
                //liveCellList.get(index).setMonsterIndex(monsterList.indexOf(m));
                Game.console("MList:" + monsterList.indexOf(m));
                temp--;
            }
        }
    }
    private void plotShop() {
        int index = rn.nextInt(liveCellList.size());
        boolean placed=false;
        while(!placed) {
            if (!liveCellList.get(index).hasWater) {
                liveCellList.get(index).setState();
                liveCellList.get(index).setShop(true);
                placed=true;
            }
        }
    }
    private void plotLoot() {
        float fillPercent = .01f;
        int loot = (int) (liveCellList.size() * fillPercent);
        while (loot > 0) {
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater) {
                Item i =new Gold();
                liveCellList.get(index).setItem(i);
                liveCellList.get(index).setHasLoot(true);
                loot--;
            }
        }
    }
    private void plotCrates() {
        float fillPercent = .003f;
        int crates = (int) (liveCellList.size() * fillPercent);
        while (crates > 0) {
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).hasWater) {
                if(rn.nextFloat()<.4){
                    if(rn.nextBoolean()){
                        liveCellList.get(index).setBoosterItem(1);
                    }else{
                        liveCellList.get(index).setBoosterItem(2);
                    }
                }
                else{
                    int q = rn.nextInt(14) + 1;
                    if (q >= 11) {
                        Item i=new Gold();
                        liveCellList.get(index).setItem(i);
                    } else {
                        Item item = new Item();
                        if (q == 1 || q == 2) item = new AttackPlus();
                        else if (q == 3 || q == 4) item = new DefPlus();
                        else if (q == 5 || q == 6) item = new IntPlus();
                        else if (q == 7 || q == 8) item = new SpeedPlus();
                        else if (q == 9 || q == 10) item = generateEquipment();
                        liveCellList.get(index).setItem(item);
                    }
                }
                liveCellList.get(index).setCrate(true);
            }
            crates--;
        }
    }
    private void plotPlayer() {
        boolean placed =false;
        int index = rn.nextInt(liveCellList.size());

        while(!placed){
            Cell c = liveCellList.get(index);
            if(!c.hasWater && c.getState()){
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
        int w = cellW;
        Game.player.setCordsPX(c.getX() * w, c.getY() * w);
        int range = 35;
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
        //loadLiveCells();
    }
}
