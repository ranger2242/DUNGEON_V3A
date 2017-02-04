package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.gui.Title;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;
import static com.quadx.dungeons.tools.gui.Text.strWidth;

/**
 * Created by Tom on 12/24/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ShopState extends State {

  private int soldItemCost=0;
    private static float dtBuy=0;
    private float dtSold=0;
    private float dtScroll=0;
    int invoffset=0;
    private boolean dispSold = false;
    private static final ShapeRenderer shapeR=new ShapeRenderer();
    private static final ArrayList<Item> shopInv = new ArrayList<>();

    public ShopState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, WIDTH, HEIGHT);
        cam.position.set(viewX+ WIDTH/2,viewY+ HEIGHT/2,0);
        if(!MapState.pause)
        genShopInv();
        Gdx.gl.glClearColor(0,0,0,1);
    }
    public static void exit(){
        MapState.pause=false;
        gsm.pop();
    }
    protected void handleInput() {
        for(Command c: commandList){
            c.execute();
        }
        //keyboard functions--------------------------------------------------------

        if (dtScroll > .1f) {

            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (invoffset > 0) {
                    invoffset--;
                }
                dtScroll = 0;

            }

            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (invoffset + 9 < player.invList.size()) {
                    invoffset++;
                }
                dtScroll = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) numberButtonHandler(0);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) numberButtonHandler(1);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) numberButtonHandler(2);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) numberButtonHandler(3);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) numberButtonHandler(4);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) numberButtonHandler(5);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) numberButtonHandler(6);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) numberButtonHandler(7);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) numberButtonHandler(8);
    }
    private void numberButtonHandler(int i){
        if(!MapState.pause && Gdx.input.isKeyPressed(Input.Keys.MINUS) && dtBuy>.3){
                if(i>=0) {
                        try {
                            try {
                                Item item = player.invList.get(i).get(0);
                                if(item.isEquip)
                                    item.loadIcon(item.getType());
                                else
                                    item.loadIcon(item.getName());
                                player.invList.get(i).remove(0);
                                soldItemCost= (int) (item.getCost()*.75);
                                Game.player.setGold((float) (Game.player.getGold()+soldItemCost));
                                dispSold=true;
                                dtBuy=0;
                                if(player.invList.get(i).isEmpty()){
                                    player.invList.remove(i);
                                }
                            } catch (IndexOutOfBoundsException e) {
                            }
                        } catch (NullPointerException e) {
                        }
                    }

        }
        if(i<shopInv.size() && dtBuy>.3 && Game.player.getGold()>=shopInv.get(i).getCost()){
            Game.player.setGold(Game.player.getGold()-shopInv.get(i).getCost());
            Game.player.addItemToInventory(shopInv.get(i));
            if(i>=6) shopInv.remove(i);
            dtBuy=0;
        }
    }
    public void update(float dt) {
        handleInput();
        dtBuy+=dt;
        dtScroll+=dt;
        if(dispSold) dtSold+=dt;
        cam.update();
    }
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        shapeR.setProjectionMatrix(cam.combined);

        if(!MapState.pause)
        drawPlayerInv(sb);

        sb.begin();
        GlyphLayout gl = new GlyphLayout();
        CharSequence cs="(TAB) EXIT";
        gl.setText(Game.getFont(),cs);
        Game.getFont().draw(sb,"(TAB) EXIT",viewX+(WIDTH/2)-gl.width/2,viewY+70);
        sb.end();

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(1,0,0,1);
        shapeR.rect(viewX+40,viewY+40, WIDTH-80, HEIGHT-80);
        shapeR.end();
    }
    private void drawPlayerInv(SpriteBatch sb){
        sb.begin();
        Game.getFont().draw(sb,"EQUIPMENT",viewX +(WIDTH/3)+100,viewY+ HEIGHT-50);
        Game.getFont().draw(sb,"INVENTORY",viewX +(2*WIDTH/3),viewY+ HEIGHT-50);

        Game.getFont().draw(sb,"G "+ Game.player.getGold(),viewX+(3*WIDTH/4),viewY+ HEIGHT-50);
        if(dtSold<.5 && dispSold){
            Game.getFont().draw(sb,"+"+soldItemCost,viewX+ WIDTH-150,viewY+ HEIGHT-50);
        }
        else{
            dtSold=0;
            dispSold=false;
        }
        int yoff=-120;
        //player inventory
        for(int i = 0; i< 9; i++) {
            try {
                ArrayList<Item> al = Game.player.invList.get(i+invoffset);
                Game.getFont().draw(sb, (i + 1) + ". " + al.get(0).getName(), viewX +(2*WIDTH/3), viewY + HEIGHT - (i * 60)+yoff);
                Game.getFont().draw(sb, "x" + al.size(), viewX + WIDTH - 100, viewY + HEIGHT  - (i * 60)+yoff-20);
                sb.draw( al.get(0).getIcon(),viewX +(2*WIDTH/3), viewY + HEIGHT - (i * 60)+yoff+10);
            }catch (IndexOutOfBoundsException e){}
        }
        //equipment
        for(int i=0;i<player.equipedList.size();i++){
            Equipment e= player.equipedList.get(i);
            Game.getFont().draw(sb, "# "+e.getName(), viewX +(WIDTH/3)+100, viewY + HEIGHT - (i * 60)+yoff);
            sb.draw(e.getIcon(),viewX +(WIDTH/3)+100, viewY + HEIGHT - (i * 60)+yoff+10);
        }
        //shop inv
        Game.getFont().draw(sb,"SHOP",viewX+ 70,viewY+ HEIGHT-50);
        for(int i=0;i<shopInv.size();i++){
            Item item=shopInv.get(i);
            Game.getFont().draw(sb,(i+1)+". "+item.getName(),viewX+70,viewY+ HEIGHT-(i*60)+yoff);
            Game.getFont().draw(sb,item.getCost()+"G",viewX+(WIDTH/4),viewY+ HEIGHT-(i*60)+yoff-20);
            sb.draw( item.getIcon(),viewX+70, viewY + HEIGHT- (i * 60)+yoff+10);

        }
        sb.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.RED);
        titleLine(new Title("SHOP", 70, HEIGHT-50));
        titleLine(new Title("INVENTORY",(2*WIDTH/3), HEIGHT-50));
        titleLine(new Title("EQUIPMENT",(WIDTH/3)+100, HEIGHT-50));
        shapeR.setColor(Color.GRAY);
        shapeR.line(viewX+(2*WIDTH/3)-20,viewY+100,viewX+(2*WIDTH/3)-20,viewY+HEIGHT-100);
        shapeR.line(viewX+(WIDTH/3)+100-20,viewY+100,viewX+(WIDTH/3)+100-20,viewY+HEIGHT-100);

        shapeR.end();
    }
    float[] fitLineToWord(String s){
        //x1,y1,x2,y2
        float[] arr=new float[8];
        arr[0]= -10;
        arr[1]=-15;
        arr[2]=strWidth(s)+20;
        arr[3]=-15;
        arr[4]= -10;
        arr[5]=-12;
        arr[6]=strWidth(s)+35;
        arr[7]=-12;
        return arr;
    }
    //---------------------------------------------------------
// Render
    void titleLine(Title t){
        float[] s = fitLineToWord(t.text);
        float x=t.x;
        float y=t.y;
        shapeR.line(viewX + x + s[0], viewY +y+ s[1] , viewX + x + s[2], viewY + y+ s[3]);
        shapeR.line(viewX + x + s[4], viewY +y+ s[5] , viewX + x + s[6], viewY + y+ s[7]);
    }
    private void genShopInv(){
        shopInv.clear();
        shopInv.add(new Potion());
        shopInv.add(new ManaPlus());
        shopInv.add(new StrengthPlus());
        shopInv.add(new DefPlus());
        shopInv.add(new IntPlus());
        shopInv.add(new SpeedPlus());
        Equipment e=Equipment.generateEquipment();
        shopInv.add(e);
        e=Equipment.generateEquipment();
        shopInv.add(e);
        SpellBook s = new SpellBook();
        shopInv.add(s);

    }
    public void dispose() {

    }
}
