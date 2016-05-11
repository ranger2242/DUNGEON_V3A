package com.quadx.dungeons.items.equipment;

import com.quadx.dungeons.items.Item;

import java.util.Random;

/**
 * Created by Tom on 12/29/2015.
 */
public class Equipment extends Item {
    public static Random rn=new Random();
    public Grade grade;
    public Boost boost;
    public Type type;

    public enum Type{
        Arms,Boots,Cape,Gloves,Helmet,Legs,Ring,Chest
    }
    public enum Grade{
        Poor,Low,Standard,High,Elite,Legendary
    }
    public enum Boost{
        Perception, Power, Health, Magic, Haste, Resistance
    }
    public Equipment(){
    isEquip=true;
    }
    public String getName(){

        return name;
    }
    public String getType(){
        return type.toString();
    }
    void setMods(){
        switch (boost){
            case Perception:{
                intelmod=calculateBuff();
                break;
            }
            case Power:{
                attackmod=calculateBuff();
                break;
            }
            case Haste:{
                speedmod=calculateBuff();
                break;
            }
            case Health:{
                hpmod=calculateBuff();
                break;
            }
            case Resistance:{
                defensemod=calculateBuff();
                break;
            }
            case Magic:{
                manamod=calculateBuff();
                break;
            }
        }
    }
    int calculateBuff(){
        int buff=0;
        switch (grade){
            case Poor:
                buff=rn.nextInt(5);
                break;
            case Low:
                buff=rn.nextInt(10);
                break;
            case Standard:
                buff=rn.nextInt(15);
                break;
            case High:
                buff=rn.nextInt(20);
                break;
            case Elite:
                buff=rn.nextInt(25);
                break;
            case Legendary:
                buff=rn.nextInt(35);
                break;
        }
        return buff;
    }

    void setBoost(){
        int x= rn.nextInt(6);
        if(x==0)boost=Boost.Haste;
        if(x==1)boost=Boost.Resistance;
        if(x==2)boost=Boost.Magic;
        if(x==3)boost=Boost.Health;
        if(x==4)boost=Boost.Perception;
        if(x==5)boost=Boost.Power;

    }
    void setGrade(){
        int x=rn.nextInt(100);
        if(x<=30)grade=Grade.Poor;
        if(x>30)grade=Grade.Low;
        if(x>50)grade=Grade.Standard;
        if(x>60)grade=Grade.High;
        if(x>80)grade=Grade.Elite;
        if(x>90)grade=Grade.Legendary;
    }
}
