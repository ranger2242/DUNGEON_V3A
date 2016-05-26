package com.quadx.dungeons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.*;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;
import java.util.Random;

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
    private float gold=0;
    public int level =1;
    int hpMax = 100;
    private int hpMod =0;
    private int attackMod=0;
    private int defenseMod=0;
    private int speedMod=0;
    int intelMod=0;
    private int manaMod = 0;
    int hp =100;
    int attack=20;
    int defense=20;
    private int speed=20;
    int intel=20;
    int mana = 100;
    int manaMax = 100;
    private int manaRegenRate=5;
    private int hpRegen =2;
    double damage;
    private int exp=0;
    private int killcount=0;
    private int energy=100;
    private int energyMax=100;
    private int energyRegen=1 ;
    int prevInvSize=0;
    int abilityPoints=1;
    public int floor= 0;
    private float moveSpeed=.08f;
    private Random rn =new Random();
    String name ="DEMO";
    private Attack fullhealSp = new FullHeal();
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

    }
    public void addKills(){killcount++;}
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
        int expGain = ((m.getLevel() * 50) + (int) (Math.random() * expmax));
        MapStateRender.setHoverText(expGain +" EXP",.8f, Color.GREEN);
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
        return ((int)gold*10)+(attack*50)+(defense*50)+(speed*50)+(level*200)+(intel*50)+(hpMax*20)+(manaMax*20)+(energyMax*20)+(floor*10)+(killcount*20);
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
            MapStateRender.setHoverText("--LVL UP--",.8f, Color.GREEN);

            exp=exp-(int)((((Math.pow(1.2,level))*1000)/2)-300);

            //System.out.println(name+" leveled up.");
            level++;
            hpMax = hpMax +( 25 + rn.nextInt(20));
            manaMax=manaMax+(25+rn.nextInt(20));
            manaRegenRate+=1;
            attack=attack+((int) (Math.random() * 4));
            defense=defense+((int) (Math.random() * 4));
            intel=intel+((int) (Math.random() * 4));
            speed=speed+((int) (Math.random() * 4));
        }
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
            ArrayList<Item> al = new ArrayList<>();
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
}
