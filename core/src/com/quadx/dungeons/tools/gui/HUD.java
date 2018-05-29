package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Time;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.res;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;
import static com.quadx.dungeons.tools.gui.Text.fitLineToWord;

/**
 * Created by Chris Cavazos on 1/25/2017.
 */
public class HUD {
    static InfoOverlay hud = new InfoOverlay();
    static InfoOverlay invOverlay = new InfoOverlay();
    static InfoOverlay equipOverlay = new InfoOverlay();
    static ArrayList<InfoOverlay> attackBarHud = new ArrayList<>();
    public static ArrayList<String> output = new ArrayList<>();

    public static Vector2[] statsPos;
    public static Vector2 minimapPos = new Vector2();
    public static Vector2 fpsGridPos = new Vector2();
    static Vector2 equipPos = new Vector2();
    public static Vector2 attackBarPos = new Vector2();
    static Vector2 inventoryPos = new Vector2();
    static Vector2 statListPos = new Vector2();
    static Vector2 playerStatBarPos = new Vector2();
    public static float dtLootPopup = 0;
    public static Texture lootPopup;

    public static Delta dPopup = new Delta(20 * Time.ft);

    public static ArrayList<Rectangle> equipBoxes = new ArrayList<>();
    public static Rectangle[] playerStatBars = new Rectangle[3];

    public HUD() {
        bufferOutput();
    }

    public static void create() {
        //reset variables
        equipBoxes.clear();
        hud.texts.clear();
        hud.rects.clear();

        Vector2 view = State.getView();
        //hud rects
        if (MapState.showStats) {
            hud.rects.add(new Rectangle(view.x, view.y + HEIGHT - 300, 300, 300));
        }
        hud.rects.add(new Rectangle(view.x, view.y, WIDTH, 207));
        //player score
        int x = (int) ((view.x + 20));
        String score = "SCORE: " + player.st.getPoints(player) + "";
        hud.texts.add(new Text(score, new Vector2(x, (view.y + 200)), Color.GRAY, 1));
        String scoreH;
        try {
            scoreH = "HIGH SCORE: " + HighScoreState.scores.get(0).getScore();
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            scoreH = "HIGH SCORE: 000000";
        }
        hud.texts.add(new Text(scoreH, new Vector2(x + (Game.WIDTH / 4) - 100, (view.y + 200)), Color.GRAY, 1));
        generateAttackBarUI(attackBarPos);
        Item item= player.inv.getSelectedItem();
        if(item!=null)
        invOverlay = generateItemDetailUI(inventoryPos,item,true);
        else
            invOverlay=new InfoOverlay();
        generateEquipmentUI(equipPos);
        generateStatListPos(statListPos);
        generatePlayerStatBars(playerStatBarPos);
    }

    public static void update() {
        Vector2 view = State.getView();
        int x = (int) view.x;
        int y = (int) viewY;
        int w = WIDTH;
        equipPos = new Vector2(new Vector2((x + (w / 3) + 35), y + 130));
        minimapPos = new Vector2(x + w - ((res * 2) + 20), y + 20);
        attackBarPos = new Vector2(x + 20, y + 30);
        inventoryPos = new Vector2((view.x + (WIDTH / 2)), viewY + 130);
        fpsGridPos.set((int) (view.x + (Game.WIDTH * 2 / 3)), viewY + 20);
        statListPos = new Vector2(view.x + 30, viewY + HEIGHT - 30);
        playerStatBarPos = new Vector2(view.x + 20, viewY + 145);
        create();

    }

    public static void out(String s) {
        System.out.println(s);
        if (output != null) {
            output.add(s);
            if (output.size() > 10) output.remove(0);
        }

    }

    public static void titleLine(ShapeRendererExt shapeR, Text t) {
        titleLine(shapeR, t.pos, t.text);
    }

    public static void titleLine(ShapeRendererExt sr, Vector2 t, String st) {
        float[] s = fitLineToWord(st);
        sr.setColor(Color.RED);
        sr.line(t.x + s[0], t.y + s[1], t.x + s[2], t.y + s[3]);
        sr.line(t.x + s[4], t.y + s[5], t.x + s[6], t.y + s[7]);

    }

    public static void titleLine(ShapeRendererExt shapeR, Title t) {
        titleLine(shapeR, new Vector2(t.x, t.y), t.text);
    }

