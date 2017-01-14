package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.abilities.DigPlus;
import com.quadx.dungeons.abilities.Investor;
import com.quadx.dungeons.abilities.WaterBreath;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpellBook;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.*;

import java.util.ArrayList;
import java.util.Random;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.GridManager.res;
import static com.quadx.dungeons.states.mapstate.MapState.*;

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
    private Vector2 absPos =new Vector2(0,0);
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
    private double mRegen =5;
    private int spd =15;
    private double hpRegen =2;
    private int exp=0;
    private int killCount =0;
    private int energy=100;
    private int energyMax=100;
    private double eRegen =2 ;
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
    float hpRegenMod=1;
    private float dtRegen = 0;
    private float dtEnergyRe = 0;
    public float dtSafe=0;
    public float dtMove=0;
    private double moveSpeed=.1f;
    private float gold=0;
    private final Random rn =new Random();
    private String name ="DEMO";
    private Texture[] icons=new Texture[4];
    private double mRegenMod=1;
    private double eRegenMod=1;
    private double moveMod=1;
    Vector2 texturePos=new Vector2();
    Vector2[] statsPos;
    Vector2 dest=new Vector2();
    Rectangle attackBox= new Rectangle();
    private float velocity=10;
    public Direction.Facing facing = Direction.Facing.North;
    boolean overrideControls=false;

    public Player() {
        //AbilityMod.resetAbilities();
        level=1;
        getStatsList();
        statsPos= new Vector2[statsList.size()];
        setStatsPos();
    }
    //SETTERS------------------------------------------------------------------
    public void setDest(Vector2 v){
        Vector2 comp=Physics.getVxyComp(1,absPos,v);
        Vector2 neg=new Vector2(-comp.x,-comp.y);
        float kick=cellW*4;
        dest.set(absPos.x+ neg.x*kick,absPos.y+neg.y*kick);
        Anim a=new Anim(getIcon(),absPos,kick,dest,2,.3f);
        MapStateUpdater.anims.add(a);

        overrideControls=true;
    }
    public void forceMove(Vector2 dest){

    }
    public void setAttackBox(Rectangle r){
        attackBox=r;
    }
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
    public void setExp(int lvl, float factor) {
        double a=65.9055;
        double b=1.17958;
        int gain= (int) (a*Math.pow(b,lvl)*factor);
        MapStateRender.setHoverText(gain +" EXP",.8f, Color.GREEN ,Game.player.getPX(),Game.player.getPY()+10,false);
        this.exp+=gain;
        out(name+" gained "+ gain +" EXP");
    }
    public void setCordsPX(int i, int i1) {
        absPos.set(i,i1);
        px=i;
        py=i1;
    }
    public void setMoveSpeed(float moveSpeed) {
        this.moveMod = moveSpeed;
    }
    public void seteRegen(int i) {
        eRegen =i;
    }
    public void setManaMax(int manaMax) {
        this.manaMax = manaMax;
    }
    public void setManaRegen(int manaRegen) {
        this.mRegen = manaRegen;
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
        eRegenMod *=i;
    }
    public void setxManaMax(double manaMax) {
        this.manaMax *= manaMax;
    }
    public void setxManaRegen(double manaRegen) {
        this.mRegenMod *= mRegenMod;
    }
    public void setxHpRegen(double hpRegen) {
        this.hpRegenMod *= hpRegenMod;
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
    public void setAbilityPoints(int abilityPoints) {
        this.abilityPoints +=abilityPoints;
    }
    public void setxEnergyMax(double xEnergyMax) {
        this.energyMax *= xEnergyMax;
    }
    void setStatsPos(){
        for(int i=0;i<statsList.size();i++){
            statsPos[i]=new Vector2(viewX+30,viewY+HEIGHT - 30 - (i * 20));
        }
    }
    public void setAbsPos(Vector2 a){
        absPos.set(a);
        px = (int) EMath.round(a.x);
        py = (int) EMath.round(a.y);
        setPos(new Vector2((int) (EMath.round(absPos.x / cellW)), (int) (EMath.round(absPos.y / cellW))));
    }
    public void setPos(Vector2 v){
        x= (int) v.x;
        y=(int) v.y;
    }
    public void fixPosition(){
        int x= (int) absPos.x;
        int y= (int) absPos.y;
        if(absPos.x<2){
            x=2;
        }
        else if(absPos.x+getIcon().getWidth()>(res*cellW)-2){
            x=(res*cellW)-(getIcon().getWidth()-2);
        }
        if(absPos.y<2){
            y=2;
        }
        else if(absPos.y+getIcon().getHeight()>(res*cellW)-2){
            y=(res*cellW)-(getIcon().getHeight()-2);
        }
        setAbsPos(new Vector2(x,y));//terminal test
    }
    //GETTERS------------------------------------------------------------------

    public Rectangle getAttackBox() {
        return attackBox;
    }
    public Rectangle getHitBox() {
        return new Rectangle(absPos.x,absPos.y,getIcon().getWidth(),getIcon().getHeight());
    }

    public Vector2 getAbsPos(){
        return absPos;
    }
    public Vector2 getTexturePos(){
        return texturePos;
    }
    public Vector2[] getStatPos(){
        return statsPos;
    }
    public Vector2 getPos(){return new Vector2(x,y);}
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
    public double getmRegen(){
        return mRegen;
    }
    public double getHpRegen(){return hpRegen;}
    public double geteRegen() {
        return eRegen;
    }
    public int getManaMax() {
        return manaMax;
    }
    public int getAbilityMod(){return abilityMod;}
    public int getKillCount() {
        return killCount;
    }
    public int getFloor() {
        return floor;
    }
    public int getExpLimit() {
        return expLimit;
    }
    public int getAbilityPoints() {
        return abilityPoints;
    }
    public double getMoveSpeed() {
        return moveSpeed;
    }
    public double getmDamage() {
        return mDamage;
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
    public Texture getIcon() {
        int u;

        switch (facing) {
            case North:
                u = 0;
                break;
            case Northwest:
                u = 0;
                break;
            case West:
                u = 3;
                break;
            case Southwest:
                u = 2;
                break;
            case South:
                u = 2;
                break;
            case Southeast:
                u = 2;
                break;
            case East:
                u = 1;
                break;
            case Northeast:
                u = 0;
                break;
            default:
                u = 0;
                break;
        }

        /*

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

        */
        return icons[u];
    }
    public Score getScore(){
        return new Score( ""+name, ""+getPoints(),""+(int)(gold), player.getAbility().getName()+" "+ability.getLevel() + " Lvl " + level, ""+ killCount);
    }
    public Item getLastItem(){return lastItem;}
    public Ability getAbility(){return ability;}
    //MISC Functions------------------------------------------------------------------
    public void regenModifiers(){
        hpRegen=(2*Math.pow(1.004,(getSpdComp()+getDefComp())/2)*hpRegenMod);
        hp+=hpRegen;
        if(hp>hpMax)hp=hpMax;

        mRegen=(5*Math.pow(1.004,(getSpdComp()+getIntComp())/2)*mRegenMod);
        mana+=mRegen;
        if(mana>manaMax)mana=manaMax;

        eRegen=(5*Math.pow(1.004,(getSpdComp()+getAttComp())/2)*eRegenMod);
        energy+=eRegen;
        if(energy>energyMax)energy=energyMax;
    }
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
    public void move(Vector2 vel){
        if(!overrideControls) {
            try {
                Vector2 end = new Vector2(absPos.x + vel.x, absPos.y + vel.y);
                int gw = cellW * (res + 1);
                if (end.x < 0)
                    end.x = getIcon().getWidth();
                else if (end.x + getIcon().getWidth() > gw)
                    end.x = (gw) - getIcon().getWidth();
                if (end.y < 0)
                    end.y = getIcon().getHeight();
                else if (end.y + getIcon().getHeight() > gw)
                    end.y = (gw) - getIcon().getHeight();
                Vector2 comp = Physics.getVxyComp(velocity, absPos, end);
                int x = (int) (EMath.round(absPos.x / cellW));
                int y = (int) (EMath.round(absPos.y / cellW));

                Cell c = dispArray[x][y];
                if (c.getState() && !c.hasWater) {
                    player.setPos(new Vector2(x, y));
                    player.setAbsPos(new Vector2(absPos.x + comp.x, absPos.y + comp.y));
                }
                if (c.getState() && c.hasWater) {
                    for (Ability a : player.secondaryAbilityList) {
                        if (a.getClass().equals(WaterBreath.class)) {
                            player.setPos(new Vector2(x, y));
                            player.setAbsPos(new Vector2(absPos.x + comp.x, absPos.y + comp.y));
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
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

        dtEnergyRe+=dt;
        dtMove+=dt;
        canMove = true;
        calculateArmorBuff();
        expLimit=(int)((((Math.pow(1.2,level))*1000)/2)-300);
        velocity= (float) (6+.0136*getSpdComp()+.000005* Math.pow(getSpdComp(),2));
        if(velocity<5)velocity=5;
        if(velocity>18)velocity=18;
        //velocity= (float) (.5*Math.pow(1.00005,(getSpdComp()))*getSpdComp());
        //if(moveSpeed<1.03)moveSpeed=1.03;
        //velocity=5;
        regenPlayer(dt);
        //set texture cords
        texturePos.set(absPos.x - getIcon().getWidth() / 4, absPos.y - getIcon().getHeight() / 4);
        setStatsPos();
        fixPosition();
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
            mRegen +=1;
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
    public boolean checkIfDead(GameStateManager gsm) {
        boolean dead=false;
        if(hp<1 && !Tests.nodeath){
            HighScoreState.pfinal=player;
            HighScoreState.addScore(player.getScore());
            StatManager.pScore=player.getScore();
            player = null;
            player = new Player();
            AbilitySelectState.pressed = false;
            inGame = false;
            AttackMod.resetAttacks();
            gsm.push(new HighScoreState(gsm));
            dead=true;
        }

        return dead;
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
        Texture t1 = new Texture(Gdx.files.internal("images/icons/player/00.png"));
        Texture t2 = new Texture(Gdx.files.internal("images/icons/player/03.png"));
        Texture t3 = new Texture(Gdx.files.internal("images/icons/player/01.png"));
        Texture t4 = new Texture(Gdx.files.internal("images/icons/player/02.png"));
        icons = new Texture[]{t1, t2, t3, t4};

    }
    boolean hasDigPlus() {
        boolean b=false;
        for(Ability a: secondaryAbilityList){
            if(a.getClass().equals(DigPlus.class))
                b= true;
        }
        return b;
    }
    void regenPlayer(float dt) {
        if(!infiniteRegen) {
            dtRegen += dt; //move all this shit to PLAYER
            if (safe) dtSafe += dt;
            if (dtRegen > .3) {
                regenModifiers();
                if (getAbility().getClass().equals(Investor.class))
                    Investor.generatePlayerGold();
                dtRegen = 0;
            }
            attackList.stream().filter(a -> a.getMod() == 6 && (dtSafe > a.getLevel())).forEach(a -> {
                safe = false;
                dtSafe = 0;
            });
        }else{
            setHp(getHpMax());
            setMana(getManaMax());
            setEnergy(getEnergyMax());
        }
    }
}

