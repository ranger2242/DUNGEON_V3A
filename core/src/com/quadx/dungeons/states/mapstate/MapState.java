package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.*;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpeedPlus;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapStateRender.renderLayers;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtAttack;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtScrollAtt;


/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapState extends State implements ControllerListener {
    static final boolean debug=true;
    public static boolean pause=false;

    static ShapeRenderer shapeR;
    static ArrayList<String> output;
    static final ArrayList<Cell> hitList=new ArrayList<>();

    static Texture lootPopup;
    public static Texture statPopup;
    public static GridManager gm;
    static ParticleEffect effect;
    static final Random rn = new Random();
    static Attack attack;
    static Attack attack2;
    public static boolean inGame=false;
    public static char lastPressed = 'w';
    static boolean effectLoaded = false;
    static final String DIVIDER= "_________________________";
    public static final int cellW=30;
    public static int warpX=0;
    public static int warpY=0;
    public static float dtStatPopup=0;
    public static float viewX;
    public static float viewY;
    static final float itemMinTime=.4f;
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
        shapeR = new ShapeRenderer();
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
        //Tests.giveItems(20);
    }
    public void handleInput() {
    }
    public void update(float dt) {
        MapStateUpdater.updateVariables(dt);
        GridManager.loadDrawList();
        MapStateUpdater.compareItemToEquipment();
        if(!Tests.nospawn)
            MapStateUpdater.spawnMonsters();
        MapStateUpdater.buttonHandler();
        if(MapStateUpdater.dtCollision>Game.frame/2) {
            MapStateUpdater.collisionHandler();
        }
        MapStateUpdater.moveMonsters();
        MapStateUpdater.checkPlayerIsAlive();
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
    static void calculateHitBox(Attack attack){
        if(attack.getMod()==10){//check earthquake
            hitList.clear();
            for(Cell c:drawList){
                setHitList(c.getX(),c.getY());
            }
        }else if(attack.getType()==4){
            for (int i = player.getX()-attack.getSpread()/2; i < player.getX()+attack.getSpread()/2; i++) {
                for (int j = player.getY()-attack.getSpread()/2; j < player.getY()+attack.getSpread()/2; j++) {
                    if(i<res && i>=0 && j<res && j>=0)
                        setHitList(i, j);
                }
            }
        }
        else {
            int xlim = 0, ylim = 0, x = 0, y = 0, a = 0, b = 0;
            int range = attack.getRange();
            int spread = attack.getSpread();
            int px = player.getX();
            int py = player.getY();
            if (lastPressed == 'w') {
                xlim = (px) + spread;
                ylim = py + range;
                x = px;
                y = py;
                a = (int) -Math.ceil(spread / 2);
                b = 0;
            } else if (lastPressed == 's') {
                xlim = (px) + spread;
                ylim = py;
                x = px;
                y = py - range;
                a = (int) -Math.ceil(spread / 2);
                b = 0;
            } else if (lastPressed == 'd') {
                xlim = (px) + range;
                ylim = py + spread;
                x = px;
                y = py;
                a = 0;
                b = (int) -Math.ceil(spread / 2);
            } else if (lastPressed == 'a') {
                xlim = px;
                ylim = py + spread;
                x = (px) - range;
                y = py;
                a = 0;
                b = (int) -Math.ceil(spread / 2);
            }
            for (int i = x; i < xlim; i++) {
                for (int j = y; j < ylim; j++) {
                    int n=i+a;
                    int m=j+b;
                    if(n<res && n>=0 && m<res && m>=0)
                    setHitList(n, m);
                }
            }
        }
        hitList.add(dispArray[player.getX()][player.getY()]);
    }
    static void attackCollisionHandler(int pos) {
        Attack attack = player.attackList.get(pos);
        StatManager.shotFired(attack);
        hitList.clear();
        player.attackList.get(pos).setUses();
        calculateHitBox(attack);
        boolean hit = false;
        for (Cell c : hitList) {
            c.setAttArea(true);
            if (c.getMonster() != null) {//monster was hit
                hit = true;
                Monster m = c.getMonster();
                m.takeEffect(attack);
                m.takeAttackDamage(Damage.calcPlayerDamage(attack, m));
                m.checkIfDead();
            }
        }
        if(AttackMod.sacrifice)
            AttackMod.sacrifice=false;
        StatManager.shotMissed(hit);
        Monster.reindexMons=true;
        loadLiveCells();
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
            player.setExp(player.getExp() + exp);
            MapStateRender.setHoverText(exp + "EXP", .5f, Color.GREEN, player.getPX(), player.getPY() - 20, false);

        }
        player.setGold(player.getGold() + gold);
        StatManager.totalGold += gold;
        out(player.getName() + " recieved " + gold + "G");
        MapStateRender.setHoverText(gold + "G", .5f, Color.GOLD, player.getPX(), player.getPY(), false);
    }
    public static void openCrate(int index) {
        if (liveCellList.get(index) != null && liveCellList.get(index).getItem() != null)
            if (liveCellList.get(index).getItem().getClass() == Gold.class) {
                lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
                player.setGold(player.getGold() + liveCellList.get(index).getGold());
                Gold g = (Gold) liveCellList.get(index).getItem();
                out(g.getValue() + " added to stash");
                StatManager.totalGold+=g.getValue();
                MapStateRender.setHoverText(g.getValue() + "G", 1, Color.GOLD, player.getPX(), player.getPY(), false);
            } else {
                Item item = liveCellList.get(index).getItem();
                if (item != null) {
                    if (item.isEquip) {
                        item.loadIcon(item.getType());
                    } else
                        item.loadIcon(item.getName());
                    try {
                        lootPopup = item.getIcon();
                        MapStateRender.dtLootPopup = 0;
                    } catch (GdxRuntimeException | NullPointerException e) {
                        Game.printLOG(e);
                    }
                    try {
                        if (item.getClass() != Gold.class) {
                            boolean a = false;
                            if (player.invList.isEmpty()) {
                                a = true;
                            }
                            player.addItemToInventory(item);
                            if (a) MapStateRender.inventoryPos = 0;
                            out(item.getName() + " added to inventory");
                            MapStateRender.setHoverText(item.getName(), 1, Color.WHITE, player.getPX(), player.getPY(), false);
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
                        if (a) MapStateRender.inventoryPos = 0;
                        out(item.getName() + " added to inventory");
                        MapStateRender.setHoverText(item.getName(), 1, Color.WHITE, player.getPX(), player.getPY(), false);
                    }
                }
            }
        StatManager.totalItems++;
    }

    private void bufferOutput(){
        for(int i=0;i<10;i++)
            out("");
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Xbox360Pad.updateSticks(axisCode,value);
        if(axisCode==Xbox360Pad.AXIS_RIGHT_TRIGGER) {
            if (value > -.5f) {
                if (dtAttack > attackMintime) {
                    MapStateExt.battleFunctions(lastNumPressed);
                    dtAttack = 0;
                }
            }
        }
        if(axisCode==Xbox360Pad.AXIS_LEFT_TRIGGER) {
            if (value < .5f) {
                if (dtAttack > attackMintime) {
                    MapStateExt.battleFunctions(altNumPressed);
                    dtAttack = 0;
                }
            }
        }
        return false;

   }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {

        if(dtScrollAtt>.3) {
            if (value == Xbox360Pad.BUTTON_DPAD_LEFT) {
                if(MapStateRender.inventoryPos>0)
                MapStateRender.inventoryPos--;
                else MapStateRender.inventoryPos=player.invList.size()-1;
                MapStateUpdater.dtInvSwitch = 0;
            }
            if (value == Xbox360Pad.BUTTON_DPAD_RIGHT) {
                if(MapStateRender.inventoryPos<player.invList.size()-1)
                MapStateRender.inventoryPos++;
                else MapStateRender.inventoryPos=0;
                MapStateUpdater.dtInvSwitch = 0;
            }
            if (value == Xbox360Pad.BUTTON_DPAD_UP) {
                if (altNumPressed < player.attackList.size()-1)
                    altNumPressed++;
                else altNumPressed = 0;
                attack2=player.attackList.get(altNumPressed);
            }
            if (value == Xbox360Pad.BUTTON_DPAD_DOWN) {
                if (lastNumPressed < player.attackList.size()-1)
                    lastNumPressed++;
                else lastNumPressed = 0;
                attack=player.attackList.get(lastNumPressed);
            }
        }
        return false;




    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
