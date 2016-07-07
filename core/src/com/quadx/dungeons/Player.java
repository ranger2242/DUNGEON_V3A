package com.quadx.dungeons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Flame;
import com.quadx.dungeons.attacks.Stab;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpellBook;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.Score;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.liveCellList;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;
import static com.quadx.dungeons.states.mapstate.MapState.lastPressed;
import static com.quadx.dungeons.states.mapstate.MapStateUpdater.setAim;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player {

    public final ArrayList<Attack> attackList = new ArrayList<>();
    public final ArrayList<ArrayList<Item>> invList = new ArrayList<>();
    public final ArrayList<Equipment> equipedList = new ArrayList<>();
    private final ArrayList<String> statsList= new ArrayList<>();
    private final Vector2 pospx=new Vector2(0,0);
    public Item lastItem = null;
    private int x;
    private int px;//(Game.WIDTH/2)+1;
    private int y;
    private int py;//(Game.HEIGHT/2)-2;
    private int hpMod =0;
    public int attackMod=0;
    private int defenseMod=0;
    private int speedMod=0;
    private int manaMod = 0;
    private int manaRegenRate=5;
    private int speed=15;
    private int hpRegen =2;
    private int exp=0;
    private int killCount =0;
    private int energy=100;
    private int energyMax=100;
    private int energyRegen=2 ;
    public int level =1;
    public int floor= 1;
    private int hpMax = 100;
    int intelMod=0;
    private int hp =100;
    int attack=15;
    private int defense=15;
    int intel=15;
    private int mana = 100;
    int manaMax = 100;
    private int damage=0;
    private int ability=0;

    double mDamage=0;

    public boolean canMove=false;
    public boolean safe=false;
    public float dtSafe=0;
    public float dtMove=0;
    private float moveSpeed=.1f;
    private float gold=0;
    private final Random rn =new Random();
    private String name ="DEMO";
    private Texture[] icons=new Texture[4];

    public Player(){
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
    public void setName(String n)
    {
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
        MapState.out(name+" gained "+ expGain +" EXP");
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
    public void setDamage(int d){damage=d;}
    public void setAbility(int o){ ability=o;}

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
        return ((int)gold*10)+(attack*100)+(defense*100)+(speed*100)+(level*2000)+(intel*100)+(hpMax*10)+(manaMax*10)+(energyMax*10)+(floor*1000)+(killCount *200);
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
    public int getDamage(){return damage;}
    public int getAbility(){return ability;}

    public float getMoveSpeed() {
        float f;
        float a=moveSpeed/1000;
        f=moveSpeed-(a*speed);
        if(f<0)f=0;
        return f;
    }
    public String getName()
    {
        return name;
    }
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
        statsList.add("KILLS:    " + killCount);
        statsList.add("GOLD:     " + gold);
        statsList.add("EXP:      " + exp + "/" + (int) ((((Math.pow(1.5, Game.player.getLevel())) * 1000) / 2) - 300));
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
        return new Score( ""+name, ""+getPoints(),""+(int)(gold), AbilityMod.ability.getName() + " Lvl " + level, ""+ killCount);
    }
    public Item getLastItem(){return lastItem;}

    //MISC Functions------------------------------------------------------------------
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
        //attack
        if (item.getAttackmod() != 0) {
            attack += item.getAttackmod();
            s = "+" +  item.getAttackmod()+" ATT";
        }
        //defense
        if (item.getDefensemod() != 0) {
            defense += item.getDefensemod();
            s ="+" +  item.getDefensemod()+" DEF";
        }
        //intel
        if (item.getIntelmod() != 0) {
            intel += item.getIntelmod();
            s = "+" + item.getIntelmod()+" INT";
        }
        //speed
        if (item.getSpeedmod() != 0) {
            speed += item.getSpeedmod();
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
        if(energy>energyMax)energy=energyMax;
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
    public void addItemToInventory(Item item){
        if(item != null) {
            lastItem=item;
            boolean added = false;
            if (!item.isEquip) {
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
    public boolean checkIfDead() {
        boolean dead=false;
        if(hp<1){
            dead=true;
        }

        return dead;
    }

    public double getmDamage() {
        return mDamage;
    }

    public int getFloor() {
        return floor;
    }
    public void loadAttacks(){
        attackList.clear();
        Attack flameSp = new Flame();
        Attack stab = new Stab();
        attackList.add(flameSp);
        attackList.add(stab);
    }
    public void loadIcons(){
        Texture t1 = new Texture(Gdx.files.internal("images/icons/player/playerUp.png"));
        Texture t2 = new Texture(Gdx.files.internal("images/icons/player/playerRight.png"));
        Texture t3 = new Texture(Gdx.files.internal("images/icons/player/playerDown.png"));
        Texture t4 = new Texture(Gdx.files.internal("images/icons/player/playerLeft.png"));
        icons = new Texture[]{t1, t2, t3, t4};

    }

    public int getKillCount() {
        return killCount;
    }

    public int getAttackMod() {
        return attackMod;
    }

    public int getIntelMod() {
        return intelMod;
    }
}
