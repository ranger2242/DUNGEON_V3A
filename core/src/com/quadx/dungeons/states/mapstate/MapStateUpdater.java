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
import static com.quadx.dungeons.GridManager.liveCellList;
import static com.quadx.dungeons.states.MainMenuState.controller;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    private static float dtRun = 0;
    private static float dtDig = 0;
    private static float dtRegen = 0;
    private static float dtEnergyRe = 0;
    private static float dtInfo = 0;
    private static float dtItem = 0;
    private static float dtMap = 0;
    public static float dtWater = 0;
    public static float dtHovText = 0;
    public static float dtCollision = 0;
    public static float dtScrollAtt=0;
    public static float dtAttack = 0;
    public static float dtInvSwitch = 0;


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
    public static void updateCamPosition() {
        Vector3 position = cam.position;
        float lerp = 0.1f;
        position.x += (player.getCordsPX().x - position.x) * lerp;
        position.y += (player.getCordsPX().y - position.y) * lerp;
        cam.position.set(position);
        cam.update();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;

    }
    public static void regenPlayer(float dt) {
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
        if (player.dtSafe > Protect.time[player.attackList.get(4).getLevel()]) {
            player.safe = false;
            player.dtSafe = 0;
        }
    }
    public static void fuckingStupidUpdateFunction(float dt) {
        updateCamPosition();
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
        dtAttack +=dt;
        dtInvSwitch += dt;
        //find coordinates to draw on screen by finding four corners of screen on grid
        GridManager.loadDrawList();
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
        int x=0,y=0;
        char c='t';
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
            if (up) {y=1;c='w';x=checkMove(left,right);}
            if (down){y=-1;c='s';x=checkMove(left,right);}
            if (left){x=-1;c='a';y=checkMove(down,up);}
            if (right){x=1;c='d';y=checkMove(down,up);}
            player.move(x,y,c);
            player.dtMove = 0;
        }
    }
    static int checkMove(boolean b2, boolean b3){
        int x;
        if(b2){x=-1;}
        else if(b3){x=1;}
        else{x=0;}
        return x;
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
            up = Gdx.input.isKeyPressed(Input.Keys.I);
            down = Gdx.input.isKeyPressed(Input.Keys.K);
            right = Gdx.input.isKeyPressed(Input.Keys.L);
            left = Gdx.input.isKeyPressed(Input.Keys.J);
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
                lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
                makeGold(player.level);
                liveCellList.get(index ).setHasLoot(false);
            }
            if (c.hasCrate()) {
                openCrate();
                liveCellList.get(index).setCrate(false);
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
