package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.EmitterAngles;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.QButton;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.ColorConverter;

import java.util.ArrayList;

/**
 * Created by Tom on 12/28/2015.
 */
public class MapStateRender extends MapState {
   public static int inventoryPos=0;

    public static ArrayList<String> equipList=new ArrayList<>();
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
        //equipList.add();
    }
    public static void loadAttackIcons(){
       if(Game.player.attackList.size() !=attackIconList.size()) {
            for (Attack attack : Game.player.attackList) {
                String s = attack.getName();
                try {
                    attackIconList.add(new Texture(Gdx.files.internal("images/icons/attacks/ic" + s + ".png")));
                } catch (GdxRuntimeException e) {

                }
            }
        }
    }
    public static void loadEquipIcons(){
        if(equipIcon.size() != Game.player.equipedList.size()) {
            equipIcon.clear();
            for (Equipment eq : Game.player.equipedList) {
                String s;
                s = eq.getType();
                try {
                    equipIcon.add(new Texture(Gdx.files.internal("images/icons/items/ic" + s + ".png")));

                } catch (GdxRuntimeException e) {

                }
            }
        }
    }
    public static void loadInventoryIcons(){
        if(invIcon.size() != Game.player.invList.size()) {
            invIcon.clear();
            invSize.clear();

            for (ArrayList<Item> list : Game.player.invList) {
                Item item = list.get(0);
                invSize.add(list.size());
                String s = item.getName();
                if (item.isEquip)
                    s = item.getType();
                if (item.isSpell)
                    s = "SpellBook";
                try {
                    invIcon.add(new Texture(Gdx.files.internal("images/icons/items/ic" + s + ".png")));

                } catch (GdxRuntimeException e) {

                }
            }
        }

    }
    public static void drawHUD(SpriteBatch sb) {
        drawPlayerStats(sb, viewX, viewY);
        drawAttackMenu(sb);
        drawInventory(sb);
        drawMiniMap(sb);
        sb.begin();
        try {
            if (dtLootPopup < .4)
                sb.draw(lootPopup, Game.player.getPX()-(lootPopup.getWidth()/2)-(cellW/4), Game.player.getPY()+5);
        }
        catch (NullPointerException e){

        }
        sb.end();
    }
    public static void drawMessageOutput(SpriteBatch sb){

        sb.begin();
        //Game.setFontSize(10);
        Game.font.setColor(Color.WHITE);
        for(int i=0;i<10;i++){
            //if(output.size()>=0&&i+messageCounter<output.size()  )
            try {
                Game.getFont().draw(sb, output.get(i), viewX+30,viewY+100+(i * 20));
            }
            catch(IndexOutOfBoundsException e){
                System.out.println((i+messageCounter)+"  Message out failed "+output.size());
            }
        }
        sb.end();
    }
    public static void drawPlayerStats(SpriteBatch sb, float x, float y) {
        sb.begin();//Draw STATS
        int hpDiffx = 0;
        int hpDiff = 0;
        int mode = 1;
        int margin = 30;
        double pHealthMax = Game.player.getHpMax();
        double pHealth = Game.player.getHp();
        double pMana = Game.player.getMana();
        double pManaMax = Game.player.getManaMax();
        double pEnergyMax=Game.player.getEnergyMax();
        double pEnergy=Game.player.getEnergy();
        int barWidth = 1;
        if (mode == 1) barWidth = 4;
        if (mode == 2) barWidth = 3;
        double pHPBarMax = (Game.WIDTH / barWidth) - margin - 15;
        double pHPBar = (pHealth / pHealthMax) * (pHPBarMax - hpDiffx);
        double pManaBarMax = (Game.WIDTH / barWidth) - margin - 15;
        double pManaBar = (pMana / pManaMax * pManaBarMax);
        double pEnergyBarMax=(Game.WIDTH/barWidth)-margin-15;
        double pEnergyBar=(pEnergy/pEnergyMax*pEnergyBarMax);

        //Game.setFontSize(10);
        Game.font.setColor(.5f, .5f, .5f, 1);
        ArrayList<String> a = Game.player.getStatsList();
        for (int i = 0; i < a.size(); i++) {
            Game.getFont().draw(sb, a.get(i), x + 30, y + Game.HEIGHT - 75 - (i * 20));
        }
        sb.end();

        //DRAW HP BARS
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        if (mode == 1) shapeR.setColor(.5f, .5f, .5f, 1);
        if (mode == 2) shapeR.setColor(.0f, .0f, .0f, 1);
        shapeR.setColor(0f, 1f, 0f, 1);
        if (Game.player.getHp() < Game.player.getHpMax() / 2) {
            shapeR.setColor(1f, 0f, 0f, 1);
        }
        shapeR.rect(x + margin, y + Game.HEIGHT - 30, (int) pHPBar, 10);
        shapeR.setColor(0f, 0f, 1f, 1);
        shapeR.rect(x + margin, y + Game.HEIGHT - 45, (int) pManaBar, 10);
        shapeR.setColor(1f,1f,0,1);
        shapeR.rect(x + margin, y + Game.HEIGHT - 60, (int) pEnergyBar, 10);

        shapeR.end();
    }
    public static void drawPlayerEquipment(SpriteBatch sb){
        equipList.clear();
        equipList.add("EQUIPMENT");
        equipList.add("HELMET");
        equipList.add("CHEST");
        equipList.add("ARMS");
        equipList.add("GLOVES");
        equipList.add("LEGS");
        equipList.add("BOOTS");
        equipList.add("CAPE");
        equipList.add("RING 1");
        //equipList.add("RING 2");

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GRAY);
        for (int i = 0; i < 2; i++) {
        for(int j=0;j<4;j++) {
                shapeR.rect(viewX + 30+ (j * 36), viewY + (Game.HEIGHT / 2)+ (i * 36), 32, 32);
            }
        }
        shapeR.end();

        sb.begin();
        for(int i=0;i<equipList.size();i++) {
           // Game.font.draw(sb,equipList.get(i),viewX+Game.WIDTH-200,viewY+Game.HEIGHT-30-(i*36));
        }
        int count =0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    sb.draw(equipIcon.get(count), viewX + 30 + (j * 36), viewY + (Game.HEIGHT / 2) + (i * 36));
                    count++;
                }
                catch (IndexOutOfBoundsException e){

                }
            }
        }
            sb.end();
    }
    public static void drawAttackMenu(SpriteBatch sb) {
        int xoffset= (int) (viewX+(Game.WIDTH/2)-(52*4));
        sb.begin();
        for(int i = 0; i< attackIconList.size(); i++){
            if(Game.player.getMana()>= Game.player.attackList.get(i).getCost()) {
                sb.draw(attackIconList.get(i), xoffset  + (i * 52), viewY + 48);
                if(i<=7) Game.getFont().draw(sb,(i+1)+"",xoffset+ (i * 52), viewY + 58);
            }
            Game.getFont().draw(sb,"Lv."+(Game.player.attackList.get(i).getLevel()+1),xoffset + (i * 52), viewY + 48);
            Game.getFont().draw(sb,"M"+ Game.player.attackList.get(i).getCost(),xoffset + (i * 52),viewY+30);
        }

        sb.end();
    }
    public static void drawInventory(SpriteBatch sb) {

        sb.begin();
        for(int i=0;i<3;i++){
            try {

                Texture t = invIcon.get(inventoryPos + i);
                int x=(int)(viewX+(Game.WIDTH -(200-(i*30))) + (t.getWidth() * (i)));
                int y=(int)viewY+ 100;
                sb.draw(t,x ,y);
                Game.getFont().draw(sb, "x" + Game.player.invList.get(inventoryPos+i).size(),x, y);
                if(i==1){
                    Game.getFont().draw(sb,Game.player.invList.get(inventoryPos+i).get(0).getName(),x-20,y-20);
                }

            }
            catch (IndexOutOfBoundsException e){
            }
        }
        /*
        qButtonList.clear();
        int count=0;
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++){
                if(count< Game.player.invList.size()){
                    int x=(int) viewX + Game.WIDTH - 200 + +(j * 40);
                    int y=(int)viewY +  (Game.HEIGHT/2) - (i * 48);

                    try {
                        sb.draw(invIcon.get(count), x,y);
                        qButtonList.add(new QButton(x,y,invIcon.get(count).getWidth(), invIcon.get(count).getHeight()));
                        QButton q= qButtonList.get(qButtonList.size()-1);
                        //out("%"+x+" "+y);
                        if(i==0 && j==0){
                            //out("icon x"+x +" y"+y);
                            //out("button "+bx+" "+by);
                        }
                        Game.getFont().draw(sb, "x" + invSize.get(count),x, y);
                        count++;
                    }
                    catch (IndexOutOfBoundsException e){

                    }
                }
            }
        }*/
        sb.end();
    }
    public static void drawMiniMap(SpriteBatch sb){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLACK);
        shapeR.rect(viewX+Game.WIDTH-250,viewY+Game.HEIGHT-250,200,200);
        shapeR.end();

        drawGrid(true);

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(viewX+Game.WIDTH-250,viewY+Game.HEIGHT-250,200,200);
        shapeR.end();
    }
    public static void drawPopup(SpriteBatch sb){
        int x=(int)viewX+ mouseX;
        int y=(int)viewY+(Game.HEIGHT- mouseY);
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
            Item popupItem= Game.player.invList.get(qButtonBeingHovered).get(0);
            popupList.add(popupItem.getName());
        }
        catch (IndexOutOfBoundsException e){

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
        String s = Game.player.attackList.get(attackListCount + pos).getName();
        effect.load(Gdx.files.internal("particles/pt" + s), Gdx.files.internal("particles"));
        effect.setPosition(Game.player.getPX(), Game.player.getPY());
        effectLoaded = true;
        emitter = effect.findEmitter(s);
        //effect.scaleEffect(2f);
        checkAttackHit(pos);
    }
    public static void drawParticleEffects(SpriteBatch sb, float x, float y) {
        particleAngleHandler();
        sb.begin();
        effect.draw(sb, Gdx.graphics.getDeltaTime());
        effect.start();
        sb.end();
        /*if(effect.isComplete()) {
            effect.dispose();
            effect.reset();
        }*/
    }
    public static void particleAngleHandler() {
        EmitterAngles.getAttackIndex(emitter.getName());
        ParticleEmitter.ScaledNumericValue angle = emitter.getAngle();
        if(emitter.getName().equals("Protect")) {
            //effect.setDuration(4000);
            //emitter.setContinuous(true);
        }
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
        Game.font.setColor(1, .5f, .5f, 1);
        Game.getFont().draw(sb, "" + playerDamage, x, y);
        sb.end();
    }
    public static void drawStatChanges(SpriteBatch sb){
        sb.begin();
        try {
            if (dtStatPopup < .4)
                sb.draw(statPopup, mHitX-(statPopup.getWidth()/2)-(cellW/4), mHitY+5);
        }
        catch (NullPointerException e){

        }
        sb.end();
    }
    public static void drawPlayer(SpriteBatch sb){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLUE);
        shapeR.rect(Game.player.getPX(), Game.player.getPY(),cellW,cellW);
        shapeR.end();
    }
    public static void drawMonsterAgro(){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(int i=0;i<gm.monsterList.size();i++){
            Monster m= gm.monsterList.get(i);
            int side=m.getSight()*2*cellW;
            double x1=m.getPX()-(side/2)+(cellW/2);
            double y1=m.getPY()-(side/2)+(cellW/2);
            shapeR.setColor(1,0,0,.2f);
            shapeR.rect((int)x1,(int)y1,side,side);
        }
        shapeR.end();
    }
    public static void drawGrid(boolean map) {
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(Cell c:gm.liveCellList){
            int x=c.getX();
            int y=c.getY();
            if(c.getState())shapeR.setColor(.235f, .235f, .196f, 1);
            else            shapeR.setColor(0f, 1f, 0f, 1);
            if(c.hasLoot()) shapeR.setColor(1f, .647f, 0f, 1);
            if(c.hasCrate())shapeR.setColor(.627f, .322f, .176f, 1);
            if(c.getShop()) shapeR.setColor(1f, 0f, 1f, 1);
            if(c.hasWarp()) shapeR.setColor(0f, 1f, 0f, 1);
            if(c.hasMon())  shapeR.setColor(1,0,0,1);
            ColorConverter water = new ColorConverter(12,41,155,1);
            if(c.getWater())shapeR.setColor(water.getLIBGDXColor());

            if(!map)
                shapeR.rect((cellW * x), cellW * y, cellW, cellW);
            else
                shapeR.rect(viewX+Game.WIDTH-250+x, viewY+Game.HEIGHT-250+y, 1, 1);

        }
        shapeR.end();

    }
}
