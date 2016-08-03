package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.mapstate.MapState.out;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AttackMod {
    public static boolean torment=false;
    public static boolean sacrifice=false;
    public static float dtTorment=0;
    public static float dtSacrifice=0;
    public static void updaterVariables(float dt){
        if(torment){
            dtTorment+=dt;
            torment();
        }
    }
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
                out(player.getName() +" stole "+ player.getmDamage()/2+" HP");
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
                break;
            }
            case 4:{//TORMENT
                MapStateRender.setHoverText("-Tormented-",.15f, Color.BLUE, player.getPX(),player.getPY()-15,false);
                torment=true;
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
                out(player.getName()+" is protected from damage!");
                break;
            }
            case 7:{//SACRIFICE
                if(dtSacrifice>1) {
                    player.setHp(player.getHp() - player.getHpMax());
                    sacrifice = true;
                    dtSacrifice=0;
                }
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
    public static void resetAttacks(){
        sacrifice=false;
        torment=false;
        dtTorment=0;
    }
    static void torment(){
        if(dtTorment<30){
            torment=true;
            player.setAttBuff(2);
            player.setIntBuff(.5f);
        }
        else{
            out("The torment has ended");
            player.setAttBuff(1);
            player.setIntBuff(1);
            dtTorment=0;
            torment=false;
        }
    }

}
