package com.quadx.dungeons.items.equipment;

import com.quadx.dungeons.items.Item;

import java.util.Random;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Equipment extends Item {
    private static final Random rn=new Random();
    Grade grade;
    Boost boost;
    Type type;

    enum Type{
        Arms,Boots,Cape,Gloves,Helmet,Legs,Ring,Chest
    }
    enum Grade{
        Poor,Low,Standard,High,Elite,Legendary
    }
    enum Boost{
        Perception, Power, Health, Magic, Haste, Resistance
    }
   Equipment(){
    isEquip=true;
    }
    public String getName(){

        return name;
    }
    public String getType(){
        return type.toString();
    }
    void setMods(){
        int buffCount=1;
        if(grade==Grade.Elite){
            buffCount=2;
        }
        if(grade==Grade.Legendary){
            buffCount=3;
        }
        while(buffCount>=1) {
            int x;
            if(buffCount>1){
                x=rn.nextInt(6);
                setBoost(x);
            }
            else setBoost();
            switch (boost) {
                case Perception: {
                    intelmod += calculateBuff();
                    break;
                }
                case Power: {
                    attackmod += calculateBuff();
                    break;
                }
                case Haste: {
                    speedmod += calculateBuff();
                    break;
                }
                case Health: {
                    hpmod += calculateBuff();
                    break;
                }
                case Resistance: {
                    defensemod += calculateBuff();
                    break;
                }
                case Magic: {
                    manamod += calculateBuff();
                    break;
                }
            }
            buffCount--;
        }
        setCost();

    }
    private int calculateBuff(){
        int buff=0;
        switch (grade){
            case Poor:
                buff=rn.nextInt(5);
                break;
            case Low:
                buff=rn.nextInt(10)+5;
                break;
            case Standard:
                buff=rn.nextInt(15)+10;
                break;
            case High:
                buff=rn.nextInt(20)+15;
                break;
            case Elite:
                buff=rn.nextInt(25)+20;
                break;
            case Legendary:
                buff=rn.nextInt(35)+25;
                break;
        }
        return buff;
    }
    public int[] compare(Equipment eq2){
        int[] x=new int[7];
        if(this.getType().equals(eq2.getType())){
            if(this.hpmod>eq2.hpmod){
                x[0]=1;
            }else if(this.hpmod<eq2.hpmod){
                x[0]=2;
            }else{
                x[0]=0;
            }
            if(this.manamod>eq2.manamod){
                x[1]=1;
            }else if(this.manamod<eq2.manamod){
                x[1]=2;
            }else{
                x[1]=0;
            }
            if(this.attackmod>eq2.attackmod){
                x[2]=1;
            }else if(this.attackmod<eq2.attackmod){
                x[2]=2;
            }else{
                x[2]=0;
            }
            if(this.defensemod>eq2.defensemod){
                x[3]=1;
            }else if(this.defensemod<eq2.defensemod){
                x[3]=2;
            }else{
                x[3]=0;
            }
            if(this.intelmod>eq2.intelmod){
                x[4]=1;
            }else if(this.intelmod<eq2.intelmod){
                x[4]=2;
            }else{
                x[4]=0;
            }
            if(this.speedmod>eq2.speedmod){
                x[5]=1;
            }else if(this.speedmod<eq2.speedmod){
                x[5]=2;
            }else{
                x[5]=0;
            }
        }

        return x;
    }
    public int[] compare(){
        int[] x=new int[7];
        if(this.hpmod>0) {
            x[0] = 1;
        }
        if(this.manamod>0)
        {
            x[1] = 1;
        }
        if(this.attackmod>0){
            x[2]=1;}

        if(this.defensemod>0){
            x[3]=1;
        }
        if(this.intelmod>0){
            x[4]=1;
        }
        if(this.speedmod>0){
            x[5]=1;
        }
        return x;
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
    private void setBoost(int x){
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
    private void setCost(){
        int x=0;
        x+= hpmod;
        x+=manamod;
        x+=attackmod;
        x+=defensemod;
        x+=intelmod;
        x+=speedmod;
        x=x*100;
        cost=x;
    }
    public static Equipment generateEquipment(){
        Equipment item=new Equipment();
        int x=rn.nextInt(8)+1;
        switch (x){
            case(1):{
                item=new Arms();
                break;
            }
            case(2):{
                item=new Boots();
                break;
            }
            case(3):{
                item= new Cape();
                break;
            }
            case(4):{
                item=new Chest();
                break;
            }
            case(5):{
                item= new Gloves();
                break;
            }
            case(6):{
                item=new Helmet();
                break;
            }
            case(7):{
                item=new Legs();
                break;
            }
            case(8):{
                item=new Ring();
                break;
            }
        }
        return item;
    }
}
