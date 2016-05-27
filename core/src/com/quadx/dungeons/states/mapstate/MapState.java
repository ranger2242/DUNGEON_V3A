package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.*;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Flame;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.State;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapState extends State {
    static ShapeRenderer shapeR;
    static ArrayList<String> output;
    static ArrayList<QButton> qButtonList =new ArrayList<>();
    static ArrayList<Texture> attackIconList =new ArrayList<>();
    static ArrayList<Texture> invIcon=new ArrayList<>();
    static ArrayList<Texture> equipIcon=new ArrayList<>();
    static ArrayList<Integer> invSize=new ArrayList<>();
    static ArrayList<Cell> hitList=new ArrayList<>();

    static Texture lootPopup;
    public static Texture statPopup;

    public static AbilityMod am;
    public static GridManager gm;
    static ParticleEffect effect;
    static ParticleEmitter emitter;
    static Random rn = new Random();
    static Attack attack;
    static Item item;
    static Item popupItem;
    static Monster targetMon;

    public static boolean inGame=false;
    public static char lastPressed = 'w';
    static boolean attackMenuOpen = false;
    static boolean inventoryOpen = false;
    static boolean displayPlayerDamage = false;
    static boolean hovering=false;
    static boolean effectLoaded = false;
    static final String DIVIDER= "_________________________";
    static int attackListCount = 0;
    static int playerDamage = 0;
    static int messageCounter=0;
    public static int invSlotHovered=0;
    public static int cellW=10;
    static int mHitX=0;
    static int mHitY=0;
    static int mouseX=0;
    static int mouseY=0;
    static int mouseRealitiveX=0;
    static int mouseRealitiveY=0;

    static int qButtonBeingHovered;

    static float dtMessage=0;
    static float dtLootPopup =0;
    public static float dtStatPopup=0;
    public static float viewX;
    public static float viewY;
    static float itemMinTime=.4f;
    static float attackMintime = .3f;

    static float dtDamageTextFloat = 0;
    static int lastNumPressed=0;

    public MapState(GameStateManager gsm) {
        super(gsm);
        inGame=true;
        gm = new GridManager();
        shapeR = new ShapeRenderer();
        output= new ArrayList<>();
        MapStateRender.loadAttackIcons();
        bufferOutput();
        Game.setFontSize(10);
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        gm.initializeGrid();
        out("---Welcome to DUNGEON---");
        for(int i=0;i<20;i++)
        openCrate();
        attack=Game.player.attackList.get(0);
        //AbilityMod.enableAbility(4);//Quick

        countWarps();
    }
    void countWarps(){
        int c1=0,c2=0;
        for(int i=0;i<Map2State.res;i++){
            for(int j=0;j<Map2State.res;j++){
                if(gm.dispArray[i][j].hasWarp()){
                    c1++;
                }
            }
        }
        for(Cell c: gm.liveCellList){
            if(c.hasWarp())c2++;
        }
        out("Warps ARR:"+c1);
        out("Warps List:"+c2);

    }
    public void handleInput() {
    }
    public void update(float dt) {
        if(output.size()>11){
            output.remove(0);
        }
        MapStateUpdater.buttonHandler();
        if(MapStateUpdater.dtCollision>.16666f)
        MapStateUpdater.collisionHandler();
        MapStateUpdater.moveMonsters();
        MapStateUpdater.fuckingStupidUpdateFunction(dt);
    }
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if(inGame) {
            shapeR.setProjectionMatrix(cam.combined);
            sb.setProjectionMatrix(cam.combined);
        }
        Game.player.checkNullInventory();
        MapStateRender.drawGrid(false);
        MapStateRender.drawMonsterAgro();
        MapStateRender.drawHUD(sb);
        MapStateRender.drawAbilityIcon(sb);
        MapStateRender.drawMessageOutput(sb);
        MapStateRender.drawStatChanges(sb);
        MapStateRender.drawPlayerEquipment(sb);
        MapStateRender.drawPlayer(sb);
        float textY = 0;


        if (displayPlayerDamage) {
            for(Cell c:hitList) {
                for (Monster m : GridManager.monsterList) {
                    if (c.getX() == m.getX() && c.getY() == m.getY()) {
                        MapStateRender.drawPlayerDamageOutput(sb, m.getX(), m.getY()+10 + textY);
                    }
                }
            }
        }



        if(hovering) MapStateRender.drawPopup(sb);
        if (effectLoaded) {MapStateRender.drawParticleEffects(sb, Game.player.getPX(), Game.player.getPY());}
        if(MapStateRender.hovText) MapStateRender.drawHovText(sb);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.RED);
         if(qButtonList.size()>0)   shapeR.rect(qButtonList.get(0).getPx(), qButtonList.get(0).getPx(), 12, 12);
            shapeR.end();
        if(MapStateRender.showCircle)
            MapStateRender.drawPlayerFinder(sb);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    public void dispose() {
        //unload attack icons
        for(Texture t : attackIconList){
            t.dispose();
        }
        attackIconList.clear();
    }
    public static void out(String s){
        output.add(s);
        if(output.size()>10)output.remove(0);
    }

    static void attackCollisionHandler(int pos) {
        int range = Game.player.attackList.get(attackListCount + pos).getRange();
        int spread = Game.player.attackList.get(attackListCount + pos).getSpread();
        int px = Game.player.getX();
        int py = Game.player.getY();
        int xrange;
        int yrange;
        hitList.clear();
        if(lastPressed=='w') {
            xrange = (px) + spread;
            yrange = py + range;
            for (int i = px; i < xrange; i++) {
                for (int j = py; j < yrange; j++) {
                    try {
                        GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j].setAttArea(true);
                        GridManager.liveCellList.set(GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j].getIndex(), GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j]);
                        hitList.add(GridManager.liveCellList.get(GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j].getIndex()));
                    }
                    catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                      //  Game.printLOG(e);
                    }
                }
            }
        }
        else if(lastPressed=='s'){
            xrange = (px) + spread;
            yrange = py - range;
            for (int i = px; i < xrange; i++) {
                for (int j = yrange; j < py; j++) {
                    try {
                        GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j-1].setAttArea(true);
                        GridManager.liveCellList.set(GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j-1].getIndex(), GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j-1]);
                        hitList.add(GridManager.liveCellList.get(GridManager.dispArray[(int) (i - Math.ceil(spread / 2)) - 1][j-1].getIndex()));

                    }
                    catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                        Game.printLOG(e);
                    }
                }
            }
        }
        else if(lastPressed=='d'){
            xrange = (px) + range;
            yrange = py + spread;
            for (int i = px; i < xrange; i++) {
                for (int j = py; j < yrange; j++) {
                    try {
                        GridManager.dispArray[i][(int)(j - Math.ceil(spread / 2))-1].setAttArea(true);
                        GridManager.liveCellList.set(GridManager.dispArray[i][(int)(j - Math.ceil(spread / 2))-1].getIndex(), GridManager.dispArray[i][(int)(j - Math.ceil(spread / 2))-1]);
                        hitList.add(GridManager.liveCellList.get(GridManager.dispArray[i][(int)(j - Math.ceil(spread / 2))-1].getIndex()));

                    }
                    catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                        Game.printLOG(e);
                    }
                }
            }
        }
        else if(lastPressed=='a'){
            xrange = (px) - range;
            yrange = py + spread;
            for (int i = xrange; i < px; i++) {
                for (int j = py; j < yrange; j++) {
                    try {
                        GridManager.dispArray[(i -1)][(int)(j - Math.ceil(spread / 2))-1].setAttArea(true);
                        GridManager.liveCellList.set(GridManager.dispArray[i  -1][(int)(j - Math.ceil(spread / 2))-1].getIndex(), GridManager.dispArray[i -1][(int)(j - Math.ceil(spread / 2))-1]);
                        hitList.add(GridManager.liveCellList.get(GridManager.dispArray[i  -1][(int)(j - Math.ceil(spread / 2))-1].getIndex()));

                    }
                    catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                        Game.printLOG(e);
                    }
                }
            }
        }
        Monster tempMon=null;
        boolean killed= false;
        for(Cell c:hitList){
            for(Monster m: GridManager.monsterList){
                if(c.getX()==m.getX() && c.getY()==m.getY()){
                    dtDamageTextFloat = 0;
                    tempMon=m;
                    playerDamage = Damage.playerMagicDamage(Game.player, m, attack.getPower());
                    out("Hit "+playerDamage+" damage");
                    int attIndex = Game.player.attackList.indexOf(attack);

                    Game.player.attackList.get(attIndex).setUses();
                    Game.player.attackList.get(attIndex).checkLvlUp();
                    displayPlayerDamage = true;
                    m.takeAttackDamage(playerDamage);
                    if (m.getHp() < 1) {
                        out(DIVIDER);
                        out(m.getName() + " Level " + m.getLevel() + " was killed.");
                        Game.player.addKills();
                        Game.player.setExp(m);
                        Game.player.checkLvlUp();
                        makeGold(m.getLevel());
                        try {
                            GridManager.dispArray[m.getX()][m.getY()].setMon(false);
                        }catch (NullPointerException e){
                            Game.printLOG(e);
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
    }

    static void makeGold(int x){
        int gold=(int) ((rn.nextFloat()/2)*100);
        if (gold<0)gold=1;
        {
            Game.player.setGold(Game.player.getGold() + gold);
            out(Game.player.getName() + " recieved " + gold + "G");
            MapStateRender.setHoverText(gold+"G",.5f,Color.GOLD);
        }
    }
    static void clearFront(){
        int j= Game.player.getX();
        int k= Game.player.getY();

        try {
            GridManager.dispArray[j][k].setFront(false);
            if (j + 1 < GridManager.res) GridManager.dispArray[j + 1][k].setFront(false);
            if (j - 1 >= 0) GridManager.dispArray[j - 1][k].setFront(false);
            if (k + 1 < GridManager.res) GridManager.dispArray[j][k + 1].setFront(false);
            if (k - 1 >= 0) GridManager.dispArray[j][k - 1].setFront(false);
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e){
            Game.printLOG(e);

        }
    }
    static void setFront(int j, int k){
        try{
            switch (lastPressed) {
                case 'w':{if(k+1< GridManager.res) GridManager.dispArray[j][k+1].setFront(true);break;}
                case 'a':{if(j-1>=0) GridManager.dispArray[j-1][k].setFront(true);break;}
                case 's':{if(k-1>=0) GridManager.dispArray[j][k-1].setFront(true);break;}
                case 'd':{if(j+1< GridManager.res) GridManager.dispArray[j+1][k].setFront(true);break;}
            }
        }
        catch (NullPointerException | ArrayIndexOutOfBoundsException e){
            Game.printLOG(e);

        }
    }
    static void openCrate(){
        int q=rn.nextInt(30)+1;
        if(q>=17){
            double rand=rn.nextFloat()/8;
            if(rand<0)rand*=-1;
            int gold=(int)(Game.player.getGold()*(rand));
            if(gold<=0)gold=1;
            Game.player.setGold(Game.player.getGold()+gold);
            lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
            out(gold+" added to stash");
            MapStateRender.setHoverText(gold+"G",1,Color.GOLD);

        }
        else{
            Item item =new Item();
            if(q==1||q==2) item=new AttackPlus();
            else if(q==3||q==4) item=new DefPlus();
            else if(q==5||q==6) item=new IntPlus();
            else if(q==7||q==8)item=new SpeedPlus();
            else if(q>=9&&q<=11)item=new ManaPlus();
            else if(q>11&&q<=14) item=new Potion();
            else if(q==15)item=new SpellBook();
            else if(q==16) item=generateEquipment();
            Game.player.addItemToInventory(item);
            String s=item.getName();
            if(item.isEquip)
                s=item.getType();
            if(item.isSpell)
                s="SpellBook";
            try {
                lootPopup = new Texture(Gdx.files.internal("images/icons/items/ic" + s + ".png"));
                dtLootPopup=0;
            }
            catch (GdxRuntimeException e){
                Game.printLOG(e);
            }
            if(item.getName()==null){
               // MapStateRender.setHoverText("ERR:0002", 1,Color.RED);
                item=new Potion();
                out(item.getName() + " added to inventory");
                MapStateRender.setHoverText(item.getName(), 1,Color.WHITE);

            }else {
                out(item.getName() + " added to inventory");
                MapStateRender.setHoverText(item.getName(), 1,Color.WHITE);
            }
        }
    }
    private static Item generateEquipment(){
        Item item=new Item();
        int x=rn.nextInt(8)+1;
        switch (x){
            case(1):{
                item=new Arms();
                break;
            }
            case(2):{
                item=new Boots();
                break;
            }
            case(3):{
                item= new Cape();
                break;
            }
            case(4):{
                item=new Chest();
                break;
            }
            case(5):{
                item= new Gloves();
                break;
            }
            case(6):{
                item=new Helmet();
                break;
            }
            case(7):{
                item=new Legs();
                break;
            }
            case(8):{
                item=new Ring();
                break;
            }
        }
        return item;
    }

    private void bufferOutput(){
        for(int i=0;i<10;i++)
            out("");
    }
}
