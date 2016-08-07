package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.MyTextInputListener;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.MainMenuState.controller;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AbilitySelectState extends State implements ControllerListener {
    private int posx=0;
    private int posy=0;
    private float dtSel=0;
    private static final GlyphLayout gl=new GlyphLayout();
    private final ArrayList<Ability> abilityList= new ArrayList<>();
    private final ArrayList<Ability> secondaryList= new ArrayList<>();
    private final int titlex;
    private final int titley;
    private Ability hovering=null;
    public static boolean pressed=false;
    private static float dtMove=0;

    public AbilitySelectState(GameStateManager gsm){
        super(gsm);
        if(Game.controllerMode)
        controller.addListener(this);
        if(!MapState.inGame) {
            MyTextInputListener listener = new MyTextInputListener();
            Gdx.input.getTextInput(listener, "Name", "", "");
        }
        Game.setFontSize(5);

        Gdx.gl.glClearColor(0,0,0,1);
        Game.setFontSize(4);
        CharSequence cs="Select Ability";
        gl.setText(Game.getFont(),cs);

        titlex=(int)((Game.WIDTH/2)-(gl.width/2));
        titley=(Game.HEIGHT-50);
        Tank tank = new Tank();
        Investor inv = new Investor();
        Mage mage = new Mage();
        Quick quick = new Quick();
        //Brawler brawler = new Brawler();
        //DigPlus dplus=new DigPlus();
        //Warp warp = new Warp();
        WaterBreath wb= new WaterBreath();
        abilityList.add(tank);
        abilityList.add(inv);
        abilityList.add(mage);
        abilityList.add(quick);
        //abilityList.add(brawler);
        secondaryList.add(wb);
    }

    @Override
    protected void handleInput() {
        //controller functions
        if(Game.controllerMode){
            if(controller.getButton(Xbox360Pad.BUTTON_START)){
                selectAbiltiy();
            }
            if(controller.getButton(Xbox360Pad.BUTTON_B)){
                exitScreen();
            }
        }
        //keyboard functions
        if(dtMove>.1) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                posy--;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                posx--;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                posy++;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                posx++;
            }
            dtMove=0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            selectAbiltiy();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.TAB)){
            exitScreen();
        }
        if(pressed){
            pressed=false;
            gsm.push(new MapState(gsm));}
    }
    private void selectAbiltiy(){
        if(dtSel >.7f) {
            if(MapState.inGame){
                boolean found=false;
                if(player.getAbility().getClass().equals(hovering)){
                    player.getAbility().upgrade();
                    found=true;
                }
                for(Ability a:player.secondaryAbilityList) {
                    if (a.getClass().equals(hovering)) {
                        a.upgrade();
                        found=true;
                    }
                }
                if(!found){
                    if(player.secondaryAbilityList.size()<player.maxSec){
                        player.secondaryAbilityList.add(hovering);
                    }
                }
            }else {
                pressed = true;
                player.setAbility(hovering);
            }
            dtSel = 0;
        }
    }
    private void exitScreen(){
        gsm.pop();
        cam.setToOrtho(false);
    }
    @Override
    public void update(float dt) {
        handleInput();
        dtMove+=dt;
        dtSel+=dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        Game.setFontSize(4);
        Game.font.setColor(Color.WHITE);
        if(!MapState.inGame) {
            Game.getFont().draw(sb, "~~Select Ability~~", viewX + titlex, viewY + titley);
            for(int i = 0; i<abilityList.size(); i++){
                sb.draw(ImageLoader.abilities.get(i),viewX+ i*150+Game.WIDTH/2,viewY+ Game.HEIGHT*2/3);
            }
        }else{
            Game.getFont().draw(sb, "~~Upgrade Ability~~", viewX + titlex, viewY + titley);
            sb.draw(player.getAbility().getIcon(), viewX + Game.WIDTH / 2, viewY + (Game.HEIGHT * 2 / 3));
            for (int i = 0; i < secondaryList.size(); i++) {
                sb.draw(ImageLoader.abilities2.get(i), viewX + i * 150 + Game.WIDTH / 2, viewY + (Game.HEIGHT * 2 / 3) - 100);
            }
        }
        Game.setFontSize(2);

        Game.getFont().draw(sb,"-PRIMARY-",viewX+titlex+100,viewY+HEIGHT-120);
        Game.getFont().draw(sb,"-SECONDARY-",viewX+titlex+100,viewY+HEIGHT-270);
        CharSequence cs="Enter:Select        Tab:Exit";
        gl.setText(Game.getFont(),cs);

        Game.getFont().draw(sb,"Enter:Select        Tab:Exit",viewX+(WIDTH/2)-(gl.width/2),viewY+30);

        if(posx<0)posx=0;
        if(posy<0)posy=0;
        if(posy>4)posy=4;
        if(posx<ImageLoader.abilities.size()) {
            Game.getFont().draw(sb, "-", viewX + posx * 150 + Game.WIDTH / 2, viewY + Game.HEIGHT * 2 / 3 - (posy * 100));
        }
        else posx=ImageLoader.abilities.size()-1;
        sb.end();
        drawInfo(sb);
        //draw player stats
        sb.begin();
        Game.setFontSize(1);
        Game.font.setColor(Color.WHITE);
        ArrayList<String> stats = player.getStatsList();
        for(int i=0;i<stats.size();i++){
            Game.font.draw(sb, stats.get(i),viewX+30,viewY+HEIGHT-30-(20*i));
        }
        sb.end();
    }
    private void drawInfo(SpriteBatch sb){
        ArrayList<Ability> temp=new ArrayList<>();
        if(posy==0){
            if(MapState.inGame){
                temp.add(player.getAbility());
            }else{
                temp=abilityList;}
        }
        if(posy==1){
            temp=secondaryList;
        }
        sb.begin();
        if(posx<temp.size()) {
            for (int i = 0; i < temp.get(posx).details().size(); i++) {
                Game.getFont().draw(sb, temp.get(posx).details().get(i), viewX + 30, viewY -100+ Game.HEIGHT * 2 / 3 - (i * 20));
                hovering = temp.get(posx);
            }
        }
        sb.end();
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
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (value == Xbox360Pad.BUTTON_DPAD_UP) {
            if(posy>=0)
            posy--;
        }if (value == Xbox360Pad.BUTTON_DPAD_DOWN) {
            if(posy<2)
            posy++;
        }if (value == Xbox360Pad.BUTTON_DPAD_LEFT) {
            if(posx>=0)
                posx--;
        }if (value == Xbox360Pad.BUTTON_DPAD_RIGHT) {
            if(posy<5)
                posx++;
        }
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
