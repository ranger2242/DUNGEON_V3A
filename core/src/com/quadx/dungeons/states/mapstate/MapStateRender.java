package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.tools.HoverText;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.GridManager.drawList;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.dtClearHits;

/**
 * Created by Tom on 12/28/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateRender extends MapState {


    public static boolean showCircle=false;
    private static boolean blink=false;
    static boolean showStats=false;
    private static float dtBlink =0;
    static float dtLootPopup=0;
    private static float dtCircle=1f;
    public static float dtWaterEffect=0;
    private static int blradius=0;
    public static int inventoryPos=0;
    public static int[] statCompare=null;

    public static void updateVariables(float dt){
        dtBlink+=dt;
        dtWaterEffect+=dt;
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
    private static void updateHoverTextTime(){
        HoverText.texts.forEach(HoverText::updateDT);
    }
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
    }
    public static void setHoverText(String s, float time, Color color, int x, int y,boolean flash){
            new HoverText(s, color, x, y, time, flash);
    }
    public static void drawHovText(SpriteBatch sb){
        for(HoverText h:HoverText.texts){
            h.draw(sb);
        }
        //delete inactive hoverText
        boolean[] index;
        if(!HoverText.texts.isEmpty()){
            index= new boolean[HoverText.texts.size()];
            HoverText.texts.stream().filter(h -> !h.isActive()).forEach(h -> index[HoverText.texts.indexOf(h)] = true);
            for(int i=HoverText.texts.size()-1;i>=0;i--){
                if(index[i]) {
                    HoverText.texts.remove(i);
                }
            }
            while (HoverText.texts.size()>10)HoverText.texts.remove(0);
        }
    }
    public static void drawAbilityIcon(SpriteBatch sb){
            sb.begin();
            sb.draw(ImageLoader.abilities.get(player.getAbility()),viewX+((WIDTH/3)*2)+30,viewY+20);
            sb.end();
    }
    public static void drawPlayerFinder(){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.circle(player.getPX(), player.getPY(),100*dtCircle);
        shapeR.end();
    }
    public static void drawCircle(int x, int y, float r, Color c){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(c);
        shapeR.circle(x, y,r);
        shapeR.end();
    }
    private static void drawMonsterInfo(SpriteBatch sb){
        Game.setFontSize(1);
        for(Monster m: GridManager.monsOnScreen){//for every monster on screen
            sb.begin();
            Game.getFont().draw(sb,"LVL "+m.getLevel(),m.getX()*cellW-22,m.getY()*cellW-10);
            if(m.isHit())
                Game.getFont().draw(sb,"!" , m.getX()*cellW, (float) ((m.getY()+1.5)*cellW));
            sb.end();
            shapeR.begin(ShapeRenderer.ShapeType.Filled);
            shapeR.setColor(Color.DARK_GRAY);
            shapeR.rect(m.getX()*cellW-22,m.getY()*cellW-31, (float) (84),6);
            if(m.getHp()>m.getHpMax()/3)
                shapeR.setColor(Color.GREEN);
            else
                shapeR.setColor(Color.RED);
            shapeR.rect(m.getX()*cellW-20,m.getY()*cellW-30, (float) (80*m.getPercentHP()),4);
            shapeR.end();
        }
    }
    static void drawLootPopup(SpriteBatch sb) {
        sb.begin();
        try {
            if (dtLootPopup < .4) {
                Texture t = player.getLastItem().getIcon();
                sb.draw(t, player.getPX(), player.getPY() + 40);
           }
        } catch (NullPointerException e) {}
        sb.end();
    }
    public static void drawHUD(SpriteBatch sb) {
        drawMonsterInfo(sb);
        drawStats(sb, viewX, viewY);
        drawAttackMenu(sb);
        drawInventory(sb);
        drawLootPopup(sb);
    }
    public static void drawMessageOutput(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(1);
        Game.font.setColor(Color.WHITE);
        for(int i=0;i<10;i++){
            try {
                Game.getFont().draw(sb, output.get(i), viewX+30,viewY+195-(i * 20));
            }
            catch(IndexOutOfBoundsException e){}
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
                        case 1: {Game.font.setColor(Color.BLUE);break;}
                        case 2: {Game.font.setColor(Color.RED);break;}
                        case 0: {Game.font.setColor(Color.WHITE);break;}
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
    private static void drawBars(){
        double barMax = (WIDTH/3)-10;
        double pHPBar = ((float) player.getHp() / (float) player.getHpMax()) * barMax;
        double pManaBar = ((float)player.getMana() / (float)player.getManaMax()) * barMax;
        double pEnergyBar=((float)player.getEnergy()/(float)player.getEnergyMax())*barMax;

        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(0f, 1f, 0f, 1);
        if (player.getHp() < player.getHpMax() / 2) {
            shapeR.setColor(1f, 0f, 0f, 1);
        }
        int barsX= (int) (viewX+(WIDTH/3)+5);
        shapeR.rect(barsX,viewY+175 , (float) pHPBar, 10);
        shapeR.setColor(0f, 0f, 1f, 1);
        shapeR.rect(barsX, viewY+160, (float) pManaBar, 10);
        shapeR.setColor(1f,1f,0,1);
        shapeR.rect(barsX, viewY+145, (float) pEnergyBar, 10);
        shapeR.end();
    }

    public static void drawEquipment(SpriteBatch sb) {
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GRAY);
        int x = (int) (viewX + ((WIDTH / 3) * 2) + 15);
        int y = (int) (viewY + 130);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                shapeR.rect(x + (j * 36), y + (i * 36) - 20, 32, 32);
            }
        }
        shapeR.end();
        sb.begin();
        int count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    sb.draw(player.equipedList.get(count).getIcon(), x + (j * 36), y + (i * 36) - 20);
                    count++;
                } catch (IndexOutOfBoundsException e) {}
            }
        }
        sb.end();
    }

    private static void drawAttackMenu(SpriteBatch sb) {
        int xoffset = (int) (viewX + (WIDTH / 2) - (52 * 4));
        sb.begin();
        for (int i = 0; i < player.attackList.size(); i++) {
            Attack a = player.attackList.get(i);
            int type = a.getType();
            int x=xoffset + (i * 52);
            try {
                if (type == 3 || type == 2) {
                    if (player.getMana() >= a.getCost()) {
                        sb.draw(a.getIcon(), x, viewY + 48);
                        if (i <= 7) Game.getFont().draw(sb, (i + 1) + "", x, viewY + 58);
                    } else {
                        int rem = a.getCost() - player.getMana();
                        Game.getFont().draw(sb, rem + "",x + 52 / 2, viewY + 70);
                    }
                    Game.getFont().draw(sb, "M" + a.getCost(),x, viewY + 30);
                } else if (type == 1) {
                    if (player.getEnergy() >= a.getCost()) {
                        sb.draw(a.getIcon(), x, viewY + 48);
                        if (i <= 7) Game.getFont().draw(sb, (i + 1) + "",x, viewY + 58);
                    } else {
                        int rem = a.getCost() - player.getEnergy();
                        Game.getFont().draw(sb, rem + "",x + 52 / 2, viewY + 70);
                    }
                    Game.getFont().draw(sb, "E" + a.getCost(), x, viewY + 30);
                }
                Game.getFont().draw(sb, "Lv." + (a.getLevel() + 1), x, viewY + 48);
                if (i == altNumPressed) {
                    Game.getFont().draw(sb, "LT ALT",x, viewY + 105);
                }
                if (i == lastNumPressed) {
                    Game.getFont().draw(sb, "RT SPACE",x, viewY + 118);
                }
            } catch (NullPointerException e) {
            }
        }
        sb.end();
    }
    private static void drawInventory(SpriteBatch sb) {
        if(!player.invList.isEmpty() && inventoryPos >-1) {
            try {
                Item item=player.invList.get(inventoryPos).get(0);
                String name = (inventoryPos) + ":" + item.getName();
                int y = (int) viewY + 130;
                Texture t= item.getIcon();
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
    public static void drawMiniMap(){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLACK);
        shapeR.rect(viewX+ WIDTH-(Map2State.res+50),viewY+ HEIGHT-(Map2State.res+50),Map2State.res,Map2State.res);
        shapeR.end();

        drawGrid();

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(viewX+ WIDTH-(Map2State.res+50),viewY+ HEIGHT-(Map2State.res+50),Map2State.res,Map2State.res);
        shapeR.end();
    }

    public static void drawPlayer(SpriteBatch sb){
        sb.begin();
        Texture t=Game.player.getIcon();
        sb.draw(t,player.getPX()-t.getWidth()/4, player.getPY()-t.getHeight()/4);
        sb.end();
    }
    public static void drawTiles(SpriteBatch sb){
        sb.begin();
        for (Cell c : drawList) {
            try {
                sb.draw(c.getTile(), (cellW * c.getX()), cellW * c.getY());
                if (c.getItem() != null && c.getState()) {
                    sb.draw(c.getItem().getIcon(), (cellW * c.getX()), cellW * c.getY());
                }
                if (c.hasWarp()) {
                    sb.draw(ImageLoader.warp, (cellW * c.getX()), cellW * c.getY());
                }
            } catch (NullPointerException e) {
            }
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
        drawList.stream().filter(c -> c.getAttArea()).forEach(c -> {
            shapeR.rect(c.getX() * cellW, c.getY() * cellW, cellW, cellW);
        });
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
    private static void drawGrid() {
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        int tx= (int) (viewX + WIDTH - (Map2State.res + 50))+warpX;
        int ty=(int) (viewY + HEIGHT - (Map2State.res + 50))+warpY;
        int px= (int) (viewX + WIDTH - (Map2State.res + 50))+ player.getX();
        int py=(int) (viewY + HEIGHT - (Map2State.res + 50))+ player.getY();
        for(Cell c: GridManager.liveCellList){
            int x=c.getX();
            int y=c.getY();
            shapeR.setColor(c.getColor());
            if(player.getX() == x && player.getY() == y){
                if(blink)
                    shapeR.setColor(0,0,1,1);
                else
                    shapeR.setColor(1,1,1,1);
            }
            int xa= (int) (viewX + WIDTH - (Map2State.res + 50))+x;
            int ya= (int) (viewY + HEIGHT - (Map2State.res + 50))+y;
            shapeR.rect(xa,ya, 1, 1);

        }

        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GREEN);
        shapeR.circle(tx,ty,5);

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
