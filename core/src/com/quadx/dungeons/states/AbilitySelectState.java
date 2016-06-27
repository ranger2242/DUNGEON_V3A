package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.AbilityMod;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;

import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AbilitySelectState extends State implements ControllerListener {
    int posx=0;
    int posy=0;
    float dtSel=0;
    private static GlyphLayout gl=new GlyphLayout();
    private ArrayList<Ability> abilityList= new ArrayList<>();
    private ArrayList<Ability> secondaryList= new ArrayList<>();
    private static ArrayList<Texture> abilityIconList = new ArrayList<>();
    private static ArrayList<Texture> secondaryIconList = new ArrayList<>();
    public static ShapeRenderer shapeR = new ShapeRenderer();
    private int titlex;
    private int titley;
    Ability hovering=null;
    public static boolean pressed=false;
    public static float dtMove=0;

    public AbilitySelectState(GameStateManager gsm){
        super(gsm);
        if(Game.controllerMode)
        MainMenuState.controller.addListener(this);
        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Name", "","");
        Game.setFontSize(5);

        Gdx.gl.glClearColor(0,0,0,1);
        CharSequence cs="Select Ability";
        Game.setFontSize(1);
        gl.setText(Game.getFont(),cs);

        titlex=(int)((Game.WIDTH/2)-(gl.width/2));
        titley=(int)(Game.HEIGHT-100);
        Tank tank = new Tank();
        abilityList.add(tank);
        Investor inv = new Investor();
        abilityList.add(inv);
        Mage mage = new Mage();
        abilityList.add(mage);
        Quick quick = new Quick();
        abilityList.add(quick);
        Brawler brawler = new Brawler();
        abilityList.add(brawler);
        DigPlus dplus=new DigPlus();
        secondaryList.add(dplus);
        Warp warp = new Warp();
        secondaryList.add(warp);
        try {
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icTank.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icInvestor.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icMage.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icQuick.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icBrawler.png")));
          /*  abilityIconList.add(tank.getIcon());
            abilityIconList.add(inv.getIcon());
            abilityIconList.add(mage.getIcon());
            abilityIconList.add(quick.getIcon());
            abilityIconList.add(brawler.getIcon());
*/
            //secondaryIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icDigPlus.png")));
            //secondaryIconList.add(warp.getIcon());
        }catch (GdxRuntimeException e){

        }


    }

    @Override
    protected void handleInput() {
        //controller functions
        if(Game.controllerMode){
            if(MainMenuState.controller.getButton(Xbox360Pad.BUTTON_START)){
                selectAbiltiy();
            }
            if(MainMenuState.controller.getButton(Xbox360Pad.BUTTON_B)){
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
        if(pressed){gsm.push(new MapState(gsm));}
    }
    void selectAbiltiy(){
        if(dtSel >.7f) {
            pressed = true;
            AbilityMod.enableAbility(hovering.getMod());
            dtSel = 0;
        }
    }
    void exitScreen(){
        gsm.pop();
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
        Game.setFontSize(2);
        Game.font.setColor(Color.WHITE);
        Game.getFont().draw(sb,"Select Ability",viewX+ titlex,viewY+titley);
        Game.getFont().draw(sb,"-PRIMARY-",viewX+titlex+100,viewY+titley-20);
        Game.getFont().draw(sb,"-SECONDARY-",viewX+titlex+100,viewY+titley-170);
        int a2=0;

        for(Ability a :abilityList) {
            for (int i = 0; i < a.details().size(); i++) {
                try {
                    int x1=(int)(viewX + 30+(a2*10));
                    int y1=(int) (viewY +Game.HEIGHT/2- 100 - (i * 20));

                   // Game.getFont().draw(sb, a.details().get(i), x1,y1);
                } catch (IndexOutOfBoundsException e) {
                }
            }
            a2++;
        }
        for(int i=0;i<abilityIconList.size();i++){
            sb.draw(abilityIconList.get(i),viewX+ i*150+Game.WIDTH/2,viewY+ Game.HEIGHT*2/3);
        }
        for(int i=0;i<secondaryIconList.size();i++){
            sb.draw(secondaryIconList.get(i), viewX+i*150+Game.WIDTH/2,viewY+Game.HEIGHT*2/3-100);
        }

        if(posx<0)posx=0;
        if(posy<0)posy=0;
        if(posy>4)posy=4;
        if(posx<abilityIconList.size()) {
            Game.getFont().draw(sb, "-", viewX + posx * 150 + Game.WIDTH / 2, viewY + Game.HEIGHT * 2 / 3 - (posy * 100));
        }
        else posx=abilityIconList.size()-1;
        sb.end();
        drawInfo(sb);
    }
    public void drawInfo(SpriteBatch sb){
        ArrayList<Ability> temp=null;
        switch (posy){
            case 0:{
                temp=abilityList;
                break;
            }case 1:{
                temp=secondaryList;
            }
        }
        if(posy==0){
            temp=abilityList;
        }
        sb.begin();
        try {
            for (int i = 0; i < temp.get(posx).details().size(); i++) {
                Game.getFont().draw(sb, temp.get(posx).details().get(i), viewX + 30, viewY + Game.HEIGHT * 2 / 3 - (i * 20));
                hovering=temp.get(posx);
            }
        }catch (IndexOutOfBoundsException |NullPointerException e){}
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
