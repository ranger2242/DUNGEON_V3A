package com.quadx.dungeons;

import com.badlogic.gdx.maps.Map;
import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

/**
 * Created by range on 5/19/2016.
 */
public class AbilityMod {
    public static int modifier=1;
    public static boolean digPlus=false;
    public static boolean brawler=false;
    public static boolean mage=false;
    public static boolean quick=false;
    public static boolean lucky=false;
    public static boolean investor=false;
    public static boolean tank=false;
    static Ability ability;

    public AbilityMod(){

    }
    public static void enableAbility(int mod){
        modifier=mod;
        switch (mod){
            case 1:{
                digPlus=true;
                break;
            }
            case 2:{//Brawler
                ability=new Brawler();
                ability.onActivate();
                brawler=true;
                break;
            }
            case 3:{//Mage
                ability=new Mage();
                ability.onActivate();
                mage=true;
                break;
            }
            case 4:{//Quick
                ability=new Quick();
                ability.onActivate();
                quick=true;
                break;
            }
            case 5: {//Investor
                ability=new Investor();
                ability.onActivate();
                investor=true;
                break;
            }
            case 6: {//Tank
                ability=new Tank();
                ability.onActivate();
                tank=true;
                break;
            }
            case 7:{//Lucky

            }
        }
    }
}
