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
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.ColorConverter;
import com.quadx.dungeons.tools.HoverText;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.MainMenuState.gl;

/**
 * Created by Tom on 12/28/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateRender extends MapState {

    public static int[] statCompare=null;
    private static ArrayList<String> equipList=new ArrayList<>();
    private static Texture abilityIcon;
    public static boolean hovText=false;
    public static boolean showCircle=false;
    private static boolean blink=false;
    public static float dtCircle=1f;
    static float dtBlink =0;
    public static float dtWaterEffect=0;
    static float dtLootPopup=0;

    public static int inventoryPos=0;
    static float dtHovBuffTime=0;
    static int blradius=0;
    private static int prevMod=0;//checks if Ability has changed
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
            while (hoverTexts.size()>4)hoverTexts.remove(0);
        }
    }
    public static void loadAttackIcons(){
       if(player.attackList.size() !=attackIconList.size()) {
            for (Attack attack : player.attackList) {
                String s = attack.getName();
                try {
                    attackIconList.add(new Texture(Gdx.files.internal("images/icons/attacks/ic" + s + ".png")));
                } catch (GdxRuntimeException e) {
                    //Game.printLOG(e);

                }
            }
        }
    }
    public static void drawAbilityIcon(SpriteBatch sb){
        if(AbilityMod.modifier !=prevMod) {
            String s = "";
            switch (AbilityMod.modifier) {
                case 1: {//DigPlus
                    s = "DigPlus.png";
                    prevMod=1;
                    break;
                }
                case 2: {//Brawler
                    s = "Brawler.png";
                    prevMod=2;
                    break;
                }
                case 3: {//Mage
                    s = "Mage.png";
                    prevMod=3;
                    break;
                }
                case 4: {//Quick
                    s = "Quick.png";
                    prevMod=4;
                    break;
                }
                case 5: {//Investor
                    s = "Investor.png";
                    prevMod=5;
                    break;
                }
                case 6: {//Tank
                    s = "Tank.png";
                    prevMod=6;
                    break;
                }
                case 7:{
                    s="Warp.png";
                    prevMod=7;
                    break;
                }

            }
            try {

                    abilityIcon = new Texture(Gdx.files.internal("images/icons/abilities/ic" + s));

            } catch (GdxRuntimeException e) {
                Game.printLOG(e);
            }
        }
        if(prevMod !=0){
            //MapState.out("Fucking Errors");
            sb.begin();
            sb.draw(abilityIcon,viewX+50,viewY+50);
            sb.end();
        }
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
        CharSequence cs= player.getPoints()+"";
        gl.setText(Game.getFont(),cs);
        Game.getFont().setColor(Color.WHITE);
        Game.getFont().draw(sb, player.getPoints()+"",(int)(viewX+Game.WIDTH/2-(gl.width/2)) ,(int)(viewY+ Game.HEIGHT-50));
        try {
            if (dtLootPopup < .4)
                sb.draw(lootPopup, player.getPX()-(lootPopup.getWidth()/2)-(cellW/4), player.getPY()+5);
        }
        catch (NullPointerException e){
            //Game.printLOG(e);
        }
        for(int i=0;i<gm.res;i+=10)
            Game.getFont().draw(sb,i+"",i*cellW,-10);
        sb.end();



    }
    public static void drawMessageOutput(SpriteBatch sb){

        sb.begin();
        Game.setFontSize(1);
        Game.font.setColor(Color.WHITE);
        for(int i=0;i<10;i++){
            //if(output.size()>=0&&i+messageCounter<output.size()  )
            try {
                Game.getFont().draw(sb, output.get(i), viewX+30,viewY+300-(i * 20));
            }
            catch(IndexOutOfBoundsException e){
                System.out.println((i+messageCounter)+"  Message out failed "+output.size());
                //Game.printLOG(e);
            }
        }
        sb.end();
    }
    private static void drawStats(SpriteBatch sb, float x, float y) {
        sb.begin();//Draw STATS
        int hpDiffx = 0;
        int margin = 30;
        double pHealthMax = player.getHpMax();
        double pHealth = player.getHp();
        double pMana = player.getMana();
        double pManaMax = player.getManaMax();
        double pEnergyMax= player.getEnergyMax();
        double pEnergy= player.getEnergy();
        int barWidth= 4;
        double pHPBarMax = (Game.WIDTH / barWidth) - margin - 15;
        double pHPBar = (pHealth / pHealthMax) * (pHPBarMax - hpDiffx);
        double pManaBarMax = (Game.WIDTH / barWidth) - margin - 15;
        double pManaBar = (pMana / pManaMax * pManaBarMax);
        double pEnergyBarMax=(Game.WIDTH/barWidth)-margin-15;
        double pEnergyBar=(pEnergy/pEnergyMax*pEnergyBarMax);

        Game.setFontSize(1);
        Game.font.setColor(Color.WHITE);
        ArrayList<String> a = player.getStatsList();
        for (int i = 0; i < a.size(); i++) {
            if(statCompare != null && i-2<statCompare.length && i-2>=0){
                switch (statCompare[i-2]){
                    case 1:{Game.font.setColor(Color.BLUE);break;}
                    case 2:{Game.font.setColor(Color.RED);break;}
                    case 0:{Game.font.setColor(Color.WHITE);break;
                    }
                }
            }
            Game.getFont().draw(sb, a.get(i), x + 30, y + Game.HEIGHT - 75 - (i * 20));
        }
        sb.end();

        //DRAW HP BARS
        shapeR.begin(ShapeRenderer.ShapeType.Filled);

        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(0f, 1f, 0f, 1);
        if (player.getHp() < player.getHpMax() / 2) {
            shapeR.setColor(1f, 0f, 0f, 1);
        }
        shapeR.rect(x + margin, y + Game.HEIGHT - 30, (int) pHPBar, 10);
        shapeR.setColor(0f, 0f, 1f, 1);
        shapeR.rect(x + margin, y + Game.HEIGHT - 45, (int) pManaBar, 10);
        shapeR.setColor(1f,1f,0,1);
        shapeR.rect(x + margin, y + Game.HEIGHT - 60, (int) pEnergyBar, 10);

        shapeR.end();
    }
    public static void drawEquipment(SpriteBatch sb){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GRAY);
        for (int i = 0; i < 2; i++) {
        for(int j=0;j<4;j++) {
                shapeR.rect(viewX + 30+ (j * 36), viewY + (Game.HEIGHT / 2)+ (i * 36)-20, 32, 32);
            }
        }
        shapeR.end();

        sb.begin();
        int count =0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    sb.draw(player.equipedList.get(count).getIcon(), viewX + 30 + (j * 36), viewY + (Game.HEIGHT / 2) + (i * 36)-20);
                    count++;
                }
                catch (IndexOutOfBoundsException e){
                    //Game.printLOG(e);

                }
            }
        }
            sb.end();

    }
    private static void drawAttackMenu(SpriteBatch sb) {
        int xoffset= (int) (viewX+(Game.WIDTH/2)-(52*4));
        sb.begin();
        for(int i = 0; i< attackIconList.size(); i++){
            if(player.getMana()>= player.attackList.get(i).getCost()) {
                sb.draw(attackIconList.get(i), xoffset  + (i * 52), viewY + 48);
                if(i<=7) Game.getFont().draw(sb,(i+1)+"",xoffset+ (i * 52), viewY + 58);
            }
            else{
                int rem= player.attackList.get(i).getCost()- player.getMana();
                Game.getFont().draw(sb,rem+"",xoffset  + (i * 52)+52/2,viewY + 70);
            }
            Game.getFont().draw(sb,"Lv."+(player.attackList.get(i).getLevel()+1),xoffset + (i * 52), viewY + 48);
            Game.getFont().draw(sb,"M"+ player.attackList.get(i).getCost(),xoffset + (i * 52),viewY+30);
            if(i==altNumPressed){
                Game.getFont().draw(sb,"LT ALT",xoffset + (i * 52), viewY + 95);
            }
            if(i==lastNumPressed){
                Game.getFont().draw(sb,"RT SPACE",xoffset + (i * 52), viewY + 108);
            }
        }

        sb.end();
    }
    private static void drawInventory(SpriteBatch sb) {
        if(!player.invList.isEmpty() && inventoryPos >-1) {
            try {
                Item item=player.invList.get(inventoryPos).get(0);
                String name = (inventoryPos) + ":" + player.invList.get(inventoryPos).get(0).getName();
                int y = (int) viewY + 150;
                Texture t= player.invList.get(inventoryPos).get(0).getIcon();
                int x = (int) (viewX + Game.WIDTH - 300);
                ArrayList<String> outList=new ArrayList<>();
                if(item.getHpmod()!=0){outList.add("HP "+ item.getHpmod());}
                if(item.getManamod()!=0){outList.add("M :"+ item.getManamod());}//Mana
                if(item.getAttackmod()!=0){outList.add("ATT :"+ item.getAttackmod());}  //attack
                if(item.getDefensemod()!=0){outList.add("DEF :"+ item.getDefensemod());} //defense
                if(item.getIntelmod()!=0){outList.add("INT :"+ item.getIntelmod());}//intel
                if(item.getSpeedmod()!=0){outList.add("SPD :"+ item.getSpeedmod());}//speed


                sb.begin();
                Game.getFont().draw(sb, name, viewX + Game.WIDTH - 300, viewY + 100 - 20);
                for(int i=0;i<outList.size();i++){
                    Game.getFont().draw(sb,outList.get(i),viewX + Game.WIDTH - 300, viewY + 100 -((i+1)*20) - 20);
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
        shapeR.rect(viewX+Game.WIDTH-(Map2State.res+50),viewY+Game.HEIGHT-(Map2State.res+50),Map2State.res,Map2State.res);
        shapeR.end();

        drawGrid(true);

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(viewX+Game.WIDTH-(Map2State.res+50),viewY+Game.HEIGHT-(Map2State.res+50),Map2State.res,Map2State.res);
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
        /*if(effect.isComplete()) {
            effect.dispose();
            effect.reset();
        }*/
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
    public static void drawMonsterAgro(){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i< GridManager.monsterList.size(); i++){
            Monster m= GridManager.monsterList.get(i);
            int side=m.getSight()*2*cellW;
            double x1=m.getPX()-(side/2)+(cellW/2);
            double y1=m.getPY()-(side/2)+(cellW/2);
            shapeR.setColor(1,0,0,.2f);
            shapeR.rect((int)x1,(int)y1,side,side);
        }
        shapeR.setColor(.1f, .1f, .1f, .5f);
        shapeR.rect(viewX,viewY, (float) (Game.WIDTH/3.5),Game.HEIGHT);
        //shapeR.rect((float) (viewX+Game.WIDTH-(Game.WIDTH/3.5)),viewY, (float) (Game.WIDTH/3.5),Game.HEIGHT);

        shapeR.end();
    }
    public static void drawGrid(boolean map) {
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        int tx= (int) (viewX + Game.WIDTH - (Map2State.res + 50))+warpX;
        int ty=(int) (viewY + Game.HEIGHT - (Map2State.res + 50))+warpY;
        int px= (int) (viewX + Game.WIDTH - (Map2State.res + 50))+ player.getX();
        int py=(int) (viewY + Game.HEIGHT - (Map2State.res + 50))+ player.getY();
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

            if(dtWaterEffect>Game.frame*60){
                if (blink&& rn.nextBoolean())
                    c.setColor( new Color(.12f,.12f,.8f,1f));
                else if(blink )
                    c.setColor( new Color(0f,0f,.8f,1f));
                else {
                    c.setColor( new Color(.07f, .07f, .8f, 1f));
                }
                dtWaterEffect=0;
            }
            else dtWaterEffect+=Gdx.graphics.getDeltaTime();

            try {
                if (c.getWater()) shapeR.setColor(c.getColor());
            }catch (NullPointerException e){}

            if(!map)
                shapeR.rect((cellW * x), cellW * y, cellW, cellW);
            else {
                int xa= (int) (viewX + Game.WIDTH - (Map2State.res + 50))+x;
                int ya= (int) (viewY + Game.HEIGHT - (Map2State.res + 50))+y;
                shapeR.rect(xa,ya, 1, 1);
            }
        }

        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        if(map){
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
        }
        shapeR.end();
        for(Cell c: hitList){
          //  c.setAttArea(false);
        }
    }
}
