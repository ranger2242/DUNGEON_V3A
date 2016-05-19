package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.Random;

/**
 * Created by Tom on 11/17/2015.
 */
public class SpellMods {

    static public void runMod(Monster m, Attack a){
        int mod=a.getMod();
        String name=a.getName();
        String s;
        Random rn = new Random();

        int[] levelA;
        double[] levelB;
        MapState.statPopup=null;
        switch (mod){
            case -1:{
                s= Game.player.name +" used "+ name;
            }
            case 0:{
                break;
            }
            case 1:{//DRAIN
                Game.player.hp+= Game.player.damage;
                MapState.out(Game.player.name +" stole "+ Game.player.damage+" HP");
                break;
            }
            case 2:{
                Game.player.hp= Game.player.hpmax;
                Game.player.mana= Game.player.manaMax;
                break;
            }
            case 3:{//BLIND
                m.acc*=.9;
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
                /*
                levelA=new int[]{10,8,6,4,2};
                int x=rn.nextInt(100)+1;
                if(x<100-levelA[a.getLevel()]) {
                    MapState.out(Game.player.getName()+" is protected from damage this turn.");
                }*/
                //else
                MapState.out(Game.player.getName()+"'s Protect failed!");
                break;
            }
            case 7:{//SACRIFICE
                levelB=new double[]{.15,.20,.30,.40,.50};
                double x= Game.player.hpmax*levelB[a.getLevel()];
                double y= Game.player.manaMax*(levelB[a.getLevel()]*2);
                Game.player.hp-=(x);
                Game.player.mana+=(x);
                MapState.out(Game.player.name+" sacrificed "+x+"HP for "+y+"M.");

                break;
            }
            case 8: {//REST
                if(Game.player.getEnergy()<Game.player.getEnergyMax()) {
                    System.out.println("Resting");
                    Game.player.setEnergy(Game.player.getEnergy() + a.getCost()/2);
                    if(Game.player.getEnergy()>Game.player.getEnergyMax())
                        Game.player.setEnergy(Game.player.getEnergyMax());
                }
            }

        }
        MapState.dtStatPopup=0;

    }

}
