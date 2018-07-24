package com.quadx.dungeons.abilities;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.items.equipment.EquipSets.equipSets;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quick extends Ability {
    public Quick(){
        name="Quick";
        gINIT(2,"icQuick");
    }

    @Override

    public  void onActivate() {
        player.setAbilityMod(3);
        hoverName();
        l1();
    }

    @Override
    public int getMod() {
        return 3;
    }

    @Override
    public void l1() {
        level=1;
        apply();
    }

    @Override
    public void l2() {
        apply();
        player.pickupItem(equipSets.ref[3].get(0));
    }

    @Override
    public void l3() {
        apply();
        player.pickupItem(equipSets.ref[3].get(1));
    }

    @Override
    public void l4() {
        apply();
        player.pickupItem(equipSets.ref[3].get(2));
    }

    @Override
    public void l5() {
        apply();
        player.pickupItem(equipSets.ref[3].get(3));
        player.maxSec=3;
    }



    @Override
    public String getName() {
        return this.name;
    }
}