    static void generateAttackBarUI(Vector2 pos) {
        attackBarHud.clear();
        for (int i = 0; i < player.attackList.size(); i++) {
            Attack a = player.attackList.get(i);
            InfoOverlay io = new InfoOverlay();
            Attack.CostType type = a.getType();
            int xoff = (i * 52);
            int x = (int) (pos.x + xoff);
            try {
                if (type == Attack.CostType.Mana) {
                    float m = player.st.getMana();
                    if (m >= a.getCost()) {
                        io.textures.add(a.getIcon());
                        io.texturePos.add(new Vector2(x, pos.y + 18));
                        if (i <= 7)
                            io.texts.add(new Text((i + 1) + "", new Vector2(x, pos.y + 28), Color.WHITE, 1));
                    } else {
                        int rem = (int) (a.getCost() - m);
                        io.texts.add(new Text(rem + "", new Vector2(x + 52 / 2, pos.y + 40), Color.WHITE, 1));
                    }
                    io.texts.add(new Text("M" + a.getCost(), new Vector2(x, pos.y), Color.WHITE, 1));
                } else if (type == Attack.CostType.Energy) {
                    float e = player.st.getEnergy();

                    if (e >= a.getCost()) {
                        io.textures.add(a.getIcon());
                        io.texturePos.add(new Vector2(x, pos.y + 18));
                        if (i <= 7)
                            io.texts.add(new Text((i + 1) + "", new Vector2(x, pos.y + 28), Color.WHITE, 1));
                    } else {
                        int rem = (int) (a.getCost() - e);
                        io.texts.add(new Text(rem + "", new Vector2(x + 52 / 2, pos.y + 40), Color.WHITE, 1));
                    }
                    io.texts.add(new Text("E" + a.getCost(), new Vector2(x, pos.y), Color.WHITE, 1));
                }
                io.texts.add(new Text("Lv." + (a.getLevel() + 1), new Vector2(x, pos.y + 18), Color.WHITE, 1));
            } catch (NullPointerException ignored) {
            }
            attackBarHud.add(io);
        }
    }

    public static InfoOverlay generateItemDetailUI(Vector2 pos, Item item, boolean isPlayer) {
        //add selected item
        InfoOverlay invOverlay = new InfoOverlay();
        //if (prevItem != item) {
        String name = (Inventory.pos) + ":" + item.getName();
        ArrayList<String> outList = new ArrayList<>();
        if (item.getHpmod() != 0) {
            outList.add("HP " + item.getHpmod());
        }
        if (item.getManamod() != 0) {
            outList.add("M :" + item.getManamod());
        }//Mana
        if (item.getEmod() != 0) {
            outList.add("E :" + item.getEmod());
        }//Mana
        if (item.getStrmod() != 0) {
            outList.add("ATT :" + item.getStrmod());
        }  //str
        if (item.getDefensemod() != 0) {
            outList.add("DEF :" + item.getDefensemod());
        } //defense
        if (item.getIntelmod() != 0) {
            outList.add("INT :" + item.getIntelmod());
        }//intel
        if (item.getSpeedmod() != 0) {
            outList.add("SPD :" + item.getSpeedmod());
        }//speed
        invOverlay.texts.add(new Text(name, new Vector2(pos.x, pos.y + 50), Color.WHITE, 1));
        for (int i = 0; i < outList.size(); i++) {
            invOverlay.texts.add(new Text(outList.get(i), new Vector2(pos.x, pos.y - ((i + 1) * 20)), Color.WHITE, 1));
        }

        if(isPlayer) {
            invOverlay.texts.add(new Text("x" + player.inv.getSelectedStack().size(), pos, Color.WHITE, 1));
        }
        invOverlay.textures.add(item.getIcon());
        invOverlay.texturePos.add(pos);


        return invOverlay;


    }

    static void generateEquipmentUI(Vector2 pos) {
        //updateMapState equipboxes positions
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                equipBoxes.add(new Rectangle(pos.x + (j * 36), pos.y + (i * 36) - 20, 32, 32));
            }
        }
        //add equipment
        equipOverlay = new InfoOverlay();
        int count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (count < player.inv.getEquiped().size()) {
                    int nx = (int) (pos.x + (j * 36));
                    int ny = (int) (pos.y + (i * 36) - 20);
                    try {
                        equipOverlay.textures.add(player.inv.getEquiped().get(count).getIcon());
                    } catch (Exception e) {
                        equipOverlay.textures.add(ImageLoader.crate);
                    }
                    equipOverlay.texturePos.add(new Vector2(nx, ny));

                    count++;
                }
            }
        }
        invOverlay.add(equipOverlay);
    }

    static void generatePlayerStatBars(Vector2 pos) {
        float h = 10;
        float barMax = (WIDTH / 3) - 10;
        float pHPBar = player.st.getPercentHP() * barMax;
        float pManaBar = player.st.getPercentMana() * barMax;
        float pEnergyBar = player.st.getPercentEnergy() * barMax;
        playerStatBars[0] = new Rectangle(pos.x, pos.y + 30, pHPBar, h);
        playerStatBars[1] = new Rectangle(pos.x, pos.y + 15, pManaBar, h);
        playerStatBars[2] = new Rectangle(pos.x, pos.y, pEnergyBar, h);
    }

    static public Vector2[] generateStatListPos(Vector2 pos) {
        //updateMapState player stat list pos
        ArrayList<String> list = new ArrayList<>(player.st.getStatsList(player));
        statsPos = new Vector2[list.size()];
        int last = 0;
        for (int i = 0; i < list.size(); i++) {
            int space = 20;
            if (i < 2 || i > 9)
                space = 10;
            if (i == 13)
                space = 20;
            last += space;
            statsPos[i] = new Vector2(pos.x, pos.y - last);
        }
        return statsPos;
    }

    public static Vector2[] getStatPos() {
        return statsPos;
    }

    public static InfoOverlay getInfoOverlay() {
        return hud;
    }

    public static InfoOverlay getInvOverlay() {
        return invOverlay;
    }

    public static ArrayList<InfoOverlay> getAttackBarOverlay() {
        return attackBarHud;
    }

    private void bufferOutput() {
        for (int i = 0; i < 10; i++)
            out("");
    }

    public static void setLootPopup(Texture t) {
        dPopup.reset();
        lootPopup = t;
    }

}
