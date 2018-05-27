package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.attacks.Stab;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Tank extends Ability {
    public Tank() {
        name = "Tank";
        icon = ImageLoader.abilities.get(0);
    }

    @Override
    public Texture getIcon() {
        return icon;
    }

    @Override

    public void onActivate() {
        player.setAbilityMod(0);
        l1();
        hoverName();
    }

    @Override
    public int getMod() {
        return 0;
    }


    @Override
    public void l1() {
        level = 1;
        player.attackList.add(new Stab());
        apply();
    }

    @Override
    public void l2() {
        apply();
        player.pickupItem(equipSets.ref[0].get(0));
    }

    @Override
    public void l3() {
        apply();
        player.pickupItem(equipSets.ref[0].get(1));
    }

    @Override
    public void l4() {
        apply();
        player.pickupItem(equipSets.ref[0].get(2));
    }

    @Override
    public void l5() {
        apply();
        player.maxSec = 3;
        player.pickupItem(equipSets.ref[0].get(3));

    }

    @Override
    public String getName() {
        return name;
    }
}
