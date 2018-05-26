package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.abilities.WaterBreath;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.FilePaths;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.MyTextInputListener;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.tools.gui.HUD;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.states.MainMenuState.controller;
import static com.quadx.dungeons.tools.gui.Text.centerString;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AbilitySelectState extends State implements ControllerListener {
    private static int posx=0;
    private static int posy=0;
    private static float dtSel=0;
    private static final GlyphLayout gl=new GlyphLayout();
    private static ArrayList<Ability> abilityList;
    private final ArrayList<Ability> secondaryList= new ArrayList<>();
    private final int titlex;
    private final int titley;
    private static Ability hovering=null;
    public static boolean pressed=false;
    private static float dtMove=0;
    private static boolean firstRun=true;
    ShapeRendererExt sr = new ShapeRendererExt();

    public AbilitySelectState(GameStateManager gsm){
        super(gsm);
        if(Game.controllerMode)
        controller.addListener(this);
        //namePopupBox();

        Game.setFontSize(5);

        Gdx.gl.glClearColor(0,0,0,1);
        Game.setFontSize(4);
        CharSequence cs="Select Ability";
        gl.setText(Game.getFont(),cs);

        titlex=(int)((Game.WIDTH/2)-(gl.width/2));
        titley=(Game.HEIGHT-50);

        WaterBreath wb= new WaterBreath();
        secondaryList.add(wb);
        cam.setToOrtho(false, scr.x, scr.y);

    }

    private void namePopupBox() {
        if(!MapState.inGame && FilePaths.checkOS()==0) {
             MyTextInputListener listener = new MyTextInputListener();
            Gdx.input.getTextInput(listener, "Name", "", "");
        }
    }

    public static void setAbilityList(ArrayList<Ability> list){
        abilityList=new ArrayList<>(list);
    }
    public static void movePointer(int x, int y){
        if(dtMove>.1) {
            posx+=x;
            posy+=y;
            dtMove=0;
        }
    }
    public static void selectAbiltiy(){
        if(firstRun){
            firstRun=false;
        }else {
            if (dtSel > .7f) {
                if (MapState.inGame) {
                    boolean found = false;
                    if (player.getAbility().getClass().equals(hovering.getClass())) {
                        player.getAbility().upgrade();
                        found = true;
                    }
                    for (Ability a : player.secondaryAbilityList) {
                        if (a.getClass().equals(hovering.getClass())) {
                            a.upgrade();
                            found = true;
                        }
                    }
                    if (!found) {
                        if (player.secondaryAbilityList.size() < player.maxSec) {
                            player.secondaryAbilityList.add(hovering);
                            player.secondaryAbilityList.get(player.secondaryAbilityList.size() - 1).onActivate();
                        }
                    }
                } else {
                    pressed = true;
                    player.setAbility(hovering);

                }
                dtSel = 0;


                exit();
            }
        }
    }
    public static void exit(){
        gsm.pop();
        cam.setToOrtho(false);
    }
    @Override
    public void update(float dt) {
        handleInput();
        setView(new Vector2(0,0));
        dtMove+=dt;
        dtSel+=dt;
        if(pressed){
            pressed=false;
            gsm.push(new MapState(gsm));}
    }

//RENDER FUNCTIONS
private void drawIcons(SpriteBatch sb){
        Game.setFontSize(4);
        Game.getFont().setColor(Color.WHITE);
        if(!MapState.inGame) {
            Game.getFont().draw(sb, "~~Select Ability~~", view.x + titlex, viewY + titley);
            for(int i = 0; i<ImageLoader.abilities.size(); i++){
                sb.draw(ImageLoader.abilities.get(i),view.x+ i*150+Game.WIDTH/2,viewY+ Game.HEIGHT*2/3);
            }
        }else{
            Game.getFont().draw(sb, "~~Upgrade Ability~~", view.x + titlex, viewY + titley);
            sb.draw(ImageLoader.abilities.get(player.getAbilityMod()), view.x + Game.WIDTH / 2, viewY + (Game.HEIGHT * 2 / 3));
            for (int i = 0; i < secondaryList.size(); i++) {
                sb.draw(ImageLoader.abilities2.get(i), view.x + i * 150 + Game.WIDTH / 2, viewY + (Game.HEIGHT * 2 / 3) - 100);
            }
        }
    }
    private void drawSelector(SpriteBatch sb){
        if(posx<0)posx=0;
        if(posy<0)posy=0;
        if(posy>4)posy=4;
        if(posx<ImageLoader.abilities.size()) {
            Game.getFont().draw(sb, "-", view.x + posx * 150 + Game.WIDTH / 2, viewY + Game.HEIGHT * 2 / 3 - (posy * 100));
        }
        else posx=ImageLoader.abilities.size()-1;

    }
    private void drawText(SpriteBatch sb){
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
        Game.setFontSize(2);
        Game.getFont().setColor(Color.WHITE);

        Game.getFont().draw(sb,"-PRIMARY-",view.x+titlex+100,viewY+HEIGHT-120);
        Game.getFont().draw(sb,"-SECONDARY-",view.x+titlex+100,viewY+HEIGHT-270);

        Game.getFont().draw(sb,"-UPGRADE AND BUY ABILITIES WITH AP-",view.x+(WIDTH/2)-(gl.width/2),viewY+Game.HEIGHT-30);

        CharSequence cs="Enter:Select        Tab:Exit";
        gl.setText(Game.getFont(),cs);

        Game.getFont().draw(sb,"Enter:Select        Tab:Exit",view.x+(WIDTH/2)-(gl.width/2),viewY+30);
    }

    private void drawAbilityScreen(SpriteBatch sb){
        sb.begin();
        drawIcons(sb);
        drawText(sb);
        drawSelector(sb);
        Vector2 stats= new Vector2(view.x + 30, viewY +HEIGHT- 70);
        player.renderStatList(sb, stats);


        drawAbilityDetails(sb,stats);
        sb.end();

    }

    private void drawAbilityDetails(SpriteBatch sb, Vector2 stats){
        ArrayList<Ability> abilities=new ArrayList<>();
        if(posy==0){
            if(MapState.inGame){
                abilities.add(player.getAbility());
            }else{
                abilities=abilityList;}
        }
        if(posy==1){
            abilities=secondaryList;
        }
        if(posx<abilities.size()) {
            ArrayList<String> details= abilities.get(posx).details();
            Vector2[] pos= HUD.getStatPos();
            for (int i = 0; i < details.size() && i <pos.length; i++) {
                Game.getFont().draw(sb, details.get(i), pos[i].x+240, pos[i].y);
                hovering = abilities.get(posx);
            }
        }
    }

    private void drawInstructions(SpriteBatch sb) {
        sb.begin();
        Game.getFont().draw(sb,"WIN",centerString("WIN"),HEIGHT/2);
        sb.end();
    }
    @Override
    public void render(SpriteBatch sb) {
        if(firstRun)
            drawInstructions(sb);
        else
           drawAbilityScreen(sb);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        Vector2 stats= new Vector2(30,HEIGHT- 70);
        Vector2[] arr= HUD.generateStatListPos(stats);
        for(int i=0;i<7;i++){
            Vector2 v=new Vector2(arr[i+2]);
            v.add(50,-13);
            sr.line(v,new Vector2(v.x+200,v.y));
        }
        Vector2 v=arr[0];
        v.add(230,0);
        sr.line(v,new Vector2(v.x,v.y-250));
        sr.end();
    }




    @Override
    public void dispose() {

    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Xbox360Pad.updateSticks(axisCode,value);
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Xbox360Pad.updatePOV(value);
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
