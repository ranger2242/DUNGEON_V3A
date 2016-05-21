package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
public class MapState extends State {
    public static ShapeRenderer shapeR;
    public static ArrayList<String> output;
    public static ArrayList<QButton> qButtonList =new ArrayList<>();
    public static ArrayList<Texture> attackIconList =new ArrayList<>();
    public static ArrayList<Texture> invIcon=new ArrayList<>();
    public static ArrayList<Texture> equipIcon=new ArrayList<>();
    public static ArrayList<Integer> invSize=new ArrayList<>();
    public static Texture lootPopup;
    public static Texture statPopup;

    public static AbilityMod am;
    public static GridManager gm;
    public static ParticleEffect effect;
    public static ParticleEmitter emitter;
    static Random rn = new Random();
    public static Attack attack;
    public static Item item;
    public static Item popupItem;
    public static Monster targetMon;

    public static char lastPressed = 'w';
    public static boolean attackMenuOpen = false;
    public static boolean inventoryOpen = false;
    public static boolean displayPlayerDamage = false;
    public static boolean hovering=false;
    public static boolean effectLoaded = false;
    public static final String DIVIDER= "_________________________";
    public static int attackListCount = 0;
    public static int playerDamage = 0;
    public static int messageCounter=0;
    public static int invSlotHovered=0;
    public static int cellW=5;
    public static int mHitX=0;
    public static int mHitY=0;
    public static int mouseX=0;
    public static int mouseY=0;
    public static int mouseRealitiveX=0;
    public static int mouseRealitiveY=0;

    public static int qButtonBeingHovered;

    public static float dtMessage=0;
    public static float dtLootPopup =0;
    public static float dtStatPopup=0;
    public static float viewX;
    public static float viewY;
    static float itemMinTime=.4f;
    static float attackMintime = .3f;

    static float dtDamageTextFloat = 0;
    float textY = 0;
    static int lastNumPressed=1;

