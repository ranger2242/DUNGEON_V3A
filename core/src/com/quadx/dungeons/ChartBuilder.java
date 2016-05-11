package com.quadx.dungeons;

import com.quadx.dungeons.monsters.Monster;

/**
 * Created by Tom on 11/24/2015.
 */
public class ChartBuilder {
    public static void checkPhysicalDamage(){
        Player player = new Player();
        Monster monster = new Monster();
        Damage d = new Damage();
        System.out.println("P.LVL M.LVL P.DMG M.DMG P.ATT M.ATT P.DEF M.ATT");
        for(int i =0; i<100;i++) {
            for(int j =0; j<100;j++) {
                monster.setLevel(i);
                monster.genHp();
                monster.genAttack();
                monster.genDefense();
                monster.genSpeed();
                monster.genIntel();
                d.playerPhysicalDamage(player, monster, 40);
                d.monsterPhysicalDamage(player, monster,40);
                String s = player.level + "     " + monster.getLevel() + "     " + (int) d.playerPhysicalDamage(player, monster, 40) + "   " + (int) d.monsterPhysicalDamage(player, monster,40) + "    " + player.attack + "   " + (int) monster.getAttack()+"    "+player.defense+"   "+(int)monster.getDefense();
                System.out.println(s);
            }
            player.setExp((int)((((Math.pow(1.2,i))*1000)/2)-300));
            player.checkLvlUp();
        }
    }
}
