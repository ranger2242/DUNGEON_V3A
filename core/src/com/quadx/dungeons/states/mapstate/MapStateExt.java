package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.QButton;
import com.quadx.dungeons.SpellMods;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.GameStateManager;

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
                popupItem = Game.player.invList.get(i).get(0);
                qButtonBeingHovered = i;
                hovering = true;
            }
        }
    }
    public static void useItem(int i){
        String s="";
        try {
            try {
                item = Game.player.invList.get(i).get(0);
                Game.player.invList.get(i).remove(0);
            } catch (IndexOutOfBoundsException e) {
                MapStateRender.setHoverText("ohfuck", 1, Color.RED, Game.player.getPX(), Game.player.getPY(), false);
                Game.printLOG(e);
            }

            if (item.getHpmod() != 0) {
                Game.player.setHp(Game.player.getHp() + item.getHpmod());
                s = Game.player.getName() + "'s HP changed by " + item.getHpmod();
            }
            //Mana
            if (item.getManamod() != 0) {
                Game.player.setMana(Game.player.getMana() + item.getManamod());
                s = Game.player.getName() + "'s M changed by " + item.getManamod();
            }
            //attack
            if (item.getAttackmod() != 0) {
                Game.player.setAttack(Game.player.getAttack() + item.getAttackmod());
                s = Game.player.getName() + "'s ATT changed by " + item.getAttackmod();
            }
            //defense
            if (item.getDefensemod() != 0) {
                Game.player.setDefense(Game.player.getDefense() + item.getDefensemod());
                s = Game.player.getName() + "'s DEF changed by " + item.getDefensemod();
            }
            //intel
            if (item.getIntelmod() != 0) {
                Game.player.setIntel(Game.player.getIntel() + item.getIntelmod());
                s = Game.player.getName() + "'s INT changed by " + item.getIntelmod();
            }
            //speed
            if (item.getSpeedmod() != 0) {
                Game.player.setSpeed(Game.player.getSpeed() + item.getSpeedmod());
                s = Game.player.getName() + "'s SPD changed by " + item.getSpeedmod();
            }

            out(s);
        }catch (NullPointerException e){}
    }
    public static void battleFunctions(int i) {
        if (Game.player.getMana() >=attack.getCost()) {

            Game.player.setMana(Game.player.getMana() - attack.getCost());
            MapStateRender.loadParticleEffects(i);
            MapState.attackCollisionHandler(i);
            SpellMods.runMod(targetMon,attack);
            attack.checkLvlUp();

        }
    }
}
