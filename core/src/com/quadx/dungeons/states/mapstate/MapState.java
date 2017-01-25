package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpeedPlus;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.tools.HoverText;
import com.quadx.dungeons.tools.ShapeRendererExt;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.states.mapstate.MapStateRender.inventoryPos;
import static com.quadx.dungeons.states.mapstate.MapStateRender.renderLayers;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.*;


/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapState extends State implements ControllerListener {
    static final boolean debug=true;
    public static boolean noLerp=false;
    public static boolean pause=false;

    static ShapeRendererExt shapeR;
    static ArrayList<String> output;
    static final ArrayList<Cell> hitList=new ArrayList<>();
     public static boolean showStats=true;

    static Texture lootPopup;
    public static Texture statPopup;
    public static GridManager gm;
    static ParticleEffect effect;
    static final Random rn = new Random();
    static Attack attack;
    static Attack attack2;
    public static boolean inGame=false;
    static boolean effectLoaded = false;
    static final String DIVIDER= "_________________________";
    public static int cellW=30;
    public static Vector2 cell = new Vector2(cellW,cellW*(2f/3f));
    public static Vector2 warp=new Vector2();
    public static Vector2 shop=new Vector2();
    public static float dtStatPopup=0;
    public static float viewX;
    public static float viewY;
    static final float itemMinTime=.15f;
    static final float attackMintime = Game.frame*10;
    static int lastNumPressed=0;
    static int altNumPressed=1;


    public MapState(GameStateManager gsm) {
        super(gsm);
        StatManager.gameTime.start();
        StatManager.reset();
        player.loadIcons();
        player.loadAttacks();
        player.getAbility().onActivate();
        inGame=true;
        gm = new GridManager();
        shapeR = new ShapeRendererExt();
        output= new ArrayList<>();
        if(controllerMode)
        MainMenuState.controller.addListener(this);
        bufferOutput();
        Game.setFontSize(1);
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        gm.initializeGrid();
        out("---Welcome to DUNGEON---");
        attack= player.attackList.get(0);
        attack2=player.attackList.get(0);
        debug();
    }
    public void debug() {
        //Tests.testEquipmentRates();
       Tests.giveItems(50);
    }
    public void handleInput() {
    }
    public void update(float dt) {
        MapStateUpdater.updateVariables(dt);
        GridManager.loadDrawList();
        MapStateUpdater.compareItemToEquipment();
        if(!Tests.nospawn) {
            if(dtRespawn>10f){
                MapStateUpdater.spawnMonsters(MapStateUpdater.spawnCount);
            }
        }
        MapStateUpdater.buttonHandler();
        if(MapStateUpdater.dtCollision>Game.frame/2) {
            if(!player.jumping)
            MapStateUpdater.collisionHandler();
        }
        MapStateUpdater.moveMonsters();
        player.checkIfDead(gsm);
        HUD.create();
    }
    public void render(SpriteBatch sb) {
        renderLayers(sb);
    }
    public void dispose() {
    }
    public static void out(String s){
        if(output != null) {
            output.add(s);
            if (output.size() > 10) output.remove(0);
        }
    }

    static void attackCollisionHandler2(int pos) {
        Attack attack = player.attackList.get(pos);
        StatManager.shotFired(attack);
        player.attackList.get(pos).setUses();
        player.setAttackBox(attack.calculateHitBox());
    }
    private static void setHitList(int x, int y){
            hitList.add(dispArray[x][y]);
    }
    public static void makeGold(int x) {
        float f = rn.nextFloat();
        while (f < .05) {
            f = rn.nextFloat();
        }
        int gold = (int) ((f) * 100) * x;
        if (gold < 0) gold = 1;
        if (gold > 1000) {
            int exp = gold - 1000;
            gold = 1000;
            //MapStateRender.setHoverText(exp + "EXP", .5f, Color.GREEN, player.getPX(), player.getPY() - 20, false);

        }
        player.setGold(player.getGold() + gold);
        StatManager.totalGold += gold;
        out(player.getName() + " recieved " + gold + "G");
        new HoverText(gold + "G", .5f, Color.GOLD, player.getAbsPos().x, player.getAbsPos().y, false);
    }
    public static void openCrate(Item item) {
        if(item.getClass().equals(Gold.class)){
            Gold g = (Gold) item;
            player.setGold(player.getGold() + g.getValue());
            out(g.getValue() + " added to stash");
            StatManager.totalGold+=g.getValue();
            new HoverText(g.getValue() + "G", 1, Color.GOLD, player.getAbsPos().x,player.getAbsPos().y,false);

        }else {
            if (item != null) {
                if (item.isEquip) {
                    item.loadIcon(item.getType());
                } else if (item.getIcon() == null)
                    item.loadIcon(item.getName());

                try {
                    if (item.getClass() != Gold.class) {
                        boolean a = false;
                        if (player.invList.isEmpty()) {
                            a = true;
                        }
                        player.addItemToInventory(item);
                        if (a) inventoryPos = 0;
                        out(item.getName() + " added to inventory");
                        new HoverText(item.getName(), 1, Color.WHITE, player.getAbsPos().x,player.getAbsPos().y, false);
                    } else {
                        player.lastItem = new Gold();
                    }
                } catch (NullPointerException e) {
                    item = new SpeedPlus();
                    boolean a = false;
                    if (player.invList.isEmpty()) {
                        a = true;
                    }
                    player.addItemToInventory(item);
                    if (a) inventoryPos = 0;
                    out(item.getName() + " added to inventory");
                    new HoverText(item.getName(), 1, Color.WHITE, player.getAbsPos().x, player.getAbsPos().y, false);
                }
            }
        }
        setLootPopup(item.getIcon());
        StatManager.totalItems++;
    }
    private void bufferOutput(){
        for(int i=0;i<10;i++)
            out("");
    }
    public static void scrollAttacks(boolean right){
        if (dtScrollAtt > .3) {
            if (right) {
                if (inventoryPos < player.invList.size() - 1)
                    inventoryPos++;
                else inventoryPos = 0;
                MapStateUpdater.dtInvSwitch = 0;
            } else {
                if (inventoryPos > 0)
                    inventoryPos--;
                else inventoryPos = player.invList.size() - 1;
                MapStateUpdater.dtInvSwitch = 0;
            }
        }
    }
    //-----------------------------------------------------------------------------------------
    //Controller Interface
    public void connected(Controller controller) {

    }
    public void disconnected(Controller controller) {

    }
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Xbox360Pad.updateSticks(axisCode,value);
        return false;

   }
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Xbox360Pad.updatePOV(value);
        return false;
    }
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
