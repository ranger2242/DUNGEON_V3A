package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.Shop;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.gui.Title;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Time;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.tools.gui.HUD.titleLine;

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
    private static final ShapeRendererExt shapeR=new ShapeRendererExt();
    private Delta dError = new Delta(72* Time.ft);
    Shop shop;


    public ShopState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, scr.x, scr.y);
        setView(new Vector2());
        cam.position.set(view.x+ scr.x/2,viewY+ scr.y/2,0);
        if(!MapState.pause)
        shop= new Shop(player);
        Gdx.gl.glClearColor(0,0,0,1);
    }
    public static void exit(){
        MapState.pause=false;
        gsm.pop();
    }
    void handleInput() {
        for(Command c: Command.commands){
            c.execute();
        }
        boolean p=false;
        //keyboard functions--------------------------------------------------------
        for(int i=0;i<9;i++){
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_1+i)){
                numberButtonHandler(i);
                p=true;
            }

        }
        if(!p&&Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && !(Gdx.input.isKeyPressed(Input.Keys.MINUS)
                    ||Gdx.input.isKeyPressed(Command.commands.get(0).getButtonK())
                ||Gdx.input.isKeyPressed(Command.commands.get(1).getButtonK()))) {
            camController.shakeScreen(5, 25);
            drawError=true;
        }
    }
    private void numberButtonHandler(int i){
        boolean minus = Gdx.input.isKeyPressed(Input.Keys.MINUS);
        if(dtBuy > .3 &&!MapState.pause) {
            if (minus) {
                soldItemCost = shop.sellItem(i);
                dispSold = true;

            } else {
                shop.buyItem(i);

            }
            dtBuy = 0;
        }


    }
    public void update(float dt) {
        handleInput();
        player.updateShopState(dt);
        drawError= dError.triggerUpdate(dt,drawError);
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
        BitmapFont font;
        sb.begin();
        Text.setFontSize(5);
        font= Text.getFont();
        font.setColor(Color.RED);
        cs= "--INVALID INPUT--";
        gl.setText(font,cs);
        float f= gl.width/scr.x;
        if(drawError) {
            font.draw(sb, cs, scrVx(.5f-(f/2)), scrVy(.5f));
        }
        sb.end();
        if(!MapState.pause)
        drawPlayerInv(sb);

        sb.begin();
         cs="(TAB) EXIT";
        gl.setText(Text.getFont(),cs);
        Text.getFont().draw(sb,"(TAB) EXIT",view.x+(scr.x/2)-gl.width/2,viewY+70);
        sb.end();

        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(1,0,0,1);
        shapeR.rect(view.x+40,viewY+40, scr.x-80, scr.y-80);
        shapeR.end();
    }
    private void drawPlayerInv(SpriteBatch sb){
        sb.begin();
        Text.setFontSize(1);
        Text.getFont().setColor(Color.WHITE);
        Text.getFont().draw(sb,"EQUIPMENT",view.x +(scr.x/3)+100,view.y+ scr.y-50);
        Text.getFont().draw(sb,"INVENTORY",view.x +(2*scr.x/3),view.y+ scr.y-50);

        Text.getFont().draw(sb,"G "+ player.getGold(),view.x+(3*scr.x/4),viewY+ scr.y-50);
        if(dtSold<.5 && dispSold){
            Text.getFont().draw(sb,"+"+soldItemCost,view.x+ scr.x-150,viewY+ scr.y-50);
        }
        else{
            dtSold=0;
            dispSold=false;
        }
        int yoff=-120;
        //player inventory
        player.renderShopInventory(sb);
        //equipment
        for(int i=0;i<player.inv.getEquiped().size();i++){
            Equipment e= player.inv.getEquiped().get(i);
            Text.getFont().draw(sb, "# "+e.getName(), view.x +(scr.x/3)+100, viewY + scr.y - (i * 60)+yoff);
            sb.draw(e.getIcon(),view.x +(scr.x/3)+100, viewY + scr.y - (i * 60)+yoff+10);
        }
        //shop inv
        Text.getFont().draw(sb,"SHOP",view.x+ 70,viewY+ scr.y-50);
        for(int i=0;i<shop.invSize();i++){
            Item item=shop.getItem(i);
            Text.getFont().draw(sb,(i+1)+". "+item.getName(),view.x+70,viewY+ scr.y-(i*60)+yoff);
            Text.getFont().draw(sb,item.getCost()+"G",view.x+(scr.x/4),viewY+ scr.y-(i*60)+yoff-20);
            sb.draw( item.getIcon(),view.x+70, viewY + scr.y- (i * 60)+yoff+10);

        }
        sb.end();
        shapeR.begin(ShapeRenderer.ShapeType.Line);
        shapeR.setColor(Color.RED);
        titleLine(shapeR, new Title("SHOP", 70, scr.y-50));
        titleLine(shapeR,new Title("INVENTORY",(2*scr.x/3), scr.y-50));
        titleLine(shapeR,new Title("EQUIPMENT",(scr.x/3)+100, scr.y-50));
        shapeR.setColor(Color.GRAY);
        shapeR.line(view.x+(2*scr.x/3)-20,viewY+100,view.x+(2*scr.x/3)-20,viewY+scr.y-100);
        shapeR.line(view.x+(scr.x/3)+100-20,viewY+100,view.x+(scr.x/3)+100-20,viewY+scr.y-100);

        shapeR.end();
    }

    //---------------------------------------------------------
// Render

    public void dispose() {

    }
}
