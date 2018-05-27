package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.attacks.Flame;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Mage extends Ability {
    public Mage(){
        name="Mage";
        icon=  ImageLoader.abilities.get(3);

    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public  void onActivate() {
        player.setAbilityMod(2);
        l1();
        hoverName();
    }

    @Override
    public int getMod() {
        return 2;
    }

    @Override
    public void l1() {
        player.attackList.add(new Flame());
        level=1;
        apply();
    }

    @Override
    public void l2() {
        apply();
        player.pickupItem(equipSets.ref[2].get(0));
    }

    @Override
    public void l3() {
        apply();
        player.pickupItem(equipSets.ref[2].get(1));
    }

    @Override
    public void l4() {
        apply();
        player.pickupItem(equipSets.ref[2].get(2));
    }

    @Override
    public void l5() {
        apply();
        player.pickupItem(equipSets.ref[2].get(3));
        player.maxSec=3;

    }



    @Override
    public String getName() {
        return "MAGE";
    }
}