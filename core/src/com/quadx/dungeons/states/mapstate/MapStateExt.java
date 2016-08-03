package com.quadx.dungeons.states.mapstate;

import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.AttackMod;
import com.quadx.dungeons.states.GameStateManager;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 1/29/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
class MapStateExt extends MapState{
    public MapStateExt(GameStateManager gsm) {
        super(gsm);
    }
    public static void battleFunctions(int i) {
        if (i < player.attackList.size()) {
            boolean condition = false;
            Attack a;
            if (i == altNumPressed) a = player.attackList.get(altNumPressed);
            else a = player.attackList.get(lastNumPressed);
            switch (a.getType()) {
                case 1: {
                    condition = player.getEnergy() >= a.getCost();
                    break;
                }
                case 2:
                case 3:
                case 4:{
                    condition = player.getMana() >= a.getCost();
                    break;
                }
            }
            if (condition) {
                switch (a.getType()) {
                    case 1: {
                        player.setEnergy(player.getEnergy() - a.getCost());
                        break;
                    }
                    case 2:
                    case 3:
                    case 4:{
                        player.setMana(player.getMana() - a.getCost());
                        break;
                    }
                }
                MapState.attackCollisionHandler(i);
                AttackMod.runMod(a);
                player.attackList.get(player.attackList.indexOf(a)).checkLvlUp();

            }
        }
    }


}
