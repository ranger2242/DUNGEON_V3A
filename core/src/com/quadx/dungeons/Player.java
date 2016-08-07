package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.abilities.DigPlus;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpellBook;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.liveCellList;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.setAim;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player {

    public final ArrayList<Attack> attackList = new ArrayList<>();
    public final ArrayList<ArrayList<Item>> invList = new ArrayList<>();
    public ArrayList<Ability> secondaryAbilityList=new ArrayList<>();
    public final ArrayList<Equipment> equipedList = new ArrayList<>();
    private final ArrayList<String> statsList= new ArrayList<>();
    private final Vector2 pospx=new Vector2(0,0);
    private Ability ability = null;
    public Item lastItem = null;
    private int x;
    private int px;//(Game.WIDTH/2)+1;
    private int y;
    private int py;//(Game.HEIGHT/2)-2;
    private int hpMod =0;
    private int attMod =0;
    private int defMod =0;
    private int spdMod =0;
    private int eMod=0;
    private int manaMod = 0;
    private int manaRegenRate=5;
    private int spd =15;
    private int hpRegen =2;
    private int exp=0;
    private int killCount =0;
    private int energy=100;
    private int energyMax=100;
    private int energyRegen=2 ;
    private int hpMax = 100;
    private int intMod =0;
    private int hp =100;
    private int def =15;
    private int intel=15;
    private int mana = 100;
    public int maxSec=2;
   // private int eMod=0;
    private int abilityMod =0;
    private int att =15;
    public int level =1;
    public int floor= 1;
    int expLimit=0;
    int manaMax = 100;
    int abilityPoints=0;

    double mDamage=0;

    public boolean infiniteRegen=false;
    public boolean canMove=false;
    public boolean safe=false;
    private float hpBuff=1;
    private float mBuff=1;
    private float eBuff=1;
    private float attBuff=1;
    private float defBuff=1;
    private float intBuff=1;
    private float spdBuff=1;

    public float dtSafe=0;
    public float dtMove=0;
    private float moveSpeed=.1f;
    private float gold=0;
    private final Random rn =new Random();
    private String name ="DEMO";
    private Texture[] icons=new Texture[4];

    public Player() {
        //AbilityMod.resetAbilities();
        level=1;
    }
    //SETTERS------------------------------------------------------------------
    public void setEnergy(int e){
        energy=e;
    }
    public void setEnergyMax(int e){
        energyMax=e;
    }
    public void setGold(float g){gold=g;}
    public void setName(String n) {
        name = n;
        if(name.length()>19){
            name=name.substring(0,19);
        }
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public void setMana(int m) {
        this.mana = m;
    }
    public void setExp(int l) {
        int expmax=l*25;
        int expGain = (int) ((rn.nextFloat()* expmax)+(l * 10));
        MapStateRender.setHoverText(expGain +" EXP",.8f, Color.GREEN ,Game.player.getPX(),Game.player.getPY()+10,false);
        exp=exp+ expGain;
        out(name+" gained "+ expGain +" EXP");
    }
    public void setCordsPX(int i, int i1) {
        pospx.set(i,i1);
        px=i;
        py=i1;
        x=px/ cellW;
        y=py/ cellW;
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
        this.att = attack;
    }
    public void setDefense(int defense) {
        this.def = defense;
    }
    public void setIntel(int intel) {
        this.intel = intel;
    }
    public void setSpeed(int speed) {
        this.spd = speed;
    }
    //Stat Multipliers--------------------------
    public void setxMoveSpeed(double moveSpeed) {
        this.moveSpeed *= moveSpeed;
    }
    public void setxEnergyRegen(double i) {
        energyRegen*=i;
    }
    public void setxManaMax(double manaMax) {
        this.manaMax *= manaMax;
    }
    public void setxManaRegen(double manaRegen) {
        this.manaRegenRate *= manaRegen;
    }
    public void setxHpRegen(double hpRegen) {
        this.hpRegen *= hpRegen;
    }
    public void setxHpMax(double hpMax) {
        this.hpMax*= hpMax;
    }
    public void setxAttack(double attack) {
        this.att *= attack;
    }
    public void setxDefense(double defense) {
        this.def *= defense;
    }
    public void setxIntel(double intel) {
        this.intel *= intel;
    }
    public void setxSpeed(double speed) {
        this.spd *= speed;
    }
    //--------------------------
    public void setAbilityMod(int o){ abilityMod =o;}

    public void setAbility(Ability a) {
        ability = a;
    }
    public void setHpBuff(float f){ hpBuff=f;}
    public void setMMod(float f){ mBuff=f;}
    public void setEBuff(float f){ eBuff=f;}
    public void setAttBuff(float f){ attBuff=f;}
    public void setDefBuff(float f){ defBuff=f;}
    public void setIntBuff(float f){ intBuff=f;}
    public void setSpdBuff(float f){ spdBuff=f;}
    //GETTERS------------------------------------------------------------------
    public Vector2 getCordsPX(){
        return pospx;
    }
    public int getLevel()
    {
        return level;
    }
    public int getGold()
    {
        return (int)gold;
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
        return def;
    }
    public int getSpeed()
    {
        return spd;
    }
    public int getAttack()
    {
        return att;
    }
    public int getIntel()
    {
        return intel;
    }
    public int getHpComp(){return (int) (hpMax*hpBuff+hpMod);}
    public int getMComp(){return (int) (manaMax*mBuff+manaMod);}
    public int getEComp(){return (int) (energyMax*eBuff+eMod);}
    public int getAttComp(){return (int) (att *attBuff+ attMod);}
    public int getDefComp(){return (int) (def *defBuff+ defMod);}
    public int getIntComp(){return (int) (intel*intBuff+ intMod);}
    public int getSpdComp(){return (int) (spd *spdBuff+ spdMod);}
    public int getExp(){return exp;}
    public int getMana(){return mana;}
    public int getPoints(){
        return ((int)gold*10)+(att *100)+(def *100)+(spd *100)+(level*2000)+(intel*100)+(hpMax*10)+(manaMax*10)+(energyMax*10)+(floor*1000)+(killCount *200);
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
    public int getManaMax() {
        return manaMax;
    }
    public int getEnergyRegen() {
        return energyRegen;
    }
    public int getAbilityMod(){return abilityMod;}
    public float getMoveSpeed() {
        float f;
        float a=moveSpeed/1000;
        f=moveSpeed-(a* spd);
        if(f<0)f=0;
        return f;
    }
    public String getName()
    {
        return name;
    }
    public ArrayList<Ability> getSecondaryAbilityList(){
        return secondaryAbilityList;
    }
    public ArrayList<String> getStatsList(){
        statsList.clear();

        statsList.add(name);
        statsList.add("Level " + level);
        statsList.add("AP:       "+abilityPoints);
        statsList.add("HP " + hp*hpBuff+ "/" + hpMax +"+"+hpMod+": "+getHpComp());
        statsList.add("M " + mana*mBuff + "/" + manaMax+" + "+manaMod+": "+getMComp());
        statsList.add("E "+energy*eBuff+"/"+energyMax+" + "+eMod+": "+getEComp());
        statsList.add("ATT:      " + att*attBuff +" + "+ attMod +": "+getAttComp());
        statsList.add("DEF:      " + def*defBuff +" + "+ defMod +": "+getDefComp());
        statsList.add("INT:      " + intel*intBuff+" + "+ intMod +": "+getIntComp());
        statsList.add("SPD:      " + spd*spdBuff +" + "+ spdMod +": "+getSpdComp());
        statsList.add("KILLS:    " + killCount);
        statsList.add("GOLD:     " + gold);
        statsList.add("EXP:      " + exp + "/" + expLimit);
        statsList.add("D:        " + floor);
        return  statsList;
    }
    public Texture getIcon(){
        int u=0;
        switch (lastPressed){
            case 'w':{
                u=0;
                break;
            }
            case 'd':{
                u=1;
                break;
            }
            case 's':{
                u=2;

                break;
            }
            case 'a':{
                u=3;

                break;
            }
        }

        return icons[u];
    }
    public Score getScore(){
        return new Score( ""+name, ""+getPoints(),""+(int)(gold), player.getAbility().getName() + " Lvl " + level, ""+ killCount);
    }
    public Item getLastItem(){return lastItem;}
    public Ability getAbility(){return ability;}
    public int getKillCount() {
        return killCount;
    }
    public int getFloor() {
        return floor;
    }
    //MISC Functions------------------------------------------------------------------
    public void maxStat(){
        int a=10000;
        hpMax=a;
        hp=a;
        manaMax=a;
        mana=a;
        energyMax=a;
        energy=a;
        att=a;
        def=a;
        intel=a;
        spd=a;
    }
    public void move(int xmod, int ymod){
        int nx=x+xmod;
        int ny=y+ymod;
        Cell c1;
        try {
            int index1=-1;
            int index2=-1;
            for(Cell cell:liveCellList){
                if(cell.getX()==x && cell.getY()==y) {index1=liveCellList.indexOf(cell);}
                if(cell.getX()==nx && cell.getY()==ny){index2=liveCellList.indexOf(cell);}
            }
            if(index1 !=-1 && index2 !=-1 &&liveCellList.get(index2).getState() && !liveCellList.get(index2).getWater()) {
             //   liveCellList.get(index1).setPlayer(false);
             //   liveCellList.get(index2).setPlayer(true);
                setCordsPX(nx * cellW, ny * cellW);
            }

            c1 = GridManager.dispArray[nx][ny];
            if (c1.getState()&& !c1.hasWater){
                setCordsPX(nx*cellW, ny*cellW);
                dtMove = 0;
            }
        }catch (IndexOutOfBoundsException e){}
         catch ( NullPointerException e){
             MapStateRender.setHoverText("NONONO",.5f, Color.RED,player.getPX(),player.getPY(),false);
         }
       // lastPressed = c;
        setAim();
    }
    public void useItem(int i){
        if(i>=0 && i<invList.size()) {
            Item item = invList.get(i).get(0);
            if (item.isEquip) {
                Equipment temp = null;
                for (Equipment eq : equipedList) {
                    if (eq.getType().equals(item.getType())) {
                        temp = eq;
                    }
                }
                invList.get(i).remove(0);
                invList.remove(i);
                if (MapStateRender.inventoryPos >= invList.size() - 1) {
                    MapStateRender.inventoryPos = 0;
                }
                if (temp != null) {
                    equipedList.remove(temp);
                    addItemToInventory(temp);
                }

                equipedList.add((Equipment) item);
            }
            else if(item.isSpell){
                SpellBook temp= (SpellBook) invList.get(i).get(0);
                invList.get(i).remove(0);
                player.attackList.add(temp.getAttack());
            }
            else {
                try {
                    try {
                        invList.get(i).remove(0);
                    } catch (IndexOutOfBoundsException e) {
                    }
                    useItem(item);
                } catch (NullPointerException e) {
                }
            }
        }
    }
    public void useItem(Item item){
        String s="";
        if(item.getClass().equals(Gold.class)){
            player.setGold(player.getGold() + item.getValue());
            StatManager.totalGold += item.getValue();
            out(name + " recieved " + item.getValue() + "G");
            MapStateRender.setHoverText(item.getValue()+ "G", .5f, Color.GOLD, getPX(), getPY(), false);
        }
        if (item.getHpmod() != 0) {
            hp += item.getHpmod();
            s ="+" + item.getHpmod()+" HP";
        }
        if (item.getEmod() != 0) {
            energy += item.getEmod();
            s ="+" + item.getEmod()+" E";
        }
        //Mana
        if (item.getManamod() != 0) {
            mana += item.getManamod();
            s = "+" + item.getManamod()+" M";
        }
        //att
        if (item.getAttackmod() != 0) {
            att += item.getAttackmod();
            s = "+" +  item.getAttackmod()+" ATT";
        }
        //def
        if (item.getDefensemod() != 0) {
            def += item.getDefensemod();
            s ="+" +  item.getDefensemod()+" DEF";
        }
        //intel
        if (item.getIntelmod() != 0) {
            intel += item.getIntelmod();
            s = "+" + item.getIntelmod()+" INT";
        }
        //spd
        if (item.getSpeedmod() != 0) {
            spd += item.getSpeedmod();
            s = "+" + item.getSpeedmod()+" SPD";
        }
        MapStateRender.setHoverText(s,.5f,Color.GREEN,px,py,false);
    }
    public void addKills(){
        killCount++;}
    public void updateVariables(float dt){
        dtMove+=dt;
        canMove = dtMove > moveSpeed;
        calculateArmorBuff();
        expLimit=(int)((((Math.pow(1.2,level))*1000)/2)-300);
        if(energy>energyMax)energy=energyMax;
        if(hp>hpMax)hp=hpMax;
        if(mana>manaMax)mana=manaMax;

    }
    public void checkLvlUp() {
        if (exp>=expLimit)
        {
            MapStateRender.setHoverText("--LVL UP--",.8f, Color.GREEN, Game.player.getPX(),Game.player.getPY()-20,true);

            exp=0;

            level++;
            hpMax = hpMax +( 25 + rn.nextInt(20));
            manaMax=manaMax+(25+rn.nextInt(20));
            energyMax=energyMax+(25+rn.nextInt(20));
            manaRegenRate+=1;
            att = att +((int) (Math.random() * 4));
            def = def +((int) (Math.random() * 4));
            intel=intel+((int) (Math.random() * 4));
            spd = spd +((int) (Math.random() * 4));
            abilityPoints++;
            MapStateRender.setHoverText("+1 Ability Point",.7f,Color.GREEN,px,py-40, true);
        }
    }
    public void addItemToInventory(Item item){
        if(item != null) {
            lastItem=item;
            boolean added = false;
            if (item.getClass().equals(Equipment.class)) {
                for (ArrayList<Item> al : invList) {
                    if (!al.isEmpty()) {
                        try {
                            if (al.get(0).getName().equals(item.getName())) {
                                al.add(item);
                                added = true;
                            }
                        } catch (NullPointerException e) {}
                    }
                }
            }
            if (!added) {
                ArrayList<Item> al = new ArrayList<>();
                al.add(item);
                invList.add(al);
            }
        }

    }
    private void calculateArmorBuff() {
        int sum1=0;
        int sum2=0;
        int sum3=0;
        int sum4=0;
        int sum5=0;
        int sum6=0;
        for(Equipment eq : equipedList){
            sum1+=eq.getAttackmod();
        }
        attMod =sum1;
        for(Equipment eq : equipedList){
            sum2+=eq.getDefensemod();
        }
        defMod =sum2;
        for(Equipment eq : equipedList){
            sum3+=eq.getIntelmod();
        }
        intMod =sum3;
        for(Equipment eq : equipedList){
            sum4+=eq.getSpeedmod();
        }
        spdMod =sum4;
        for(Equipment eq : equipedList){
            sum5+=eq.getHpmod();
        }
        hpMod=sum5;
        for(Equipment eq : equipedList){
            sum6+=eq.getManamod();
        }
        manaMod=sum6;
    }
    public boolean checkIfDead() {
        boolean dead=false;
        if(hp<1 && !Tests.nodeath){

            dead=true;
        }

        return dead;
    }
    public double getmDamage() {
        return mDamage;
    }
    public void loadAttacks(){
        attackList.clear();
        Attack flameSp = new Flame();
        Attack stab = new Stab();
        Attack torment= new Torment();
        Attack light= new Lightning();
        Attack quake = new Quake();
        Attack blind= new Blind();
        Attack sac = new Sacrifice();
        attackList.add(flameSp);
        attackList.add(stab);
        attackList.add(sac);
    }
    public void loadIcons(){
        Texture t1 = new Texture(Gdx.files.internal("images/icons/player/playerUp.png"));
        Texture t2 = new Texture(Gdx.files.internal("images/icons/player/playerRight.png"));
        Texture t3 = new Texture(Gdx.files.internal("images/icons/player/playerDown.png"));
        Texture t4 = new Texture(Gdx.files.internal("images/icons/player/playerLeft.png"));
        icons = new Texture[]{t1, t2, t3, t4};

    }

    public int getExpLimit() {
        return expLimit;
    }

    public int getAbilityPoints() {
        return abilityPoints;
    }

    public boolean hasDigPlus() {
        boolean b=false;
        for(Ability a: secondaryAbilityList){
            if(a.getClass().equals(DigPlus.class))
                b= true;
        }
        return b;
    }

    public void setAbilityPoints(int abilityPoints) {
        this.abilityPoints +=abilityPoints;
    }

    public void setxEnergyMax(double xEnergyMax) {
        this.energyMax *= xEnergyMax;
    }
}

