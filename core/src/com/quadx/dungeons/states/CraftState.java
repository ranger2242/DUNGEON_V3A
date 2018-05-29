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
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.Leather;
import com.quadx.dungeons.items.Ore;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.modItems.*;
import com.quadx.dungeons.items.recipes.Recipe;
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
    Item[] resources = new Item[7];
    ShapeRendererExt sr = new ShapeRendererExt();
    private Text resTitle;
    private Text slotTitle;
    static Inventory slots = new Inventory();
    Delta dClick = new Delta(10*ft);
    static Equipment currentCraft = null;
    Rectangle[] resRegions = new Rectangle[resources.length];
    Rectangle[] slotRegions = new Rectangle[5];
    InfoOverlay crafted= new InfoOverlay();
    ButtonHandler buttons = new CraftStateButtonHandler();


    public CraftState(GameStateManager gsm) {
        super(gsm);
        craftStacks = player.inv.getCraftStacks();
        resources[0] = new Ore();
        resources[1] = new Leather();
        resources[2] = new StrengthPlus();
        resources[3] = new DefPlus();
        resources[4] = new IntPlus();
        resources[5] = new SpeedPlus();
        resources[6] = new Hypergem();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, WIDTH, HEIGHT);
        slotTitle = new Text("SLOTS", new Vector2(scrV(.1f, .25f)), WHITE, 2);

        //click regions
        for (int i = 0; i < craftStacks.length; i++) {
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
/*            int[] cost = currentCraft.getCraftCost();
            if (cost[0] == slots.getStackSize(Ore.class)) {
                for (int i = 0; i < cost[0]; i++) {
                    slots.getStackByType(Ore.class).remove(0);
                }
            }
            if (cost[0] == slots.getStackSize(Leather.class))
                for (int i = 0; i < cost[1]; i++) {
                    slots.getStackByType(Leather.class).remove(0);
                }*/
        }
    }

    @Override
    public void update(float dt) {
        buttons.update(dt);
        dClick.update(dt);
        craftStacks = player.inv.getCraftStacks();

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER ) &&dClick.isDone()){
            CraftState.craftItem();
            dClick.reset();
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) ) {
            out(buttons.mpos.toString());
            for (int i = 0; i < resRegions.length; i++) {
                Rectangle r = resRegions[i];
                if (r.contains(new Vector2(buttons.mpos).add(view))&& dClick.isDone()) {
                    out(i + " Clicked");
                    if(slots.hasStack(resources[i]) || slots.getList().size()<5) {
                        slots.addToInv(resources[i]);
                        dClick.reset();
                        new HoverText("-1", Color.RED, new Vector2(r.x, r.y), false);
                    }
                }
            }
            for (int i = 0; i < slotRegions.length; i++) {
                Rectangle r = slotRegions[i];
                if (r.contains(new Vector2(buttons.mpos).add(view)) && dClick.isDone()) {
                    try {
                        out(i + " Clicked");
                        Item item=slots.getStack(i).get(0);
                        if(item instanceof Ore){
                            player.addOre();
                        }else if(item instanceof Leather){
                            player.addLeather();
                        }
                        player.inv.addToInv(item);
                        slots.remove(i);
                        dClick.reset();
                        new HoverText("-1", Color.RED, new Vector2(r.x, r.y), false);
                    }catch (NullPointerException e){

                    }
                }
            }
        }
        while (slots.getList().size()>=6){
            slots.removeStack(5);
        }

        for (int i = 0; i < craftStacks.length; i++) {
            Vector2 v = scrV(.1f + (.05f * i), 5f / 6f);
            resRegions[i] = new Rectangle(v.x, v.y, 32, 32);
        }
        checkOutputItem();
        HoverText.update(dt);
        camController.update(dt, cam);
        cam.update();
    }

    private void checkOutputItem() {
        int o= slots.getStackSize(Ore.class);
        int l= slots.getStackSize(Leather.class);
        int st = slots.getStackSize(StrengthPlus.class);
        int de = slots.getStackSize(DefPlus.class);
        int in = slots.getStackSize(IntPlus.class);
        int sp = slots.getStackSize(SpeedPlus.class);

        out(o + " " + l);

        Equipment e = null;
        for (Recipe r : player.getCraftable()) {
            Pair<Integer, Item>[] ar = r.getCosts();
            if (ar[0].getKey() == o && ar[1].getKey() == l) {//add gold cost here
                e = r.getEquip();
                out("___________________");
            }
        }
        int s = 2;
        Vector2 v = scrV(.4f, .15f);
        if(e!=null) {
            int[] buffs = new int[]{0, 0, 0, s * st, s * de, s * in, s * sp};
            currentCraft = new Equipment(e.getTypeEnum(), "Custom " + e.getName(), buffs);
            out(currentCraft.getName());
            out(Arrays.toString(currentCraft.getBuffs()));
            crafted = HUD.generateItemDetailUI(v, currentCraft,false);
        }else{
            crafted=new InfoOverlay();
        }
    }


    private void drawResourcesSb(SpriteBatch sb) {
        resTitle = new Text("RESOURCES", new Vector2(), WHITE, 2);
        resTitle.pos = new Vector2(scrV(.1f, .95f));
        Game.setFontSize(2);
        BitmapFont font = Game.getFont();
        font.setColor(WHITE);
        Vector2 tv = new Vector2(resTitle.pos);
        Vector2 t2 = new Vector2(slotTitle.pos);

        font.draw(sb, resTitle.text, tv.x, tv.y);
        font.draw(sb, slotTitle.text, t2.x, t2.y);
        for (int i = 0; i < craftStacks.length; i++) {
            Rectangle r = resRegions[i];
            Vector2 v = new Vector2(r.x, r.y);
            font.draw(sb, craftStacks[i] + "", v.x, v.y);
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
        if(currentCraft !=null){
            //Vector2 v = scrV(.5f,.12f);
            //currentCraft.loadIcon();
            //sb.draw(currentCraft.getIcon(),v.x,v.y);
            crafted.draw(sb);
        }
        HoverText.render(sb);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        titleLine(sr, resTitle);
        titleLine(sr,slotTitle);
        titleLine(sr,reTitle);
        drawSlotsSR(sr);
        sr.end();
    }

    Text reTitle;
    private void drawRecipes(SpriteBatch sb) {
        ArrayList<Recipe> rs= player.getCraftable();
        reTitle= new Text("RECIPES",  scrV(2f/3f, .95f),WHITE,2);
        Game.getFont().draw(sb,reTitle.text,reTitle.pos.x,reTitle.pos.y);
        for(int i=0;i<rs.size();i++){
            rs.get(i).render(sb,scrV(2f/3f, .85f-(.1f*i)));
        }
    }

    private void drawSlotsSB(SpriteBatch sb) {
        for (int i = 0; i < slots.getList().size(); i++) {
            Rectangle r=slotRegions[i];
            Vector2 v= new Vector2(r.x,r.y);
            sb.draw(slots.getStack(i).get(0).getIcon(), v.x, v.y);
            font.draw(sb, slots.getStack(i).size() + "", v.x, v.y-20);

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
    }

    @Override
    public void dispose() {

    }
}
