package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.*;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.State;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.GridManager.liveCellList;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtAttack;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtScrollAtt;

/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapState extends State implements ControllerListener {
    static ShapeRenderer shapeR;
    static ArrayList<String> output;
    static ArrayList<QButton> qButtonList =new ArrayList<>();
    static ArrayList<Texture> attackIconList =new ArrayList<>();
    static ArrayList<Texture> invIcon=new ArrayList<>();
    static ArrayList<Cell> hitList=new ArrayList<>();

    static Texture lootPopup;
    public static Texture statPopup;

    public static AbilityMod am;
    public static GridManager gm;
    static ParticleEffect effect;
    static ParticleEmitter emitter;
    static Random rn = new Random();
    static Attack attack;
    static Attack attack2;
    static Item popupItem;
    static Monster targetMon;

    public static boolean inGame=false;
    public static char lastPressed = 'w';
    static boolean displayPlayerDamage = false;
    static boolean hovering=false;
    static boolean effectLoaded = false;
    static final String DIVIDER= "_________________________";
    static int attackListCount = 0;
    static int messageCounter=0;
    public static int cellW=30;
    static int mHitX=0;
    static int mHitY=0;
    static int mouseX=0;
    static int mouseY=0;
    static int mouseRealitiveX=0;
    static int mouseRealitiveY=0;
    public static int warpX=0;
    public static int warpY=0;

    static int qButtonBeingHovered;

    static float dtMessage=0;
    public static float dtStatPopup=0;
    public static float viewX;
    public static float viewY;
    static float itemMinTime=.4f;
    static float attackMintime = Game.frame*10;

    static float dtDamageTextFloat = 0;
    static int lastNumPressed=0;
    static int altNumPressed=1;

    public MapState(GameStateManager gsm) {
        super(gsm);
        player.loadIcons();
        inGame=true;
        gm = new GridManager();
        shapeR = new ShapeRenderer();
        output= new ArrayList<>();
        if(controllerMode)
        MainMenuState.controller.addListener(this);
        MapStateRender.loadAttackIcons();
        bufferOutput();
        Game.setFontSize(1);
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        gm.initializeGrid();
        out("---Welcome to DUNGEON---");
        attack= player.attackList.get(0);
        attack2=player.attackList.get(1);


        for(int i=0; i<12;i++){
            for(int j=0;j<12; j++){
                System.out.print((i*j)% 12+"\t");
            }
            System.out.print("\n");
        }
        out((8*12)%15+"");

        for(int i=0;i<20;i++){

 //          player.addItemToInventory(new Arms());
        }
    }
    public void handleInput() {
    }
    public void update(float dt) {
        if(output.size()>11){
            output.remove(0);
        }
        MapStateUpdater.buttonHandler();
        if(MapStateUpdater.dtCollision>Game.frame/2)
        MapStateUpdater.collisionHandler();
        MapStateUpdater.moveMonsters();
        MapStateUpdater.fuckingStupidUpdateFunction(dt);
    }
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeR.setProjectionMatrix(cam.combined);
            sb.setProjectionMatrix(cam.combined);
       // Game.player.checkNullInventory();
        MapStateRender.drawGrid(false);
        MapStateRender.drawMonsterAgro();
        MapStateRender.drawHUD(sb);
        MapStateRender.drawAbilityIcon(sb);
        MapStateRender.drawMessageOutput(sb);
        MapStateRender.drawStatChanges(sb);
        MapStateRender.drawEquipment(sb);
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
        //if (effectLoaded) {MapStateRender.drawParticleEffects(sb, Game.player.getPX(), Game.player.getPY());}
        MapStateRender.drawHovText(sb);
        MapStateRender.drawMiniMap(sb);

        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.RED);
         if(qButtonList.size()>0)   shapeR.rect(qButtonList.get(0).getPx(), qButtonList.get(0).getPx(), 12, 12);
            shapeR.end();
        if(MapStateRender.showCircle)
            MapStateRender.drawPlayerFinder(sb);
        sb.begin();
        for(Monster m: GridManager.monsterList){
            Texture t=m.getIcon();
                    sb.draw(t, m.getX() * cellW -t.getWidth()/4, m.getY() * cellW -t.getHeight()/4);
        }
        sb.end();
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
        try {
            output.add(s);
            if(output.size()>10)output.remove(0);

        }catch (NullPointerException e){}
    }

    static void attackCollisionHandler(int pos) {
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
        hitList.add(liveCellList.get(player.getLiveListIndex()));
        if(lastPressed=='w') {
            xrange = (px) + spread;
            yrange = py + range;
            for (int i = px; i < xrange; i++) {
                for (int j = py; j < yrange; j++) {
                    setHitList((int) (i - Math.ceil(spread / 2)) - 1,j);
                }
            }
        }
        else if(lastPressed=='s'){
            xrange = (px) + spread;
            yrange = py - range;
            for (int i = px; i < xrange; i++) {
                for (int j = yrange; j < py; j++) {
                    setHitList((int) (i - Math.ceil(spread / 2)) - 1,j-1);
                }
            }
        }
        else if(lastPressed=='d'){
            xrange = (px) + range;
            yrange = py + spread;
            for (int i = px; i < xrange; i++) {
                for (int j = py; j < yrange; j++) {
                    setHitList(i,(int)(j - Math.ceil(spread / 2))-1);
                }
            }
        }
        else if(lastPressed=='a'){
            xrange = (px) - range;
            yrange = py + spread;
            for (int i = xrange; i < px; i++) {
                for (int j = py; j < yrange; j++) {
                    setHitList((i -1),(int)(j - Math.ceil(spread / 2))-1);
                }
            }
        }
        Monster tempMon=null;
        boolean killed= false;
        for(Cell c:hitList)    {
            dispArray[c.getX()][c.getY()].setAttArea(true);
            for(Monster m: GridManager.monsterList){
                if(c.getX()==m.getX() && c.getY()==m.getY()){
                    int tempMonIndex;
                    dtDamageTextFloat = 0;
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
                    GridManager.monsterList.get(tempMonIndex).setHit();

                    if (m.getHp() < 1) {
                        out(DIVIDER);
                        out(m.getName() + " Level " + m.getLevel() + " was killed.");
                        c.setMon(false);
                        player.addKills();
                        player.setExp(m);
                        player.checkLvlUp();
                        makeGold(m.getLevel());
                        try {
                            GridManager.dispArray[m.getX()][m.getY()].setMon(false);
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
    }

    static void setHitList(int x, int y){
        try {
            Cell cell= GridManager.dispArray[x][y];
            cell.setAttArea(true);
            //liveCellList.set(cell.getIndex(), cell);
            hitList.add(cell);

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
    static void openCrate(){
        int q=rn.nextInt(18)+1;
        if(q>=17){
            double rand=rn.nextFloat()/16;
            if(rand<0)rand*=-1;
            int gold=(int)(player.getGold()*(rand));
            if(gold<=0)gold=1;
            player.setGold(player.getGold()+gold);
            lootPopup = new Texture(Gdx.files.internal("images/imCoin.png"));
            out(gold+" added to stash");
            MapStateRender.setHoverText(gold+"G",1,Color.GOLD, player.getPX(), player.getPY(),false);

        }
        else{
            Item item =new Item();
            if(q==1||q==2) item=new AttackPlus();
            else if(q==3||q==4) item=new DefPlus();
            else if(q==5||q==6) item=new IntPlus();
            else if(q==7||q==8)item=new SpeedPlus();
            else if(q>=9&&q<=11)item=new ManaPlus();
            else if(q>11&&q<=14) item=new Potion();
            else if(q==15 || q==16) item=generateEquipment();
            player.addItemToInventory(item);
            String s=item.getName();
            if(item.isEquip)
                s=item.getType();
            if(item.isSpell)
                s="SpellBook";
            try {
                lootPopup = item.getIcon();
                MapStateRender.dtLootPopup=0;
            }
            catch (GdxRuntimeException e){
                Game.printLOG(e);
            }
            if(item.getName()==null){
               // MapStateRender.setHoverText("ERR:0002", 1,Color.RED);
                item=new Potion();
                out(item.getName() + " added to inventory");
                MapStateRender.setHoverText(item.getName(), 1,Color.WHITE, player.getPX(), player.getPY(),false);

            }else {
                out(item.getName() + " added to inventory");
                MapStateRender.setHoverText(item.getName(), 1,Color.WHITE, player.getPX(), player.getPY(),false);
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
