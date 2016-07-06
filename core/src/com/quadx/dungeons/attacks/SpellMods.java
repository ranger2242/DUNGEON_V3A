package com.quadx.dungeons.attacks;

import com.quadx.dungeons.states.mapstate.MapState;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class SpellMods {
    static public void runMod(Attack a){
        int mod=a.getMod();
        MapState.statPopup=null;
        switch (mod){
            case -1:{
            }
            case 0:{
                break;
            }
            case 1:{//DRAIN
                player.setHp((int) (player.getHp()+player.getmDamage()/2));
                MapState.out(player.getName() +" stole "+ player.getmDamage()/2+" HP");
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
                /*
//                m.acc*=.9;
                MapState.statPopup=new Texture(Gdx.files.internal("images/icons/stats/icAccD.png"));
                MapState.out(m.getName()+"'s accuracy was lowered by 10%.");*/
                break;
            }
            case 4:{//TORMENT
                /*
                m.setAttack(m.getAttack()*.9);
                ;
                MapState.statPopup=new Texture(Gdx.files.internal("images/icons/stats/icAttD.png"));
                MapState.out(m.getName()+"'s a=ATT was lowered by 10%");*/
                break;
            }
            case 5:{//ILLUSION
                /*
                levelA=new int[]{25,30,40,45,50};
                m.setIntel(m.getIntel()*1-levelA[a.getLevel()]);
                ;
                MapState.statPopup=new Texture(Gdx.files.internal("images/icons/stats/icIntD.png"));
                MapState.out(m.getName()+"'s INT is lowered by "+levelA[a.getLevel()]+"%.");*/
                break;
            }
            case 6: {//PROTECT
                player.safe=true;
                MapState.out(player.getName()+" is protected from damage!");
                break;
            }
            case 7:{//SACRIFICE
                /*
                levelB=new double[]{.15,.20,.30,.40,.50};
                double x= player.getHpMax() *levelB[a.getLevel()];
                double y= player.getManaMax()*(levelB[a.getLevel()]*2);
             //   player.hp-=(x);
             //   player.mana+=(x);
                MapState.out(player.name+" sacrificed "+x+"HP for "+y+"M.");
                */
                break;
            }
            case 8: {//REST
                if(player.getEnergy()< player.getEnergyMax()) {
                    player.setEnergy(player.getEnergy() + a.getCost()/2);
                    if(player.getEnergy()> player.getEnergyMax())
                        player.setEnergy(player.getEnergyMax());
                }
                break;
            }
            case 9:{//FOCUS;
                    player.setMana(player.getMana() + a.getCost()/2);
                    if(player.getMana()> player.getManaMax())
                        player.setMana(player.getManaMax());
                break;

            }
            case 10:{//EARTHQUAKE
                break;
            }

        }
        MapState.dtStatPopup=0;

    }

}