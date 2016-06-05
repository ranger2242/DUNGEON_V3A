package com.quadx.dungeons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.equipment.Arms;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player {
    public ArrayList<Attack> attackList = new ArrayList<>();
    public ArrayList<ArrayList<Item>> invList = new ArrayList<>();
    public ArrayList<Equipment> equipedList = new ArrayList<>();
    private ArrayList<String> statsList= new ArrayList<>();
    private Damage d = new Damage();
    private Vector2 pospx=new Vector2(0,0);
    private int x;
    private int px;//(Game.WIDTH/2)+1;
    private int y;
    private int py;//(Game.HEIGHT/2)-2;
    private int liveCellListIndex;
    private int hpMod =0;
    private int attackMod=0;
    private int defenseMod=0;
    private int speedMod=0;
    private int manaMod = 0;
    private int manaRegenRate=5;
    private int speed=15;
    private int hpRegen =2;
    private int exp=0;
    private int killcount=0;
    private int energy=100;
    private int energyMax=100;
    private int energyRegen=2 ;
    public int level =1;
    public int floor= 1;
    int hpMax = 100;
    int intelMod=0;
    int hp =100;
    int attack=15;
    int defense=15;
    int intel=15;
    int mana = 100;
    int manaMax = 100;
    int prevInvSize=0;
    int abilityPoints=1;
    double mDamage=0;
    double pDamage=0;
    public boolean safe=false;

    public float dtSafe=0;
    private float moveSpeed=.2f;
    private float gold=0;

    private Random rn =new Random();
    String name ="DEMO";
    private Attack fullhealSp = new Heal();
    private Attack flameSp= new Flame();
    private Attack restSp =new Rest();
    private Attack drainSp= new Drain();
    private Attack blindSp = new Blind();
    private Attack tormentSp = new Torment();
    private Attack illusionSp= new Illusion();
    private Attack protectSp= new Protect();
    private Attack sacrificeSp= new Sacrifice();
    Attack slashSp = new Slash();
    Attack stabSp = new Stab();

    public Player(){
        System.out.println("5");
        level=1;
        attackList.clear();
        attackList.add(flameSp);
        attackList.add(restSp);
        attackList.add (drainSp);
        attackList.add(fullhealSp);
        attackList.add(protectSp);
        attackList.add(stabSp);
        attackList.add(slashSp);

    }
    //SETTERS------------------------------------------------------------------
    public void setEnergy(int e){
        energy=e;
    }
    public void setEnergyMax(int e){
        energyMax=e;
    }
    public void setGold(float g){gold=g;}
    public void setCords(int a, int b) {
        x=a;
        y=b;
    }
    public void setName(String n)
    {
        name = n;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setMana(int m) {
        this.mana = m;
    }
    public void setExp(Monster m) {
        int expmax=m.getLevel()*25;
        int expGain = (int) ((rn.nextFloat()* expmax)+(m.getLevel() * 10));
        MapStateRender.setHoverText(expGain +" EXP",.8f, Color.GREEN ,Game.player.getPX(),Game.player.getPY()+10,false);
        exp=exp+ expGain;
        MapState.out(name+" gained "+ expGain +" EXP");
    }
    public void setExp(int a){exp=a;}
    public void setLiveListIndex(int i){
        liveCellListIndex=i;
    }
    public void setCordsPX(int i, int i1) {
        pospx.set(i,i1);
        px=i;
        py=i1;
        x=px/MapState.cellW;
        y=py/MapState.cellW;
    }
    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    public void setEnergyRegen(int i) {
        energyRegen=i;
    }
    public void setManaMax(int manaMax) {
        this.manaMax = manaMax;
    }
    public void setManaRegen(int manaRegen) {
        this.manaRegenRate = manaRegen;
    }
    public void setHpRegen(int hpRegen) {
        this.hpRegen = hpRegen;
    }
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public void setDefense(int defense) {
        this.defense = defense;
    }
    public void setIntel(int intel) {
        this.intel = intel;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //GETTERS------------------------------------------------------------------
    public Vector2 getCordsPX(){
        return pospx;
    }
    public int getGold()
    {
        return (int)gold;
    }
    public int getLiveListIndex(){
        return liveCellListIndex;
    }
    public int getX()
    {
        return x;
    }
    public int getPX()
    {
        return px;
    }
    public int getY()
    {
        return y;
    }
    public int getPY()
    {
        return py;
    }
    private int getLevel()
    {
        return level;
    }
    public int getHp()
    {
        return hp;
    }
    public int getHpMax()
    {
        return hpMax;
    }
    public int getDefense()
    {
        return defense;
    }
    public int getSpeed()
    {
        return speed;
    }
    public int getAttack()
    {
        return attack;
    }
    public int getIntel()
    {
        return intel;
    }
    public int getExp(){return exp;}
    public int getMana(){return mana;}
    public int getPoints(){
        return ((int)gold*10)+(attack*100)+(defense*100)+(speed*100)+(level*2000)+(intel*100)+(hpMax*10)+(manaMax*10)+(energyMax*10)+(floor*1000)+(killcount*200);
    }
    public int getEnergy(){
        return  energy;
    }
    public int getEnergyMax(){
        return energyMax;
    }
    public int getManaRegenRate(){
        return  manaRegenRate;
    }
    public int getHpRegen(){return hpRegen;}
    public ArrayList<String> getStatsList(){
        statsList.clear();

        statsList.add(name);
        statsList.add("Level " + level);
        statsList.add("HP " + hp + "/" + hpMax +"+"+hpMod+": "+(hpMax +hpMod));
        statsList.add("M " + mana + "/" + manaMax+" + "+manaMod+": "+(manaMax+manaMod));
        statsList.add("ATT:      " + attack+" + "+attackMod+": "+(attack+attackMod));
        statsList.add("DEF:      " + defense+" + "+defenseMod+": "+(defense+defenseMod));
        statsList.add("INT:      " + intel+" + "+intelMod+": "+(intel+intelMod));
        statsList.add("SPD:      " + speed+" + "+speedMod+": "+(speed+speedMod));
        statsList.add("KILLS:    " + killcount);
        statsList.add("GOLD:     " + gold);
        statsList.add("EXP:      " + exp + "/" + (int) ((((Math.pow(1.5, Game.player.getLevel())) * 1000) / 2) - 300));
        statsList.add("D:        " + floor);
        return  statsList;
    }
    public String getName()
    {
        return name;
    }
    public int getManaMax() {
        return manaMax;
    }
    public float getMoveSpeed() {
        return moveSpeed;
    }
    public int getEnergyRegen() {
        return energyRegen;
    }
    public String getKills() {
        return ""+killcount;
    }

    //MISC Functions------------------------------------------------------------------
    public void useItem(int i){
        String s="";
        Item item =invList.get(i).get(0);
        if(item.isEquip){
            Equipment temp=null;
            for(Equipment eq : equipedList){
                if(eq.getType().equals(item.getType())){
                    temp=eq;
                }
            }
            invList.get(i).remove(0);
            invList.remove(i);
            if(MapStateRender.inventoryPos>=invList.size()-1){
                MapStateRender.inventoryPos=0;
            }
            if(temp != null) {
                equipedList.remove(temp);
                addItemToInventory(temp);
            }

            equipedList.add((Equipment) item);


        }else {
            try {
                try {
                    invList.get(i).remove(0);
                } catch (IndexOutOfBoundsException e) {
                }

                if (item.getHpmod() != 0) {
                    hp += item.getHpmod();
                    s = name + "'s HP changed by " + item.getHpmod();
                }
                //Mana
                if (item.getManamod() != 0) {
                    mana += item.getManamod();
                    s = name + "'s M changed by " + item.getManamod();
                }
                //attack
                if (item.getAttackmod() != 0) {
                    attack += item.getAttackmod();
                    s = name + "'s ATT changed by " + item.getAttackmod();
                }
                //defense
                if (item.getDefensemod() != 0) {
                    defense += item.getDefensemod();
                    s = name + "'s DEF changed by " + item.getDefensemod();
                }
                //intel
                if (item.getIntelmod() != 0) {
                    intel += item.getIntelmod();
                    s = player.getName() + "'s INT changed by " + item.getIntelmod();
                }
                //speed
                if (item.getSpeedmod() != 0) {
                    speed += item.getSpeedmod();
                    s = player.getName() + "'s SPD changed by " + item.getSpeedmod();
                }
                MapState.out(s);
            } catch (NullPointerException e) {
            }
        }
       // invList.get(i).remove(0);

    }
    public void addKills(){killcount++;}
    public void updateVariables(float dt){
        calculateArmorBuff();
        if(hp>hpMax)hp=hpMax;
        if(mana>manaMax)mana=manaMax;

    }
    public void checkLvlUp() {
        //System.out.println("!"+exp+" n"+((((Math.pow(1.2,level))*1000)/2)-300));
        if (exp>=(int)((((Math.pow(1.5,level))*1000)/2)-300))
        {
            MapStateRender.setHoverText("--LVL UP--",.8f, Color.GREEN, Game.player.getPX(),Game.player.getPY()-20,true);

            exp=exp-(int)((((Math.pow(1.5,level))*1000)/2)-300);

            //System.out.println(name+" leveled up.");
            level++;
            hpMax = hpMax +( 25 + rn.nextInt(20));
            manaMax=manaMax+(25+rn.nextInt(20));
            energyMax=energyMax+(25+rn.nextInt(20));
            manaRegenRate+=1;
            attack=attack+((int) (Math.random() * 4));
            defense=defense+((int) (Math.random() * 4));
            intel=intel+((int) (Math.random() * 4));
            speed=speed+((int) (Math.random() * 4));
        }
    }
    public void addSpell(){




    }
    public void addItemToInventory(Item item){
        boolean added=false;
        if(!item.isEquip) {
            for (ArrayList<Item> al : invList) {
                if (!al.isEmpty()) {
                    try {
                        if (al.get(0).getName().equals(item.getName())) {
                            al.add(item);
                            added = true;
                        }
                    } catch (NullPointerException e) {

                    }
                }
            }
        }
        if(!added){
            ArrayList<Item> al = new ArrayList<>();
            al.add(item);
            invList.add(al);
        }
    }
    public void calculateArmorBuff() {
        int sum1=0;
        int sum2=0;
        int sum3=0;
        int sum4=0;
        int sum5=0;
        int sum6=0;
        for(Equipment eq : equipedList){
            sum1+=eq.getAttackmod();
        }
        attackMod=sum1;
        for(Equipment eq : equipedList){
            sum2+=eq.getDefensemod();
        }
        defenseMod=sum2;
        for(Equipment eq : equipedList){
            sum3+=eq.getIntelmod();
        }
        intelMod=sum3;
        for(Equipment eq : equipedList){
            sum4+=eq.getSpeedmod();
        }
        speedMod=sum4;
        for(Equipment eq : equipedList){
            sum5+=eq.getHpmod();
        }
        hpMod=sum5;
        for(Equipment eq : equipedList){
            sum6+=eq.getManamod();
        }
        manaMod=sum6;
    }
    public void checkNullInventory() {
        ArrayList<Integer> toRemove=new ArrayList<>();
        if(prevInvSize!=invList.size()){
            for(ArrayList<Item> list :invList){
                if(list.get(0).getName()==null){
                    toRemove.add(invList.indexOf(list));
                }
            }
            for(int i=0;i<toRemove.size();i++){
                invList.remove(toRemove.get(i));
            }
            prevInvSize=invList.size();
        }
    }
    public boolean checkIfDead() {
        boolean dead=false;
        if(hp<1){
            dead=true;
        }

        return dead;
    }
}
