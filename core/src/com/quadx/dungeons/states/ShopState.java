package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.Delta;
import com.quadx.dungeons.tools.gui.CamController;
import com.quadx.dungeons.tools.gui.Title;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
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
    private boolean dispSold = false;
    private boolean drawError=false;
    private static final ShapeRenderer shapeR=new ShapeRenderer();
    private static final ArrayList<Item> shopInv = new ArrayList<>();
    CamController camController = new CamController();
    Delta dError = new Delta(72*ft);


    public ShopState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, WIDTH, HEIGHT);
        cam.position.set(view.x+ WIDTH/2,viewY+ HEIGHT/2,0);
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
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) numberButtonHandler(0);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) numberButtonHandler(1);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) numberButtonHandler(2);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) numberButtonHandler(3);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) numberButtonHandler(4);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) numberButtonHandler(5);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) numberButtonHandler(6);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) numberButtonHandler(7);
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) numberButtonHandler(8);
        else if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)
                && !(Gdx.input.isKeyPressed(Input.Keys.MINUS)
                    ||Gdx.input.isKeyPressed(commandList.get(0).getButtonK())
                ||Gdx.input.isKeyPressed(commandList.get(1).getButtonK()))) {
            camController.shakeScreen(5, 25);
            drawError=true;
        }
    }
    private void numberButtonHandler(int i){
        boolean minus = Gdx.input.isKeyPressed(Input.Keys.MINUS);
        if (!MapState.pause && minus && dtBuy > .3) {
            if (i < player.invList.size()) {
                Item item = player.invList.get(i).get(0);
                if (item.isEquip)
                    item.loadIcon(item.getType());
                else
                    item.loadIcon(item.getName());
                player.invList.get(i).remove(0);
                soldItemCost = item.getSellPrice();
                Game.player.setGold((float) (Game.player.getGold() + soldItemCost));
                dispSold = true;
                dtBuy = 0;
                if (player.invList.get(i).isEmpty()) {
                    player.invList.remove(i);
                }
            }
        } else if (!minus && i < shopInv.size() && dtBuy > .3
                && Game.player.getGold() >= shopInv.get(i).getCost()) {
            Game.player.setGold(Game.player.getGold() - shopInv.get(i).getCost());
            Game.player.addItemToInventory(shopInv.get(i));
            if (i >= 6) shopInv.remove(i);
            dtBuy = 0;
        }

    }
    public void update(float dt) {
        handleInput();
        player.updateShopState(dt);
        if(drawError){
            dError.update(dt);
            if(dError.isDone()){
                drawError=false;
                dError.reset();
            }
        }
        dtBuy+=dt;
        dtScroll+=dt;
        if(dispSold)
            dtSold+=dt;
        camController.update(dt,cam);
        cam.update();
    }
    public void render(SpriteBatch sb) {
        GlyphLayout gl = new GlyphLayout();
        CharSequence cs;

        sb.setProjectionMatrix(cam.combined);
        shapeR.setProjectionMatrix(cam.combined);
        BitmapFont font = Game.getFont();
        sb.begin();
        Game.setFontSize(5);
        font=Game.getFont();
        font.setColor(Color.RED);
        cs= "--INVALID INPUT--";
        gl.setText(font,cs);
        float f= gl.width/scr.x;
        if(drawError) {
            font.draw(sb, cs, scrx(.5f-(f/2)), scry(.5f));
        }
        sb.end();
        if(!MapState.pause)
        drawPlayerInv(sb);

        sb.begin();
         cs="(TAB) EXIT";
        gl.setText(Game.getFont(),cs);
        Game.getFont().draw(sb,"(TAB) EXIT",view.x+(WIDTH/2)-gl.width/2,viewY+70);
        sb.end();

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(1,0,0,1);
        shapeR.rect(view.x+40,viewY+40, WIDTH-80, HEIGHT-80);
        shapeR.end();
    }
    private void drawPlayerInv(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(1);
        Game.getFont().setColor(Color.WHITE);
        Game.getFont().draw(sb,"EQUIPMENT",view.x +(WIDTH/3)+100,view.y+ HEIGHT-50);
        Game.getFont().draw(sb,"INVENTORY",view.x +(2*WIDTH/3),view.y+ HEIGHT-50);

        Game.getFont().draw(sb,"G "+ Game.player.getGold(),view.x+(3*WIDTH/4),viewY+ HEIGHT-50);
        if(dtSold<.5 && dispSold){
            Game.getFont().draw(sb,"+"+soldItemCost,view.x+ WIDTH-150,viewY+ HEIGHT-50);
        }
        else{
            dtSold=0;
            dispSold=false;
        }
        int yoff=-120;
        //player inventory
        player.renderShopInventory(sb);
        //equipment
        for(int i=0;i<player.equipedList.size();i++){
            Equipment e= player.equipedList.get(i);
            Game.getFont().draw(sb, "# "+e.getName(), view.x +(WIDTH/3)+100, viewY + HEIGHT - (i * 60)+yoff);
            sb.draw(e.getIcon(),view.x +(WIDTH/3)+100, viewY + HEIGHT - (i * 60)+yoff+10);
        }
        //shop inv
        Game.getFont().draw(sb,"SHOP",view.x+ 70,viewY+ HEIGHT-50);
        for(int i=0;i<shopInv.size();i++){
            Item item=shopInv.get(i);
            Game.getFont().draw(sb,(i+1)+". "+item.getName(),view.x+70,viewY+ HEIGHT-(i*60)+yoff);
            Game.getFont().draw(sb,item.getCost()+"G",view.x+(WIDTH/4),viewY+ HEIGHT-(i*60)+yoff-20);
            sb.draw( item.getIcon(),view.x+70, viewY + HEIGHT- (i * 60)+yoff+10);

        }
        sb.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.RED);
        titleLine(new Title("SHOP", 70, HEIGHT-50));
        titleLine(new Title("INVENTORY",(2*WIDTH/3), HEIGHT-50));
        titleLine(new Title("EQUIPMENT",(WIDTH/3)+100, HEIGHT-50));
        shapeR.setColor(Color.GRAY);
        shapeR.line(view.x+(2*WIDTH/3)-20,viewY+100,view.x+(2*WIDTH/3)-20,viewY+HEIGHT-100);
        shapeR.line(view.x+(WIDTH/3)+100-20,viewY+100,view.x+(WIDTH/3)+100-20,viewY+HEIGHT-100);

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
        shapeR.line(view.x + x + s[0], viewY +y+ s[1] , view.x + x + s[2], viewY + y+ s[3]);
        shapeR.line(view.x + x + s[4], viewY +y+ s[5] , view.x + x + s[6], viewY + y+ s[7]);
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
