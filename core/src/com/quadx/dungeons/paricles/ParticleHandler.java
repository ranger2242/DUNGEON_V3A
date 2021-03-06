package com.quadx.dungeons.paricles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.files.FilePaths;

import java.util.ArrayList;

import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ParticleHandler {
    public static ArrayList<ParticleEffectPool.PooledEffect> itemEffects = new ArrayList<>();
    ParticleEffectPool itemEffectPool;
    ParticleEffectPool fireEffectPool;

    ArrayList<Integer> rmQueue = new ArrayList<>();
    ParticleEffect itemEffect=  new ParticleEffect();
    public ParticleHandler(){
        TextureAtlas part= new TextureAtlas(Gdx.files.internal("particles/itemParticlePack.atlas"));
        itemEffect.load(Gdx.files.internal(FilePaths.getPath("particles/ptItem")),part);
        ParticleEffect e = new ParticleEffect();
        e.load(Gdx.files.internal("particles/ptFlame"),Gdx.files.internal("particles"));
        itemEffectPool = new ParticleEffectPool(itemEffect,0,10);
        fireEffectPool = new ParticleEffectPool(e,0,10);

    }

    public void update(float dt) {

    }

    public void render(SpriteBatch sb) {
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = itemEffects.get(i);
            effect.draw(sb, Gdx.graphics.getDeltaTime());
            ParticleEmitter em = effect.getEmitters().get(0);
            if(effect.isComplete()) {
                out("Remove: " + em.getX() + " " + em.getY());
                effect.free();
                effect.dispose();
                itemEffects.remove(effect);
            }
        }
    }

    public enum EffectType {
        ATTACK, FIELD
    }



    public static ParticleEffect loadParticles(String fname, float x, float y, EffectType type, TextureAtlas texture) {
/*        //type 1 for attacks
        //type 2 for field itemEffects
        ParticleEffect effect;
        effect = new ParticleEffect();
        ParticleEmitter emitter;
        effect.load(Gdx.files.internal(FilePaths.getPath("particles\\" + fname)), Gdx.files.internal("particles"));

        emitter = effect.getEmitters().first();
        ParticleEmitter.ScaledNumericValue spawnWidth = emitter.getSpawnWidth();
        if (type == EffectType.ATTACK) {
            ParticleEmitter.ScaledNumericValue spawnHeight = emitter.getSpawnHeight();

            ParticleEmitter.ScaledNumericValue angle = emitter.getAngle();
            int ang = 0;
            int sw = 0;
            int sh = 0;
            Attack a = player.attackList.get(Attack.pos);
            Vector2 v = a.getSpawnBox();
            switch (player.body.getFacing()) {

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
            //swh=sw;
            spawnHeight.setHigh(sh);
            spawnHeight.setLow(sh);
            spawnWidth.setHigh(sw);
            spawnWidth.setLow(sw);
            angle.setHigh(ang);
            angle.setLow(ang);
        }
        float r = cellW * (2f / 3f);
        effect.getEmitters().get(0).setPosition(x + (cellW / 2), GridManager.fixY(new Vector2(x, y)) + r);
        effect.setPosition(x + (cellW / 2), GridManager.fixY(new Vector2(x, y)) + r);

        return effect;
        */
        return new ParticleEffect();
    }

    public int addEffect(Vector2 p, Color c) {
        ParticleEffectPool.PooledEffect effect = itemEffectPool.obtain();
        Vector2 a=new Vector2(p);
        effect.setPosition(a.x,a.y);
        float[] f= effect.getEmitters().get(0).getTint().getColors();
        f[0]=c.r;
        f[1]=c.g;
        f[2]=c.b;
        out("EFFECT ADDED:"+p.toString());
        effect.start();
        itemEffects.add(effect);
        return itemEffects.size();
    }
    public int addFireEffect(Vector2 p) {
        ParticleEffectPool.PooledEffect er= fireEffectPool.obtain();
        er.setPosition(p.x,p.y);
        out("EFFECT ADDED:"+p.toString());
        er.start();
        itemEffects.add(er);
        return itemEffects.size();
    }

    public void remove(int i){
        itemEffects.get(i).allowCompletion();
    }

    public static void removeParticles(){
        // Reset all itemEffects:
        for (int i = itemEffects.size() - 1; i >= 0; i--)
            itemEffects.get(i).free(); //free all the itemEffects back to the pool
        itemEffects.clear(); //clear the current itemEffects array

    }
}
