package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.states.GameStateManager;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateExt extends MapState{
    public static ArrayList<ParticleEffect> effects = new ArrayList<>();
    public static ParticleEffect loadParticles(String fname, float x, float y, Color c, int type) {
        ParticleEffect e=loadParticles(fname,x,y,type);
        e.getEmitters().first().getTint().setColors(new float[]{c.r,c.g,c.b});
        return e;
    }


        public static ParticleEffect loadParticles(String fname, float x, float y,int type){
        //type 1 for attacks
        //type 2 for field effects
        ParticleEffect effect;
        effect = new ParticleEffect();
        ParticleEmitter emitter;
       // String s = "fla";
        effect.load(Gdx.files.internal("particles\\"+fname), Gdx.files.internal("particles"));
        effect.getEmitters().get(0).setPosition(x,y);
        effect.setPosition(x,y);
        emitter=effect.getEmitters().first();
        if (type == 1) {

            ParticleEmitter.ScaledNumericValue spawnHeight = emitter.getSpawnHeight();
            ParticleEmitter.ScaledNumericValue spawnWidth = emitter.getSpawnWidth();

            ParticleEmitter.ScaledNumericValue angle = emitter.getAngle();
            int ang = 0;
            int sw = 0;
            int sh = 0;
            Attack a = player.attackList.get(MapState.lastNumPressed);
            Vector2 v = a.getSpawnBox();
            switch (player.facing) {

                case North:
                    ang = 90;
                    sw = (int) v.x;
                    sh = (int) v.y;
                    break;
                case Northwest:
                    ang = 135;
                    break;
                case West:
                    ang = 180;
                    sw = (int) v.y;
                    sh = (int) v.x;
                    break;
                case Southwest:
                    ang = 225;
                    break;
                case South:
                    ang = 270;
                    sw = (int) v.x;
                    sh = (int) v.y;
                    break;
                case Southeast:
                    ang = 315;
                    break;
                case East:
                    ang = 0;
                    sw = (int) v.y;
                    sh = (int) v.x;
                    break;
                case Northeast:
                    ang = 45;
                    break;
            }
            spawnHeight.setHigh(sh);
            spawnHeight.setLow(sh);
            spawnWidth.setHigh(sw);
            spawnWidth.setLow(sw);
            angle.setHigh(ang);
            angle.setLow(ang);
        }
        return effect;
    }
    public MapStateExt(GameStateManager gsm) {
        super(gsm);
    }
    public static void battleFunctions(int i) {
        if (i < player.attackList.size()) {
            boolean condition = false;
            Attack a;
            if (i == altNumPressed) a = player.attackList.get(altNumPressed);
            else a = player.attackList.get(lastNumPressed);
            switch (a.getType()) {
                case 1: {
                    condition = player.getEnergy() >= a.getCost();
                    break;
                }
                case 2:
                case 3:
                case 4:{
                    condition = player.getMana() >= a.getCost();
                    break;
                }
            }
            if (condition) {
                switch (a.getType()) {
                    case 1: {
                        player.setEnergy(player.getEnergy() - a.getCost());
                        break;
                    }
                    case 2:
                    case 3:
                    case 4:{
                        player.setMana(player.getMana() - a.getCost());
                        break;
                    }
                }
                addEffect(loadParticles("ptfla",player.getAbsPos().x,player.getAbsPos().y,1));

                MapState.attackCollisionHandler2(i);
                AttackMod.runMod(a);
                player.attackList.get(player.attackList.indexOf(a)).checkLvlUp();

            }
        }
    }
    public static void addEffect(ParticleEffect e){
        e.start();
        effects.add(e);
    }

}
