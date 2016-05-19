package com.quadx.dungeons;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tom on 11/9/2015.
 */
public class Player {
    public ArrayList<Attack> attackList = new ArrayList<>();
    public ArrayList<ArrayList<Item>> invList = new ArrayList<>();
    public ArrayList<Equipment> equipedList = new ArrayList<>();
    ArrayList<String> statsList= new ArrayList<>();
    Damage d = new Damage();
    Vector2 pospx=new Vector2(0,0);
    int x,px;//(Game.WIDTH/2)+1;
    int y,py;//(Game.HEIGHT/2)-2;
    int liveCellListIndex;
    int gold=500;
    public int level =1;
    int hpmax = 100;
    int hpMod =0;
    int attackMod=0;
    int defenseMod=0;
    int speedMod=0;
    int intelMod=0;
    int manaMod = 0;
    int hp =100;
    int attack=20;
    int defense=20;
    int speed=20;
    int intel=20;
    int mana = 100;
    int manaMax = 100;
    int manaRegenRate=5;
    int hpRegenRate=2;
    double damage;
    int exp=0;
    int expGain=0;
    int killcount=0;
    int energy=100;
    int energyMax=100;
    int energyRegen=2;
    public int floor= 0;
    float moveSpeed=.08f;
    Random rn =new Random();
    String name ="DEMO";
    Attack fullhealSp = new FullHeal();
    Attack flameSp= new Flame();
    Attack restSp =new Rest();
    Attack drainSp= new Drain();
    Attack blindSp = new Blind();
    Attack tormentSp = new Torment();
    Attack illusionSp= new Illusion();
    Attack protectSp= new Protect();
    Attack sacrificeSp= new Sacrifice();
    Attack slashSp = new Slash();
    Attack stabSp = new Stab();

    public Player(){
        System.out.println("5");
        level=1;

    }
    public void addKills(){killcount++;}
    public void setEnergy(int e){
        energy=e;
    }
    public void setEnergyMax(int e){
        energyMax=e;
    }
    public void setGold(int g){gold=g;}
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
        expGain=((m.getLevel()*50)+(int)(Math.random()*expmax));
        exp=exp+expGain;
        MapState.out(name+" gained "+expGain+" EXP");
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
    public Vector2 getCordsPX(){
        return pospx;
    }
    public int getGold()
    {
        return gold;
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
    int getLevel()
    {
        return level;
    }
    public int getHp()
    {
        return hp;
    }
    public int getHpMax()
    {
        return hpmax;
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
    public int getEnergy(){
        return  energy;
    }
    public int getEnergyMax(){
        return energyMax;
    }
    public int getManaRegenRate(){
        return  manaRegenRate;
    }
    public int getHpRegenRate(){return  hpRegenRate;}
    public ArrayList<String> getStatsList(){
        statsList.clear();

        statsList.add(name);
        statsList.add("Level " + level);
        statsList.add("HP " + hp + "/" + hpmax+"+"+hpMod+": "+(hpmax+hpMod));
        statsList.add("M " + mana + "/" + manaMax+" + "+manaMod+": "+(manaMax+manaMod));
        statsList.add("ATT:      " + attack+" + "+attackMod+": "+(attack+attackMod));
        statsList.add("DEF:      " + defense+" + "+defenseMod+": "+(defense+defenseMod));
        statsList.add("INT:      " + intel+" + "+intelMod+": "+(intel+intelMod));
        statsList.add("SPD:      " + speed+" + "+speedMod+": "+(speed+speedMod));
        statsList.add("KILLS:    " + killcount);
        statsList.add("GOLD:     " + gold);
        statsList.add("EXP:      " + exp + "/" + (int) ((((Math.pow(1.2, Game.player.getLevel())) * 1000) / 2) - 300));
        statsList.add("D:        " + floor);
        return  statsList;
    }
    public String getName()
    {
        return name;
    }
    public void checkLvlUp() {
        //System.out.println("!"+exp+" n"+((((Math.pow(1.2,level))*1000)/2)-300));
        if (exp>=(int)((((Math.pow(1.2,level))*1000)/2)-300))
        {
            exp=exp-(int)((((Math.pow(1.2,level))*1000)/2)-300);

            //System.out.println(name+" leveled up.");
            level++;
            hpmax=hpmax+( 25 + rn.nextInt(20));
            manaMax=manaMax+(25+rn.nextInt(20));
            manaRegenRate+=1;
            attack=attack+( 0 + (int)(Math.random()*4));
            defense=defense+( 0 + (int)(Math.random()*4));
            intel=intel+( 0 + (int)(Math.random()*4));
            speed=speed+( 0 + (int)(Math.random()*4));
        }
    }
    public void takeAttackDamage(int i) {hp=hp-i;}
    public double getAttackDamage(int power) {
        damage=d.playerPhysicalDamage(this,Main.monster,power);
        return damage;
    }
    public double getIntelDamage(int power) {
        damage=d.playerMagicDamage(this,Main.monster,power);
        return damage;
    }
    public void addSpell(){
        attackList.clear();
        attackList.add(flameSp);
        attackList.add(restSp);
        attackList.add(blindSp);
        attackList.add (drainSp);
        attackList.add(illusionSp);
        attackList.add(fullhealSp);
        //attackList.add(slashSp);
        //attackList.add(stabSp);
        attackList.add(tormentSp);
        attackList.add(protectSp);
        attackList.add(sacrificeSp);

        for(Attack a: Game.player.attackList){
            System.out.println(a.getName());

        }

    }
    public void addItemToInventory(Item item){
        boolean added=false;
        for(ArrayList<Item> al:invList)
        {
            if(!al.isEmpty()) {
                try {
                    if (al.get(0).getName().equals(item.getName())) {
                        al.add(item);
                        added = true;
                    }
                }
                catch (NullPointerException e)
                {

                }
            }
        }
        if(!added){
            ArrayList al =new ArrayList<Item>();
            al.add(item);
            invList.add(al);
        }
    }
    public int getManaMax() {
        return manaMax;
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

    public int getEnergyRegen() {
        return energyRegen;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