    public MapState(GameStateManager gsm) {
        super(gsm);
        gm = new GridManager();
        shapeR = new ShapeRenderer();
        output= new ArrayList<>();
        MapStateRender.loadAttackIcons();
        bufferOutput();
        Game.setFontSize(10);
        attack=new Flame();
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        gm.initializeGrid();
        out("---Welcome to DUNGEON---");
        for(int i=0;i<10;i++)
        openCrate();
    }
    public void handleInput() {
    }
    public void update(float dt) {
        if(output.size()>11){
            output.remove(0);
        }
        MapStateUpdater.buttonHandler();
        MapStateUpdater.collisionHandler();
        MapStateUpdater.moveMonsters();
        MapStateUpdater.fuckingStupidUpdateFunction(dt);
    }
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glEnable(Gdx.gl20.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE_MINUS_SRC_ALPHA);
        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        MapStateRender.drawGrid(false);
        MapStateRender.drawMonsterAgro();
        MapStateRender.drawHUD(sb);
        MapStateRender.drawAbilityIcon(sb);
        MapStateRender.drawMessageOutput(sb);
        MapStateRender.drawStatChanges(sb);
        MapStateRender.drawPlayerEquipment(sb);
        MapStateRender.drawPlayer(sb);
        if (displayPlayerDamage) MapStateRender.drawPlayerDamageOutput(sb, mHitX, mHitY + textY);
        if(hovering) MapStateRender.drawPopup(sb);
        if (effectLoaded) {MapStateRender.drawParticleEffects(sb, Game.player.getPX(), Game.player.getPY());}
        if(MapStateRender.hovText) MapStateRender.drawHovText(sb);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.RED);
         if(qButtonList.size()>0)   shapeR.rect(qButtonList.get(0).getPx(), qButtonList.get(0).getPx(), 12, 12);
            shapeR.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
    }
    public void dispose() {
        //unload attack icons
        for(Texture t : attackIconList){
            t.dispose();
        }
        attackIconList.clear();
    }
    public static void out(String s){
        //System.out.println(s);

        //output.clear();
        output.add(s);
        //dtMessage=0;
       // messageCounter++;
        if(output.size()>10)output.remove(0);
    }
    public static void checkAttackHit(int pos) {
        int r = Game.player.attackList.get(attackListCount + pos).getRange();
        int s = Game.player.attackList.get(attackListCount + pos).getSpread();
        int pow = Game.player.attackList.get(attackListCount + pos).getPower();
        int px = Game.player.getX();
        int py = Game.player.getY();
        switch (lastPressed) {
            case ('w'): {attackCollisionHandler(r, s, pow, px, py);break;}
            case ('a'): {attackCollisionHandler(-r, s, pow, px, py);break;}
            case ('s'): {attackCollisionHandler(-r, s, pow, px, py);break;}
            case ('d'): {attackCollisionHandler(r, s, pow, px, py);break;}}
    }
    public static void attackCollisionHandler(int r, int s, int pow, int px, int py) {
        s = 0;
        int xrange=0;
        int yrange=0;
        dtDamageTextFloat = 0;
            while (r != 0) {
            if (lastPressed == 'w' || lastPressed == 's') {
                xrange=px+s;
                yrange=py+r;
            }
            else if(lastPressed == 'a' || lastPressed == 'd') {
                xrange=px+r;
                yrange=py+s;
            }
            int index = 0;
            if (xrange >= 0 && xrange < gm.res && yrange >= 0 && yrange < gm.res) {
                try {
                    index = gm.dispArray[xrange][yrange].getIndex();
                }
                catch (NullPointerException e){}
            }
            if (index > -1) {
                Monster tempMon = new Monster();
                if (xrange >= 0 && xrange < gm.res && yrange >= 0 && yrange < gm.res)
                    try {
                        for(Cell c : gm.liveCellList){
                            if(c.getX()==xrange && c.getY()==yrange && c.hasMon()){
                                for (Monster m : gm.monsterList) {
                                    if (m.getX() == xrange && m.getY() == yrange) {
                                        targetMon=m;
                                        out("Hit");
                                        index = gm.monsterList.indexOf(m);
                                        playerDamage = (int) Damage.playerMagicDamage(Game.player, m, attack.getPower());
                                        int attIndex = Game.player.attackList.indexOf(attack);
                                        mHitX = m.getPX();
                                        mHitY = m.getPY();

                                        Game.player.attackList.get(attIndex).setUses();
                                        Game.player.attackList.get(attIndex).checkLvlUp();
                                        displayPlayerDamage = true;
                                        m.takeAttackDamage(playerDamage);
                                        if (m.getHp() <= 0) {
                                            out(DIVIDER);
                                            out(m.getName() + " Level " + m.getLevel() + " was killed.");
                                            Game.player.addKills();
                                            Game.player.setExp(m);
                                            Game.player.checkLvlUp();
                                            makeGold(m.getLevel());
                                            gm.dispArray[m.getX()][m.getY()].setMon(false);
                                            tempMon = m;

                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (NullPointerException e){}
                gm.monsterList.remove(tempMon);
            }
            if (r > 0) r--;
            if (r < 0) r++;
        }
    }
    public static void makeGold(int x){
        int gold=(int) ((x*10)+rn.nextGaussian()*100);
        if (gold<0)gold=1;
        Game.player.setGold(Game.player.getGold()+gold);
        out(Game.player.getName()+" recieved "+gold+"G");
    }
    static void clearFront(){
        int j= Game.player.getX();
        int k= Game.player.getY();

        try {
            gm.dispArray[j][k].setFront(false);
            if (j + 1 < gm.res) gm.dispArray[j + 1][k].setFront(false);
            if (j - 1 >= 0) gm.dispArray[j - 1][k].setFront(false);
            if (k + 1 < gm.res) gm.dispArray[j][k + 1].setFront(false);
            if (k - 1 >= 0) gm.dispArray[j][k - 1].setFront(false);
        }
        catch (NullPointerException e){

        }
        catch(ArrayIndexOutOfBoundsException e){}
    }
    public static void setFront(int j, int k){
        try{
            switch (lastPressed) {
                case 'w':{if(k+1< gm.res) gm.dispArray[j][k+1].setFront(true);break;}
                case 'a':{if(j-1>=0) gm.dispArray[j-1][k].setFront(true);break;}
                case 's':{if(k-1>=0) gm.dispArray[j][k-1].setFront(true);break;}
                case 'd':{if(j+1< gm.res) gm.dispArray[j+1][k].setFront(true);break;}
            }
        }
        catch (NullPointerException e){

        }
    }
    static void openCrate(){
        int q=rn.nextInt(17)+1;
        if(q==15){
            double rand=rn.nextGaussian()*100;
            int gold=(int)(Game.player.getGold()+rand);
            Game.player.setGold(gold);
            lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
            out(gold+" added to stash");
        }
        else{
            Item item =new Item();
            if(q==1||q==2) item=new AttackPlus();
            else if(q==3||q==4) item=new DefPlus();
            else if(q==5||q==6) item=new IntPlus();
            else if(q==7||q==8)item=new SpeedPlus();
            else if(q>=9&&q<=11)item=new ManaPlus();
            else if(q>11&&q<=14) item=new Potion();
            else if(q==16)item=new SpellBook();
            else if(q==17) item=generateEquipment();
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

            }
            out(item.getName()+" added to inventory");
        }
    }
    public static Item generateEquipment(){
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
    void bufferOutput(){
        for(int i=0;i<10;i++)
            out("");
    }
}
