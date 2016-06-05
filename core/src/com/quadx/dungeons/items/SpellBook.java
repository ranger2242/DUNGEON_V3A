package com.quadx.dungeons.items;

import com.quadx.dungeons.attacks.*;

import java.util.Random;

/**
 * Created by Tom on 12/22/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SpellBook extends Item {
    public SpellBook(){
        isSpell=true;
        Random rn = new Random();
        int x= rn.nextInt(10);
        setAttack(x);
        name="Spell Book ("+attack.getName()+")";
        loadIcon(name);
    }

    public Attack getAttack(){
        return attack;
    }

    private void setAttack(int x){
        switch (x){
            case(0):{
                attack=new Blind();
                break;
            }
            case(1):{
                attack=new Drain();
                break;
            }
            case(2):{
                attack=new Flame();
                break;
            }
            case(3):{
                attack=new Heal();
                break;
            }
            case(4):{
                attack=new Illusion();
                break;
            }
            case(5):{
                attack=new Protect();
                break;
            }
            case(6):{
                attack=new Sacrifice();
                break;
            }
            case(7):{
                attack=new Slash();
                break;
            }
            case(8):{
                attack=new Stab();
                break;
            }
            case(9):{
                attack=new Torment();
                break;
            }
        }
        cost=attack.getCostGold();
    }
}
