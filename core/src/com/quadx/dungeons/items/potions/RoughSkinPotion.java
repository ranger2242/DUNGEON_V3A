package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 6/6/2018.
 */
public class RoughSkinPotion extends Potion {
    public RoughSkinPotion(){
        dEffectTime=new Delta(15*SECOND);
        name="Rough Skin Potion";
        fileName="potions\\pDarkGreen.png";
        loadIcon();
        effects.add("DAMAGE ENEMIES ON TOUCH");
    }
    @Override
    void onStart() {
        Game.player.setRoughSkin(true);
    }

    @Override
    void onEnd() {
        Game.player.setRoughSkin(false);
    }

    @Override
    public void onUpdate(float dt) {
    }
}