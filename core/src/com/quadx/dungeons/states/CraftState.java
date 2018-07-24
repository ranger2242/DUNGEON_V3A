package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.potions.StatPotion;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.recipes.potionRecipes.StatPotionRe;
import com.quadx.dungeons.items.resources.*;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.tools.buttons.ButtonHandler;
import com.quadx.dungeons.tools.buttons.CraftStateButtonHandler;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.timers.Delta;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.gui.HUD.titleLine;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 5/28/2018.
 */
public class CraftState extends State {
    int[] craftStacks;
    Item[] resources;
    ShapeRendererExt sr = new ShapeRendererExt();
    private Text resTitle;
    private Text slotTitle;
    static Inventory slots = new Inventory();
    Delta dClick = new Delta(10 * ft);
    static Item currentCraft = null;
    Rectangle[] resRegions;
    Rectangle[] slotRegions = new Rectangle[5];
    Rectangle[] qRegions = new Rectangle[10];
    InfoOverlay crafted = new InfoOverlay();
    ButtonHandler buttons = new CraftStateButtonHandler();


    public CraftState(GameStateManager gsm) {
        super(gsm);
        craftStacks = player.inv.getCraftStacks();
        resources = new Item[32];
        resRegions = new Rectangle[resources.length];
        resources[0] = new Ore();
        resources[1] = new Gold();
        resources[2] = new StrengthPlus();
        resources[3] = new DefPlus();
        resources[4] = new IntPlus();
        resources[5] = new SpeedPlus();
        resources[6] = new Hypergem();
        resources[7] = new ChargeStone();
        resources[8] = new Crystal(1);
        resources[9] = new Crystal(2);
        resources[10] = new Crystal(3);
        resources[11] = new Meat(1);
        resources[12] = new Meat(2);
        resources[13] = new Meat(3);
        resources[14] = new Claw();
        resources[15] = new Tooth();
        resources[16] = new Water();
        resources[17] = new Blood();
        resources[18] = new Venom();
        resources[19] = new Wing();
        resources[20] = new Tail();
        resources[21] = new Brain();
        resources[22] = new Heart();
        resources[23] = new Leather();
        resources[24] = new Bone();
        resources[25] = new Flower();
        resources[26] = new Mushroom();
        resources[27] = new Leaf();
        resources[28] = new Grass();
        resources[29] = new Beetle();
        resources[30] = new Spider();
        resources[31] = new Fish();


        cam = new OrthographicCamera();
        cam.setToOrtho(false, scr.x, scr.y);
        slotTitle = new Text("SLOTS", new Vector2(scrV(.1f, .25f)), WHITE, 2);

        //click regions
        for (int i = 0; i < resRegions.length; i++) {
            Vector2 v = scrV(.1f + (.05f * i), .9f);
            resRegions[i] = new Rectangle(v.x, v.y, 32, 32);
        }
        for (int i = 0; i < slotRegions.length; i++) {
            Vector2 v = scrV(.1f + (.05f * i), .1f);
            slotRegions[i] = new Rectangle(v.x, v.y, 32, 32);
        }

    }

    public static void exit() {
        gsm.pop();
    }

    public static void craftItem() {
        if (currentCraft != null) {
            player.pickupItem(currentCraft);
            slots.clear();
        }
    }

    void checkResourceClick(Vector2 mpos) {
        for (int i = 0; i < resRegions.length; i++) {
            Rectangle r = resRegions[i];
            if (r.contains(mpos) && dClick.isDone()) {
                out(i + " Clicked");
                if (slots.hasStack(resources[i]) || slots.getList().size() < 5) {
                    slots.addToInv(resources[i]);
                    dClick.reset();
                    new HoverText("-1", Color.RED, new Vector2(r.x, r.y), false);
                }
            }
        }
    }

    void checkQRegion(Vector2 mpos) {
        for (int i = 0; i < qRegions.length; i++) {
            Rectangle r = qRegions[i];
            if (r.contains(mpos) && dClick.isDone()) {
                ArrayList<Recipe> rs = player.getCraftable();
                Pair<Integer, Item>[] pair = rs.get(i).getCosts();
                for (int j = 0; j < pair.length; j++) {
                    if (pair[j] != null) {
                        int amt = pair[j].getKey();
                        Item item = pair[j].getValue();
                        for (int k = 0; k < amt; k++) {
                            slots.addToInv(item);
                        }
                    }
                }
                dClick.reset();
            }
        }
    }

