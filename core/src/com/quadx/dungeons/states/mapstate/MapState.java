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
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpeedPlus;
import com.quadx.dungeons.monsters.MonAIv1;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.State;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
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
    private static final int attackListCount = 0;
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
        player.loadIcons();
        player.loadAttacks();
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

        int x3=26;
        for(int i=0; i<x3;i++){
            for(int j=0;j<x3; j++){
                System.out.print((i*j)% x3+"\t");
            }
            System.out.print("\n");
        }

       /* for(int i=0;i<20;i++){
       //    openCrate();
    //         player.addItemToInventory(new Arms());
        }*/
    }
    public void handleInput() {
    }
    public void update(float dt) {
        MapStateUpdater.updateVariables(dt);
        GridManager.loadDrawList();
        MapStateUpdater.compareItemToEquipment();
        MapStateUpdater.spawnMonsters();
        MapStateUpdater.buttonHandler();
        if(MapStateUpdater.dtCollision>Game.frame/2) {
            MapStateUpdater.collisionHandler();
        }
        MapStateUpdater.moveMonsters();
        MapStateUpdater.checkPlayerIsAlive();
    }
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        MapStateRender.drawTiles(sb);
        MapStateRender.drawTransparentThings();
        MapStateRender.drawHUD(sb);
        MapStateRender.drawAbilityIcon(sb);
        MapStateRender.drawMessageOutput(sb);
        MapStateRender.drawEquipment(sb);
        MapStateRender.drawPlayer(sb);
        MapStateRender.drawHovText(sb);
        MapStateRender.drawMiniMap();
        if(MapStateRender.showCircle)
            MapStateRender.drawPlayerFinder();

        for(Monster m: GridManager.monsterList)
            MonAIv1.callForHelp(m);

        sb.begin();
        for(Monster m: GridManager.monsterList){
            Texture t=m.getIcon();
                    sb.draw(t, m.getX() * cellW -t.getWidth()/4, m.getY() * cellW -t.getHeight()/4);
        }
        sb.end();
    }
    public void dispose() {
    }
    public static void out(String s){
        if(output != null) {
            output.add(s);
            if (output.size() > 10) output.remove(0);
        }
    }

    static void attackCollisionHandler(int pos, Attack att) {
        int range = player.attackList.get(attackListCount + pos).getRange();
        int spread = player.attackList.get(attackListCount + pos).getSpread();
        int px = player.getX();
        int py = player.getY();
        int xrange;
        int yrange;
        hitList.clear();
        if(player.attackList.get(pos).getType()==3) {
            player.attackList.get(pos).setUses();
            player.attackList.get(pos).checkLvlUp();
        }
        hitList.add(dispArray[player.getX()][player.getY()]);
        if(lastPressed=='w') {
            xrange = (px) + spread;
            yrange = py + range;
            for (int i = px; i < xrange; i++) {
                for (int j = py; j < yrange; j++) {
                    setHitList((int) (i - Math.ceil(spread / 2)),j);
                }
            }
        }
        else if(lastPressed=='s'){
            xrange = (px) + spread;
            yrange = py - range;
            for (int i = px; i < xrange; i++) {
                for (int j = yrange; j < py; j++) {
                    setHitList((int) (i - Math.ceil(spread / 2)),j);
                }
            }
        }
        else if(lastPressed=='d'){
            xrange = (px) + range;
            yrange = py + spread;
            for (int i = px; i < xrange; i++) {
                for (int j = py; j < yrange; j++) {
                    setHitList(i,(int)(j - Math.ceil(spread / 2)));
                }
            }
        }
        else if(lastPressed=='a'){
            xrange = (px) - range;
            yrange = py + spread;
            for (int i = xrange; i < px; i++) {
                for (int j = py; j < yrange; j++) {
                    setHitList((i),(int)(j - Math.ceil(spread / 2)));
                }
            }
        }
        Monster tempMon=null;
        boolean killed= false;
        if(att.getMod()==10){//check earthquake

            hitList.clear();
            for(Cell c:drawList){
                setHitList(c.getX(),c.getY());
            }
        }
        for(Cell c:hitList)    {
            try {
                drawList.get(drawList.indexOf(c)).setAttArea(true);
            }catch (ArrayIndexOutOfBoundsException ignored){}
            liveCellList.get(liveCellList.indexOf(c)).setAttArea(true);
            dispArray[c.getX()][c.getY()].setAttArea(true);
            for(Monster m: GridManager.monsterList){
                if(c.getX()==m.getX() && c.getY()==m.getY()){
                    int tempMonIndex;
                    tempMon=m;
                    tempMonIndex=GridManager.monsterList.indexOf(m);
                    player.setDamage(Damage.playerMagicDamage(player, m, player.attackList.get(pos).getPower()));
                    out("Hit "+player.getDamage()+" damage");
                    if(player.attackList.get(pos).getType() !=3) {

                        player.attackList.get(pos).setUses();
                        player.attackList.get(pos).checkLvlUp();
                    }
                    //displayPlayerDamage = true;
                    MapStateRender.setHoverText("-"+player.getDamage(),.8f,Color.RED,m.getPX(),m.getPY(),true);
                    m.takeAttackDamage(player.getDamage());
                    monsterList.get(tempMonIndex).setHit();

                    if (m.getHp() < 1) {
                        out(DIVIDER);
                        out(m.getName() + " Level " + m.getLevel() + " was killed.");
                        c.clearMonster();
                        player.addKills();
                        player.setExp(m);
                        player.checkLvlUp();
                        makeGold(m.getLevel());
                        try {
                            dispArray[m.getX()][m.getY()].setMon(false);
                        }catch (NullPointerException |ArrayIndexOutOfBoundsException e){
                            //Game.printLOG(e);
                        }
                        tempMon = m;
                        killed=true;
                    }
                }
            }
            if(tempMon !=null && killed){
                GridManager.monsterList.remove(tempMon);
            }
        }
        for(Monster m:monsterList){
            m.setMonListIndex(monsterList.indexOf(m));
        }
        loadLiveCells();
    }

    private static void setHitList(int x, int y){
        try {
            hitList.add(dispArray[x][y]);
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            Game.printLOG(e);
        }
    }

    static void makeGold(int x){
        float f= rn.nextFloat();
        while(f<.05){
            f= rn.nextFloat();
        }
        int gold=(int) ((f)*100)*x;
        if (gold<0)gold=1;
        if(gold>1000){
            int exp=gold-1000;
            gold=1000;
            player.setExp(player.getExp()+exp);
            MapStateRender.setHoverText(exp+"EXP",.5f,Color.GREEN, player.getPX(), player.getPY()-20,false);

        }
        {
            player.setGold(player.getGold() + gold);
            out(player.getName() + " recieved " + gold + "G");
            MapStateRender.setHoverText(gold+"G",.5f,Color.GOLD, player.getPX(), player.getPY(),false);
        }
    }
    static void openCrate(int index){
        if(liveCellList.get(index) !=null &&liveCellList.get(index).getItem() != null)
            if (liveCellList.get(index).getItem().getClass() == Gold.class) {
                lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
                player.setGold(player.getGold() +liveCellList.get(index).getGold());
                Gold g= (Gold) liveCellList.get(index).getItem();
                out(g.getValue() + " added to stash");
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
                        }
                        else{
                            player.lastItem=new Gold();
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
