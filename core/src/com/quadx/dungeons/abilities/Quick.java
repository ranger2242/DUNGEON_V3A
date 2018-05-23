package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.attacks.Drain;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quick extends Ability {
    public Quick(){
        name="Quick";
        icon= ImageLoader.abilities.get(4);
    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public  void onActivate() {
        player.setAbilityMod(3);
        new HoverText("QUICK!",1.5f, Color.WHITE, Game.player.getAbsPos().x,Game.player.getAbsPos().y,false);
        l1();
    }

    @Override
    public int getMod() {
        return 3;
    }

    @Override
    public void l1() {
        level=1;
        Game.player.setxMoveSpeed(.3);
        Game.player.setxSpeed(2);
        //Game.player.setxEnergyRegen(1.5);
        Game.player.setxManaRegen(1.5);
        Game.player.setxDefense(.75);
        Game.player.setxIntel(.75);
        player.attackList.add(new Drain());
    }

    @Override
    public void l2() {
        player.setxMoveSpeed(.8);
        player.setxSpeed(1.3);
        player.setxManaRegen(1.1);
        player.pickupItem(equipSets.ref[3].get(0));

    }

    @Override
    public void l3() {
        player.setxMoveSpeed(.7);
        player.setxSpeed(1.5);
        player.setxHpRegen(1.2);
        //player.setxEnergyRegen(1.2);
        player.pickupItem(equipSets.ref[3].get(3));
    }

    @Override
    public void l4() {
        player.setxHpRegen(1.4);
        player.setxManaRegen(1.3);
        player.setxMoveSpeed(.5);
        player.setxSpeed(1.6);
        player.pickupItem(equipSets.ref[3].get(2));
    }

    @Override
    public void l5() {
        player.setxSpeed(2);
        player.setxMoveSpeed(.4);
        player.setxHpMax(1.6);
        player.setxManaRegen(1.4);
        //player.setxEnergyRegen(1.4);
        player.pickupItem(equipSets.ref[3].get(1));
        player.maxSec=3;
    }



    @Override
    public String getName() {
        return this.name;
    }
}