    void checkSlotRegion(Vector2 mpos){
        for (int i = 0; i < slotRegions.length; i++) {
            Rectangle r = slotRegions[i];
            if (r.contains(mpos) && dClick.isDone()) {
                try {
                    out(i + " Clicked");
                    Item item = slots.getStack(i).get(0);
                    if (item instanceof Ore) {
                        player.addOre();
                    } else if (item instanceof Leather) {
                        player.addLeather();
                    }
                    player.inv.addToInv(item);
                    slots.remove(i);
                    dClick.reset();
                    new HoverText("-1", Color.RED, new Vector2(r.x, r.y), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        buttons.update(dt);
        dClick.update(dt);
        craftStacks = player.inv.getCraftStacks();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && dClick.isDone()) {
            CraftState.craftItem();
            dClick.reset();
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            out(buttons.mpos.toString());
            Vector2 mpos = new Vector2(buttons.mpos).add(view);
            checkResourceClick(mpos);
            checkQRegion(mpos);
            checkSlotRegion(mpos);
        }
        while (slots.getList().size() >= 6) {
            slots.removeStack(5);
        }

        int ycnt = 0;
        int m = 0;
        for (int i = 0; i < resRegions.length; i++) {
            if (i % 8 == 0) {
                ycnt++;
                m = 0;
            }
            Vector2 v = scrV(.1f, .9f);
            resRegions[i] = new Rectangle(v.x + (50 * m), v.y - (ycnt * 50), 32, 32);
            m++;
        }
        checkOutputItem();
        HoverText.update(dt);
        camController.update(dt, cam);
        cam.update();
    }

    private void checkOutputItem() {
        Item e = null;
        int st = slots.getStackSize(StrengthPlus.class);
        int de = slots.getStackSize(DefPlus.class);
        int in = slots.getStackSize(IntPlus.class);
        int sp = slots.getStackSize(SpeedPlus.class);
        int s = 2;
        int[] buffs = new int[]{0, 0, 0, s * st, s * de, s * in, s * sp};

        for (Recipe r : player.getCraftable()) {
            if (r.isEquipRecipe() && r.meetsCost(slots, false)) {
                e = r.getOutput();
                out("___________________");
                currentCraft = new Equipment(((Equipment) e).getTypeEnum(), "Custom " + e.getName(), buffs);
                out(currentCraft.getName());
                out(Arrays.toString(((Equipment) currentCraft).getBuffs()));
            }
            if (r.isPotionRecipe()) {
                if (r instanceof StatPotionRe && r.meetsCost(slots, false)) {
                    StatPotion statPotion = (StatPotion) r.getOutput();
                    statPotion.setStats(new int[]{s * st, s * de, s * in, s * sp});
                    e = currentCraft = statPotion;
                    out(currentCraft.getName());
                }
                if (r.meetsCost(slots, true)) {
                    e = currentCraft = r.getOutput();
                    out(currentCraft.getName());
                }
            }
        }
        if (e != null) {
            Vector2 v = scrV(.4f, .15f);
            crafted = HUD.generateItemDetailUI(v, currentCraft, false);
        } else {
            crafted = new InfoOverlay();
        }

    }


    private void drawResourcesSb(SpriteBatch sb) {
        resTitle = new Text("RESOURCES", new Vector2(), WHITE, 2);
        resTitle.pos = new Vector2(scrV(.1f, .95f));
        Text.setFontSize(2);
        BitmapFont font = Text.getFont();
        font.setColor(WHITE);
        Vector2 tv = new Vector2(resTitle.pos);
        Vector2 t2 = new Vector2(slotTitle.pos);

        font.draw(sb, resTitle.text, tv.x, tv.y);
        font.draw(sb, slotTitle.text, t2.x, t2.y);
        for (int i = 0; i < resRegions.length; i++) {
            Rectangle r = resRegions[i];
            Vector2 v = new Vector2(r.x, r.y);
            if (i < craftStacks.length)
                font.draw(sb, craftStacks[i] + "", v.x, v.y);
            if(resources[i].getIcon()!=null)
            sb.draw(resources[i].getIcon(), v.x, v.y);
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);

        sb.begin();
        drawResourcesSb(sb);
        drawSlotsSB(sb);
        drawRecipes(sb);
        if (currentCraft != null) {
            //Vector2 v = scrV(.5f,.12f);
            //currentCraft.addIcon();
            //sb.draw(currentCraft.getIcon(),v.x,v.y);
            crafted.draw(sb);
        }
        HoverText.render(sb);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        titleLine(sr, resTitle);
        titleLine(sr, slotTitle);
        titleLine(sr, reTitle);
        drawSlotsSR(sr);
        sr.end();
    }

    Text reTitle;

    private void drawRecipes(SpriteBatch sb) {
        ArrayList<Recipe> rs = player.getCraftable();
        reTitle = new Text("RECIPES", scrV(2f / 3f, .95f), WHITE, 2);
        Text.getFont().draw(sb, reTitle.text, reTitle.pos.x, reTitle.pos.y);
        for (int i = 0; i < rs.size(); i++) {
            Vector2 p = scrV(2f / 3f, .85f - (.125f * i));
            if (i < 10)
                qRegions[i] = rs.get(i).getRect(p);
            rs.get(i).render(sb, p);
        }
    }

    private void drawSlotsSB(SpriteBatch sb) {
        for (int i = 0; i < slots.getList().size(); i++) {
            Rectangle r = slotRegions[i];
            Vector2 v = new Vector2(r.x, r.y);
            sb.draw(slots.getStack(i).get(0).getIcon(), v.x, v.y);
            Text.font.draw(sb, slots.getStack(i).size() + "", v.x, v.y - 20);

        }
    }

    private void drawSlotsSR(ShapeRendererExt sr) {
        sr.setColor(WHITE);
        for (Rectangle slotRegion : slotRegions) {
            sr.rect(slotRegion);
        }
        for (Rectangle resRegion : resRegions) {
            sr.rect(resRegion);
        }
        for (Rectangle qRegion : qRegions) {
            if(qRegion!=null)
            sr.rect(qRegion);
        }
    }

    @Override
    public void dispose() {

    }
}
