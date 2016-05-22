package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.AbilityMod;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by range on 5/20/2016.
 */
public class         AbilitySelectState extends State {
    public static GlyphLayout gl=new GlyphLayout();
    ArrayList<Ability> abilityList= new ArrayList<>();
    public static ArrayList<Texture> abilityIconList = new ArrayList<>();
    Tank tank=new Tank();
    Investor inv=new Investor();
    Mage mage=new Mage();
    Quick quick=new Quick();
    Brawler brawler=new Brawler();
    int titlex;
    int titley;

    public AbilitySelectState(GameStateManager gsm){
        super(gsm);
        Game.setFontSize(28);

        Gdx.gl.glClearColor(0,0,0,1);
        CharSequence cs="Select Ability";
        Game.setFontSize(10);
        gl.setText(Game.getFont(),cs);
        titlex=(int)(MapState.viewX +(Game.WIDTH/2)-(gl.width/2));
        titley=(int)(MapState.viewY +Game.HEIGHT-100);
        abilityList.add(tank);
        abilityList.add(inv);
        abilityList.add(mage);
        abilityList.add(quick);
        abilityList.add(brawler);
        try {
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icTank.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icInvestor.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icMage.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icQuick.png")));
            abilityIconList.add(new Texture(Gdx.files.internal("images/icons/abilities/icBrawler.png")));
        }catch (GdxRuntimeException e){

        }


    }
    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            gsm.push(new MapState(gsm));
            AbilityMod.enableAbility(6);//Tank
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            gsm.push(new MapState(gsm));
            AbilityMod.enableAbility(5);//Investor

        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            gsm.push(new MapState(gsm));
            AbilityMod.enableAbility(3);//Mage

        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
            gsm.push(new MapState(gsm));

            AbilityMod.enableAbility(4);//Quick
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_5)){
            gsm.push(new MapState(gsm));

            AbilityMod.enableAbility(2);//Brawler
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();


        Game.setFontSize(12);
        Game.font.setColor(Color.WHITE);
        Game.getFont().draw(sb,"Select Ability",titlex,titley);
        int a2=0;

        for(Ability a :abilityList) {
            for (int i = 0; i < a.details().size(); i++) {
                try {
                    int x1=(int)(MapState.viewX + 30+(a2*Game.WIDTH/5));
                    int y1=(int) (MapState.viewY +Game.HEIGHT/2- 100 - (i * 20));

                    Game.getFont().draw(sb, a.details().get(i), x1,y1);
                } catch (IndexOutOfBoundsException e) {
                }
            }
            a2++;
        }
        for(int i=0;i<abilityIconList.size();i++){
            sb.draw(abilityIconList.get(i), i*(Game.WIDTH/5)+60,Game.HEIGHT/2);
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
