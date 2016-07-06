package com.quadx.dungeons;


import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.WallPattern;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.ImageLoader.a;

public class GridManager {
    public static final Random rn = new Random();
    public static final ArrayList<Cell> liveCellList = new ArrayList<>();
    public static final ArrayList<Cell> drawList = new ArrayList<>();
    public static final ArrayList<Monster> monsterList = new ArrayList<>();
    public static ArrayList<Monster> monsOnScreen = new ArrayList<>();

    private static int liveCount=0;
    public static Cell[][] dispArray;
    public static final int res = Map2State.res;

    public void initializeGrid() {
        MapStateUpdater.spawnCount=1;
        MapStateUpdater.dtRespawn=0;
        player.setMana(player.manaMax);
        player.setEnergy(player.getEnergyMax());
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
    private static ArrayList<Cell> getSurroundingCells(int x, int y){
        ArrayList<Cell> list=new ArrayList<>();
        boolean a =x+1<res;
        boolean b=x-1>=0;
        boolean c=y+1<res;
        boolean d=y-1>=0;

        if(b && c)  list.add(dispArray[x - 1][y + 1]);
        if(c)       list.add(dispArray[x][y + 1]);
        if(a && c)  list.add(dispArray[x + 1][y + 1]);
        if(b)       list.add(dispArray[x - 1][y]);
        if(a &&b && d)       list.add(dispArray[x + 1][y]);
        if(b && d)  list.add(dispArray[x - 1][y - 1]);
        if(d)       list.add(dispArray[x][y - 1]);
        if(a && d)  list.add(dispArray[x + 1][y - 1]);
        return list;
    }
    private void clearDigPlusCells(int ii, int jj, int x, int y){
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
                if(nx >=0 && nx <res && ny >=0 && ny <res) {
                    if (!dispArray[nx][ny].getState()) {
                        dispArray[nx][ny].setState();
                    }
                }
            }
        }
    }
    public void clearArea(int x, int y, boolean player) {
        ArrayList<Cell> temp= getSurroundingCells(x,y);
        temp.stream().filter(c -> !c.getState()).forEach(Cell::setState);
        temp.clear();
        if (player && AbilityMod.modifier==1) {//checks if players dig ability is active
            if (MapState.lastPressed == 'd' ||MapState.lastPressed == 'a')
                clearDigPlusCells(9,3,x,y);
            if (MapState.lastPressed == 's' ||MapState.lastPressed == 'w')
                clearDigPlusCells(3,9,x,y);
        }
            splitMapDataToList();
        }
    static Cell loadTiles(Cell c){
        int x1 = c.getX();
        int y1 = c.getY();
        ArrayList<Cell> temp = getSurroundingCells(x1, y1);
        int count = 0;
        Texture t = c.getTile();
        if (temp.size() == 8) {
            for (int ii = 0; ii < 3; ii++) {
                for (int jj = 0; jj < 3; jj++) {
                    if (count < temp.size()) {
                        if (ii == 1 && jj == 1) {
                            WallPattern.p[ii][jj] = false;
                        } else {

                            if (c.getWater()) {
                                WallPattern.p[ii][jj] = !temp.get(count).getWater();
                            } else {
                                WallPattern.p[ii][jj] = temp.get(count).getState();
                            }
                            count++;
                        }
                    }
                }
            }
            int a = 0;
            if (c.getWater()) a = 1;
            Texture t1 = WallPattern.getTile(a);
            if (t1 != null && t != t1) {
                c.setTile(t1);
            }
        } else {
            if (!c.getTile().equals(a[0]))
                c.setTile(a[0]);
        }
        return c;
    }

    public static void loadDrawList() {
        drawList.clear();
        monsOnScreen.clear();
        int x = (int) (viewX / cellW);
        int y = (int) (viewY / cellW);
        int endx = (int) ((viewX + Game.WIDTH) / cellW);
        int endy = (int) ((viewY + Game.HEIGHT) / cellW);
        for (int i = x - 1; i < endx + 1; i++) {
            for (int j = y - 1; j < endy + 1; j++) {
                Cell c;
                try {
                    c = dispArray[i][j];
                    if (!c.getState() || c.getWater()) {
                        c=loadTiles(c);

                    }
                    drawList.add(c);
                    //check for monster
                    if (c.getMonster() != null) {
                        monsOnScreen.add(c.getMonster());
                    }
                } catch (ArrayIndexOutOfBoundsException e) {

                }
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
        int count=0;
        for(int i=0;i<res;i++){
            for(int j=0;j<res;j++){
                        dispArray[i][j].setCords(i,j);
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
        int index = rn.nextInt(liveCellList.size());
        Cell c = liveCellList.get(index);
        while (!placed) {
            while (!(!c.getWater() && c.getState())) {
                index = rn.nextInt(liveCellList.size());
                c = liveCellList.get(index);
            }
            int x = c.getX();
            int y = c.getY();
            int count = 0;
            if (x - 1 >= 0 && x + 1 < res && y - 1 >= 0 && y + 1 < res) {
                if (dispArray[x + 1][y].hasWater) count++;
                if (dispArray[x - 1][y].hasWater) count++;
                if (dispArray[x][y + 1].hasWater) count++;
                if (dispArray[x][y - 1].hasWater) count++;
                if (count < 4) {
                    placed = true;
                    MapState.warpX = x;
                    MapState.warpY = y;
                }
            } else index = rn.nextInt(liveCellList.size());
        }
        liveCellList.get(index).setWarp();
    }
    private void plotMonsters() {
        if (player.getFloor() == 1)
            splitMapDataToList();
        int temp = (int) (liveCount * .01);//calculate number of monsters
        while (temp > 0) {
            Monster m = Monster.getNew();
            int listSize = liveCellList.size();
            int point = rn.nextInt(listSize);
            Cell c = liveCellList.get(point);
            c.setState();
            m.setMonListIndex(listSize + 1);
            m.setLiveCellIndex(point);
            c.setMonster(m);
            monsterList.add(c.getMonster());
            c.setMonsterIndex(listSize + 1);
            liveCellList.set(point, c);
            temp--;

        }
    }
    private void plotShop() {
        int index = rn.nextInt(liveCellList.size());
        liveCellList.get(index).setState();
        liveCellList.get(index).setShop(true);
    }

    private void plotLoot() {
        float fillPercent = .01f;
        int loot = (int) (liveCellList.size() * fillPercent);
        while (loot > 0) {
            int index = rn.nextInt(liveCellList.size());
            Cell c =liveCellList.get(index);
            while(c.getWater()){
                index = rn.nextInt(liveCellList.size());
                c = liveCellList.get(index);
            }
            Item i = new Gold();
            liveCellList.get(index).setItem(i);
            liveCellList.get(index).setHasLoot(true);
            loot--;
        }
    }

    private void plotCrates() {
        float fillPercent = .01f;
        int crates = (int) (liveCellList.size() * fillPercent);
        for (int a = 0; a < crates; a++) {
            int index = rn.nextInt(liveCellList.size());
            Cell c =liveCellList.get(index);
            while(c.getWater()){
                index = rn.nextInt(liveCellList.size());
                c = liveCellList.get(index);
            }
            if (rn.nextFloat() < .4) {
                int x = rn.nextInt(3);
                liveCellList.get(index).setBoosterItem(x);
            } else {
                int q = rn.nextInt(14) + 1;
                if (q >= 11) {
                    Item i = new Gold();
                    liveCellList.get(index).setItem(i);
                } else {
                    Item item = new Item();
                    if (q == 1 || q == 2) item = new AttackPlus();
                    else if (q == 3 || q == 4) item = new DefPlus();
                    else if (q == 5 || q == 6) item = new IntPlus();
                    else if (q == 7 || q == 8) item = new SpeedPlus();
                    else if (q == 9 || q == 10) {
                        if (rn.nextFloat() < .1) {
                            item = new SpellBook();
                        } else
                            item = Equipment.generateEquipment();
                    }
                    liveCellList.get(index).setItem(item);
                }
            }
            liveCellList.get(index).setCrate(true);
        }

    }
    private void plotPlayer() {
        int index = rn.nextInt(liveCellList.size());
        Cell c = liveCellList.get(index);
        int w = cellW;
        while(!(!c.getWater() && c.getState())){
            index = rn.nextInt(liveCellList.size());
            c = liveCellList.get(index);
        }
        player.setCordsPX(c.getX() * w, c.getY() * w);
        int range = 35;
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                int x = c.getX() - (range / 2) + i;
                int y = c.getY() - range / 2 + j;

                if (x < 0) x = 0;
                if (x > res - 1) x = res - 1;
                if (y < 0) y = 0;
                if (y > res - 1) y = res - 1;
                for (Cell cell : liveCellList) {
                    if (cell.hasMon() && cell.getX() == x && cell.getY() == y) {
                        monsterList.remove(cell.getMonster());
                        cell.clearMonster();
                    }
                }
            }
        }
    }
}
