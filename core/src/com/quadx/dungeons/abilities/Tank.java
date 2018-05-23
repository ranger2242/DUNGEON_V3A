package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.attacks.Stab;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Tank extends Ability {
    public Tank() {
        name="Tank";
        icon= ImageLoader.abilities.get(0);
    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public void onActivate() {
        player.setAbilityMod(0);
        l1();
        new HoverText("Tank!!",1.5f, Color.WHITE, player.getAbsPos().x,player.getAbsPos().y,false);

    }

    @Override
    public int getMod() {
        return 0;
    }

    @Override
    public void l1() {
        level=1;
        player.attackList.add(new Stab());
        player.setxDefense(2);
        player.setxHpMax(1.5);
        player.setxHpRegen(2);
        player.setxMoveSpeed(1.005);
    }

    @Override
    public void l2() {
        player.setxDefense(1.2);
        player.setxMoveSpeed(.9);
        player.pickupItem(equipSets.ref[0].get(0));
    }

    @Override
    public void l3() {
        player.setxDefense(1.3);
        player.setxAttack(1.1);
        player.setxMoveSpeed(.8);
        player.pickupItem(equipSets.ref[0].get(1));
    }

    @Override
    public void l4() {
        player.setxDefense(1.3);
        player.setxAttack(1.1);
        player.setxMoveSpeed(.8);
        player.pickupItem(equipSets.ref[0].get(2));

    }

    @Override
    public void l5() {
        player.setxDefense(1.4);
        player.setxSpeed(1.3);
        player.setxIntel(1.1);
        player.setxHpRegen(1.5);
        player.setxMoveSpeed(.9);
        player.maxSec=3;
        player.pickupItem(equipSets.ref[0].get(3));

    }




    @Override
    public String getName() {
        return name;
    }
}
