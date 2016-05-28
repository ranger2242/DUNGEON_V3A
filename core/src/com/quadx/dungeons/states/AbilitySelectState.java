package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.AbilityMod;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;

import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class         AbilitySelectState extends State {
    private static GlyphLayout gl=new GlyphLayout();
    private ArrayList<Ability> abilityList= new ArrayList<>();
    private ArrayList<Ability> secondaryList= new ArrayList<>();
    private static ArrayList<Texture> abilityIconList = new ArrayList<>();
    private static ArrayList<Texture> secondaryIconList = new ArrayList<>();
    private int titlex;
    private int titley;
    public static boolean pressed=false;

    public AbilitySelectState(GameStateManager gsm){
        super(gsm);
        Game.setFontSize(28);

        Gdx.gl.glClearColor(0,0,0,1);
        CharSequence cs="Select Ability";
        Game.setFontSize(10);
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
            secondaryIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icDigPlus.png")));
            secondaryIconList.add(warp.getIcon());
        }catch (GdxRuntimeException e){

        }


    }
    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            pressed=true;
            AbilityMod.enableAbility(6);//Tank
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            pressed=true;
            AbilityMod.enableAbility(5);//Investor
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            pressed=true;
            AbilityMod.enableAbility(3);//Mage
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
            pressed=true;
            AbilityMod.enableAbility(4);//Quick
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)){
            pressed=true;
            AbilityMod.enableAbility(2);//Brawler
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            pressed=true;
            AbilityMod.enableAbility(1);//Dig+
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            pressed=true;
            AbilityMod.enableAbility(7);//warp+
        }
        if(pressed){gsm.push(new MapState(gsm));}
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        //shapeR.setProjectionMatrix(cam.combined);
       // sb.setProjectionMatrix(cam.combined);
      //  sb.setProjectionMatrix(cam.combined);

        sb.begin();


        Game.setFontSize(12);
        Game.font.setColor(Color.WHITE);
        Game.getFont().draw(sb,"Select Ability",viewX+ titlex,viewY+titley);
        Game.getFont().draw(sb,"-PRIMARY-",viewX+titlex,viewY+titley-20);
        Game.getFont().draw(sb,"-SECONDARY-",viewX+titlex,viewY+titley-170);
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
            sb.draw(abilityIconList.get(i),viewX+ i*150+Game.WIDTH/4,viewY+ Game.HEIGHT*2/3);
        }
        for(int i=0;i<secondaryIconList.size();i++){
            sb.draw(secondaryIconList.get(i), viewX+i*150+Game.WIDTH/4,viewY+Game.HEIGHT*2/3-100);
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
