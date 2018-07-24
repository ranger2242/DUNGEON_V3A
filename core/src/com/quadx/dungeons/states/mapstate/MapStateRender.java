package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Protect;
import com.quadx.dungeons.items.resources.Gold;
import com.quadx.dungeons.monsters.MonsterHandler;
import com.quadx.dungeons.shapes1_5.Line;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Oscillator;
import com.quadx.dungeons.tools.timers.Time;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.scr;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.tools.StatManager.gameTime;


/**
 * Created by Tom on 12/28/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateRender extends MapState {
    private static int playerCircleRadius = 0;
    public static ArrayList<Rectangle> monAgroBoxes = new ArrayList<>();
    public static double time = 0;

    public static Oscillator oBlink1 = new Oscillator(20 * Time.ft);
    public static Oscillator oBlink2 = new Oscillator(4 * Time.ft);
    static Delta dGrow = new Delta(5 * Time.ft);


    static void renderLayers(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);

        //Game Layer
        drawGLShapes();
        drawGLBatch(sb);
        //HUD Layer
        drawHUDShapes();
        drawHUDBatch(sb);

        Tests.drawFPSModule(sb, sr, HUD.fpsGridPos);
        drawMiniMapModule(HUD.minimapPos);
        srDrawAttackSelectors();
    }

    static void drawHUDShapes() {
        //shape renderer filled------------------------------------
        //enable transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        //Knockback
        sr.setColor(WHITE);

        Vector2 c = new Vector2(
                view.x + scr.x / 2,
                view.y + scr.y / 2);
        Vector2 e = new Vector2(
                c.x + player.kba.x,
                c.y + player.kba.y);
        sr.line(c, e);

        //draw hud rectangles
        for (Rectangle r : HUD.getInfoOverlay().rects) {
            sr.setColor(.1f, .1f, .1f, .7f);
            sr.rect(r);
        }
        if (Protect.active)
            sr.setColor(0, 0, 1, 1 / 3f);
        else
            sr.setColor(player.getDeathShade());

        sr.rect(view.x, viewY, scr.x, scr.y);
        sr.end();
        Gdx.gl.glDisable(GL_BLEND);
        // end transparency------------------------------------------------------------
        sr.begin(ShapeRenderer.ShapeType.Filled);
        player.renderStatBars(sr);

        //line draw functions---------------------------------------------------
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GRAY);
        //draw equipment slots
        for (Rectangle r : HUD.equipBoxes)
            sr.rect(r);
        sr.end();

    }

    //---HUD SPRITEBATCH FUNCTIONS---------------------------------------
    static void drawGameTime(SpriteBatch sb) {
        NumberFormat formatter = new DecimalFormat("#00.00");

        double d = gameTime.getElapsedD();//Double.valueOf(formatter.format( Double.valueOf(gameTime.getElapsed())));
        time += d;
        // time=Double.valueOf(formatter.format(time));
        String t = "Game Time: " + (int) (time / 60) + ":" + Double.valueOf(formatter.format(time % 60));
        Player.multiplier = (float) (1 + (.1 * (int) (gameTime.getElapsedD() / 30f)));
        String t2 = gameTime.runtime();
        String t3 = "MULT:" + Player.multiplier;
        Text.getFont().draw(sb, t2, view.x - 100 + scr.x / 2, viewY + scr.y - 30);
        Text.getFont().draw(sb, t3, view.x - 100 + scr.x / 2, viewY + scr.y - 40);

        gameTime.end();
        //gameTime.start();
    }

    static void drawHUDBatch(SpriteBatch sb) {
        //spritebatch draw functions---------------------------------------------------
        sb.begin();
        Text.setFontSize(1);
        drawGameTime(sb);

        //Draw player stats
        if (showStats) {
            player.renderStatList(sb, new Vector2(view.x + 30, viewY + scr.y - 30));
        }
        //Draw score
        Text.getFont().setColor(Color.WHITE);
        for (Text t : HUD.getInfoOverlay().texts) {
            Text.getFont().draw(sb, t.text, t.pos.x, t.pos.y);
        }
        //draw str menu
        ArrayList<InfoOverlay> list = HUD.getAttackBarOverlay();
        for (InfoOverlay io : list) {
            io.draw(sb);
        }
        //draw inventory
        HUD.getInvOverlay().draw(sb);
        //draw details messages
        if (Tests.output) {
            Text.setFontSize(1);
            Text.font.setColor(Color.WHITE);
            for (int i = 0; i < 10; i++) {
                try {
                    Text.getFont().draw(sb, HUD.output.get(i), scrVx(2f / 3f), viewY + scr.y - (i * 20) - 30);
                } catch (IndexOutOfBoundsException | NullPointerException ignored) {
                }
            }
        }
        //draw loot popup
        try {
            if (HUD.dPopup.isDone()) {
                Vector2 v = new Vector2(player.abs());
                Vector2 ic = player.body.getIconDim();
                v.y += 40 + ic.y;
                v.x += ic.x / 2;
                sb.draw(HUD.lootPopup, v.x, GridManager.fixY(v));
            }
        } catch (NullPointerException ignored) {
        }
        //draw ability icon
        if (!HUD.equipBoxes.isEmpty()) {
            Vector2 v = new Vector2(HUD.equipBoxes.get(0).getPosition(new Vector2()));
            Ability ab=player.getAbility();
            TextureRegion t=ab.getIcon();
            int h= (int) (v.y - 60);

            sb.draw(t, v.x, h);
            Text.getFont().setColor(Color.WHITE);
            Text.getFont().draw(sb, ab.getLevel() + "", v.x, v.y - 60);
            ArrayList<Ability> alist=player.getSecondaryAbilityList();
            if (!alist.isEmpty())
                for (int i = 0; i <alist.size(); i++) {
                    Ability a = alist.get(i);
                    int w= (int) (v.x+(t.getRegionWidth()* (i + 1)));
                    sb.draw(a.getIcon(), w, h);
                    Text.getFont().draw(sb, a.getLevel() + "", w, h);

                }
        }
        sb.end();
    }

    //Game Layer---------------------------------------------------------

    static void drawGLShapes() {
        //shape renderer filled------------------------------------
        sr.setAutoShapeType(true);
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        for (Cell c : drawList) c.renderSR(sr);     //draw cells
        player.renderSR(sr);                        //draw player
        MonsterHandler.renderSR(sr);                //draw monsters
        sr.end();

        //draw transparent things---------------------------------------------------------------
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (Tests.showhitbox) {
            //draw monster aggro box
            for (Rectangle rec : monAgroBoxes) {
                try {
                    sr.setColor(1, 0, 0, .2f);
                    sr.rect(rec);
                } catch (NullPointerException ignored) {}
            }
        }
        sr.end();

        //shape renderer lines-------------------------------------------------
        sr.begin(ShapeRenderer.ShapeType.Line);
        for (Cell c : drawList) {
            if (c.isClear()) {
                //draw grid
                sr.setColor(Color.GRAY);
                sr.getColor().a = .3f;
                sr.polygon(Arrays.copyOf(c.getCorners().getVertices(), 8));
            }
        }
        ArrayList<Line> lines = player.getAttackChain();
        for (Line l : lines) {
            sr.setColor(WHITE);
            sr.line(l);
        }
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);


    }

    static void drawGLBatch(SpriteBatch sb) {
        sb.begin();
        for (Cell c : drawList) c.render(sb);   //draw cells
        for (Anim a : Anim.anims) a.render(sb); //draw animations
        particleHandler.render(sb);             //draw particles
        player.render(sb);                      //draw player
        MonsterHandler.render(sb);              //draw monsters
        HoverText.render(sb);                   //draw hovertext
        Text.resetFont();
        sb.end();
    }
    ////Modules----------------------------------------------------------

    private static void drawMiniMapModule(Vector2 pos) {
        float r = res * 2;
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(BLACK);
        sr.rect(pos.x, pos.y, r, r);
        sr.end();

        drawGrid(pos);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.rect(pos.x, pos.y, r, r);
        sr.end();
    }

    //OLD RENDERING------------------------------------------------------
    private static void sbDrawTiles(SpriteBatch sb) {
        sb.begin();
        for (Cell c : drawList) {
            Vector2 v = c.abs();
            sb.draw(c.getTile(), v.x, v.y);
            if (c.getItem() != null && c.isClear()) {
                if (c.getItem().getIcon() == null) {
                    TextureRegion t = ImageLoader.nullIc;
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

    private static void srDrawAttackSelectors() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        int xoffset = (int) HUD.attackBarPos.x;
        for (int i = 0; i < player.attackList.size(); i++) {

            int x = xoffset + (i * 52);
            if (i == altNumPressed) {
                sr.setColor(Color.BLUE);
                sr.rect(x, viewY + 86, 10, 10);
            }
            if (i == Attack.pos) {
                sr.setColor(Color.RED);
                sr.rect(x + 38, viewY + 86, 10, 10);
            }
        }
        sr.end();
    }

    //OTHER----------------------------------------------------------------
    public MapStateRender(GameStateManager gsm) {
        super(gsm);
    }

    static void updateVariables(float dt) {
        dGrow.update(dt);
        oBlink1.update(dt);
        oBlink2.update(dt);

        if (dGrow.isDone()) {
            playerCircleRadius++;
            dGrow.reset();
        }
    }

    public static void drawCircle(int x, int y, float r, Color c) {
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(c);
        sr.circle(x, y, r);
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
                if (player.pos().x == x && player.pos().y == y) {
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
        sr.circle(mapwarp.x + 1, mapwarp.y + 1, 9);
        sr.setColor(Color.MAGENTA);
        sr.circle(mapshop.x + 1, mapshop.y + 1, 9);

        if (oBlink1.getVal()) {
            sr.setColor(Color.CYAN);
        } else {
            sr.setColor(Color.WHITE);
        }


        sr.circle(mapplay.x + 1, mapplay.y + 1, playerCircleRadius);
        playerCircleRadius = boundW(playerCircleRadius, 10);

        sr.end();

    }

}
