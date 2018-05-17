package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.abilities.Warp;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.attacks.Dash;
import com.quadx.dungeons.attacks.Protect;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.ShopState;
import com.quadx.dungeons.tools.DebugTextInputListener;
import com.quadx.dungeons.tools.Delta;
import com.quadx.dungeons.tools.FilePaths;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.commandList;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapStateRender.dtWaterEffect;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    static ArrayList<Integer> fpsList= new ArrayList<>();

    private static Delta dDig = new Delta(2*Game.ft);
    private static Delta dStatToggle = new Delta(10*Game.ft);

    private static float dtMap = 0;
    private static float dtShowStats =0;
    private static float dtFPS=0;
    private static float dtClearHits =0;
    public static float dtf=0;
    public static float fps=0;
    static float dtCollision = 0;
    static float dtScrollAtt=0;

    static boolean displayFPS=true;


    public MapStateUpdater(GameStateManager gsm) {
        super(gsm);
    }

    private static void numButtonFunctions(int x){
        if(Gdx.input.isKeyPressed(Input.Keys.TAB) &&Inventory.dtItem >.15){
            Inventory.unequip(x);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.APOSTROPHE)) {
            Attack.showDescription(x);
        }else
        Attack.pos = x;

    }
    public static void pause(){
        pause=true;
        cam.position.set(0, 0, 0);
        gsm.push(new ShopState(gsm));
    }


    //updateMapState methods=============================================
    static void updateVariables(float dt) {
        //nothing before metrics load
        Tests.processMetrics();
        //updateMapState below this line-----------------------
        updateTimers(dt);
        updateAnims(dt);
        updateParticleEffects(dt);
        updateAttackEffects(dt);
        camController.update(dt, cam);
        monsterCollisionHandler();
        player.updateVariables(dt);
        HUD.update();
        MapStateRender.updateVariables(dt);
        if (shakeScreen){
            camController.shakeScreen(20f, 5);
            shakeScreen = false;
        }
    }
    static void updateAttackEffects(float dt) {
        AttackMod.updaterVariables(dt);
        Protect.update(dt);
        Dash.update(dt);
    }
    static void updateAnims(float dt){
        try {
            for (Anim a : Anim.anims) {
                a.update();
            }
        } catch (ConcurrentModificationException e) {
        }
    }
    static void updateParticleEffects(float dt){
  /*      if (effectLoaded)
             effect.updateMapState(Gdx.graphics.getDeltaTime());*/
        for (ParticleEffect e : MapStateExt.effects) {
            e.update(dt);
        }
    }
    static void updateTimers(float dt){
        dStatToggle.update(dt);
        dDig.update(dt);
        if (dtClearHits <= .1)
            dtClearHits += dt;
        if (Inventory.dtItem <= Inventory.itemMinTime)
            Inventory.dtItem += dt;
        if (Attack.dtInfo <= .4)
            Attack.dtInfo += dt;
        if (dtStatPopup <= .4)
            dtStatPopup += dt;
        if (dtMap <= .6)
            dtMap += dt;
        if (dtCollision <= Game.ft / 2)
            dtCollision += dt;
        if (dtScrollAtt <= .3)
            dtScrollAtt += dt;
        if ( Attack.dtAttack <= Attack.attackMintime)
            Attack.dtAttack += dt;
        if (Inventory.dtInvSwitch <= .3)
            Inventory.dtInvSwitch += dt;
        if (dtShowStats <= .2)
            dtShowStats += dt;
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
        dtf += dt;
        dtWaterEffect += dt;
        HUD.dtLootPopup += dt;

        if (Warp.isEnabled())
            Warp.updateTimeCounter();


    }
/*     void updateCamPosition() {
        Vector3 position = cam.position;
        player.fixPosition();
        float[] f = dispArray[(int) player.getPos().x][(int) player.getPos().y].getCorners().getVertices();
        Vector3 disp = new Vector3(f[8], f[9], 0);
        if(snapCam) {
            position.set(player.getAbsPos().x,GridManager.fixHeight(player.getAbsPos()),0);
            snapCam =false;
        }else{
            disp.add(camController.camDisplacement());
            float alpha = camController.isShaking()? .5f:.2f;
            position.lerp(disp,alpha);
        }

        cam.position.set(position);
        cam.updateMapState();
        viewX = cam.position.x - cam.viewportWidth / 2;
        viewY = cam.position.y - cam.viewportHeight / 2;
    }*/
    //===========================================================

    static void buttonHandler() {
        //keyboard functions--------------------------------------------------------
        for(Command c: commandList){
            c.execute();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            numButtonFunctions(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            numButtonFunctions(1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            numButtonFunctions(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            numButtonFunctions(3);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            numButtonFunctions(4);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            numButtonFunctions(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
            numButtonFunctions(6);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            numButtonFunctions(7);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            numButtonFunctions(8);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            numButtonFunctions(9);
        }
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
                MapStateExt.warpToNext(false);
                dtMap = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {//show FPS module
            if(Inventory.dtItem>.2){
                displayFPS=!displayFPS;
                Inventory.dtItem=0;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F3)){
            Tests.calcAvgMapLoadTime();
            Tests.loadEmptyMap();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F4)) {
            gsm.push(new ShopState(gsm));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F5)) {
            if(dStatToggle.isDone()) {
                player.toggleSimpleStats();
                dStatToggle.reset();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F6)) {
            if(dStatToggle.isDone()) {
                player.forceLevelUp();
                dStatToggle.reset();
            }
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
    static void monsterCollisionHandler() {
        try {
            for (Monster m : monsterList) {
                Attack.fixPos();
                Attack.HitBoxShape hbs = player.attackList.get(Attack.pos).getHitBoxShape();
                if (hbs != null)
                    switch (hbs) {
                        case Circle:
                            if (player.getAttackCircle().overlaps(m.getHitBox())) {
                                m.hitByAttack();
                            }
                            break;
                        case Rect:
                            if (player.getAttackBox().overlaps(m.getHitBox())) {
                                m.hitByAttack();
                            }
                            break;
                        case Triangle: {
                            if (player.getAttackTriangle().overlaps(m.getHitBox()))
                                m.hitByAttack();
                            break;
                        }
                    }
            }
        } catch (ConcurrentModificationException e) { }
    }
    static void collisionHandler() {
        ArrayList<Cell> list = getSurroundingCells((int)player.getPos().x,(int)player.getPos().y,1);
        list.removeIf(x-> !(x.hasItem() || x.isWarp() || x.isShop()));
        for(Cell c: list){
            if(c.hasItem()){
                HUD.out("ITEM");
                Item item= c.getItem();
                if(item != null)
                item.colliion(c.getPos());
            }
            if (c.isWarp()) {
                MapStateExt.warpToNext(true);
            }
            if (c.isShop()) {
                MapStateExt.warpToShop();
                c.setCleared();
            }
        }
    }
}
