package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.HoverText;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.gui.Triangle;
import com.quadx.dungeons.tools.heightmap.HeightMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.Map2State.res;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.*;


/**
 * Created by Tom on 12/28/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateRender extends MapState {
    public static boolean showCircle=false;
    private static boolean blink=false;
    private static boolean blinkp=false;

    private static float dtBlink =0;
    private static float dtBlinkP =0;

    static float dtLootPopup=0;
    private static float dtCircle=1f;
    public static float dtWaterEffect=0;
    private static int blradius=0;
    public static int inventoryPos=0;
    public static ArrayList<Rectangle> monAgroBoxes= new ArrayList<>();
     static int[] statCompare=null;


    static void renderLayers(SpriteBatch sb) {

        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        drawGameLayer(sb);
        //HUD Layer ---------------------------------------------------------------------------------------------------------
        srDrawAttackSelectors();
        srDrawFPSMeter();
        sbdrawFPS(sb);
        srDrawMiniMap();
        srDrawHUD();
        sbDrawHUD(sb);
    }
    //Game Layer
    public static void drawGameLayer(SpriteBatch sb){
        //shape renderer filled------------------------------------
        shapeR.setAutoShapeType(true);
        shapeR.begin();
        shapeR.set(ShapeRenderer.ShapeType.Filled);
        for (Cell c : drawList) {//cycle through draw list
            float[] f = c.getCorners().getVertices();
            if(!Tests.noLand) {
                if(!c.getWater()) {
                    if (c.getState()) {
                        //draw land shaded
                        shapeR.setColor(HeightMap.getColors().get(Math.round(f[10])));
                        for(Triangle t: c.getTris()){
                            shapeR.triangle(t);
                        }
                    } else {
                        //draw walls
                        shapeR.setColor(Color.BLACK);
                        shapeR.rect(f[0], f[1] + cell.x, cell.x, cell.y);
                    }
                }
                else {
                    //draw water
                    shapeR.setColor(c.getColor());
                    for(Triangle t: c.getTris()){
                        shapeR.triangle(t);
                    }
                }
            }
        }
        //draw player
        if ((blinkp || !player.wasHit) && player.isInBounds()) {
            //draw player shadow
            ArrayList<Triangle> tris = dispArray[(int) player.getPos().x][(int) player.getPos().y].getTris();
            shapeR.setColor(Color.DARK_GRAY);
            for (Triangle t : tris) {
                shapeR.triangle(t);
            }
            //draw player attack box
            shapeR.setColor(Color.RED);
            shapeR.rect(player.getAttackBox());
            //draw player hitbox
            if (Tests.showhitbox) {
                shapeR.setColor(Color.BLUE);
                shapeR.rect(player.getHitBox());
            }
        }
        try {
            //monster list shapeR functions
            monAgroBoxes.clear();
            for (Monster m : GridManager.monsterList) { // TODO fix this loop to run only monsters on screen
                if (m != null) {
                    if (Tests.showhitbox) {
                        // add agro box to transparent draw queue
                        monAgroBoxes.add(m.getAgroBox());
                        //draw monster hitbox
                        shapeR.setColor(Color.RED);
                        shapeR.rect(m.getHitBox());
                    }
                    //draw healthbars
                    shapeR.setColor(Color.DARK_GRAY);
                    shapeR.rect(m.getHbar());
                    if(m.isLowHP())
                        shapeR.setColor(Color.GREEN);
                    else
                        shapeR.setColor(Color.RED);
                    shapeR.rect(m.getHbar2());
                }
            }
        }catch (ConcurrentModificationException e){}
        shapeR.end();

        //draw transparent things---------------------------------------------------------------
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        if(Tests.showhitbox) {
            //draw monster aggro box
            for (Rectangle rec : monAgroBoxes) {
                try {
                    shapeR.setColor(1, 0, 0, .2f);
                    shapeR.rect(rec);
                } catch (NullPointerException e) {
                }
            }
        }
        shapeR.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //shape renderer lines-------------------------------------------------
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        for (Cell c : drawList) {
            if (c.getState()) {
                //draw grid
                shapeR.setColor(Color.DARK_GRAY);
                shapeR.polygon(Arrays.copyOf(c.getCorners().getVertices(),8));
            }
        }
        shapeR.end();

        //spritebatch drawing section------------------------------------------
        sb.begin();
        for (Cell c : drawList) {
            //draw items
            Item item = c.getItem();
            if (item != null && c.getState())
                sb.draw(item.getIcon(),item.getTexturePos().x,item.getTexturePos().y);
            //draw warp
            if(c.hasWarp())
                sb.draw(ImageLoader.warp,c.getFixedPos().x,c.getFixedPos().y);
        }
        //draw animations
        for(Anim a: anims){
            if(a.getTexture() != null)
                sb.draw(a.getTexture(),a.getPos().x,fixHeight(a.getPos()));
        }
        //draw player
        Vector2 v = player.getTexturePos();
        sb.draw(player.getIcon(), v.x, v.y);

        //spritebatch monster draw functions
        try {
            for (Monster m : GridManager.monsterList) { // TODO fix this loop to run only monsters on screen
                if (m != null) {
                    //draw icon
                    sb.draw(m.getIcon(), m.getTexturePos().x,m.getTexturePos().y);
                    //draw monster texts
                    for(Text t: m.getInfoOverlay().texts) {
                        Game.setFontSize(t.size);
                        Game.getFont().draw(sb,t.text,m.getAbsPos().x,m.getAbsPos().y-40);
                    }
                }
            }
        } catch (ConcurrentModificationException e) {}
        //draw hover texts
        try {
            for (HoverText h : HoverText.texts)
                h.draw(sb);
            Game.getFont().getColor().a=1;
        }catch (ConcurrentModificationException e){}

        sb.end();//end drawing--------------------------------------------------------------------------------
    }
    //SPRITEBATCH RENDERING-----------------------------------------------
    private static void sbDrawHUD(SpriteBatch sb) {
        sb.begin();
        Game.setFontSize(1);
        sbDrawPlayerStats(sb);
        sbDrawAttackMenu(sb);
        sbDrawInventory(sb);
        sbDrawMessageOutput(sb);
        sbDrawLootPopup(sb);
        sb.end();
    }
    private static void sbDrawMessageOutput(SpriteBatch sb){
        Game.setFontSize(1);
        Game.font.setColor(Color.WHITE);
        for(int i=0;i<10;i++){
            try {
                Game.getFont().draw(sb, output.get(i), viewX+30,viewY+195-(i * 20));
            }
            catch(IndexOutOfBoundsException ignored){}
        }
    }
    private static void sbDrawPlayerStats(SpriteBatch sb) {

        if(showStats) {//Draw STATS
            Game.setFontSize(1);
            Game.font.setColor(Color.WHITE);
            ArrayList<String> a = player.getStatsList();
            Vector2[] v=player.getStatPos();
            for (int i = 0; i < a.size(); i++) {
                if (statCompare != null && i-3 < statCompare.length && i-3 >= 0) {
                      switch (statCompare[i-3]) {
                        case 1: {Game.font.setColor(Color.BLUE);break;}
                        case 2: {Game.font.setColor(Color.RED);break;}
                        default: {Game.font.setColor(Color.WHITE);break;}
                      }
                }
                else{
                    Game.font.setColor(Color.WHITE);
                }
                Game.getFont().draw(sb, a.get(i), v[i].x, v[i].y);
            }
        }
        //Draw score
        Game.getFont().setColor(Color.WHITE);
        for(Text t: MapState.getInfoOverlay().texts){
            Game.getFont().draw(sb,t.text,t.pos.x,t.pos.y);
        }

    }
    private static void sbDrawTiles(SpriteBatch sb) {
        sb.begin();
        for (Cell c : drawList) {
            Vector2 v= c.getAbsPos();
            sb.draw(c.getTile(), v.x, v.y);
            if (c.getItem() != null && c.getState()) {
                if (c.getItem().getIcon()==null) {
                    Texture t=ImageLoader.crate;
                    sb.draw(t, v.x, v.y);
                } else {
                    sb.draw(c.getItem().getIcon(), v.x, v.y);
                }
            }
            if (c.hasWarp()) {
                sb.draw(ImageLoader.warp, v.x, v.y);
            }
        }
        sb.end();
    }
    private static void sbDrawAttackMenu(SpriteBatch sb) {
        ArrayList<InfoOverlay> list = MapState.getAttackBarOverlay();
        for(InfoOverlay io :list){
            io.draw(sb,shapeR);
        }
    }
    private static void sbDrawInventory(SpriteBatch sb) {
        InfoOverlay io= getInvOverlay();
        io.draw(sb,shapeR);
    }
    private static void sbDrawLootPopup(SpriteBatch sb) {
        try {
            if (dtLootPopup < .4) {
                Vector2 v=new Vector2(player.getAbsPos());
                v.y+=40+player.getIcon().getHeight();
                v.x+=(player.getIcon().getWidth()/2);
                sb.draw(lootPopup,  v.x, GridManager.fixHeight(v));
            }
        } catch (NullPointerException ignored) {}
    }
    private static void sbdrawFPS(SpriteBatch sb) {
        sb.begin();
        int yoff=-70;
        int basex = (int) (viewX + Game.WIDTH - 150);
        int basey = (int) (viewY + Game.HEIGHT / 2)+ yoff;
        if(displayFPS){
            Game.setFontSize(1);
            Game.getFont().setColor(Color.WHITE);
            Game.getFont().draw(sb, (int) fps + " FPS", basex+2, basey + 80);
            double x=0;
            try {
                x= Tests.memUsageList.get(Tests.memUsageList.size() - 1);
            }catch(Exception e){}
            Game.getFont().draw(sb, (int) Tests.currentMemUsage + "MB "+Math.floor(x*100)+"%" , basex+2, basey + 95);

        }
        sb.end();
    }
    static void sbDrawParticleEffects(SpriteBatch sb){
        ArrayList<Integer> rem= new ArrayList<>();
        sb.begin();
        for(ParticleEffect p: MapStateExt.effects){
            p.draw(sb);
            if(p.isComplete()){
                rem.add(MapStateExt.effects.indexOf(p));
            }
        }
        sb.end();
        Collections.sort(rem);
       // Collections.reverse(rem);
        for(int i=MapStateExt.effects.size()-1;i>=0;i--){
            for(Integer r:rem){
                if(r==i){
                 //   MapStateExt.effects.remove(i);
                }
            }
        }
    }
    //SHAPE RENDERING------------------------------------------------------
    private static void srDrawAttackSelectors(){
        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        int xoffset = (int) (viewX + (WIDTH / 2) - (52 * 4));
        for (int i = 0; i < player.attackList.size(); i++) {

            int x = xoffset + (i * 52);
            if (i == altNumPressed) {
                shapeR.setColor(Color.BLUE);
                shapeR.rect(x,viewY + 86,10,10);
            }
            if (i == lastNumPressed) {
                shapeR.setColor(Color.RED);
                shapeR.rect(x+38,viewY + 86,10,10);
            }
        }
        shapeR.end();
    }
    private static void srDrawHUD(){
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        InfoOverlay io = MapState.getInfoOverlay();
        for(Rectangle r:io.rects){
            if(io.rects.indexOf(r)<3) shapeR.setColor(Color.WHITE);
            else shapeR.setColor(.1f, .1f, .1f, .7f);
            shapeR.rect(r.x,r.y,r.width,r.height);
        }
        shapeR.end();
        Gdx.gl.glDisable(GL_BLEND);
        srDrawPlayerStatBars();
        srDrawEquipmentSlots();
    }
    private static void srDrawFPSMeter(){
        int yoff=-70;
        if(displayFPS) {
            int basex = (int) (viewX + Game.WIDTH - 150);
            int basey = (int) (viewY + Game.HEIGHT / 2)+ yoff;
            //DRAW FPS COUNTER
            shapeR.begin(ShapeRenderer.ShapeType.Filled);
            shapeR.setColor(Color.BLACK);
            shapeR.rect(basex, basey, 100, 100);
            shapeR.end();
            shapeR.begin(ShapeRenderer.ShapeType.Line);
            shapeR.setColor(Color.WHITE);
            shapeR.rect(basex, basey, 100, 100);
            shapeR.line(basex, basey, basex + 100, basey);

            shapeR.setColor(Color.GREEN);
            int prev = 0;
            for (int i = 0; i < fpsList.size(); i++) {
                shapeR.line(basex + (i * 2), basey + prev, basex + ((i + 1) * 2), basey + fpsList.get(i));
                prev = fpsList.get(i);
            }
            double prev1=0;
            for (int i = 0; i < Tests.memUsageList.size(); i++) {
                shapeR.setColor(Color.PURPLE);
                shapeR.line(basex + (i * 2), (float) (basey +100* prev1), basex + ((i + 1) * 2), (float) (basey + 100*Tests.memUsageList.get(i)));
                prev1 = Tests.memUsageList.get(i);
            }
            shapeR.end();
        }
    }
    private static void srDrawPlayerStatBars(){
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
    private static void srDrawEquipmentSlots(){
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
    }
    private static void srDrawMiniMap(){
        float r=res*2;
        float off=20;
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(Color.BLACK);
        shapeR.rect(viewX+ WIDTH-(r+off),viewY+ HEIGHT-(r+off), r, r);
        shapeR.end();

        drawGrid(r, off);

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(viewX+ WIDTH-(r+off),viewY+ HEIGHT-(r+off), r, r);
        shapeR.end();
    }
    //OTHER----------------------------------------------------------------
    static void updateVariables(float dt){
        dtBlink+=dt;
        dtLootPopup +=dt;
        updateHoverTextTime();
        dtBlinkP+=dt;
        if(dtBlinkP>.1f){
            blinkp=!blinkp;
            dtBlinkP=0;
        }
        if(showCircle && dtCircle>0){
            dtCircle-=dt;
        }
        else{
            dtCircle=1f;
            showCircle=false;
        }
    }
    private static void updateHoverTextTime(){
        try {
            HoverText.texts.forEach(HoverText::updateDT);
        }catch (ConcurrentModificationException e){}
    }
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
    }
    public static void setHoverText(String s, float time, Color color, float x, float y, boolean flash){
            new HoverText(s, color, x, GridManager.fixHeight(new Vector2(x,y)), time, flash);
    }
    private static void drawPlayerFinder(){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.circle( player.getAbsPos().x, player.getAbsPos().y,100*dtCircle);
        shapeR.end();
    }
    public static void drawCircle(int x, int y, float r, Color c){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(c);
        shapeR.circle(x, y,r);
        shapeR.end();
    }
    private static void drawGrid(float r, float off) {
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        Vector2 mapshop=new Vector2(((viewX + WIDTH - (r + off))+shop.x*2),((viewY + HEIGHT - (r + off))+shop.y*2));
        int tx= (int) ((viewX + WIDTH - (r + off))+warp.x*2);
        int ty= (int) ((viewY + HEIGHT - (r + off))+warp.y*2);
        int px= (int) (viewX + WIDTH - (r + off))+ player.getX()*2;
        int py=(int) (viewY + HEIGHT - (r + off))+ player.getY()*2;
        for(Cell c: GridManager.liveCellList){
            int x=c.getX();
            int y=c.getY();
            if(c.getColor()!=null) {
                shapeR.setColor(c.getColor());
                if (player.getX() == x && player.getY() == y) {
                    if (blink)
                        shapeR.setColor(0, 0, 1, 1);
                    else
                        shapeR.setColor(1, 1, 1, 1);
                }
                int xa = (int) (viewX + WIDTH - (r + off)) + x * 2;
                int ya = (int) (viewY + HEIGHT - (r + off)) + y * 2;
                shapeR.rect(xa, ya, 2, 2);
            }

        }

        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GREEN);
        shapeR.circle(tx+1,ty+1,9);
        shapeR.setColor(Color.MAGENTA);
        shapeR.circle(mapshop.x+1,mapshop.y+1,9);

        if(blink){shapeR.setColor(Color.BLUE);}
        else if(!blink){shapeR.setColor(Color.WHITE);}
        if(dtBlink>Game.frame*4){
            blink=!blink;
            blradius++;
            dtBlink=0;
        }
        shapeR.circle(px+1,py+1,blradius);
        if(blradius>9)blradius=0;
        shapeR.end();

    }

}
