package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.*;
import com.quadx.dungeons.abilities.Investor;
import com.quadx.dungeons.abilities.Warp;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Protect;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.*;
import com.quadx.dungeons.tools.Score;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.MainMenuState.controller;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    static boolean moveLeft;
    static boolean moveRight;
    static boolean moveUp;
    static boolean moveDown;

    public static float dtWater = 0;
    public static float dtHovText = 0;
    public static float dtCollision = 0;
    public static float dtScrollAtt=0;
    private static float dtRun = 0;
    private static float dtDig = 0;
    private static float dtMove = 0;
    public static float dtAttack = 0;
    private static float dtRegen = 0;
    private static float dtEnergyRe = 0;
    private static float dtInfo = 0;
    private static float dtItem = 0;
    public static float dtInvSwitch = 0;
    private static float dtMap = 0;


    public MapStateUpdater(GameStateManager gsm) {
        super(gsm);
    }
    public static void moveMonsters() {
        for (Monster m : GridManager.monsterList) {
            m.updateVariables(Gdx.graphics.getDeltaTime());
            if (m.getdtMove() > m.getMoveSpeed()) {
                m.move();
            }
        }
    }
    static void updateMousePosition() {
        mouseX = Gdx.input.getX();
        mouseY = Gdx.input.getY();
        mouseRealitiveX = (int) (mouseX + viewX);
        mouseRealitiveY = (int) (Game.HEIGHT - mouseY + viewY);
    }
    public static void updateCamPosition() {
        Vector3 position = cam.position;
        float lerp = 0.2f;
        position.x += (player.getCordsPX().x - position.x) * lerp;
        position.y += (player.getCordsPX().y - position.y) * lerp;
        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;

    }
    public static void regenPlayer(float dt) {
        dtRegen += dt; //move all this shit to PLAYER
        dtMove+=dt;
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
        if (player.dtSafe > Protect.time[player.attackList.get(4).getLevel()]) {
            player.safe = false;
            player.dtSafe = 0;
        }
    }
    public static void fuckingStupidUpdateFunction(float dt) {
        updateCamPosition();
        updateMousePosition();
        regenPlayer(dt);
        player.updateVariables(dt);
        MapStateRender.updateVariables(dt);
        dtDig += dt;
        dtRun += dt;
        dtItem += dt;
        dtInfo += dt;
        dtStatPopup += dt;
        dtDamageTextFloat += dt;
        dtMap += dt;
        dtMessage += dt;
        dtEnergyRe += dt;
        dtCollision += dt;
        dtScrollAtt+=dt;
        dtMove +=dt;
        dtAttack +=dt;
        dtInvSwitch += dt;


        /*if(MapStateRender.inventoryPos>player.invList.size()-1){
            MapStateRender.inventoryPos=player.invList.size()-1;
        }*/
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
        //out(player.invList.size()+" "+MapStateRender.inventoryPos);
        if (Warp.isEnabled()) {
            Warp.updateTimeCounter();
        }

        if (effectLoaded) effect.update(Gdx.graphics.getDeltaTime());
        MapStateExt.mouseOverHandler();
        if (player.checkIfDead()) {
            HighScoreState.addScore(new Score(player.getPoints(), player.getGold(), player.getName(), AbilityMod.ability.getName() + " Lvl " + player.level, player.getKills()));
            player = null;
            player = new Player();
            AbilitySelectState.pressed = false;
            inGame = false;
            gsm.push(new HighScoreState(gsm));
        }
    }
    static void getMovementInput(){
        if(controllerMode){
            if (dtMove > player.getMoveSpeed()) {
                if (controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) < -.2) {
                    if(controller.getAxis(Xbox360Pad.AXIS_LEFT_X) < -.2){movementHandler(-1, 1, 'w');}
                    else if(controller.getAxis(Xbox360Pad.AXIS_LEFT_X) > .2){movementHandler(1, 1, 'w');}
                    else{movementHandler(0, 1, 'w');}
                }
                if (controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) > .2){
                    if(controller.getAxis(Xbox360Pad.AXIS_LEFT_X) < -.2){movementHandler(-1, -1, 's');}
                    else if(controller.getAxis(Xbox360Pad.AXIS_LEFT_X) > .2){movementHandler(1, -1, 's');}
                    else{movementHandler(0, -1, 's');}
                }
                if (controller.getAxis(Xbox360Pad.AXIS_LEFT_X) < -.2){
                    if(controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) < -.2){movementHandler(-1, 1, 'a');
                    }else if(controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) > .2){movementHandler(-1, -1, 'a');}
                    else{movementHandler(-1, 0, 'a');}
                }
                if (controller.getAxis(Xbox360Pad.AXIS_LEFT_X) > .2){
                    if(controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) < -.2){movementHandler(1, 1, 'd');}
                    else if(controller.getAxis(Xbox360Pad.AXIS_LEFT_Y) > .2){movementHandler(1, -1, 'd');}
                    else{movementHandler(1, 0, 'd');}
                    moveLeft=false;
                }
                dtMove = 0;
            }
        }
        else{
            if (dtMove > player.getMoveSpeed()) {
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    if(Gdx.input.isKeyPressed(Input.Keys.A)){movementHandler(-1, 1, 'w');}
                    else if(Gdx.input.isKeyPressed(Input.Keys.D)){movementHandler(1, 1, 'w');}
                    else{movementHandler(0, 1, 'w');}
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S)){
                    if(Gdx.input.isKeyPressed(Input.Keys.A)){movementHandler(-1, -1, 's');}
                    else if(Gdx.input.isKeyPressed(Input.Keys.D)){movementHandler(1, -1, 's');}
                    else{movementHandler(0, -1, 's');}
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A)){
                    if(Gdx.input.isKeyPressed(Input.Keys.W)){movementHandler(-1, 1, 'a');}
                    else if(Gdx.input.isKeyPressed(Input.Keys.S)){movementHandler(-1, -1, 'a');}
                    else{movementHandler(-1, 0, 'a');}
                }
                if (Gdx.input.isKeyPressed(Input.Keys.D)){
                    if(Gdx.input.isKeyPressed(Input.Keys.W)){movementHandler(1, 1, 'd');}
                    else if(Gdx.input.isKeyPressed(Input.Keys.S)){movementHandler(1, -1, 'd');}
                    else{movementHandler(1, 0, 'd');}
                    moveLeft=false;
                }
                dtMove = 0;
            }
        }
    }
    static void keyboardAttackSelector(){
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
    public static void buttonHandler() {
        getMovementInput();
        setAim();
        //controller functions------------------------------------------------------
        if(controllerMode){
            if(controller.getButton(Xbox360Pad.BUTTON_Y)){//use item
                selectItemFromInventory(0);
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
            selectItemFromInventory(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {//to main menu
            gsm.push(new MainMenuState(gsm));
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
        if (Gdx.input.isKeyPressed(Input.Keys.T)) {//test crates
            MapState.openCrate();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {//dig
            activateDig();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) {//test shopstate
            cam.position.set(0, 0, 0);
            gsm.push(new ShopState(gsm));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {//reload map
            if (dtMap > .6) {
                gm.initializeGrid();
                dtMap = 0;
            }
        }
    }
    static void activateDig(){
        if (dtDig > player.getMoveSpeed()) {
            if (player.getEnergy() > 5) {
                int x = player.getX();
                int y = player.getY();
                gm.clearArea(x, y, true);
                player.setEnergy(player.getEnergy() - 2);
            }
            dtDig = 0;
        }
    }
    private static  void setAttackButton(int x){
        if(Gdx.input.isKeyPressed(Input.Keys.MINUS)){
            altNumPressed=x;
            attack2 = player.attackList.get(attackListCount + x);
        }
        else numberButtonHandler(x);
    }
    private static void selectItemFromInventory(int i) {
        if (dtItem > itemMinTime && player.invList.size() > i) {            //check if cooldown is over
            player.useItem(MapStateRender.inventoryPos + i);                //actually use the item
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
            if (dtInfo > .4) {
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
                    attack = player.attackList.get(attackListCount + i);
                }catch (IndexOutOfBoundsException e){}
            }
        }
    }
    public static void movementHandler(int xmod, int ymod, char c) {
        int x = player.getPX() + xmod * cellW;
        int y = player.getPY() + ymod * cellW;
        for (Cell cell : GridManager.liveCellList) {
            if(dtMove>player.getMoveSpeed())
            if (cell.getY() * cellW == y && cell.getX() * cellW == x && !cell.getWater()) {
                player.setCordsPX(x, y);
                player.setLiveListIndex(GridManager.liveCellList.indexOf(cell));
                dtMove=0;
            }
        }
        lastPressed = c;
        setAim();

    }
    static void setAim(){
        if(controllerMode){
            if (controller.getAxis(Xbox360Pad.AXIS_RIGHT_X)<-.2) setFront('a' , player.getX(), player.getY());
            if (controller.getAxis(Xbox360Pad.AXIS_RIGHT_X)>.2) setFront('d',player.getX(), player.getY());
            if (controller.getAxis(Xbox360Pad.AXIS_RIGHT_Y)<-.2) setFront('w',player.getX(), player.getY());
            if (controller.getAxis(Xbox360Pad.AXIS_RIGHT_Y)>.2) setFront('s',player.getX(), player.getY());
        }
        else {
            if (Gdx.input.isKeyPressed(Input.Keys.J)) setFront('a' , player.getX(), player.getY());
            if (Gdx.input.isKeyPressed(Input.Keys.L)) setFront('d',player.getX(), player.getY());
            if (Gdx.input.isKeyPressed(Input.Keys.I)) setFront('w',player.getX(), player.getY());
            if (Gdx.input.isKeyPressed(Input.Keys.K)) setFront('s',player.getX(), player.getY());
        }
    }
    public static void collisionHandler() {
        int index = player.getLiveListIndex();
        Cell c = GridManager.liveCellList.get(index);
        if (player.getX() == c.getX() && player.getY() == c.getY()) {
            if (c.hasLoot()) {
                MapStateRender.dtLootPopup = 0;
                lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
                makeGold(player.level);
                GridManager.liveCellList.get(index).setHasLoot(false);
            }
            if (c.hasCrate()) {
                openCrate();
                GridManager.liveCellList.get(index).setCrate(false);
            }
            if (c.hasWarp()) {
                player.floor++;
                gm.initializeGrid();
            }
            if (c.getShop()) {
                GridManager.liveCellList.get(index).setShop(false);
                gsm.push(new ShopState(gsm));
            }
        }
    }

}
