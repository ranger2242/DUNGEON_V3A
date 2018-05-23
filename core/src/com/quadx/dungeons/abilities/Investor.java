package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.attacks.Blind;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;


/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Investor extends Ability {
    static float dtGold= 0;
    public Investor(){
        icon=  ImageLoader.abilities.get(1);
        name="Investor";
    }

    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public void onActivate() {
        player.setAbilityMod(1);
        l1();
        new HoverText("INVESTOR!",1.5f, Color.WHITE, player.getAbsPos().x,player.getAbsPos().y,false);
    }

    @Override
    public int getMod() {
        return 1;
    }

    @Override
    public void l1() {
        level=1;
        player.setxHpRegen(2);
        //player.setxEnergyRegen(2);
        player.attackList.add(new Blind());

    }

    @Override
    public void l2() {
            player.setxManaRegen(1.1);
        player.setxMoveSpeed(.85);
        player.setxAttack(1.1);
        player.addItemToInventory(equipSets.ref[1].get(7));

    }

    @Override
    public void l3() {
        player.setxMoveSpeed(.75);
        //.setxEnergyRegen(2);
        player.setxAttack(1.2);
        player.setxIntel(1.2);
        player.addItemToInventory(equipSets.ref[1].get(0));

    }

    @Override
    public void l4() {
        player.setxDefense(1.3);
        player.setxIntel(1.2);
        player.setxAttack(1.25);
        player.setxSpeed(1.2);
        player.addItemToInventory(equipSets.ref[1].get(2));
    }

    @Override
    public void l5() {
        player.setxHpRegen(1.5);
        player.setxManaRegen(1.4);
        player.setxSpeed(1.5);
        player.setxAttack(1.6);
        player.addItemToInventory(equipSets.ref[1].get(3));
        player.maxSec=3;
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
