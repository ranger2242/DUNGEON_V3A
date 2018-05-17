package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.ShopState;
import com.quadx.dungeons.tools.FilePaths;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.GridManager.fixHeight;

/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MapStateExt extends MapState {
    public static ArrayList<ParticleEffect> effects = new ArrayList<>();

    public enum EffectType {
        ATTACK, FIELD
    }

    public static ParticleEffect loadParticles(String fname, float x, float y, Color c, EffectType type) {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("particles\\itemParticlePack.atlas"));
        ParticleEffect e = loadParticles(fname, x, y, type,atlas);
        e.getEmitters().first().getTint().setColors(new float[]{c.r, c.g, c.b});
        return e;
    }

    public static ParticleEffect loadParticles(String name, Vector2 pos) {
        ParticleEffect effect = new ParticleEffect();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("particles\\particlesPack.atlas"));
        effect.load(Gdx.files.internal("particles\\" + name), atlas);
        float y = fixHeight(pos);
        effect.setPosition(pos.x, y);
        effect.getEmitters().get(0).setPosition(pos.x, y);
        return effect;

    }

    public static ParticleEffect loadParticles(String fname, float x, float y, EffectType type, TextureAtlas texture) {
        //type 1 for attacks
        //type 2 for field effects
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
            //swh=sw;
            spawnHeight.setHigh(sh);
            spawnHeight.setLow(sh);
            spawnWidth.setHigh(sw);
            spawnWidth.setLow(sw);
            angle.setHigh(ang);
            angle.setLow(ang);
        }
        float r = cellW * (2f / 3f);
        effect.getEmitters().get(0).setPosition(x + (cellW / 2), GridManager.fixHeight(new Vector2(x, y)) + r);
        effect.setPosition(x + (cellW / 2), GridManager.fixHeight(new Vector2(x, y)) + r);

        return effect;
    }

    public MapStateExt(GameStateManager gsm) {
        super(gsm);
    }

    public static void addEffect(ParticleEffect e) {
        e.start();
        effects.add(e);
    }

    public static void warpToNext(boolean abilityState) {
        resetRoomVars();
        if (player.hasAP() && abilityState) {
            gsm.push(new AbilitySelectState(gsm));
        }
        player.floor++;
        gm.initializeGrid();
    }

    public static void warpToShop() {
        dispArray[(int) shop.x][(int) shop.y].setShop(false);
        gsm.push(new ShopState(gsm));
    }

    static void resetRoomVars(){
        removeParticles();

    }

    static void removeParticles(){
        for(ParticleEffect e : effects){
            e.dispose();
        }
        effects.clear();
    }
}
