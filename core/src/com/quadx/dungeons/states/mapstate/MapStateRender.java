package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.EmitterAngles;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.tools.ColorConverter;
import com.quadx.dungeons.tools.HoverText;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtClearHits;

/**
 * Created by Tom on 12/28/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateRender extends MapState {


    public static boolean showCircle=false;
    private static boolean blink=false;
    static boolean showStats=false;
    static float dtBlink =0;
    static float dtLootPopup=0;
    static float dtHovBuffTime=0;
    public static float dtCircle=1f;
    public static float dtWaterEffect=0;
    static int blradius=0;
    public static int inventoryPos=0;
    public static int[] statCompare=null;
    public static ArrayList<HoverText> hoverTexts = new ArrayList<>();
    public static ArrayList<HoverText> hoverBuff = new ArrayList<>();

    public static void updateVariables(float dt){
        dtBlink+=dt;
        dtWaterEffect+=dt;
        dtHovBuffTime+=dt;
        dtLootPopup +=dt;
        updateHoverTextTime();
        if(showCircle && dtCircle>0){
            dtCircle-=dt;
        }
        else{
            dtCircle=1f;
            showCircle=false;
        }
    }
    public static void updateHoverTextTime(){
        hoverTexts.forEach(HoverText::updateDT);
    }
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
    }
    public static void setHoverText(String s, float time, Color color, int x, int y,boolean flash){
        if(dtHovBuffTime>HoverText.bufferTime) {
            hoverTexts.add(new HoverText(s, color, x, y, time, flash));
            dtHovBuffTime=0;
        }
        else{
            hoverBuff.add(new HoverText(s, color, x, y, time, flash));
        }
    }
    public static void drawHovText(SpriteBatch sb){
        if(!hoverBuff.isEmpty() &&dtHovBuffTime>HoverText.bufferTime){
            hoverTexts.add(hoverBuff.get(0));
            hoverBuff.remove(0);
            dtHovBuffTime=0;
        }
        for(HoverText h:hoverTexts){
            h.draw(sb);
        }
        //delete inactive hoverText
        boolean[] index;
        if(!hoverTexts.isEmpty()){
            index= new boolean[hoverTexts.size()];
            hoverTexts.stream().filter(h -> !h.isActive()).forEach(h -> {
                index[hoverTexts.indexOf(h)] = true;
            });
            for(int i=hoverTexts.size()-1;i>=0;i--){
                if(index[i]) {
                    hoverTexts.remove(i);
                }
            }
            while (hoverTexts.size()>10)hoverTexts.remove(0);
        }
    }
    public static void drawAbilityIcon(SpriteBatch sb){
            sb.begin();
            sb.draw(ImageLoader.abilities.get(player.getAbility()),viewX+((WIDTH/3)*2)+30,viewY+20);
            sb.end();
    }
    public static void drawPlayerFinder(SpriteBatch sb){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);

        shapeR.circle(player.getPX(), player.getPY(),100*dtCircle);
        shapeR.end();
    }
    private static void drawMonsterHp(SpriteBatch sb){
        Game.setFontSize(1);
        for(Monster m: GridManager.monsterList){
            sb.begin();
            Game.getFont().draw(sb,"LVL "+m.getLevel(),m.getX()*cellW-22,m.getY()*cellW-10);
            if(m.isHit())Game.getFont().draw(sb,"!" , m.getX()*cellW, (float) ((m.getY()+1.5)*cellW));
            sb.end();
            shapeR.begin(ShapeRenderer.ShapeType.Filled);
            double f=m.getHp()/m.getHpMax();

            shapeR.setColor(Color.DARK_GRAY);
            shapeR.rect(m.getX()*cellW-22,m.getY()*cellW-31, (float) (84),6);

            if(m.getHp()>m.getHpMax()/3) shapeR.setColor(Color.GREEN);
            else shapeR.setColor(Color.RED);
            shapeR.rect(m.getX()*cellW-20,m.getY()*cellW-30, (float) (80*f),4);
            shapeR.end();
        }
    }
    public static void drawHUD(SpriteBatch sb) {
        drawMonsterHp(sb);
        drawStats(sb, viewX, viewY);
        drawAttackMenu(sb);
        drawInventory(sb);
        sb.begin();
        try {
            if (dtLootPopup < .4)
                sb.draw(lootPopup, player.getPX()-(lootPopup.getWidth()/2)-(cellW/4), player.getPY()+5);
        }
        catch (NullPointerException e){
            //Game.printLOG(e);
        }
        for(int i = 0; i< res; i+=10)
            Game.getFont().draw(sb,i+"",i*cellW,-10);
        sb.end();



    }
    public static void drawMessageOutput(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(1);
        Game.font.setColor(Color.WHITE);
        for(int i=0;i<10;i++){
            try {
                Game.getFont().draw(sb, output.get(i), viewX+30,viewY+195-(i * 20));
            }
            catch(IndexOutOfBoundsException e){
                System.out.println((i+messageCounter)+"  Message out failed "+output.size());
            }
        }
        sb.end();
    }
    private static void drawStats(SpriteBatch sb, float x, float y) {
        drawBars();
        if(showStats) {
            sb.begin();//Draw STATS
            Game.setFontSize(1);
            Game.font.setColor(Color.WHITE);
            ArrayList<String> a = player.getStatsList();
            for (int i = 0; i < a.size(); i++) {
                if (statCompare != null && i - 2 < statCompare.length && i - 2 >= 0) {
                    switch (statCompare[i - 2]) {
                        case 1: {
                            Game.font.setColor(Color.BLUE);
                            break;
                        }
                        case 2: {
                            Game.font.setColor(Color.RED);
                            break;
                        }
                        case 0: {
                            Game.font.setColor(Color.WHITE);
                            break;
                        }
                    }
                }
                Game.getFont().draw(sb, a.get(i), x + 30, y + HEIGHT - 30 - (i * 20));
            }


            sb.end();
        }
        //Draw score
        sb.begin();
        Game.getFont().setColor(Color.WHITE);
        Game.getFont().draw(sb, "SCORE: "+player.getPoints()+"",(viewX+Game.WIDTH/3)+4,(viewY+200));
        try {
            Game.getFont().draw(sb, "HIGH SCORE: " + HighScoreState.scores.get(0).getScore(), (viewX+(Game.WIDTH/3)*2)-(Game.WIDTH/3/2),(viewY+200));
        }catch (NullPointerException | IndexOutOfBoundsException e){}
        sb.end();
    }
    static void drawBars(){
        int hpDiffx = 0;
        double pHealthMax = player.getHpMax();
        double pHealth = player.getHp();
        double pMana = player.getMana();
        double pManaMax = player.getManaMax();
        double pEnergyMax= player.getEnergyMax();
        double pEnergy= player.getEnergy();
        double pHPBarMax = (WIDTH/3)-10;
        double pHPBar = (pHealth / pHealthMax) * (pHPBarMax - hpDiffx);
        double pManaBarMax = (WIDTH/3)-10;
        double pManaBar = (pMana / pManaMax * pManaBarMax);
        double pEnergyBarMax=(WIDTH/3)-10;
        double pEnergyBar=(pEnergy/pEnergyMax*pEnergyBarMax);

        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(0f, 1f, 0f, 1);
        if (player.getHp() < player.getHpMax() / 2) {
            shapeR.setColor(1f, 0f, 0f, 1);
        }
        shapeR.rect(viewX+(WIDTH/3)+5,viewY+175 , (int) pHPBar, 10);
        shapeR.setColor(0f, 0f, 1f, 1);
        shapeR.rect(viewX+(WIDTH/3)+5, viewY+160, (int) pManaBar, 10);
        shapeR.setColor(1f,1f,0,1);
        shapeR.rect(viewX+(WIDTH/3)+5, viewY+145, (int) pEnergyBar, 10);
        shapeR.end();
    }
    public static void drawEquipment(SpriteBatch sb){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GRAY);
        int x= (int) (viewX+((WIDTH/3)*2)+15);
        int y= (int) (viewY + 130);
        for (int i = 0; i < 2; i++) {
        for(int j=0;j<4;j++) {
                shapeR.rect(x+ (j * 36),y+ (i * 36)-20, 32, 32);
            }
        }
        shapeR.end();

        sb.begin();
        int count =0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    sb.draw(player.equipedList.get(count).getIcon(),x+ (j * 36),y+  (i * 36)-20);
                    count++;
                }
                catch (IndexOutOfBoundsException e){}
            }
        }
            sb.end();
    }
    private static void drawAttackMenu(SpriteBatch sb) {
        int xoffset= (int) (viewX+(WIDTH/2)-(52*4));
        sb.begin();
        for(int i = 0; i< player.attackList.size(); i++){
            Attack a =player.attackList.get(i);
            int type=a.getType();
            try {
                if (type == 3 || type == 2) {
                    if (player.getMana() >= a.getCost()) {
                        sb.draw(a.getIcon(), xoffset + (i * 52), viewY + 48);
                        if (i <= 7) Game.getFont().draw(sb, (i + 1) + "", xoffset + (i * 52), viewY + 58);
                    } else {
                        int rem = a.getCost() - player.getMana();
                        Game.getFont().draw(sb, rem + "", xoffset + (i * 52) + 52 / 2, viewY + 70);
                    }
                } else if (type == 1) {
                    if (player.getEnergy() >= a.getCost()) {
                        sb.draw(a.getIcon(), xoffset + (i * 52), viewY + 48);
                        if (i <= 7) Game.getFont().draw(sb, (i + 1) + "", xoffset + (i * 52), viewY + 58);
                    } else {
                        int rem = a.getCost() - player.getEnergy();
                        Game.getFont().draw(sb, rem + "", xoffset + (i * 52) + 52 / 2, viewY + 70);
                    }
                }
                Game.getFont().draw(sb, "Lv." + (a.getLevel() + 1), xoffset + (i * 52), viewY + 48);
                Game.getFont().draw(sb, "M" + a.getCost(), xoffset + (i * 52), viewY + 30);
                if (i == altNumPressed) {
                    Game.getFont().draw(sb, "LT ALT", xoffset + (i * 52), viewY + 105);
                }
                if (i == lastNumPressed) {
                    Game.getFont().draw(sb, "RT SPACE", xoffset + (i * 52), viewY + 118);
                }
            }catch (NullPointerException e){}
        }

        sb.end();
    }
    private static void drawInventory(SpriteBatch sb) {
        if(!player.invList.isEmpty() && inventoryPos >-1) {
            try {
                Item item=player.invList.get(inventoryPos).get(0);
                String name = (inventoryPos) + ":" + player.invList.get(inventoryPos).get(0).getName();
                int y = (int) viewY + 130;
                Texture t= player.invList.get(inventoryPos).get(0).getIcon();
                int x = (int) (viewX + WIDTH - 290);
                ArrayList<String> outList=new ArrayList<>();
                if(item.getHpmod()!=0){outList.add("HP "+ item.getHpmod());}
                if(item.getManamod()!=0){outList.add("M :"+ item.getManamod());}//Mana
                if(item.getAttackmod()!=0){outList.add("ATT :"+ item.getAttackmod());}  //attack
                if(item.getDefensemod()!=0){outList.add("DEF :"+ item.getDefensemod());} //defense
                if(item.getIntelmod()!=0){outList.add("INT :"+ item.getIntelmod());}//intel
                if(item.getSpeedmod()!=0){outList.add("SPD :"+ item.getSpeedmod());}//speed


                sb.begin();
                Game.getFont().draw(sb, name, viewX + WIDTH - 290, viewY + 200 - 20);
                for(int i=0;i<outList.size();i++){
                    Game.getFont().draw(sb,outList.get(i),viewX + WIDTH - 290, viewY + 150 -((i+1)*20) - 20);
                }
                Game.getFont().draw(sb, "x" + player.invList.get(inventoryPos).size(), x, y);
                sb.draw(t, x, y);
                sb.end();
            }catch (IndexOutOfBoundsException |NullPointerException e){}
        }
    }
    public static void drawMiniMap(SpriteBatch sb){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLACK);
        shapeR.rect(viewX+ WIDTH-(Map2State.res+50),viewY+ HEIGHT-(Map2State.res+50),Map2State.res,Map2State.res);
        shapeR.end();

        drawGrid(true);

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(viewX+ WIDTH-(Map2State.res+50),viewY+ HEIGHT-(Map2State.res+50),Map2State.res,Map2State.res);
        shapeR.end();
    }
    public static void drawPopup(SpriteBatch sb){
        int x=(int)viewX+ mouseX;
        int y=(int)viewY+(HEIGHT- mouseY);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        //ColorConverter darkGrey=new ColorConverter(250,70,70,1);
        shapeR.setColor(new ColorConverter(20,20,20,1).getLIBGDXColor());
        shapeR.rect(x-300,y,300,200);
        shapeR.end();

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(new ColorConverter(70,70,70,1).getLIBGDXColor());
        shapeR.rect(x-300,y,300,200);
        shapeR.end();

        sb.begin();
        //Game.setFontSize(8);
        Game.font.setColor(Color.WHITE);
        ArrayList<String> popupList =new ArrayList<>();
        try {
            sb.draw(invIcon.get(qButtonBeingHovered), x - 40, y + 150);
            Item popupItem= player.invList.get(qButtonBeingHovered).get(0);
            popupList.add(popupItem.getName());
        }
        catch (IndexOutOfBoundsException e){
            //Game.printLOG(e);
        }
        if(popupItem.getHpmod()!=0){
            popupList.add("HP "+ popupItem.getHpmod());
        }
        //Mana
        if(popupItem.getManamod()!=0){
            popupList.add("M :"+ popupItem.getManamod());
        }
        //attack
        if(popupItem.getAttackmod()!=0){
            popupList.add("ATT :"+ popupItem.getAttackmod());
        }
        //defense
        if(popupItem.getDefensemod()!=0){
            popupList.add("DEF :"+ popupItem.getDefensemod());
        }
        //intel
        if(popupItem.getIntelmod()!=0){
            popupList.add("INT :"+ popupItem.getIntelmod());

        }
        //speed
        if(popupItem.getSpeedmod()!=0){
            popupList.add("SPD :"+ popupItem.getSpeedmod());

        }
        for(int i=0;i<popupList.size();i++){
            Game.getFont().draw(sb,popupList.get(i),x-290,y+190-(i*12));
        }
        sb.end();
    }
    public static void loadParticleEffects(int pos) {
        effect = new ParticleEffect();
        emitter = new ParticleEmitter();
        String s = player.attackList.get(attackListCount + pos).getName();
        effect.load(Gdx.files.internal("particles/pt" + s), Gdx.files.internal("particles"));
        effect.setPosition(player.getPX(), player.getPY());
        effectLoaded = true;
        emitter = effect.findEmitter(s);
        //effect.scaleEffect(2f);
        //checkAttackHit(pos);
    }
    public static void drawParticleEffects(SpriteBatch sb, float x, float y) {
        particleAngleHandler();
        sb.begin();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
        effect.start();
        sb.end();
    }
    private static void particleAngleHandler() {
        EmitterAngles.getAttackIndex(emitter.getName());
        ParticleEmitter.ScaledNumericValue angle = emitter.getAngle();
        switch (lastPressed) {
            case ('w'): {
                //angle.setHigh(EmitterAngles.angleHigh[0]);
                //angle.setLow(EmitterAngles.angleLow[0]);
                if (EmitterAngles.angleHHigh != null) {
                    angle.setHighMax(EmitterAngles.angleHHigh[0]);
                    angle.setHighMin(EmitterAngles.angleHigh[0]);
                } else {
                    angle.setHigh(EmitterAngles.angleHigh[0]);
                }
                if (EmitterAngles.angleLHigh != null) {
                    angle.setLowMax(EmitterAngles.angleLHigh[0]);
                    angle.setLowMin(EmitterAngles.angleLow[0]);
                } else {
                    angle.setLow(EmitterAngles.angleLow[0]);
                }
                break;
            }
            case ('a'): {
                if (EmitterAngles.angleHHigh != null) {
                    angle.setHighMax(EmitterAngles.angleHHigh[1]);
                    angle.setHighMin(EmitterAngles.angleHigh[1]);
                } else {
                    angle.setHigh(EmitterAngles.angleHigh[1]);
                }
                if (EmitterAngles.angleLHigh != null) {
                    angle.setLowMax(EmitterAngles.angleLHigh[1]);
                    angle.setLowMin(EmitterAngles.angleLow[1]);
                } else {
                    angle.setLow(EmitterAngles.angleLow[1]);
                }
                break;
            }
            case ('s'): {
                if (EmitterAngles.angleHHigh != null) {
                    angle.setHighMax(EmitterAngles.angleHHigh[2]);
                    angle.setHighMin(EmitterAngles.angleHigh[2]);
                } else {
                    angle.setHigh(EmitterAngles.angleHigh[2]);
                }
                if (EmitterAngles.angleLHigh != null) {
                    angle.setLowMax(EmitterAngles.angleLHigh[2]);
                    angle.setLowMin(EmitterAngles.angleLow[2]);
                } else {
                    angle.setLow(EmitterAngles.angleLow[2]);
                }
                break;
            }
            case ('d'): {
                if (EmitterAngles.angleHHigh != null) {
                    angle.setHighMax(EmitterAngles.angleHHigh[3]);
                    angle.setHighMin(EmitterAngles.angleHigh[3]);
                } else {
                    angle.setHigh(EmitterAngles.angleHigh[3]);
                }
                if (EmitterAngles.angleLHigh != null) {
                    angle.setLowMax(EmitterAngles.angleLHigh[3]);
                    angle.setLowMin(EmitterAngles.angleLow[3]);
                } else {
                    angle.setLow(EmitterAngles.angleLow[3]);
                }
                break;
            }
        }
    }
    public static void drawPlayerDamageOutput(SpriteBatch sb, float x, float y) {
        sb.begin();
        //Game.setFontSize(20);
        Game.font.setColor(1f,0f,0f,1f);
        Game.getFont().draw(sb, "-" + player.getDamage(), x*cellW, y*cellW);
        sb.end();
    }
    public static void drawStatChanges(SpriteBatch sb){
        sb.begin();
        try {
            if (dtStatPopup < .4)
                sb.draw(statPopup, mHitX-(statPopup.getWidth()/2)-(cellW/4), mHitY+5);
        }
        catch (NullPointerException e){
            //Game.printLOG(e);
        }
        sb.end();
    }
    public static void drawPlayer(SpriteBatch sb){
        sb.begin();
        Texture t=Game.player.getIcon();
        sb.draw(t,player.getPX()-t.getWidth()/4, player.getPY()-t.getHeight()/4);
        sb.end();
    }
    public static void drawTiles(SpriteBatch sb){
        sb.begin();
        for(int i=0;i<drawList.size();i++){
            Cell c=drawList.get(i);
            try {
                sb.draw(c.getTile(), (cellW * c.getX()), cellW * c.getY());
                if(c.getItem() != null && c.getState()){
                    sb.draw(c.getItem().getIcon(),(cellW * c.getX()), cellW * c.getY());
                }
                if(c.hasWarp()){
                    sb.draw(ImageLoader.warp,(cellW * c.getX()), cellW * c.getY());
                }
            }catch (NullPointerException e){}
        }
        sb.end();
    }
    public static void drawTransparentThings(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i< GridManager.monsterList.size(); i++){
            Monster m= GridManager.monsterList.get(i);
            int side=m.getSight()*2*cellW;
            double x1=m.getPX()-(side/2)+(cellW/2);
            double y1=m.getPY()-(side/2)+(cellW/2);
            shapeR.setColor(1,0,0,.2f);
            shapeR.rect((int)x1,(int)y1,side,side);
        }
        for(Cell c:drawList){
            if(c.getAttArea()){
                shapeR.rect(c.getX()*cellW,c.getY()*cellW,cellW,cellW);
            }
        }
        shapeR.setColor(.1f, .1f, .1f, .2f);
        shapeR.rect(viewX,viewY, (float) WIDTH,HEIGHT);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(viewX+WIDTH/3,viewY,2,205);
        shapeR.rect(viewX+(WIDTH/3)*2,viewY,2,205);
        shapeR.rect(viewX,viewY+205,WIDTH,2);

        shapeR.setColor(.1f, .1f, .1f, .7f);
        if(showStats)
            shapeR.rect(viewX,viewY+HEIGHT-300, 300, 300);
        shapeR.rect(viewX,viewY, WIDTH,207);
        shapeR.end();
        Gdx.gl.glDisable(GL_BLEND);
        if(dtClearHits>.1) {
            for (Cell c : hitList) {
                dispArray[c.getX()][c.getY()].setAttArea(false);
            }
            dtClearHits=0;
        }
    }
    public static void drawGrid(boolean map) {
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        int tx= (int) (viewX + WIDTH - (Map2State.res + 50))+warpX;
        int ty=(int) (viewY + HEIGHT - (Map2State.res + 50))+warpY;
        int px= (int) (viewX + WIDTH - (Map2State.res + 50))+ player.getX();
        int py=(int) (viewY + HEIGHT - (Map2State.res + 50))+ player.getY();
        ArrayList<Cell> temp=GridManager.drawList;
        if(map){
            temp=GridManager.liveCellList;
        }
        for(Cell c: temp){
            int x=c.getX();
            int y=c.getY();
            shapeR.setColor(c.getColor());
            if(map && player.getX()==x && player.getY()==y){
                if(blink)
                    shapeR.setColor(0,0,1,1);
                else
                    shapeR.setColor(1,1,1,1);
            }
            if(map){
                int xa= (int) (viewX + WIDTH - (Map2State.res + 50))+x;
                int ya= (int) (viewY + HEIGHT - (Map2State.res + 50))+y;
                shapeR.rect(xa,ya, 1, 1);

            }
        }

        shapeR.end();
        if(map){
            shapeR.begin(ShapeRenderer.ShapeType.Line);
            shapeR.setColor(Color.GREEN);
            shapeR.circle(tx,ty,5);

            //shapeR.setColor(Color.BLUE);
            if(blink){shapeR.setColor(Color.BLUE);}
            else if(!blink){shapeR.setColor(Color.WHITE);}
            if(dtBlink>Game.frame*4){
                blink=!blink;
                blradius++;
                dtBlink=0;
            }
            shapeR.circle(px,py,blradius);
            if(blradius>5)blradius=0;
            shapeR.end();
        }

    }
}
