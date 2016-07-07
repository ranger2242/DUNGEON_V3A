package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.*;
import com.quadx.dungeons.abilities.Investor;
import com.quadx.dungeons.abilities.Warp;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.*;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.liveCellList;
import static com.quadx.dungeons.GridManager.monsterList;
import static com.quadx.dungeons.states.MainMenuState.controller;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    static ArrayList<Integer> fpsList= new ArrayList<>();
    public static boolean displayFPS=true;

    private static float dtDig = 0;
    private static float dtRegen = 0;
    private static float dtEnergyRe = 0;
    private static float dtInfo = 0;
    private static float dtItem = 0;
    private static float dtMap = 0;
    private static float dtShowStats =0;
    public static float dtCollision = 0;
    public static float dtScrollAtt=0;
    public static float dtAttack = 0;
    public static float dtRespawn=0;
    public static float dtFPS=0;
    public static float fps=0;
    public static float dtClearHits =0;
    public static float dtInvSwitch = 0;
    public static int spawnCount=1;

    public MapStateUpdater(GameStateManager gsm) {
        super(gsm);
    }

    private static int checkMove(boolean b2, boolean b3){
        int x;
        if(b2){x=-1;}
        else if(b3){x=1;}
        else{x=0;}
        return x;
    }
    private static void updateCamPosition() {
        Vector3 position = cam.position;
        float lerp = 0.1f;
        position.x += (player.getCordsPX().x - position.x) * lerp;
        position.y += (player.getCordsPX().y - position.y) * lerp;
        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;

    }
    private static void regenPlayer(float dt) {
        dtRegen += dt; //move all this shit to PLAYER
        if (player.safe) player.dtSafe += dt;
        if (dtEnergyRe > .2 && player.getEnergy() < player.getEnergyMax()) {
            player.setEnergy(player.getEnergy() + player.getEnergyRegen());
            if (player.getEnergy() > player.getEnergyMax()) player.setEnergy(player.getEnergyMax());
            dtEnergyRe = 0;
        }
        if (dtRegen > .3) {
            if (AbilityMod.investor)
                Investor.generatePlayerGold();
            if (player.getMana() < player.getManaMax())
                player.setMana(player.getMana() + player.getManaRegenRate());
            if (player.getHp() < player.getHpMax())
                player.setHp(player.getHp() + player.getHpRegen());
            dtRegen = 0;
        }
        player.attackList.stream().filter(a -> a.getMod() == 6 && (player.dtSafe > a.getLevel())).forEach(a -> {
            player.safe = false;
            player.dtSafe = 0;
        });
    }
    private static void getMovementInput(){
        int x=0,y=0;
        boolean up;
        boolean down;
        boolean right;
        boolean left;
        if(controllerMode) {
            up = controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) < -.2;
            down = controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) > .2;
            right = controller.getAxis(Xbox360Pad.AXIS_LEFT_X) > .2;
            left = controller.getAxis(Xbox360Pad.AXIS_LEFT_X) < -.2;
        }else {
            up=Gdx.input.isKeyPressed(Input.Keys.W);
            down=Gdx.input.isKeyPressed(Input.Keys.S);
            right=Gdx.input.isKeyPressed(Input.Keys.D);
            left=Gdx.input.isKeyPressed(Input.Keys.A);
        }
        if (player.canMove) {
            if (up) {y=1;x=checkMove(left,right);}
            if (down){y=-1;x=checkMove(left,right);}
            if (left){x=-1;y=checkMove(down,up);}
            if (right){x=1;y=checkMove(down,up);}
            player.move(x,y);
            player.dtMove = 0;
        }
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
    private static void activateDig(){
        if (dtDig > player.getMoveSpeed()) {
            if (player.getEnergy() > 2) {
                int x = player.getX();
                int y = player.getY();
                gm.clearArea(x, y, true);
                player.setEnergy(player.getEnergy() - 2);
            }
            dtDig = 0;
        }
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
    private static void selectItemFromInventory() {
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
    public static void moveMonsters() {
        for (Monster m : monsterList) {
            m.updateVariables(Gdx.graphics.getDeltaTime());
            if (m.getdtMove() > m.getMoveSpeed()) {
                m.move();
            }
        }
    }
    public static void updateVariables(float dt){
        updateCamPosition();
        regenPlayer(dt);
        player.updateVariables(dt);
        MapStateRender.updateVariables(dt);
        if(dtFPS>.05){
            fps= 1/Gdx.graphics.getDeltaTime();
            fpsList.add((int)fps);
            if(fpsList.size()>50){
                fpsList.remove(0);
            }
            dtFPS=0;
        }else{
            dtFPS+=dt;
        }
        if(dtClearHits<=.1)
            dtClearHits+=dt;
        if(dtRespawn<=10f)
            dtRespawn+=dt;
        if(dtDig<=player.getMoveSpeed())
            dtDig += dt;
        if(dtItem<=itemMinTime)
            dtItem += dt;
        if(dtInfo<=.4)
            dtInfo += dt;
        if(dtStatPopup<=.4)
            dtStatPopup += dt;
        if(dtMap<=.6)
            dtMap += dt;
        if(dtEnergyRe<=.2)
            dtEnergyRe += dt;
        if(dtCollision<=Game.frame/2)
            dtCollision += dt;
        if(dtScrollAtt<=.3)
            dtScrollAtt+=dt;
        if(dtAttack<=attackMintime)
            dtAttack +=dt;
        if(dtInvSwitch<=.3)
            dtInvSwitch += dt;
        if(dtShowStats<=.2)
            dtShowStats+=dt;
        if (Warp.isEnabled()) {
            Warp.updateTimeCounter();
        }
        if (effectLoaded) effect.update(Gdx.graphics.getDeltaTime());
    }
    public static void spawnMonsters(){
        if(dtRespawn>10f) {
            for (int i = 0; i < spawnCount; i++) {
                if(monsterList.size()<150) {
                    Monster m =Monster.getNew();
                    int index = rn.nextInt(liveCellList.size());
                    if (!liveCellList.get(index).getWater() && liveCellList.get(index).getState()) {

                        Cell c = liveCellList.get(index);
                        if (rn.nextBoolean()) m.setHit();
                        m.setCords(c.getX(), c.getY());
                        monsterList.add(m);
                        c.setMon(true);
                        c.setMonsterIndex(monsterList.indexOf(m));
                        liveCellList.set(index, c);
                        m.setMonListIndex(monsterList.indexOf(m));
                        m.setLiveCellIndex(index);
                        //liveCellList.get(index).setMon(true);
                        //liveCellList.get(index).setMonsterIndex(monsterList.indexOf(m));
                        Game.console("MList:" + monsterList.indexOf(m));
                        spawnCount++;
                        MapStateRender.setHoverText("!", .5f, Color.RED, player.getPX(), player.getPY(), true);
                    }
                }
            }
            GridManager.loadLiveCells();
            dtRespawn=0;
        }
    }
    public static void compareItemToEquipment(){
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
    public static void checkPlayerIsAlive() {
        if (player.checkIfDead()) {
            HighScoreState.addScore(player.getScore());
            player = null;
            player = new Player();
            AbilitySelectState.pressed = false;
            inGame = false;
            gsm.push(new HighScoreState(gsm));

        }
    }
    public static void buttonHandler() {
        getMovementInput();
        setAim();
        //controller functions------------------------------------------------------
        if(controllerMode){
            if(controller.getButton(Xbox360Pad.BUTTON_Y)){//use item
                selectItemFromInventory();
            }
            if(controller.getButton(Xbox360Pad.BUTTON_X)){//dig
                activateDig();
            }
        }
        //keyboard functions--------------------------------------------------------
        keyboardAttackSelector();
        if (dtInvSwitch > .3) {
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {//scroll items left
                if(MapStateRender.inventoryPos>0)
                MapStateRender.inventoryPos--;
                else{
                    MapStateRender.inventoryPos=player.invList.size()-1;
                }
                dtInvSwitch = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {//scroll items right
                if(MapStateRender.inventoryPos<player.invList.size()-1)
                    MapStateRender.inventoryPos++;
                else{
                    MapStateRender.inventoryPos=0;
                }
                dtInvSwitch = 0;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {//use item
            selectItemFromInventory();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {//use primary attack
            if (dtAttack > attackMintime) {
                MapStateExt.battleFunctions(lastNumPressed);
                dtAttack = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {//use secondary attack
            if(dtAttack>attackMintime){
                MapStateExt.battleFunctions(altNumPressed);
                dtAttack = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F1) && debug) {//reload map
            if (dtMap > .6) {
                gm.initializeGrid();
                dtMap = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {//to main menu
            if(dtItem>.2){
                displayFPS=!displayFPS;
                dtItem=0;
            }
        }
        /*
        if (Gdx.input.isKeyPressed(Input.Keys.F8) && debug) {//change stats
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)&& player.getGold()>1){
                player.setGold(player.getGold()-1000);
            }else
                player.setGold(player.getGold()+1000);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F9) && debug) {//change stats
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)&& player.getAttack()>1){
                player.setAttack(player.getAttack()-1);
            }else
            player.setAttack(player.getAttack()+1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F10) && debug) {//change stats
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS )&& player.getDefense()>1) {
                player.setDefense(player.getDefense() - 1);
            }else
                player.setDefense(player.getDefense() + 1);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.F11) && debug) {//change stats
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)&& player.getIntel()>1) {
                player.setIntel(player.getIntel() - 1);
            }else
                player.setIntel(player.getIntel() + 1);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.F12) && debug) {//change stats
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)&& player.getSpeed()>1) {
                player.setSpeed(player.getSpeed() - 1);
            }else
                player.setSpeed(player.getSpeed() + 1);
        }*/
        if (Gdx.input.isKeyPressed(Input.Keys.M) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {//dig
            activateDig();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)){//open charater menu
            if(dtShowStats>.2) {
                MapStateRender.showStats = !MapStateRender.showStats;
                dtShowStats=0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {//test shopstate
            pause=true;
            cam.position.set(0, 0, 0);
            gsm.push(new ShopState(gsm));
        }
    }
    public static void setAim(){
        boolean up;
        boolean down;
        boolean right;
        boolean left;
        if(controllerMode){
            up=controller.getAxis(Xbox360Pad.AXIS_RIGHT_Y)<-.2;
            down=controller.getAxis(Xbox360Pad.AXIS_RIGHT_Y)>.2;
            right=controller.getAxis(Xbox360Pad.AXIS_RIGHT_X)>.2;
            left=controller.getAxis(Xbox360Pad.AXIS_RIGHT_X)<-.2;
        }
        else {
            up = Gdx.input.isKeyPressed(Input.Keys.I)||Gdx.input.isKeyPressed(Input.Keys.UP);
            down = Gdx.input.isKeyPressed(Input.Keys.K)||Gdx.input.isKeyPressed(Input.Keys.DOWN);
            right = Gdx.input.isKeyPressed(Input.Keys.L)||Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            left = Gdx.input.isKeyPressed(Input.Keys.J)||Gdx.input.isKeyPressed(Input.Keys.LEFT);
            if(!up && !down && !left && !right) {
                up=Gdx.input.isKeyPressed(Input.Keys.W);
                down=Gdx.input.isKeyPressed(Input.Keys.S);
                right=Gdx.input.isKeyPressed(Input.Keys.D);
                left=Gdx.input.isKeyPressed(Input.Keys.A);
            }
        }
        char c=lastPressed;
        if (up) c='w';
        if (right) c='d';
        if (down) c='s';
        if (left) c='a';
        lastPressed =c;
    }
    public static void collisionHandler() {
        int x=player.getX();
        int y=player.getY();
        Cell c = GridManager.dispArray[x][y];
        int index=liveCellList.indexOf(c);
        if(c .getState())
        if (x == c.getX() && y == c.getY()) {
            if (c.hasLoot()) {
                MapStateRender.dtLootPopup = 0;
                makeGold(player.level);
                liveCellList.get(index ).setHasLoot(false);
                liveCellList.get(index).setItem(null);
                player.lastItem=new Gold();
            }
            if (c.hasCrate()) {
                int x1=liveCellList.get(index).getBoosterItem();
                if(x1==0){
                    player.useItem(new EnergyPlus());}
                else if(x1==1){
                    player.useItem(new Potion());
                }else if(x1==2){
                    player.useItem(new ManaPlus());
                }else{
                    openCrate(index);
                }
                liveCellList.get(index).setBoosterItem(-1);
                liveCellList.get(index).setCrate(false);
                liveCellList.get(index).setItem(null);
            }
            if (c.hasWarp()) {
                player.floor++;
                gm.initializeGrid();
            }
            if (c.getShop()) {
                liveCellList.get(index).setShop(false);
                gsm.push(new ShopState(gsm));
            }
        }
    }

}
