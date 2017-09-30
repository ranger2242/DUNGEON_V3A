package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.*;
import com.quadx.dungeons.abilities.Warp;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.attacks.Dash;
import com.quadx.dungeons.attacks.Protect;
import com.quadx.dungeons.commands.Command;
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

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapStateRender.dtWaterEffect;


/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateUpdater extends MapState{
    static ArrayList<Integer> fpsList= new ArrayList<>();

    private static Delta dDig = new Delta(2*Game.frame);

    private static float dtMap = 0;
    private static float dtShowStats =0;
    public static float endShake=0;
    static float dtCollision = 0;
    static float dtScrollAtt=0;
    static float dtFPS=0;
    public static float dtf=0;
    public static float fps=0;
    static float dtClearHits =0;
    static float dtShake=0;
    public static float force=0;

    static boolean displayFPS=true;


    public MapStateUpdater(GameStateManager gsm) {
        super(gsm);
    }

    private static void updateCamPosition() {
        Vector3 position = cam.position;
        player.fixPosition();
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
    public static void shakeScreen(float time , float f){
        force=f;
        endShake=time;
        shakeCam=true;
        dtShake=0;
    }
    static void updateVariables(float dt) {
        //nothing before metrics load
        Tests.processMetrics();
        //update below this line-----------------------
        //reset inventorypos

        try{
        for(Anim a: Anim.anims){
            a.update();
        }}catch (ConcurrentModificationException e){}
        for(ParticleEffect e:MapStateExt.effects){
            e.update(dt);
        }
        AttackMod.updaterVariables(dt);

        player.updateVariables(dt);
        try {
            for (Monster m : monsterList) {
                Attack.fixPos();
                Attack.HitBoxShape hbs = player.attackList.get(Attack.pos).getHitBoxShape();
                if(hbs != null)
                switch (hbs){

                    case Circle:
                        if ( player.getAttackCircle().overlaps(m.getHitBox())) {
                            m.hitByAttack();
                        }
                        break;
                    case Rect:
                        if ( player.getAttackBox().overlaps(m.getHitBox())) {
                            m.hitByAttack();
                        }
                        break;
                    case Triangle:{
                        if(player.getAttackTriangle().overlaps(m.getHitBox()))
                        m.hitByAttack();
                        break;
                    }
                }

            }
        } catch (ConcurrentModificationException e) {
        }

        MapStateRender.updateVariables(dt);
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
        //dtDig+=dt;
        dtWaterEffect+=dt;
        HUD.dtLootPopup +=dt;

        //System.out.println(EMath.randomGaussianAverage(100,20)+"");
        Protect.update(dt);
        Dash.update(dt);

        if (dtClearHits <= .1)
            dtClearHits += dt;



        dDig.update(dt);
        if (Inventory.dtItem <= Inventory.itemMinTime)
            Inventory.dtItem += dt;

        if (Attack.dtInfo <= .4)
            Attack.dtInfo += dt;
        if (dtStatPopup <= .4)
            dtStatPopup += dt;
        if (dtMap <= .6)
            dtMap += dt;
        if (dtCollision <= Game.frame / 2)
            dtCollision += dt;
        if (dtScrollAtt <= .3)
            dtScrollAtt += dt;
        if ( Attack.dtAttack <= Attack.attackMintime)
            Attack.dtAttack += dt;
        if (Inventory.dtInvSwitch <= .3)
            Inventory.dtInvSwitch += dt;
        if (dtShowStats <= .2)
            dtShowStats += dt;
            dtShake+=dt;

        if(dtShake>endShake){
            shakeCam=false;
        }
        if (Warp.isEnabled()) {
            Warp.updateTimeCounter();
        }

        if (effectLoaded)
            effect.update(Gdx.graphics.getDeltaTime());
        //put hud update positions here
        updateCamPosition();
        HUD.update();
    }
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
        }        if(Gdx.input.isKeyPressed(Input.Keys.B)){
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
        list.removeIf(x-> !(x.hasItem() || x.hasWarp() || x.hasShop()));
        for(Cell c: list){
            if(c.hasItem()){
                c.getItem().colliion(c.getPos());
            }
            if (c.hasWarp()) {
                MapStateExt.warpToNext();
            }
            if (c.hasShop()) {
                MapStateExt.warpToShop();
            }
        }
    }
}
