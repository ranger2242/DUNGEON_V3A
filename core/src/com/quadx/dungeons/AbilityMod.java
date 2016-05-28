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
    public static Ability ability = null;


    public static void enableAbility(int mod){
        modifier=mod;
        switch (mod){
            case 1:{
                ability = new DigPlus();
                break;
            }
            case 2:
            {//Brawler
                ability =new Brawler();
                boolean brawler = true;
                break;
            }
            case 3:{//Mage
                ability =new Mage();
                boolean mage = true;
                break;
            }
            case 4:{//Quick
                ability =new Quick();
                boolean quick = true;
                break;
            }
            case 5: {//Investor
                ability =new Investor();
                investor=true;
                break;
            }
            case 6: {//Tank
                ability =new Tank();
                boolean tank = true;
                break;
            }
            case 7:{//Lucky
                ability=new Warp();
                break;
            }

        }
        ability.onActivate();

    }
}
