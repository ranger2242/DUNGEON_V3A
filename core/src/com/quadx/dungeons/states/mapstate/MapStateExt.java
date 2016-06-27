package com.quadx.dungeons.states.mapstate;

import com.quadx.dungeons.QButton;
import com.quadx.dungeons.attacks.SpellMods;
import com.quadx.dungeons.attacks.Attack;
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

    public static void mouseOverHandler(){
        for(int i=0;i<qButtonList.size();i++) {
            QButton button = qButtonList.get(i);
            if (mouseRealitiveX >= button.getPx() && mouseRealitiveX < (button.getPx() + button.getWidth())
                    && mouseRealitiveY >= button.getPy() && mouseRealitiveY <= (button.getPy() + button.getHeight())) {
                popupItem = player.invList.get(i).get(0);
                qButtonBeingHovered = i;
                hovering = true;
            }
        }
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
                case 3: {
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
                    case 3: {
                        player.setMana(player.getMana() - a.getCost());
                        break;
                    }
                }
                MapState.attackCollisionHandler(i,a);
                SpellMods.runMod(targetMon,a);
                player.attackList.get(player.attackList.indexOf(a)).checkLvlUp();

            }
        }
    }


}
