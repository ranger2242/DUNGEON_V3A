package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.*;
import com.quadx.dungeons.abilities.Warp;
import com.quadx.dungeons.abilities.WaterBreath;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.commands.cellcommands.AddItemComm;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.ShopState;
import com.quadx.dungeons.tools.*;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.heightmap.Matrix;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.monsters.Monster.reindexMons;
import static com.quadx.dungeons.states.mapstate.MapStateRender.dtWaterEffect;
import static com.quadx.dungeons.states.mapstate.MapStateRender.inventoryPos;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    public static ArrayList<Anim> anims= new ArrayList<>();
    static ArrayList<Integer> fpsList= new ArrayList<>();
    static ArrayList<Cell> pending= new ArrayList<>();

    private static float dtDig = 0;
    private static float dtInfo = 0;
    private static float dtItem = 0;
    private static float dtMap = 0;
    private static float dtShowStats =0;
    public static float dtLootPopup=0;
    public static float dtRespawn=0;
    public static float endShake=0;
    static float dtWater=0;
    static float dtCollision = 0;
    static float dtScrollAtt=0;
    static float dtAttack = 0;
    static float dtFPS=0;
    static float dtf=0;
    static float fps=0;
    static float dtClearHits =0;
    static float dtInvSwitch = 0;
    static float dtShake=0;
    static float force=0;

    public static int spawnCount=1;

    static boolean displayFPS=true;


    public MapStateUpdater(GameStateManager gsm) {
        super(gsm);
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
    private static void updateCamPosition() {
        Vector3 position = cam.position;
        float[] f = dispArray[(int) player.getPos().x][(int) player.getPos().y].getCorners().getVertices();
        Vector3 v = new Vector3(f[8], f[9], 0);
        if(!noLerp) {
            if (shakeCam) {
                v.add((float) (force * rn.nextGaussian()), (float) (force * rn.nextGaussian()), 0);
                position.lerp(v, .5f);

            } else {

                position.lerp(v, .2f);
            }
        }else{
            position.set(player.getAbsPos().x,GridManager.fixHeight(player.getAbsPos()),0);
            noLerp=false;
        }

        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;
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
    public static void spawnMonsters(int x){
        for (int i = 0; i < x; i++) {
            if(monsterList.size()<70) {
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
                    new HoverText("!", .5f, Color.RED,  player.getAbsPos().x, player.getAbsPos().y, true);
                }
            }
        }
        reindexMons=true;
        GridManager.loadLiveCells();
        spawnCount++;
        dtRespawn=0;
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
        }                   // new HoverText("-!-",.5f, Color.MAGENTA,player.getAbsPos().x+(player.getIcon().getWidth()/2),player.getAbsPos().y+player.getIcon().getHeight()+10,true);

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
    public static void rotateMap(boolean left){
        if(dtf>.15) {noLerp=true;
            Matrix<Integer> rotator = new Matrix<>(Integer.class);
            dispArray = rotator.rotateMatrix(dispArray, res, left);
            player.setPos(rotateCords(left,player.getPos()));
            warp.set ( rotateCords(left,warp));
            shop.set(rotateCords(left,shop));
            player.setAbsPos(new Vector2(player.getPos().x*cellW,player.getPos().y*cellW));
            for(Monster m:monsterList){
                m.setPos(rotateCords(left,m.getPos()));
                m.setAbsPos(new Vector2(m.getPos().x*cellW,m.getPos().y*cellW));
            }
            GridManager.hm.calcCorners(dispArray);
            dispArray=hm.getCells();
            GridManager.loadLiveCells();
            GridManager.loadDrawList();
            dtf=0;
        }
    }
    public static void pause(){
        pause=true;
        cam.position.set(0, 0, 0);
        gsm.push(new ShopState(gsm));
    }
    public static void shakeScreen(float time , float f){
        force=f;
        endShake=time;
        shakeCam=true;
        dtShake=0;
    }
    public static void setAim(Direction.Facing f){//why wont this shit update
        player.facing=f;
    }
    static void moveMonsters() {
            try {
                for (int i = 0; i < monsterList.size(); i++) {
                    Monster m = monsterList.get(i);
                    if (m != null) {
                        m.checkAgro();
                        if (m.isDrawable() || m.isHit()) {
                            //check for attack collision
                            boolean hit = false;
                            if (player.getAttackBox().overlaps(m.getHitBox())) {
                                hit = true;
                                m.takeEffect(attack);
                                m.takeAttackDamage(Damage.calcPlayerDamage(attack, m));
                                m.checkIfDead();
                            }
                            StatManager.shotMissed(hit);
                            ////
                            m.updateVariables(Gdx.graphics.getDeltaTime());
                            if (!Tests.allstop)
                                m.move();
                            //check collisions with other monsters
                            for (int j = i; j < monsterList.size(); j++) {
                                Monster m1 = monsterList.get(j);
                                if (!m.equals(m1) && m.isDrawable())
                                    if (m.getHitBox().overlaps(m1.getHitBox())) {
                                        float ang = (float) Math.toRadians(EMath.angle(m.getAbsPos(), m1.getAbsPos()));//dvs
                                        m.setAbsPos(m.getAbsPos().add((float) (10 * Math.cos(ang)), (float) (10 * Math.sin(ang))));
                                        m1.setAbsPos(m1.getAbsPos().add((float) (10 * Math.cos(ang + Math.toRadians(180))), (float) (10 * Math.sin(ang + Math.toRadians(180)))));
                                    }
                            }
                        }
                    }
                    //reindex mons as needed
                    if (reindexMons)
                        m.setMonListIndex(monsterList.indexOf(m));
                }
            }
            catch (ConcurrentModificationException e){}
        reindexMons = false;
    }
    static void updateVariables(float dt) {
        //nothing before these inits
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory()/(1024*1024);
        long allocatedMemory = runtime.totalMemory()/(1024*1024);
        Tests.currentMemUsage=allocatedMemory;
        Tests.memUsageList.add((double) (allocatedMemory/maxMemory));
        if(Tests.memUsageList.size()>Tests.meterListMax)
            Tests.memUsageList.remove(0);
        //update below this line-----------------------
        //reset inventorypos
        if(inventoryPos<0 || inventoryPos>player.invList.size()-1){
            inventoryPos=0;
        }

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
        dtf+=dt;
        dtDig+=dt;
        dtWater+=dt;
        dtWaterEffect+=dt;
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

        //put hud update positions here
        updateCamPosition();
        HUD.update();
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
    static void buttonHandler() {
        //keyboard functions--------------------------------------------------------
        for(Command c: commandList){
            c.execute();
        }
        keyboardAttackSelector();
        if(Gdx.input.isKeyPressed(Input.Keys.B)){
            player.jumping=true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            rotateMap(false);

        }
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
        if(Gdx.input.isKeyPressed(Input.Keys.F4)) {
            gsm.push(new ShopState(gsm));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.B)){
            player.jumping=true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){//show stats
            if(dtShowStats>.2) {
                MapStateRender.showStats = !MapStateRender.showStats;
                dtShowStats=0;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.T) && FilePaths.checkOS()==0){//open debug prompt
            DebugTextInputListener listener = new DebugTextInputListener();
            Gdx.input.getTextInput(listener, "Command", "","");
        }
    }
    static void collisionHandler() {
        ArrayList<Cell> list = getSurroundingCells((int)player.getPos().x,(int)player.getPos().y,1);
        for(Cell c: list){
            //check item
            if(c.hasItem() && c.getItem()!=null){
                c.getItem().colliion((int)c.getPos().x,(int)c.getPos().y);
            }
            //check warp
            if (c.hasWarp()) {
                if (player.getAbilityPoints() != 0) {
                    gsm.push(new AbilitySelectState(gsm));
                }
                player.floor++;
                gm.initializeGrid();
            }
            //check shop
            if (c.getShop()) {
                dispArray[(int)c.getPos().x][(int)c.getPos().y].setShop(false);
                gsm.push(new ShopState(gsm));
            }
        }
        for (Cell c1 : drawList) {
            int x = (int) c1.getPos().x;
            int y = (int) c1.getPos().y;
            Cell c = dispArray[x][y];
            //grid collision---------------------------------------------------
            if (Math.round(player.getPos().x) == x &&Math.round( player.getPos().y) == y) {
                if (c.getWater()&& !player.hasAbility(WaterBreath.class)) {
                    if (dtWater > .2f) {
                        player.addHp(-40);
                        dtWater = 0;
                    }
                }

                if (!c.getState()) {
                    int e = 5 + player.getLevel();
                    if (player.getEnergy() > e) {
                        MapStateUpdater.activateDig();
                        player.setEnergy(player.getEnergy() - e);
                    }else{
                        player.canMove=false;
                        new HoverText("-!-",.5f, Color.YELLOW,player.getAbsPos().x+(player.getIcon().getWidth()/2),player.getAbsPos().y+player.getIcon().getHeight()+10,true);
                    }
                }else{
                    player.canMove=true;
                }
            }
            //pixel collision detection-----------------------------------------
            if (player.getHitBox().overlaps(c.getBounds())) {
                //check walls
            }
        }
    }
    static void setLootPopup(Texture t){
        dtLootPopup = 0;
        lootPopup = t;
    }
    static Vector2 rotateCords(boolean left,Vector2 pos){
        Vector2 v=new Vector2();
        if(left){
            v.x=res-pos.y-1;
            v.y=pos.x;
        }else{
            v.x=pos.y;
            v.y=res-pos.x-1;
        }
        return v;
    }
}
