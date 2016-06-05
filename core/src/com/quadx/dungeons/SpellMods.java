package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SpellMods {

    static public void runMod(Monster m, Attack a){
        int mod=a.getMod();
        //String name=a.getName();
       // String s;

        int[] levelA;
        double[] levelB;
        MapState.statPopup=null;
        switch (mod){
            case -1:{
            }
            case 0:{
                break;
            }
            case 1:{//DRAIN
                player.hp+= player.mDamage/2;
                MapState.out(player.name +" stole "+ player.mDamage/2+" HP");
                break;
            }
            case 2:{//Heal
                int total=player.getMana()+player.getEnergy()+player.getHp();
                if(total>player.getHpMax())total=player.getHpMax();
                player.setHp(total);
                player.setMana(0);
                player.setEnergy(0);
                break;
            }
            case 3:{//BLIND
//                m.acc*=.9;
                MapState.statPopup=new Texture(Gdx.files.internal("images/icons/stats/icAccD.png"));
                MapState.out(m.name+"'s accuracy was lowered by 10%.");
                break;
            }
            case 4:{//TORMENT
                m.attack*=.9;
                MapState.statPopup=new Texture(Gdx.files.internal("images/icons/stats/icAttD.png"));
                MapState.out(m.getName()+"'s a=ATT was lowered by 10%");
                break;
            }
            case 5:{//ILLUSION
                levelA=new int[]{25,30,40,45,50};
                m.intel*=1-levelA[a.getLevel()];
                MapState.statPopup=new Texture(Gdx.files.internal("images/icons/stats/icIntD.png"));
                MapState.out(m.name+"'s INT is lowered by "+levelA[a.getLevel()]+"%.");
                break;
            }
            case 6: {//PROTECT
                player.safe=true;
                MapState.out(player.getName()+" is protected from damage!");
                break;
            }
            case 7:{//SACRIFICE
                levelB=new double[]{.15,.20,.30,.40,.50};
                double x= player.hpMax *levelB[a.getLevel()];
                double y= player.manaMax*(levelB[a.getLevel()]*2);
                player.hp-=(x);
                player.mana+=(x);
                MapState.out(player.name+" sacrificed "+x+"HP for "+y+"M.");

                break;
            }
            case 8: {//REST
                if(player.getEnergy()< player.getEnergyMax()) {
                    System.out.println("Resting");
                    player.setEnergy(player.getEnergy() + a.getCost()/2);
                    if(player.getEnergy()> player.getEnergyMax())
                        player.setEnergy(player.getEnergyMax());
                }
            }

        }
        MapState.dtStatPopup=0;

    }

}
