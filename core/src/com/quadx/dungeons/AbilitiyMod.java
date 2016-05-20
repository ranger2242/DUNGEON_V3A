package com.quadx.dungeons;

/**
 * Created by range on 5/19/2016.
 */
public class AbilitiyMod {
    public static boolean digPlus=false;
    public AbilitiyMod(){

    }
    public void enableAbility(int mod){
        switch (mod){
            case 1:{
                digPlus=true;
                break;
            }
        }
    }
}
