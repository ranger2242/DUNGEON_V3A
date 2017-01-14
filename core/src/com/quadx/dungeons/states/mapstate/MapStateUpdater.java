package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.abilities.Warp;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.commands.DigComm;
import com.quadx.dungeons.commands.cellcommands.AddItemComm;
import com.quadx.dungeons.items.EnergyPlus;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.ManaPlus;
import com.quadx.dungeons.items.Potion;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.ShopState;
import com.quadx.dungeons.tools.DebugTextInputListener;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.EMath;
import com.quadx.dungeons.tools.Tests;

import java.util.*;

import static com.quadx.dungeons.Game.commandList;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.shakeCam;
import static com.quadx.dungeons.GridManager.*;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    static ArrayList<Integer> fpsList= new ArrayList<>();
    public static ArrayList<Anim> anims= new ArrayList<>();
     static ArrayList<Cell> pending= new ArrayList<>();
    static boolean displayFPS=true;
    private static float dtDig = 0;

    private static float dtInfo = 0;
    private static float dtItem = 0;
    private static float dtMap = 0;
    private static float dtShowStats =0;
    static float dtCollision = 0;
    static float dtScrollAtt=0;
    static float dtAttack = 0;
    public static float dtRespawn=0;
    static float dtFPS=0;
    static float fps=0;
    static float dtClearHits =0;
    static float dtInvSwitch = 0;
    static float dtShake=0;
    public static float endShake=0;

    public static int spawnCount=1;


    public MapStateUpdater(GameStateManager gsm) {
        super(gsm);
    }

    private static void updateCamPosition() {
        Vector3 position = cam.position;
        float lerp = 0.2f;
        float mag= 5;
        if(shakeCam) {
            position.x += ((player.getAbsPos().x - position.x) * lerp) + (mag * rn.nextGaussian());
            position.y += ((player.getAbsPos().y - position.y) * lerp) + (mag * rn.nextGaussian());
        }else{
            position.x += ((player.getAbsPos().x - position.x) * lerp);
            position.y += ((player.getAbsPos().y - position.y) * lerp);
        }
        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;

    }
    private static void keyboardAttackSelector(){
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            setAttackButton(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            setAttackButton(1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            setAttackButton(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            setAttackButton(3);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            setAttackButton(4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            setAttackButton(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
            setAttackButton(6);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            setAttackButton(7);
        }
    }
    public static void activateDig(){
        //if (dtDig > player.getMoveSpeed()) {
           // if (player.getEnergy() > 2) {
                int x = player.getX();
                int y = player.getY();
                gm.clearArea(x, y, true);
              //  player.setEnergy(player.getEnergy() - 2);
            //}
            //dtDig = 0;
    }
    private static void setAttackButton(int x){
        if(Gdx.input.isKeyPressed(Input.Keys.MINUS)){
            if(player.attackList.get(x) != null) {
                altNumPressed = x;
                attack2 = player.attackList.get(x);
            }
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.X) &&dtItem >.15){
            if(x<player.equipedList.size()) {
                Equipment e = player.equipedList.get(x);
                player.equipedList.remove(x);
                player.addItemToInventory(e);
                dtItem =0;
            }
        }
        else if(!Gdx.input.isKeyPressed(Input.Keys.X) && !Gdx.input.isKeyPressed(Input.Keys.MINUS))
            numberButtonHandler(x);
    }
    public static void selectItemFromInventory() {
        if (dtItem > itemMinTime && player.invList.size() > 0) {            //check if cooldown is over
            player.useItem(MapStateRender.inventoryPos);                    //actually use the item
            dtItem = 0;                                                     //reset cooldown

            int remove=-1;
            for(ArrayList<Item> arr : player.invList){                      //search for empty list just created
                if(arr.isEmpty()){
                    remove=player.invList.indexOf(arr);                     //get index if any
                }
            }
            if(remove>-1) {                                                 //if empty list is found
                player.invList.remove(remove);                              //remove at index
                if(MapStateRender.inventoryPos>=player.invList.size()-1){   //reset inventory postion if out of bounds
                    MapStateRender.inventoryPos=player.invList.size()-1;
                }
            }
        }
    }
    private static void numberButtonHandler(int i) {
        lastNumPressed = i;
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            if (dtInfo > .4 && i<player.attackList.size()) {
                Attack a = player.attackList.get(i);
                out(DIVIDER);
                out(a.getName() + ":");
                out("P:" + a.getPowerArr());
                out("M:" + a.getCostArr());
                out(a.getDescription());
                dtInfo = 0;
            }
        } else {
            if (dtAttack > attackMintime) {
                try {
                    attack = player.attackList.get( i);
                }catch (IndexOutOfBoundsException e){}
            }
        }
    }
    static void moveMonsters() {
        if(!Tests.allstop) {
            try {
                for (Monster m : monsterList) {
                    if(m !=null) {
                        m.updateVariables(Gdx.graphics.getDeltaTime());
                        m.move();
                    }
                }

            }catch (ConcurrentModificationException e){}
            for(Monster m: monsterList){
                for(Monster m1:monsterList){
                    if(m !=null) {
                        if (!m.equals(m1))
                            if (m.getHitBox().overlaps(m1.getHitBox())) {
                                float ang = (float) Math.toRadians(EMath.angle(m.getAbsPos(), m1.getAbsPos()));

                                m.setAbsPos(m.getAbsPos().add((float) (10 * Math.cos(ang)), (float) (10 * Math.sin(ang))));
                                m1.setAbsPos(m1.getAbsPos().add((float) (10 * Math.cos(ang + Math.toRadians(180))), (float) (10 * Math.sin(ang + Math.toRadians(180)))));
                            }
                    }
                }
            }
        }
    }
    static void updateVariables(float dt) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory()/(1024*1024);
        long allocatedMemory = runtime.totalMemory()/(1024*1024);
        //long freeMemory = runtime.freeMemory()/1024;
        Tests.currentMemUsage=allocatedMemory;
        Tests.memUsageList.add((double) (allocatedMemory/maxMemory));
        if(Tests.memUsageList.size()>Tests.meterListMax)
            Tests.memUsageList.remove(0);
        for(Anim a: anims){
            a.update();
        }
        for(ParticleEffect e:MapStateExt.effects){
            e.update(dt);
        }
        if(!anims.isEmpty())
            for(int i=anims.size()-1; i>=0; i--){
                if(anims.get(i).isEnd()){
                    ArrayList<Integer> remCell= new ArrayList<>();
                    for(Cell c: pending) {
                        if (anims.get(i).getFlag() == 0) {
                            int remComm=-1;
                            for(Command comm: c.commandQueue){
                                if(comm.getClass().equals(AddItemComm.class)){
                                    remComm=c.commandQueue.indexOf(comm);
                                    comm.execute();
                                }
                            }
                            if(remComm>=0 &&remComm<c.commandQueue.size())
                            c.commandQueue.remove(remComm);
                        }
                        if(c.commandQueue.isEmpty()){
                            remCell.add(pending.indexOf(c));
                        }
                    }
                    for(int j=remCell.size()-1;j>=0;j--){
                        pending.remove(remCell.get(i));
                    }
                    anims.remove(i);
                }
            }
        updateCamPosition();
        AttackMod.updaterVariables(dt);
        player.updateVariables(dt);

        MapStateRender.updateVariables(dt);
        //Tests.reloadMap(dt);
        if (dtFPS > .05) {
            fps = 1 / Gdx.graphics.getDeltaTime();
            fpsList.add((int) fps);
            if (fpsList.size() > Tests.meterListMax) {
                fpsList.remove(0);
            }
            dtFPS = 0;
        } else {
            dtFPS += dt;
        }
        if (dtClearHits <= .1)
            dtClearHits += dt;
        if (dtRespawn <= 10f)
            dtRespawn += dt;
        if (dtDig <= player.getMoveSpeed())
            dtDig += dt;
        if (dtItem <= itemMinTime)
            dtItem += dt;
        if (dtInfo <= .4)
            dtInfo += dt;
        if (dtStatPopup <= .4)
            dtStatPopup += dt;
        if (dtMap <= .6)
            dtMap += dt;
        if (dtCollision <= Game.frame / 2)
            dtCollision += dt;
        if (dtScrollAtt <= .3)
            dtScrollAtt += dt;
        if (dtAttack <= attackMintime)
            dtAttack += dt;
        if (dtInvSwitch <= .3)
            dtInvSwitch += dt;
        if (dtShowStats <= .2)
            dtShowStats += dt;
            dtShake+=dt;
        if(dtShake>endShake){
            shakeCam=false;
        }
        if (Warp.isEnabled()) {
            Warp.updateTimeCounter();
        }

        if (effectLoaded) effect.update(Gdx.graphics.getDeltaTime());
    }
    public static void spawnMonsters(int x){
            for (int i = 0; i < x; i++) {
                if(monsterList.size()<200) {
                    Monster m =Monster.getNew();
                    int index = rn.nextInt(liveCellList.size());
                    if (!liveCellList.get(index).getWater() && liveCellList.get(index).getState()) {
                        Cell c = liveCellList.get(index);
                        m.setCords(c.getX(), c.getY());
                        monsterList.add(m);
                        c.setMon(true);
                        c.setMonsterIndex(monsterList.indexOf(m));
                        liveCellList.set(index, c);
                        Game.console("MList:" + monsterList.indexOf(m));
                        MapStateRender.setHoverText("!", .5f, Color.RED, player.getPX(), player.getPY(), true);
                    }
                }
            }
            Monster.reindexMons=true;
            GridManager.loadLiveCells();
            spawnCount++;
            dtRespawn=0;
    }
    static void compareItemToEquipment(){
        try {
            if (!player.invList.isEmpty()) {
                if (player.invList.get(MapStateRender.inventoryPos).get(0).isEquip) {
                    boolean found = false;
                    Equipment eq = (Equipment) player.invList.get(MapStateRender.inventoryPos).get(0);

                    for (Equipment e : player.equipedList) {
                        if (e.getType().equals(player.invList.get(MapStateRender.inventoryPos).get(0).getType())) {
                            MapStateRender.statCompare = eq.compare(e);
                            found = true;
                        }
                    }
                    if (!found) MapStateRender.statCompare = eq.compare();
                } else {
                    MapStateRender.statCompare=null;
                }
            }
            else{
                MapStateRender.statCompare=null;
            }
        }catch (IndexOutOfBoundsException e){
            Game.getFont().setColor(Color.WHITE);
        }
    }
    public static void scrollItems(boolean right){
        if((dtInvSwitch > .3)) {
            if (right) {
                if (MapStateRender.inventoryPos < player.invList.size() - 1)
                    MapStateRender.inventoryPos++;
                else {
                    MapStateRender.inventoryPos = 0;
                }
                dtInvSwitch = 0;
            } else {
                if (MapStateRender.inventoryPos > 0)
                    MapStateRender.inventoryPos--;
                else {
                    MapStateRender.inventoryPos = player.invList.size() - 1;
                }
                dtInvSwitch = 0;
            }
        }
    }
    public static void useMainAtt(){
        if (dtAttack > attackMintime) {
            MapStateExt.battleFunctions(lastNumPressed);
            dtAttack = 0;
        }
    }
    public static void useAltAtt(){
        if(dtAttack>attackMintime){
            MapStateExt.battleFunctions(altNumPressed);
            dtAttack = 0;
        }
    }
    public static void discardItem(){
        if(dtItem>itemMinTime){
            try {
                Item item= player.invList.get(MapStateRender.inventoryPos).get(0);
                int nx=(int) (player.getX()+(rn.nextGaussian()*2));
                int ny=(int) (player.getY()+(rn.nextGaussian()*2));
                Cell test= dispArray[nx][ny];
                while(test.hasCrate() || !test.getState() || test.hasLoot() ||test.hasWarp() || test.getWater()
                        ||( nx == player.getX() && ny == player.getY())){
                    nx=(int) (player.getX()+(rn.nextGaussian()*2));
                    ny=(int) (player.getY()+(rn.nextGaussian()*2));
                    test= dispArray[nx][ny];
                }
                Vector2 v = new Vector2(nx,ny);
                Cell c= dispArray[(int) v.x][(int) v.y];
                int index= liveCellList.indexOf(c);
                c.addCommand(new AddItemComm(item,index));
                pending.add(c);
                int x= (int) player.getAbsPos().x;
                int y= (int) player.getAbsPos().y;
                Vector2 v2= new Vector2(x,y);
                anims.add(new Anim(item.getIcon(),v2,10,liveCellList.get(index).getAbsPos(),0));
                player.invList.get(MapStateRender.inventoryPos).remove(0);
                if (player.invList.get(MapStateRender.inventoryPos).isEmpty()) {
                    player.invList.remove(MapStateRender.inventoryPos);
                    if (MapStateRender.inventoryPos >= player.invList.size())
                        MapStateRender.inventoryPos = player.invList.size();
                }
                dtItem=0;
            }catch (IndexOutOfBoundsException e){}
        }
    }
    static void buttonHandler() {
        //keyboard functions--------------------------------------------------------
        for(Command c: commandList){
            c.execute();
        }
        keyboardAttackSelector();
        if (Gdx.input.isKeyPressed(Input.Keys.F1) && debug) {//reload map
            if (dtMap > .6) {
                gm.initializeGrid();
                dtMap = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {//show FPS module
            if(dtItem>.2){
                displayFPS=!displayFPS;
                dtItem=0;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F3)){
            Tests.calcAvgMapLoadTime();
            Tests.loadEmptyMap();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){//show stats
            if(dtShowStats>.2) {
                MapStateRender.showStats = !MapStateRender.showStats;
                dtShowStats=0;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T)){//open debug prompt
            DebugTextInputListener listener = new DebugTextInputListener();
            Gdx.input.getTextInput(listener, "Command", "","");
        }
    }
    public static void pause(){
        pause=true;
        cam.position.set(0, 0, 0);
        gsm.push(new ShopState(gsm));
    }
    public static void shakeScreen(float time){
        endShake=time;
        shakeCam=true;
        dtShake=0;
    }
    static void collisionHandler() {
        int x=player.getX();
        int y=player.getY();
        Cell c;
        try {
            c = GridManager.dispArray[x][y];
        }catch (ArrayIndexOutOfBoundsException e){
            c=GridManager.dispArray[0][0];
        }
        int index=liveCellList.indexOf(c);
        if(c .getState()) {
            if (x == c.getX() && y == c.getY()) {

                if (c.hasLoot()) {
                    MapStateRender.dtLootPopup = 0;
                    liveCellList.get(index).setHasLoot(false);
                    player.lastItem = liveCellList.get(index).getItem();
                    player.useItem(player.lastItem);
                    liveCellList.get(index).setItem(null);
                }
                if (c.hasCrate()) {
                    int x1 = liveCellList.get(index).getBoosterItem();
                    if (x1 == 0) {
                        player.useItem(new EnergyPlus());
                    } else if (x1 == 1) {
                        player.useItem(new Potion());
                    } else if (x1 == 2) {
                        player.useItem(new ManaPlus());
                    } else {

                        openCrate(index);
                    }
                    liveCellList.get(index).setBoosterItem(-1);
                    liveCellList.get(index).setCrate(false);
                    liveCellList.get(index).setItem(null);
                }

                if (c.hasWarp()) {
                    if (player.getAbilityPoints() != 0) {
                        gsm.push(new AbilitySelectState(gsm));
                    }
                    player.floor++;
                    gm.initializeGrid();
                }
                if (c.getShop()) {
                    liveCellList.get(index).setShop(false);
                    gsm.push(new ShopState(gsm));
                }
            }
        }else{
            DigComm d=new DigComm();
            d.execute();
        }
    }
    public static void setAim(Direction.Facing f){//why wont this shit update
        player.facing=f;
    }
}
