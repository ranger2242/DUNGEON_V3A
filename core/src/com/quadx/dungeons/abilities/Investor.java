package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
import com.quadx.dungeons.Game;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.items.equipment.EquipSets.equipSets;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Investor extends Ability {
    static float dtGold = 0;

    public Investor() {
        name = "Investor";
        gINIT(2,"icInvestor");

    }

    @Override

    public void onActivate() {
        player.setAbilityMod(1);
        l1();
        hoverName();
    }

    @Override
    public int getMod() {
        return 1;
    }

    @Override
    public void l1() {
        level = 1;
        apply();
    }

    @Override
    public void l2() {
        apply();
        player.pickupItem(equipSets.ref[1].get(0));
    }

    @Override
    public void l3() {
        apply();
        player.pickupItem(equipSets.ref[1].get(1));
    }

    @Override
    public void l4() {
        apply();
        player.pickupItem(equipSets.ref[1].get(2));
    }

    @Override
    public void l5() {
        apply();
        player.pickupItem(equipSets.ref[1].get(3));
        player.maxSec = 3;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static void generatePlayerGold() {
        dtGold += Gdx.graphics.getDeltaTime();
        if (dtGold > 1) {
            Game.player.addGold((int) (Game.player.getGold() * .0001));
            dtGold = 0;
        }
    }

}
