package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Torment extends Attack {
    public Torment()  {
        costGold=6000;
        type=CostType.Mana;
        powerA= new int[]{0,0,0,0,0};
        costA= new int[]{50,50,50,50,50};
        name="Torment";
        description="INT x0.5 and ATT x2 for 30 time";
        power= 0;
        cost= 0;
        range= 0;
        spread= 0;
        mod=4;
        hitBoxShape=HitBoxShape.None;
        setIcon(ImageLoader.attacks.get(11));
    }
    public void runAttackMod() {
        /*player.setEnergyMax((int) (player.getEnergyMax()/2));
        player.setDefense((int) (player.getDefense()*1.5f));
        player.setSpeed((int) (player.getSpeed()*1.5f));*/
        int index=-1;
        for(Attack a: player.attackList){
            if(a.getClass().equals(Torment.class)){
                index=player.attackList.indexOf(a);
            }
        }
        //MapStateUpdater.shakeScreen(.2f,1);
        /*new HoverText("--TORMENT--",2, Color.MAGENTA,player.abs().x,player.abs().y+100,false);
        new HoverText("DEF x1.5",2, Color.GREEN,player.abs().x,player.abs().y+80,false);
        new HoverText("SPD x1.5",2, Color.GREEN,player.abs().x,player.abs().y+60,false);
        new HoverText("E x0.5",2, Color.RED,player.abs().x,player.abs().y+40,false);
*/

        if(index!=-1){
            player.attackList.remove(index);
        }

    }
}
