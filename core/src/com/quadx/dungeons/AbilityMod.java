package com.quadx.dungeons;

import com.quadx.dungeons.abilities.*;

/**
 * Created by range on 5/19/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AbilityMod {
    public static int modifier=1;
    public static boolean digPlus=false;
    public static boolean investor=false;

    public static void enableAbility(int mod){
        modifier=mod;
        switch (mod){
            case 1:{
                digPlus=true;
                break;
            }
            case 2:Ability ability;
            {//Brawler
                ability =new Brawler();
                ability.onActivate();
                boolean brawler = true;
                break;
            }
            case 3:{//Mage
                ability =new Mage();
                ability.onActivate();
                boolean mage = true;
                break;
            }
            case 4:{//Quick
                ability =new Quick();
                ability.onActivate();
                boolean quick = true;
                break;
            }
            case 5: {//Investor
                ability =new Investor();
                ability.onActivate();
                investor=true;
                break;
            }
            case 6: {//Tank
                ability =new Tank();
                ability.onActivate();
                boolean tank = true;
                break;
            }
            case 7:{//Lucky

            }
        }
    }
}
