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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapStateRender.inventoryPos;
import static com.quadx.dungeons.states.mapstate.MapStateRender.renderLayers;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtRespawn;
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
     static boolean showStats=true;

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
    static InfoOverlay hud= new InfoOverlay();
    static InfoOverlay invOverlay=new InfoOverlay();
    static InfoOverlay equipOverlay=new InfoOverlay();

    static ArrayList<InfoOverlay> attackBarHud = new ArrayList<>();
    Item prevItem= null;



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
        if(!Tests.nospawn) {
            if(dtRespawn>10f){
                MapStateUpdater.spawnMonsters(MapStateUpdater.spawnCount);
            }
        }
        MapStateUpdater.buttonHandler();
        if(MapStateUpdater.dtCollision>Game.frame/2) {
            MapStateUpdater.collisionHandler();
        }
        MapStateUpdater.moveMonsters();
        player.checkIfDead(gsm);
        createHUD();

    }
    public void render(SpriteBatch sb) {
        renderLayers(sb);
    }
    public void dispose() {
    }
    public static InfoOverlay getInfoOverlay(){
        return hud;
    }
    public static InfoOverlay getInvOverlay(){return invOverlay;}
    public static ArrayList<InfoOverlay> getAttackBarOverlay(){
        return attackBarHud;
    }

    public static void out(String s){
        if(output != null) {
            output.add(s);
            if (output.size() > 10) output.remove(0);
        }
    }
    void createHUD(){
        hud.rects.clear();
        hud.rects.add(new Rectangle(viewX + WIDTH / 3, viewY, 2, 205));
        hud.rects.add(new Rectangle(viewX + (WIDTH / 3) * 2, viewY, 2, 205));
        hud.rects.add(new Rectangle(viewX, viewY + 205, WIDTH, 2));
        if (showStats)
            hud.rects.add(new Rectangle(viewX, viewY + HEIGHT - 300, 300, 300));
        hud.rects.add(new Rectangle(viewX, viewY, WIDTH, 207));
        hud.texts.clear();
        hud.texts.add(new Text("SCORE: " + player.getPoints() + "", new Vector2((viewX + Game.WIDTH / 3) + 4, (viewY + 200)), Color.GRAY, 1));
        hud.texts.add(new Text("HIGH SCORE: " + HighScoreState.scores.get(0).getScore(), new Vector2((viewX + (Game.WIDTH / 3) * 2) - (Game.WIDTH / 3 / 2), (viewY + 200)), Color.GRAY, 1));
        //attack bar hud
        generateAttackBarUI();
        generateInventoryUI();
    }
    void generateAttackBarUI(){
        attackBarHud.clear();
        for (int i = 0; i < player.attackList.size(); i++) {
            Attack a = player.attackList.get(i);
            InfoOverlay io=new InfoOverlay();
            int type = a.getType();
            int xoffset = (int) (viewX + (WIDTH / 2) - (52 * 4));
            int x=xoffset + (i * 52);
            try {
                if (type == 3 || type == 2 || type==4) {
                    if (player.getMana() >= a.getCost()) {
                        io.textures.add(a.getIcon());
                        io.texturePos.add(new Vector2( x, viewY + 48));
                        if (i <= 7)
                            io.texts.add(new Text( (i + 1) + "", new Vector2( x, viewY + 58),Color.WHITE,1));
                    } else {
                        int rem = a.getCost() - player.getMana();
                        io.texts.add(new Text( rem + "", new Vector2(x + 52 / 2, viewY + 70),Color.WHITE,1));
                    }
                    io.texts.add(new Text("M" + a.getCost(), new Vector2(x, viewY + 30),Color.WHITE,1));
                } else if (type == 1) {
                    if (player.getEnergy() >= a.getCost()) {
                        io.textures.add(a.getIcon());
                        io.texturePos.add(new Vector2( x, viewY + 48));
                        if (i <= 7)
                            io.texts.add(new Text( (i + 1) + "", new Vector2( x, viewY + 58),Color.WHITE,1));
                    } else {
                        int rem = a.getCost() - player.getEnergy();
                        io.texts.add(new Text( rem + "", new Vector2(x + 52 / 2, viewY + 70),Color.WHITE,1));
                    }
                    io.texts.add(new Text("E" + a.getCost(), new Vector2(x, viewY + 30),Color.WHITE,1));
                }
                io.texts.add(new Text("Lv." + (a.getLevel() + 1), new Vector2(x, viewY + 48),Color.WHITE,1));
            } catch (NullPointerException ignored) {
            }
            attackBarHud.add(io);
        }
    }

    void generateInventoryUI() {
        //add ability icon
        invOverlay=new InfoOverlay();
        String sss;
        try {
            sss=player.getAbility().getName() +" "+player.getAbility().getLevel();
        }catch (NullPointerException e){
            sss="Error #0092";
        }
        invOverlay.texts.add(new Text(sss,new Vector2(viewX+((WIDTH/3)*2)+30,viewY+80),Color.GRAY, 1));
        invOverlay.textures.add(ImageLoader.abilities.get(player.getAbilityMod()));
        invOverlay.texturePos.add(new Vector2(viewX+((WIDTH/3)*2)+30,viewY+20));
        //add selected item
        if (!player.invList.isEmpty() && inventoryPos > -1) {
            try {
                Item item = player.invList.get(inventoryPos).get(0);
                invOverlay = new InfoOverlay();
                //if (prevItem != item) {
                    String name = (inventoryPos) + ":" + item.getName();
                    int y = (int) viewY + 130;
                    int x = (int) (viewX + WIDTH - 290);
                    ArrayList<String> outList = new ArrayList<>();
                    if (item.getHpmod() != 0) {
                        outList.add("HP " + item.getHpmod());
                    }
                    if (item.getManamod() != 0) {
                        outList.add("M :" + item.getManamod());
                    }//Mana
                    if (item.getEmod() != 0) {
                        outList.add("E :" + item.getEmod());
                    }//Mana
                    if (item.getAttackmod() != 0) {
                        outList.add("ATT :" + item.getAttackmod());
                    }  //attack
                    if (item.getDefensemod() != 0) {
                        outList.add("DEF :" + item.getDefensemod());
                    } //defense
                    if (item.getIntelmod() != 0) {
                        outList.add("INT :" + item.getIntelmod());
                    }//intel
                    if (item.getSpeedmod() != 0) {
                        outList.add("SPD :" + item.getSpeedmod());
                    }//speed
                    invOverlay.texts.add(new Text(name, new Vector2(viewX + WIDTH - 290, viewY + 200 - 20), Color.WHITE, 1));
                    for (int i = 0; i < outList.size(); i++) {
                        invOverlay.texts.add(new Text(outList.get(i), new Vector2(viewX + WIDTH - 290, viewY + 150 - ((i + 1) * 20) - 20), Color.WHITE, 1));
                    }
                    invOverlay.texts.add(new Text("x" + player.invList.get(inventoryPos).size(), new Vector2(x, y), Color.WHITE, 1));
                    try {
                        invOverlay.textures.add(player.invList.get(inventoryPos).get(0).getIcon());
                        invOverlay.texturePos.add(new Vector2(x, y));

                    } catch (Exception e) {
                        invOverlay.textures.add(ImageLoader.crate);
                        invOverlay.texturePos.add(new Vector2(x, y));
                    }
                //}
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        if(player.invList.isEmpty()){
            invOverlay= new InfoOverlay();
        }
        //add equipment
        equipOverlay= new InfoOverlay();
        int count = 0;
        int x = (int) (viewX + ((WIDTH / 3) * 2) + 15);
        int y = (int) (viewY + 130);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if(count<player.equipedList.size()) {
                    try {
                        equipOverlay.textures.add(player.equipedList.get(count).getIcon());
                        equipOverlay.texturePos.add(new Vector2( x + (j * 36), y + (i * 36) - 20));
                    }catch (Exception e){
                        equipOverlay.textures.add(ImageLoader.crate);
                        equipOverlay.texturePos.add(new Vector2( x + (j * 36), y + (i * 36) - 20));
                    }
                    count++;
                }
            }
        }
        invOverlay.add(equipOverlay);
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

            if (player.facing.equals(Direction.Facing.North) ||player.facing.equals(Direction.Facing.Northeast) ||player.facing.equals(Direction.Facing.Northwest)) {
                xlim = (px) + spread;
                ylim = py + range;
                x = px;
                y = py;
                a = (int) -Math.ceil(spread / 2);
                b = 0;

            } else if (player.facing.equals(Direction.Facing.South) ||player.facing.equals(Direction.Facing.Southeast)||player.facing.equals(Direction.Facing.Southwest)) {
                xlim = (px) + spread;
                ylim = py;
                x = px;
                y = py - range;
                a = (int) -Math.ceil(spread / 2);
                b = 0;
            } else if (player.facing.equals(Direction.Facing.East)) {
                xlim = (px) + range;
                ylim = py + spread;
                x = px;
                y = py;
                a = 0;
                b = (int) -Math.ceil(spread / 2);
            } else if (player.facing.equals(Direction.Facing.West)) {
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
                    if(n<res && n>=0 && m<res && m>=0) {
                        setHitList(n, m);
                    }
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
            //MapStateRender.setHoverText(exp + "EXP", .5f, Color.GREEN, player.getPX(), player.getPY() - 20, false);

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
                MapStateRender.setHoverText(g.getValue() + "G", 1, Color.GOLD, (int) player.getAbsPos().x, (int) player.getAbsPos().y, false);
            } else {
                Item item = liveCellList.get(index).getItem();
                if (item != null) {
                    if (item.isEquip) {
                        item.loadIcon(item.getType());
                    } else
                        if(item.getIcon() == null)
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
                            if (a) inventoryPos = 0;
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
                        if (a) inventoryPos = 0;
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
