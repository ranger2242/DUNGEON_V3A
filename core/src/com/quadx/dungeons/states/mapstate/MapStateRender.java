package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Illusion;
import com.quadx.dungeons.attacks.Protect;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.Mine;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.Line;
import com.quadx.dungeons.shapes1_5.Triangle;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.heightmap.HeightMap;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Oscillator;
import com.quadx.dungeons.tools.timers.Time;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.tools.StatManager.gameTime;


/**
 * Created by Tom on 12/28/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateRender extends MapState {
    private static int playerCircleRadius =0;
    public static ArrayList<Rectangle> monAgroBoxes= new ArrayList<>();
    public static double time=0;

    static Oscillator oBlink1 = new Oscillator(20* Time.ft);
    static Oscillator oBlink2 = new Oscillator(4* Time.ft);
    static Delta dGrow = new Delta(5* Time.ft);


    static void renderLayers(SpriteBatch sb) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        drawGameLayer(sb);
        drawHUDLayer(sb);
        Tests.drawFPSModule(sb,sr, HUD.fpsGridPos);
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
        sr.begin(ShapeRenderer.ShapeType.Filled);

        //Knockback
        sr.setColor(WHITE);

        Vector2 c=new Vector2(
                view.x+ scr.x/2,
                view.y+scr.y/2);
        Vector2 e=new Vector2(
                c.x+player.kba.x,
                c.y+player.kba.y);
        sr.line(c, e);

        //draw hud rectangles
        for(Rectangle r: HUD.getInfoOverlay().rects){
            sr.setColor(.1f, .1f, .1f, .7f);
            sr.rect(r);
        }
        if(Protect.active)
        sr.setColor(0,0,1,1/3f);
        else
            sr.setColor(player.getDeathShade());

        sr.rect(view.x,viewY,WIDTH,HEIGHT);
        sr.end();
        Gdx.gl.glDisable(GL_BLEND);
        // end transparency------------------------------------------------------------
        sr.begin(ShapeRenderer.ShapeType.Filled);
        player.renderStatBars(sr);

        //line draw functions---------------------------------------------------
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GRAY);
        //draw equipment slots
        for(Rectangle r: HUD.equipBoxes)
            sr.rect(r);
        sr.end();

    }
    //---HUD SPRITEBATCH FUNCTIONS---------------------------------------
    static void drawGameTime(SpriteBatch sb){
        NumberFormat formatter = new DecimalFormat("#00.00");

        double d=gameTime.getElapsedD();//Double.valueOf(formatter.format( Double.valueOf(gameTime.getElapsed())));
        time+=d;
       // time=Double.valueOf(formatter.format(time));
        String t="Game Time: "+(int)(time/60)+":"+Double.valueOf(formatter.format(time%60));
        multiplier= (float) (1+ (.1*(int) (gameTime.getElapsedD()/30f)));
        String t2=gameTime.runtime();
        String t3="MULT:"+multiplier;
        Game.getFont().draw(sb,t2,view.x-100+WIDTH/2,viewY+HEIGHT-30);
        Game.getFont().draw(sb,t3,view.x-100+WIDTH/2,viewY+HEIGHT-40);

        gameTime.end();
        //gameTime.start();
    }
    static void drawHUDBatch(SpriteBatch sb){
        //spritebatch draw functions---------------------------------------------------
        sb.begin();
        Game.setFontSize(1);
        drawGameTime(sb);

        //Draw player stats
        if(showStats) {
            player.renderStatList(sb, new Vector2(view.x + 30, viewY +HEIGHT- 30));
        }
        //Draw score
        Game.getFont().setColor(Color.WHITE);
        for(Text t: HUD.getInfoOverlay().texts){
            Game.getFont().draw(sb,t.text,t.pos.x,t.pos.y);
        }
        //draw str menu
        ArrayList<InfoOverlay> list = HUD.getAttackBarOverlay();
        for(InfoOverlay io :list){
            io.draw(sb);
        }
        //draw inventory
        HUD.getInvOverlay().draw(sb);
        //draw details messages
        if(Tests.output) {
            Game.setFontSize(1);
            Game.font.setColor(Color.WHITE);
            for (int i = 0; i < 10; i++) {
                try {
                    Game.getFont().draw(sb, HUD.output.get(i), scrx(2f/3f), viewY + HEIGHT - (i * 20)-30);
                } catch (IndexOutOfBoundsException|NullPointerException ignored) {
                }
            }
        }
        //draw loot popup
        try {
        if (HUD.dPopup.isDone()) {
                Vector2 v=new Vector2(player.abs());
                Vector2 ic= player.body.getIconsDim();
                v.y+=40+ic.y;
                v.x+=ic.x/2;
                sb.draw(HUD.lootPopup,  v.x, GridManager.fixY(v));
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
            str box
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
           warpToNext
           animations
           player
           monsters
         textQueue
            mon stats
            hovertexts
        */
        drawGLBatch(sb);
    }
    static void drawGLShapes(){
        //shape renderer filled------------------------------------
        sr.setAutoShapeType(true);
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        for (Cell c : drawList) {//cycle through draw list
            float[] f = c.getCorners().getVertices();
            if(!Tests.noLand) {
                if(!c.isWater()) {
                    if(!c.isClear()) {
                        sr.setColor(new Color(.1f,.1f,.1f,1));
                        for(Triangle t: c.getTris()){
                            sr.triangle(t);
                        }
                    }
                    else {
                        //draw land shaded
                        sr.setColor(HeightMap.getColors().get(Math.round(f[10])));
                        for(Triangle t: c.getTris()){
                            sr.triangle(t);
                        }
                    }
                }
                else {
                    //draw water
                    sr.setColor(c.getColor());
                    for(Triangle t: c.getTris()){
                        sr.triangle(t);
                    }
                }
            }
        }
        //draw player
        if ((oBlink2.getVal() || !player.wasHit) && GridManager.isInBounds(player.pos())) {
            //draw player shadow
            ArrayList<Triangle> tris = dispArray[(int) player.pos().x][(int) player.pos().y].getTris();
            sr.setColor(Color.DARK_GRAY);
            for (Triangle t : tris) {
                sr.triangle(t);
            }
            //draw player attack box
            sr.setColor(Color.RED);
            player.renderAttackbox(sr);
            //draw player hitbox
            if (Tests.showhitbox) {
                sr.setColor(Color.BLUE);
                sr.rect(player.body.getHitBox());
            }
        }
        try {
            //monster list sr functions
            monAgroBoxes.clear();
            for (Monster m : GridManager.monsterList) { // TODO fix this loop to run only monsters on screen
                if (m != null) {
                    if (Tests.showhitbox) {
                        // add agro box to transparent draw queue
                        monAgroBoxes.add(m.getAgroBox());
                        //draw monster hitbox
                        sr.setColor(Color.RED);
                        sr.rect(m.body.getHitBox());
                    }
                    //draw healthbars
                    sr.setColor(Color.DARK_GRAY);
                    sr.rect(m.getHbar());
                    if(m.isLowHP())
                        sr.setColor(Color.GREEN);
                    else
                        sr.setColor(Color.RED);
                    sr.rect(m.getHbar2());
                }
            }
        }catch (ConcurrentModificationException ignored){}
        //sr.circle(MapState.circle.center.x,MapState.circle.center.y,MapState.circle.radius);
        sr.end();

        //draw transparent things---------------------------------------------------------------
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if(Tests.showhitbox) {
            //draw monster aggro box
            for (Rectangle rec : monAgroBoxes) {
                try {
                    sr.setColor(1, 0, 0, .2f);
                    sr.rect(rec);
                } catch (NullPointerException ignored) {
                }
            }
        }
        sr.end();

        //shape renderer lines-------------------------------------------------
        sr.begin(ShapeRenderer.ShapeType.Line);
        for (Cell c : drawList) {
            if (c.isClear()) {
                //draw grid
                sr.setColor(Color.GRAY);
                sr.getColor().a=.3f;
                sr.polygon(Arrays.copyOf(c.getCorners().getVertices(),8));
            }
        }
        ArrayList<Line> lines = player.getAttackChain();
         for(Line l:lines){
             sr.setColor(WHITE);
            sr.line(l);
        }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


    }
    static void drawGLBatch(SpriteBatch sb){
        //spritebatch drawing section------------------------------------------
        sb.begin();
        for (Cell c : drawList) {
            //draw items
            Item item = c.getItem();
            if (item != null && c.isClear()) {
                if(item instanceof Mine){
                    ((Mine)item).render(sb);
                }else {
                    Vector2 p = c.fixed();
                    sb.draw(item.getIcon(), p.x, p.y);
                }
            }
            //draw warpToNext
            if(c.isWarp())
                sb.draw(ImageLoader.warp,c.fixed().x,c.fixed().y);
        }
        //draw animations
        for(Anim a: Anim.anims){
            a.render(sb);
        }
        sbDrawParticleEffects(sb);
        //draw player
        player.render(sb);

        for(Illusion.Dummy d:Illusion.dummies){
            sb.draw(d.icon,d.absPos.x, fixY(d.absPos));
        }

        //spritebatch monster draw functions
        try {
            for (Monster m : Monster.mdrawList) { // TODO fix this loop to run only monsters on screen
                if (m != null) {
                    //draw icon
                    sb.draw(m.getIcon(), m.fixed().x,m.fixed().y);
                    //draw monster textQueue
                    for(Text t: m.getInfoOverlay().texts) {
                        Game.setFontSize(t.size);
                        Game.getFont().draw(sb,t.text,m.abs().x, fixY(m.abs())-40);
                    }
                }
            }
        } catch (ConcurrentModificationException ignored) {}
        //draw hover textQueue
        HoverText.render(sb);
        Game.resetFont();
        sb.end();//end drawing--------------------------------------------------------------------------------
    }
    ////Modules----------------------------------------------------------

    private static void drawMiniMapModule(Vector2 pos){
        float r=res*2;
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(BLACK);
        sr.rect(pos.x,pos.y, r, r);
        sr.end();

        drawGrid(pos);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.rect(pos.x,pos.y, r, r);
        sr.end();
    }
    //OLD RENDERING------------------------------------------------------
    private static void sbDrawTiles(SpriteBatch sb) {
        sb.begin();
        for (Cell c : drawList) {
            Vector2 v= c.abs();
            sb.draw(c.getTile(), v.x, v.y);
            if (c.getItem() != null && c.isClear()) {
                if (c.getItem().getIcon()==null) {
                    Texture t=ImageLoader.crate;
                    sb.draw(t, v.x, v.y);
                } else {
                    sb.draw(c.getItem().getIcon(), v.x, v.y);
                }
            }
            if (c.isWarp()) {
                sb.draw(ImageLoader.warp, v.x, v.y);
            }
        }
        sb.end();
    }
    static void sbDrawParticleEffects(SpriteBatch sb){
        particleHandler.render(sb);
/*        ArrayList<Integer> remove= new ArrayList<>();
        for(ParticleEffect p: ParticleHandler.itemEffects){
            p.draw(sb);
            if(p.isComplete()){
                remove.add(ParticleHandler.itemEffects.indexOf(p));
            }
        }
        Collections.sort(remove);
       Collections.reverse(remove);
        for(int i = ParticleHandler.itemEffects.size()-1; i>=0; i--){
            for(Integer r:remove){
                if(r==i){
                   ParticleHandler.itemEffects.remove(i);
                }
            }
        }*/
    }
    private static void srDrawAttackSelectors(){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        int xoffset = (int) HUD.attackBarPos.x;
        for (int i = 0; i < player.attackList.size(); i++) {

            int x = xoffset + (i * 52);
            if (i == altNumPressed) {
                sr.setColor(Color.BLUE);
                sr.rect(x,viewY + 86,10,10);
            }
            if (i == Attack.pos) {
                sr.setColor(Color.RED);
                sr.rect(x+38,viewY + 86,10,10);
            }
        }
        sr.end();
    }
    //OTHER----------------------------------------------------------------
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
    }
    static void updateVariables(float dt){
        dGrow.update(dt);
        oBlink1.update(dt);
        oBlink2.update(dt);

        if(dGrow.isDone()) {
            playerCircleRadius++;
            dGrow.reset();
        }
    }

    public static void drawCircle(int x, int y, float r, Color c){
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(c);
        sr.circle(x, y,r);
        sr.end();
    }
    private static void drawGrid(Vector2 pos) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        Vector2 mapshop = new Vector2((pos.x + shop.x * 2), (pos.y + shop.y * 2));
        Vector2 mapwarp = new Vector2((pos.x + warp.x * 2), (pos.y + warp.y * 2));
        Vector2 mapplay = new Vector2(pos.x + player.pos().x * 2, pos.y + player.pos().y * 2);
        for (Cell c : GridManager.liveCellList) {
            int x = c.getX();
            int y = c.getY();
            if (c.getColor() != null) {
                sr.setColor(c.getColor());
                if (c.getItem() != null && !c.getItem().getClass().equals(Gold.class))
                    sr.setColor(Color.SKY);
                if (player.pos().x == x &&player.pos().y == y) {
                    if (oBlink1.getVal())
                        sr.setColor(0, 0, 1, 1);
                    else
                        sr.setColor(1, 1, 1, 1);
                }
                float xa = pos.x + x * 2;
                float ya = pos.y + y * 2;
                sr.rect(xa, ya, 2, 2);
            }

        }

        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GREEN);
        sr.circle(mapwarp.x+1,mapwarp.y+1,9);
        sr.setColor(Color.MAGENTA);
        sr.circle(mapshop.x+1,mapshop.y+1,9);

        if(oBlink1.getVal()){
            sr.setColor(Color.CYAN);}
        else{
            sr.setColor(Color.WHITE);}



        sr.circle(mapplay.x+1,mapplay.y+1, playerCircleRadius);
        playerCircleRadius = boundW(playerCircleRadius,10);

        sr.end();

    }

}
