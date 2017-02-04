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
import com.quadx.dungeons.*;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.shapes.Circle;
import com.quadx.dungeons.tools.shapes.Line;
import com.quadx.dungeons.tools.shapes.Triangle;
import com.quadx.dungeons.tools.heightmap.HeightMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;
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
    private static float dtCircle=1f;
    public static float dtWaterEffect=0;
    private static int blradius=0;
    public static ArrayList<Rectangle> monAgroBoxes= new ArrayList<>();



    static void renderLayers(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeR.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        drawGameLayer(sb);
        drawHUDLayer(sb);
        drawFPSModule(sb, HUD.fpsGridPos);
        drawMiniMapModule(HUD.minimapPos);
        srDrawAttackSelectors();
    }
    //HUD Layer----------------------------------------------------------
    static void drawHUDLayer(SpriteBatch sb){
        //HUD Layer ---------------------------------------------------------------------------------------------------------//
        drawHUDShapes();
        drawHUDBatch(sb);
    }
    static void drawHUDShapes(){
        //shape renderer filled------------------------------------
        //enable transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        //draw hud rectangles
        for(Rectangle r: HUD.getInfoOverlay().rects){
            shapeR.setColor(.1f, .1f, .1f, .7f);
            shapeR.rect(r);
        }
        shapeR.setColor(1,0,0,(1-((float)player.getHp()/(float)player.getHpMax()))/2.8f);
        shapeR.rect(viewX,viewY,WIDTH,HEIGHT);
        shapeR.end();
        Gdx.gl.glDisable(GL_BLEND);
        // end transparency------------------------------------------------------------
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        //draw player stat bars
        shapeR.setColor(0f, 1f, 0f, 1);
        try {
            Rectangle[] bars = HUD.playerStatBars;
            if (player.getHp() < player.getHpMax() / 2) {
                shapeR.setColor(1f, 0f, 0f, 1);
            }
            shapeR.rect(bars[0]);
            shapeR.setColor(0f, 0f, 1f, 1);
            shapeR.rect(bars[1]);
            shapeR.setColor(1f, 1f, 0, 1);
            shapeR.rect(bars[2]);
        }catch (NullPointerException e){}
        shapeR.end();


        //line draw functions---------------------------------------------------
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GRAY);
        //draw equipment slots
        for(Rectangle r: HUD.equipBoxes)
            shapeR.rect(r);
        shapeR.end();

    }
    static void drawHUDBatch(SpriteBatch sb){
        //spritebatch draw functions---------------------------------------------------
        sb.begin();
        Game.setFontSize(1);
        //Draw player stats
        if(showStats) {
            Game.setFontSize(1);
            Game.font.setColor(Color.WHITE);
            ArrayList<String> a = player.getStatsList();
            if(HUD.statsPos!=null) {
                Vector2[] v = HUD.statsPos;
                for (int i = 0; i < a.size(); i++) {
                    if (Inventory.statCompare != null && i - 3 < Inventory.statCompare.length && i - 3 >= 0) {
                        switch (Inventory.statCompare[i - 3]) {
                            case 1: {
                                Game.font.setColor(Color.BLUE);
                                break;
                            }
                            case 2: {
                                Game.font.setColor(Color.RED);
                                break;
                            }
                            default: {
                                Game.font.setColor(Color.WHITE);
                                break;
                            }
                        }
                    } else {
                        Game.font.setColor(Color.WHITE);
                    }
                    Game.getFont().draw(sb, a.get(i), v[i].x, v[i].y);
                }
            }
        }
        //Draw score
        Game.getFont().setColor(Color.WHITE);
        for(Text t: HUD.getInfoOverlay().texts){
            Game.getFont().draw(sb,t.text,t.pos.x,t.pos.y);
        }
        //draw attack menu
        ArrayList<InfoOverlay> list = HUD.getAttackBarOverlay();
        for(InfoOverlay io :list){
            io.draw(sb,shapeR);
        }
        //draw inventory
        HUD.getInvOverlay().draw(sb,shapeR);
        //draw output messages
        if(Tests.output) {
            Game.setFontSize(1);
            Game.font.setColor(Color.WHITE);
            for (int i = 0; i < 10; i++) {
                try {
                    Game.getFont().draw(sb, HUD.output.get(i), viewX+(2*WIDTH/3) - 30, viewY + HEIGHT - (i * 20)-30);
                } catch (IndexOutOfBoundsException|NullPointerException ignored) {
                }
            }
        }
        //draw loot popup
        try {
        if (HUD.dtLootPopup < .4) {
                Vector2 v=new Vector2(player.getAbsPos());
                v.y+=40+player.getIcon().getHeight();
                v.x+=(player.getIcon().getWidth()/2);
                sb.draw(HUD.lootPopup,  v.x, GridManager.fixHeight(v));
            }
        } catch (NullPointerException ignored) {}
        //draw ability icon
        if(!HUD.equipBoxes.isEmpty()) {
            Vector2 v = new Vector2(HUD.equipBoxes.get(0).x, HUD.equipBoxes.get(0).y);
            sb.draw(player.getAbility().getIcon(), v.x, v.y - 60);
            Game.getFont().setColor(Color.WHITE);
            Game.getFont().draw(sb,player.getAbility().getLevel()+"",v.x, v.y - 60);
            if (!player.getSecondaryAbilityList().isEmpty())
                for (int i=0;i<player.getSecondaryAbilityList().size();i++) {
                    Ability a = player.getSecondaryAbilityList().get(i);
                    sb.draw(a.getIcon(), v.x + (player.getAbility().getIcon().getWidth()*(i+1)), v.y - 60);
                    Game.getFont().draw(sb,a.getLevel()+"",v.x + (player.getAbility().getIcon().getWidth()*(i+1)), v.y - 60);

                }
        }
    sb.end();
    }
    //Game Layer---------------------------------------------------------
    static void drawGameLayer(SpriteBatch sb){
        /*
        functions in glshapes
        draw cells
            land shaded
            walls
            water
        draw player
            shadow
            attack box
            hit box
        draw monsters
            hit box
            health bars
        transparents-
            mon agro box
        lines
            grid
        */
        drawGLShapes();
        /*
        functions in glBatch
        textures
           items
           warp
           animations
           player
           monsters
         texts
            mon stats
            hovertexts
        */
        drawGLBatch(sb);
    }
    static void drawGLShapes(){
        //shape renderer filled------------------------------------
        shapeR.setAutoShapeType(true);
        shapeR.begin();
        shapeR.set(ShapeRenderer.ShapeType.Filled);
        for (Cell c : drawList) {//cycle through draw list
            float[] f = c.getCorners().getVertices();
            if(!Tests.noLand) {
                if(!c.getWater()) {
                    if(!c.getState()) {
                        shapeR.setColor(new Color(.1f,.1f,.1f,1));
                        for(Triangle t: c.getTris()){
                            shapeR.triangle(t);
                        }
                    }
                    else {
                        //draw land shaded
                        shapeR.setColor(HeightMap.getColors().get(Math.round(f[10])));
                        for(Triangle t: c.getTris()){
                            shapeR.triangle(t);
                        }
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
        if ((blinkp || !player.wasHit) && GridManager.isInBounds(player.getPos())) {
            //draw player shadow
            ArrayList<Triangle> tris = dispArray[(int) player.getPos().x][(int) player.getPos().y].getTris();
            shapeR.setColor(Color.DARK_GRAY);
            for (Triangle t : tris) {
                shapeR.triangle(t);
            }
            //draw player attack box
            shapeR.setColor(Color.RED);
            Attack.HitBoxShape hbs=player.attackList.get(Attack.pos).getHitBoxShape();
            switch (hbs){

                case Circle:
                    Circle c= player.getAttackCircle();
                    shapeR.circle(c.center.x,c.center.y,c.radius);
                    break;
                case Rect:
                    shapeR.rect(player.getAttackBox());
                    break;
            }
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
        //shapeR.circle(MapState.circle.center.x,MapState.circle.center.y,MapState.circle.radius);
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

        //shape renderer lines-------------------------------------------------
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        for (Cell c : drawList) {
            if (c.getState()) {
                //draw grid
                shapeR.setColor(Color.GRAY);
                shapeR.getColor().a=.3f;
                shapeR.polygon(Arrays.copyOf(c.getCorners().getVertices(),8));
            }
        }
        ArrayList<Line> lines = player.getAttackChain();
         for(Line l:lines){
             shapeR.setColor(WHITE);
            shapeR.line(l);
        }
        shapeR.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


    }
    static void drawGLBatch(SpriteBatch sb){
        //spritebatch drawing section------------------------------------------
        sb.begin();
        for (Cell c : drawList) {
            //draw items
            Item item = c.getItem();
            if (item != null && c.getState())
                sb.draw(item.getIcon(),c.getAbsPos().x,fixHeight(c.getAbsPos()));
            //draw warp
            if(c.hasWarp())
                sb.draw(ImageLoader.warp,c.getFixedPos().x,c.getFixedPos().y);
        }
        //draw animations
        for(Anim a: Anim.anims){
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
                        Game.getFont().draw(sb,t.text,m.getAbsPos().x,fixHeight(m.getAbsPos())-40);
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
    ////Modules----------------------------------------------------------
    static void drawFPSModule(SpriteBatch sb,Vector2 pos){
        //fps meter module
        if(displayFPS) { //TODO optomize this to draw faster
            //DRAW FPS meter
            shapeR.begin(ShapeRenderer.ShapeType.Filled);
            shapeR.setColor(BLACK);
            shapeR.rect(pos.x, pos.y, 100, 100);
            shapeR.end();
            shapeR.begin(ShapeRenderer.ShapeType.Line);
            shapeR.setColor(Color.WHITE);
            shapeR.rect(pos.x, pos.y, 100, 100);
            shapeR.line(pos.x, pos.y, pos.x + 100, pos.y);

            shapeR.setColor(Color.GREEN);
            int prev = 0;
            for (int i = 0; i < fpsList.size(); i++) {
                shapeR.line(pos.x + (i * 2), pos.y + prev, pos.x + ((i + 1) * 2), pos.y + fpsList.get(i));
                prev = fpsList.get(i);
            }
            double prev1=0;
            for (int i = 0; i < Tests.memUsageList.size(); i++) {
                shapeR.setColor(Color.PURPLE);
                shapeR.line(pos.x + (i * 2), (float) (pos.y +100* prev1), pos.x + ((i + 1) * 2), (float) (pos.y + 100*Tests.memUsageList.get(i)));
                prev1 = Tests.memUsageList.get(i);
            }
            shapeR.end();
            //draw fps counter
            sb.begin();
            if(displayFPS){
                Game.setFontSize(1);
                Game.getFont().setColor(Color.WHITE);
                Game.getFont().draw(sb, (int) fps + " FPS", pos.x+2, pos.y + 80);
                double x=0;
                try {
                    x= Tests.memUsageList.get(Tests.memUsageList.size() - 1);
                }catch(Exception e){}
                Game.getFont().draw(sb, (int) Tests.currentMemUsage + "MB "+Math.floor(x*100)+"%" , pos.x+2, pos.y + 95);
            }
            sb.end();
        }
    }
    private static void drawMiniMapModule(Vector2 pos){
        float r=res*2;
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(BLACK);
        shapeR.rect(pos.x,pos.y, r, r);
        shapeR.end();

        drawGrid(pos);

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.WHITE);
        shapeR.rect(pos.x,pos.y, r, r);
        shapeR.end();
    }
    //OLD RENDERING------------------------------------------------------
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
    private static void srDrawAttackSelectors(){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        int xoffset = (int) HUD.attackBarPos.x;
        for (int i = 0; i < player.attackList.size(); i++) {

            int x = xoffset + (i * 52);
            if (i == altNumPressed) {
                shapeR.setColor(Color.BLUE);
                shapeR.rect(x,viewY + 86,10,10);
            }
            if (i == Attack.pos) {
                shapeR.setColor(Color.RED);
                shapeR.rect(x+38,viewY + 86,10,10);
            }
        }
        shapeR.end();
    }
    //OTHER----------------------------------------------------------------
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
    }
    static void updateVariables(float dt){
        dtBlink+=dt;
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
    public static void drawCircle(int x, int y, float r, Color c){
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(c);
        shapeR.circle(x, y,r);
        shapeR.end();
    }
    private static void drawGrid(Vector2 pos) {
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        Vector2 mapshop = new Vector2((pos.x + shop.x * 2), (pos.y + shop.y * 2));
        Vector2 mapwarp = new Vector2((pos.x + warp.x * 2), (pos.y + warp.y * 2));
        Vector2 mapplay = new Vector2(pos.x + player.getPos().x * 2, pos.y + player.getY() * 2);
        for (Cell c : GridManager.liveCellList) {
            int x = c.getX();
            int y = c.getY();
            if (c.getColor() != null) {
                shapeR.setColor(c.getColor());
                if (c.getItem() != null && !c.getItem().getClass().equals(Gold.class))
                    shapeR.setColor(Color.SKY);
                if (player.getPos().x == x &&player.getPos().y == y) {
                    if (blink)
                        shapeR.setColor(0, 0, 1, 1);
                    else
                        shapeR.setColor(1, 1, 1, 1);
                }
                float xa = pos.x + x * 2;
                float ya = pos.y + y * 2;
                shapeR.rect(xa, ya, 2, 2);
            }

        }

        shapeR.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.GREEN);
        shapeR.circle(mapwarp.x+1,mapwarp.y+1,9);
        shapeR.setColor(Color.MAGENTA);
        shapeR.circle(mapshop.x+1,mapshop.y+1,9);

        if(blink){shapeR.setColor(Color.BLUE);}
        else if(!blink){shapeR.setColor(Color.WHITE);}
        if(dtBlink>Game.frame*4){
            blink=!blink;
            blradius++;
            dtBlink=0;
        }
        shapeR.circle(mapplay.x+1,mapplay.y+1,blradius);
        if(blradius>9)blradius=0;
        shapeR.end();

    }

}
