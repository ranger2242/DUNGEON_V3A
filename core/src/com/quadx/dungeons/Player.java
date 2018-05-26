package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.abilities.Investor;
import com.quadx.dungeons.abilities.WaterBreath;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpellBook;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.modItems.ModItem;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.*;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.ParticleHandler;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.timers.Delta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.Game.rn;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.ImageLoader.pl;
import static com.quadx.dungeons.tools.ImageLoader.statIcons;
import static com.quadx.dungeons.tools.Tests.fastreg;
import static com.quadx.dungeons.tools.Tests.infiniteRegen;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.timers.Time.SECOND;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player {



    private final ArrayList<String> statsList = new ArrayList<>();
    public final ArrayList<Attack> attackList = new ArrayList<>();
    public final ArrayList<Ability> secondaryAbilityList = new ArrayList<>();
    private ArrayList<Line> attackChain = new ArrayList<>();

    public Vector2 kba = new Vector2();
    private final Vector2 pos = new Vector2(0,0);
    private final Vector2 absPos = new Vector2(0, 0);
    private final Vector2 texturePos = new Vector2();
    private Texture[] icons = new Texture[4];
    private ParticleEffect lvlupEffect;
    private Rectangle attackBox = new Rectangle();
    private Circle attackCircle = new Circle();
    private Triangle attackTri = new Triangle();
    private Ability ability = null;
    public Direction.Facing facing = Direction.Facing.North;
    public Item lastItem = null;
    DecimalFormat df = new DecimalFormat("0.00");
    public Inventory inv = new Inventory();

    private static Delta dWater = new Delta(10 * ft);
    private Delta dShopInvScroll = new Delta(10 * ft);
    private Delta dJump = new Delta(2 / 3 * SECOND);
    private Delta dClearHitBox= new Delta(5*ft);
    private Delta dInvincibility= new Delta(.2f*SECOND);

    private int hpMod = 0;
    private int strMod = 0;
    private int defMod = 0;
    private int spdMod = 0;
    private int eMod = 0;
    private int manaMod = 0;
    private int spd = 15;
    private int exp = 0;
    private int killCount = 0;
    private int intMod = 0;
    private int def = 15;
    private int intel = 15;
    private int abilityMod = 0;
    private int str = 15;
    private int expLimit = 0;
    private int abilityPoints = 0;
    private int shopInvPos = 0;
    public int maxSec = 2;
    public int level;
    public int floor = 1;


    public boolean canMove = false;
    public boolean wasHit = false;
    public boolean protect = false;
    public boolean jumping = false;
    private boolean simpleStats = true;
    boolean overrideControls = false;
    private boolean renderEffect = false;

    private float hpMax = barStatGrowthFunction(1);
    private float hp = barStatGrowthFunction(1);
    private float mana = barStatGrowthFunction(1);
    private float manaMax = barStatGrowthFunction(1);
    private float energy = barStatGrowthFunction(1);
    private float energyMax = barStatGrowthFunction(1);
    private float hpMult = 1;
    private float mMult = 1;
    private float eMult = 1;
    private float strMult = 1;
    private float defMult = 1;
    private float intMult = 1;
    private float spdMult = 1;
    private float gold = 0;
    private float velocity = 10;
    private float hpRegenMod = 1;
    private double mRegenMod = 1;
    private double moveSpeed = .1f;

    private String name = "DEMO";
    private String[] statNames= new String[]{
            "HP","M","E","STR","DEF","INT","SPD"
    };


    public Player() {
        //AbilityMod.resetAbilities();
        level = 1;
        //getStatsList();
        fullHeal();
        //secondaryAbilityList.add(new WaterBreath());
    }

    //-----------------------------------------------SETTERS------------------------------------------------------------------
    public void setKnockBackDest(Vector2 initPos) {
        Vector2 vel = Physics.getVector(1, initPos, absPos);
        float force = cellW * 5;

        kba = vel.scl(force);
        float x = absPos.x + vel.x;
        float y = absPos.y + vel.y;
        setAbsPos(new Vector2(x, y));
        gm.clearArea(pos(), true);
    }

    public void setAbsPos(Vector2 a) {
        float max = res * cellW;
        a.x = boundW(a.x, max, getIconDim().x);
        a.y = boundW(a.y, max, getIconDim().y);
        absPos.set(a);
        float x = boundW((float) EMath.round(absPos.x / cellW));
        float y = boundW((float) EMath.round(absPos.y / cellW));
        setPos(new Vector2(x, y));
    }


    public void setPos(Vector2 v) {
        pos.set(v);
    }

    public void setAim(Direction.Facing f) {
        facing = f;
    }

    public void setAttackBox(Rectangle r) {
        attackBox = r;
    }

    public void setAttackCircle(Circle c) {
        attackCircle = c;
    }

    public void setAttackTriangle(Triangle t) {
        attackTri = t;
    }

    public void setAttackChain(ArrayList<Line> c) {
        attackChain = c;
    }

    public void setName(String n) {
        name = n;
        if (name.length() > 19) {
            name = name.substring(0, 19);
        }
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public void setMana(float m) {
        this.mana = m;
    }

    public void setManaMax(int manaMax) {
        this.manaMax = manaMax;
    }

    public void setEnergy(float e) {
        energy = e;
    }

    public void setEnergyMax(int e) {
        energyMax = e;
    }

    public void setStrength(int attack) {
        this.str = attack;
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

    public void setExp(int lvl, float factor) {
        double a = 50.9055;
        double b = 1.0015;
        int gain = (int) (a * Math.pow(b, lvl) * factor);
        new HoverText(gain + " EXP", Color.GREEN, fixed(), false);
        this.exp += gain;
        out(name + " gained " + gain + " EXP");
    }

    public void setGold(float g) {
        gold = g;
    }

    public void setxMoveSpeed(double moveSpeed) {
        this.moveSpeed *= moveSpeed;
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
        this.hpMax *= hpMax;
    }

    public void setxAttack(double attack) {
        this.str *= attack;
    }

    public void setxEnergyMax(double xEnergyMax) {
        this.energyMax *= xEnergyMax;
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

    public void setAbilityMod(int o) {
        abilityMod = o;
    }

    public void setAbility(Ability a) {
        ability = a;
    }

    public void setAbilityPoints(int abilityPoints) {
        this.abilityPoints += abilityPoints;
    }

    //ADDERS------------------------------------------------------------------
    public void addHp(int hp) {
        this.hp += hp;
    }

    public void addMana(int m) {
        this.mana += m;
    }

    public void addEnergy(int e) {
        energy += e;
    }

    public void addGold(int g) {
        gold += g;
    }

    public void addGold(Gold g) {
        addGold(g.getValue());
        out(g.getValue() + " added to stash");
        StatManager.totalGold += g.getValue();
        new HoverText(g.getValue() + "G",  Color.GOLD, player.fixed(), false);
        lastItem = g;
        HUD.setLootPopup(g.getIcon());

    }

    public void addKills() {
        killCount++;
    }

    //GETTERS------------------------------------------------------------------
    @SuppressWarnings("WeakerAccess")

    //====BOOLS------------------------------------------------------------------
    public boolean haveAbility(Class cls) {
        return secondaryAbilityList.stream().anyMatch(x -> x.getClass().equals(cls));
    }

    @SuppressWarnings("WeakerAccess")
    public boolean notHaveAbility(Class cls) {
        return !haveAbility(cls);
    }

    public boolean isInvEmpty() {
        return inv.getList().isEmpty();
    }

    public boolean attackOverlaps(Rectangle hitBox) {
        Attack.HitBoxShape hbs = getAttack().getHitBoxShape();
        switch (hbs) {
            case Circle:
                if (getAttackCircle().overlaps(hitBox))
                    return true;
            case Rect:
                if (getAttackBox().overlaps(hitBox))
                    return true;
            case Chain:
                break;
            case Triangle:
                if (getAttackTriangle().overlaps(hitBox))
                    return true;
            case None:
                break;
        }

        return false;
    }

    public boolean isDead(GameStateManager gsm) {
        boolean dead = false;
        if (hp < 1 && !Tests.nodeath) {
            HighScoreState.pfinal = player;
            HighScoreState.addScore(player.getScore());
            StatManager.pScore = player.getScore();
            player = null;
            player = new Player();
            AbilitySelectState.pressed = false;
            inGame = false;
            AttackMod.resetAttacks();
            gsm.push(new HighScoreState(gsm));
            dead = true;
        }

        return dead;
    }

    public boolean isSimpleStatsEnabled() {
        return simpleStats;
    }

    public boolean hasAP() {
        return getAbilityPoints() != 0;
    }

    public boolean canUseAttack() {
        Attack a = getAttack();
        switch (a.getType()){
            case Energy:
                return player.getEnergy() >= a.getCost();
            case Mana:
                return player.getMana() >= a.getCost();
        }
        return false;
    }
    public boolean canBuy(Item item) {
        return Game.player.getGold() >=item.getCost();
    }


    //====INTS------------------------------------------------------------------

    public int getLevel() {
        return level;
    }

    public int getGold() {
        return (int) gold;
    }


    public int getPoints() {
        return (int) ((int) (
                (gold * 10)
                        + (getStrComp() * 100)
                        + (getDefComp() * 100)
                        + (getSpdComp() * 100)
                        + (getIntComp() * 100)
                        + (level * 2000)
                        + (hpMax * 10)
                        + (manaMax * 10)
                        + (energyMax * 10)
                        + (floor * 1000)
                        + (killCount * 200)
        ) * multiplier);
    }

    public int getAbilityMod() {
        return abilityMod;
    }

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

    public int getStrength() {
        return str;
    }

    public int getDefense() {
        return def;
    }

    public int getSpeed() {
        return spd;
    }

    public int getIntel() {
        return intel;
    }

    private int getHpComp() {
        return (int) (hpMax * hpMult + hpMod);
    }

    private int getMComp() {
        return (int) (manaMax * mMult + manaMod);
    }

    private int getEComp() {
        return (int) (energyMax * eMult + eMod);
    }

    private int getStrComp() {
        return (int) (str * strMult + strMod);
    }

    public int getDefComp() {
        return (int) (def * defMult + defMod);
    }

    public int getIntComp() {
        return (int) (intel * intMult + intMod);
    }

    private int getSpdComp() {
        return (int) (spd * spdMult + spdMod);
    }

    private int barStatGrowthFunction(int level) {
        return (int) (45 * Math.pow(Math.E, .25 * (level - 1) / 2) + 100);
    }

    public int invSize(){
        return inv.getList().size();
    }
    //====FLOATS------------------------------------------------------------------
    private float regenGrowthFunction(int level, int stat) {

        float rate = level * (level / 192f) + (stat / 3650f) + .25f;
        float g = (60 * ft) * rate;
        return g;
    }

    public float getHp() {
        return hp;
    }

    public float getHpMax() {
        return hpMax;
    }

    public float getEnergy() {
        return energy;
    }

    public float getEnergyMax() {
        return energyMax;
    }

    public float getManaMax() {
        return manaMax;
    }

    public float getMana() {
        return mana;
    }

    private float getHpRegen() {
        return regenGrowthFunction(level, getDefComp() / 2);
    }

    private float getManaRegen() {
        return regenGrowthFunction(level, getIntComp());
    }

    private float getEnergyRegen() {
        return regenGrowthFunction(level, getStrComp());
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    //====OTHER------------------------------------------------------------------
    public String getName() {
        return name;
    }

    private Rectangle getAttackBox() {
        //return new Rectangle(attackBox.x,,attackBox.width,attackBox.height);
        return attackBox;
    }

    private Triangle getAttackTriangle() {
        return attackTri;
    }

    private Circle getAttackCircle() {
        return attackCircle;
    }

    public ArrayList<Line> getAttackChain() {
        return attackChain;
    }

    public Attack getAttack() {
        return attackList.get(Attack.pos);
    }

    public Illusion.Dummy getDummy() {
        return new Illusion.Dummy((int) getHpMax() * 2, pos(), abs(), getHitBox());
    }

    public Rectangle getHitBox() {
        return new Rectangle(absPos.x, GridManager.fixY(absPos), getIcon().getWidth(), getIcon().getHeight());
    }

    public Vector2 abs() {
        /*if (absPos.y + jump() > absPos.y)
            return new Vector2(absPos.x, absPos.y + jump());
        else*/
            return new Vector2(absPos.x, absPos.y);
    }

    public Vector2 getTexturePos() {
        texturePos.set(abs().x, GridManager.fixY(abs()));
        return texturePos;
    }

    public Vector2 pos() {
        return pos;
    }
    public Vector2 fixed() {
        return getFixPos();
    }


    public ArrayList<Ability> getSecondaryAbilityList() {
        return secondaryAbilityList;
    }

    public ArrayList<String> getStatsList() {
        statsList.clear();

        statsList.add(name);
        statsList.add("Level " + level);

        if (simpleStats) {
            statsList.add(": " + (int) hp + "/" + getHpComp() + " :" + df.format(getHpRegen()));
            statsList.add(": " + (int) mana + "/" + getMComp() + " :" + df.format(getManaRegen()));
            statsList.add(": " + (int) energy + "/" + getEComp() + " :" + df.format(getEnergyRegen()));
            statsList.add(": " + getStrComp());
            statsList.add(": " + getDefComp());
            statsList.add(": " + getIntComp());
            statsList.add(": " + getSpdComp());
/*            statsList.add("HP : " + getHpComp()+"/" + (int) hpMax);
            statsList.add("M  : " + getMComp()+ "/" + (int) manaMax);
            statsList.add("E  : " + getEComp()+ "/" + (int) energyMax);
            statsList.add("STR: " + getStrComp());
            statsList.add("DEF: " + getDefComp());
            statsList.add("INT: " + getIntComp());
            statsList.add("SPD: " + getSpdComp());*/

        } else {
            statsList.add("HP   " + (int) (hp * hpMult) + "/" + (int) hpMax + " +" + hpMod + ": " + getHpComp());
            statsList.add("M    " + (int) (mana * mMult) + "/" + (int) manaMax + " +" + manaMod + ": " + getMComp());
            statsList.add("E    " + (int) (energy * eMult) + "/" + (int) energyMax + " +" + eMod + ": " + getEComp());
            statsList.add("STR: " + (int) (str * strMult) + " +" + strMod + ": " + getStrComp());
            statsList.add("DEF: " + (int) (def * defMult) + " +" + defMod + ": " + getDefComp());
            statsList.add("INT: " + (int) (intel * intMult) + " +" + intMod + ": " + getIntComp());
            statsList.add("SPD: " + (int) (spd * spdMult) + " +" + spdMod + ": " + getSpdComp());
        }
        statsList.add("AP:    " + abilityPoints);
        statsList.add("KILLS: " + killCount);
        statsList.add("GOLD:  " + (int) gold);
        statsList.add("EXP:   " + exp + "/" + expLimit);
        statsList.add("FLOOR: " + floor);
        return statsList;
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

    private Score getScore() {
        return new Score("" + name, "" + getPoints(), "" + (int) (gold), player.getAbility().getName() + " " + ability.getLevel() + " Lvl " + level, "" + killCount);
    }

    public Ability getAbility() {
        return ability;
    }

    public Vector2 getIconDim() {
        Texture ic = getIcon();
        return new Vector2(ic.getWidth(), ic.getHeight());
    }

    public Vector2 getFixPos() {
        return new Vector2(absPos.x, fixY(absPos));
    }

    private Cell getStandingTile() {
        if (GridManager.isInBounds(pos()))
            return dispArray[(int) pos().x][(int) pos().y];
        else return new Cell();
    }





    //-----------------------------------------------MISC------------------------------------------------------------------
    public void discard() {
        inv.discard(pos(),true,null);
    }

    public void useAttack(Attack a) {
        int c=a.getCost();
        switch (a.getType()){
            case Energy:
                addEnergy(-c);
                break;
            case Mana:
                addMana(-c);
                break;
        }
        setHitBoxShape(a);
    }

    private void setHitBoxShape(Attack a){
        Attack.HitBoxShape hbs=a.getHitBoxShape();
        switch (hbs) {
            case Rect:
                setAttackBox(a.getHitBox());
                break;
            case Chain:
                setAttackChain(a.getHitChainList());
                break;
            case Triangle:
                setAttackTriangle(a.getHitTri());
                break;
            case None:
                a.runAttackMod();
                break;
        }

    }

    public void unequip(int x){
        if(inv.ready())
            pickupItem(inv.unequipSlot(x));
    }

    public void scrollItems(boolean b){
        inv.scroll(b);
    }



    private void calcVeloctiy() {
        float v = (float) (6 + .0136 * getSpdComp() + .000005 * Math.pow(getSpdComp(), 2));
        if (Dash.active)
            v *= 6;
        v=bound((int) v,5,18);
        velocity = v;
    }

    private void regenModifiers() {
        if (!fastreg) {
            hp += getHpRegen();
            if (hp > hpMax) hp = hpMax;
            mana += getManaRegen();
            if (mana > manaMax) mana = manaMax;
            energy += getEnergyRegen();
            if (energy > energyMax) energy = energyMax;
        } else {
            energy = energyMax;
            mana = manaMax;
            hp = hpMax;
        }
    }

    private void regenPlayer() {
        if (infiniteRegen) {
            fullHeal();
        } else {
            regenModifiers();
        }
    }

    private void calculateArmorBuff() {
        int sum1 = 0,sum2 = 0,sum3 = 0
        ,sum4 = 0,sum5 = 0,sum6 = 0;
        for (Equipment eq : inv.getEquiped()) {
            sum1 += eq.getStrmod();
            sum2 += eq.getDefensemod();
            sum3 += eq.getIntelmod();
            sum4 += eq.getSpeedmod();
            sum5 += eq.getHpmod();
            sum6 += eq.getManamod();
        }
        strMod = sum1;
        defMod = sum2;
        intMod = sum3;
        spdMod = sum4;
        hpMod = sum5;
        manaMod = sum6;
    }

    public void fullHeal() {
        setHp(getHpMax());
        setMana(getManaMax());
        setEnergy(getEnergyMax());
    }

    public void boundStatBars() {
        hp=bound(hp,hpMax);
        mana=bound(mana,manaMax);
        energy=bound(energy,energyMax);
    }



    public void equipSpell(Item item){
        player.attackList.add(((SpellBook) item).getAttack());
    }

    public void addItemMods(int[] arr) {
        hp += arr[0];
        mana += arr[1];
        energy += arr[2];
        str += arr[3];
        def += arr[4];
        intel += arr[5];
        spd += arr[6];
        for(int i=0;i<7;i++){
            if (arr[i] != 0) {
                String s = "+" + arr[i] + " "+statNames[i];
                new HoverText(s,  Color.GREEN, fixed(),false);

            }
        }
    }

    //#####MOVE TO INVENTORY
    public void useItem(){
       if(inv.ready()){
           inv.useItem(this);
       }
    }


    public void useItem(Item item) {
        int[] mods = ((ModItem) item).runMod();
        addItemMods(mods);
        if (item.getClass().equals(Gold.class)) {
            player.setGold(player.getGold() + item.getValue());
            StatManager.totalGold += item.getValue();
            out(name + " recieved " + item.getValue() + "G");
            new HoverText(item.getValue() + "G", Color.GOLD, fixed(), false);
        }
    }

    public void pickupItem(Item item) {
        item.loadIcon();
        if (isInvEmpty()) {
            Inventory.pos = 0;
        }
        if (item.isGold()) {
            addGold((Gold) item);
        } else {
            lastItem = item;
            boolean added = false;
            for (ArrayList<Item> al : inv.getList()) {
                if (!al.isEmpty()) {
                    try {
                        if (al.get(0).getName().equals(item.getName())) {
                            al.add(item);
                            added = true;
                        }
                    } catch (NullPointerException ignored) {
                    }
                }
            }

            if (!added) {
                inv.addItem(item);
            }

            out(item.getName() + " added to inventory");
            new HoverText(item.getName(), Color.WHITE, fixed(), false);
            StatManager.totalItems++;
            HUD.setLootPopup(item.getIcon());
        }

    }
    //#######
    public void initPlayer() {
        //load icons
        icons = new Texture[]{pl[0], pl[1], pl[2], pl[3]};
        //load attacks
        attackList.clear();

        ability.onActivate();
        attackList.add(new Dash());

        fullHeal();
    }

    public void checkLvlUp() {
        if (exp >= expLimit) {
            Vector2 p=new Vector2(fixed());
            p.add(0,-40);
            new HoverText("--LVL UP--", Color.GREEN, p, true);
            exp = 0;
            level++;
            hpMax = barStatGrowthFunction(level) * 2;
            manaMax = barStatGrowthFunction(level);
            energyMax = barStatGrowthFunction(level);
            str += rn.nextInt(15);
            def += rn.nextInt(15);
            intel +=rn.nextInt(15);
            spd +=rn.nextInt(15);
            expLimit = (int) ((((Math.pow(1.2, level)) * 1000) / 2) - 300);

            abilityPoints++;
            p.add(0,-40);
            new HoverText("+1 Ability Point", Color.GREEN,p, true);
            lvlupEffect = ParticleHandler.loadParticles("ptFlame", absPos);
            renderEffect = true;
            lvlupEffect.start();
            fullHeal();
        }
    }

    public void addAllAttacks() {
        attackList.clear();
        attackList.add(new Flame());
        attackList.add(new Dash());
        attackList.add(new Illusion());
        attackList.add(new Lightning());
        attackList.add(new Quake());
        attackList.add(new Protect());
        attackList.add(new Focus());
        attackList.add(new Stab());
    }

    public void toggleSimpleStats() {
        simpleStats = !simpleStats;
    }

    public void forceLevelUp() {
        exp += expLimit;
        checkLvlUp();
    }

    public void addShopInvOffset(int i) {
        if (dShopInvScroll.isDone()) {
            shopInvPos += i;
            dShopInvScroll.reset();
        }
    }

    private void clearHitBox(){
        if (dClearHitBox.isDone()) {
            attackBox =new Rectangle(0, 0, 0, 0);
            attackCircle = new Circle();
            attackChain = new ArrayList<>();
            attackTri = new Triangle();
            dClearHitBox.reset();
        }
    }

    private void attackCollision() {
        try {
            for (Monster m : monsterList) {
                if (attackOverlaps(m.getHitBox())) {
                    m.takeDamage();
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }
    private void invincibilityFrames(float dt) {
        if(wasHit){
            if(dInvincibility.isDone()){
                wasHit = false;
                dInvincibility.reset();
            }else
                dInvincibility.update(dt);
        }
    }

    //UPDATE METHODS------------------------------------------------
    public void update(float dt, Class cls) {
        if (cls.equals(MapState.class)) {
            updateMapState(dt);
        }
    }

    public void updateMapState(float dt) {
        inv.update(dt);
        dClearHitBox.update(dt);
        if (renderEffect)
            lvlupEffect.update(dt);

        swim(dt);
        dig();
        jump(dt);
        clearHitBox();
        invincibilityFrames(dt);
        calculateArmorBuff();
        calcVeloctiy();
        attackCollision();
        worldCollision();
        regenPlayer();
        boundStatBars();

        Investor.generatePlayerGold();
        isDead(MapState.gsm);
    }

    public void updateShopState(float dt) {
        dShopInvScroll.update(dt);

    }

    private void worldCollision() {
        ArrayList<Cell> list = GridManager.getSurroundingCells(pos(), 1);
        list.removeIf(x -> !(x.hasItem() || x.isWarp() || x.isShop()));
        for (Cell c : list) {
            if (c.hasItem()) {
                out("ITEM");
                Item item = c.getItem();
                if (item != null)
                    item.colliion(c.pos());
            }
            if (c.isWarp()) {
                MapState.warpToNext(true);
            }
            if (c.isShop()) {
                MapState.warpToShop();
                c.setCleared();
            }
        }
    }


    //RENDER METHODS------------------------------------------------
    public void render(SpriteBatch sb) {
        renderEffect(sb);
    }

    private void renderEffect(SpriteBatch sb) {
        if (renderEffect) {
            lvlupEffect.draw(sb);
            if (lvlupEffect.isComplete())
                lvlupEffect.dispose();
        }
    }

    public void renderStatList(SpriteBatch sb, Vector2 pos) {
        Game.setFontSize(1);
        Game.getFont().setColor(Color.WHITE);
        Vector2[] v = HUD.generateStatListPos(pos);
        ArrayList<String> stats = getStatsList();
        for (int i = 0; i < stats.size(); i++) {
            //draw all stat icons here
            float x = v[i].x;
            float y = v[i].y;
            if (i > 1 && i < statIcons.size())
                sb.draw(statIcons.get(i), x - 16, y - 14);
            Game.font.draw(sb, stats.get(i), x, y);
        }

        ArrayList<String> a = player.getStatsList();
        if (HUD.statsPos != null) {

            for (int i = 0; i < a.size(); i++) {
                if (Inventory.statCompare != null && i - 2 < Inventory.statCompare.length && i - 2 >= 0) {
                    switch (Inventory.statCompare[i - 2]) {
                        case 1: {
                            Game.font.setColor(Color.BLUE);
                            break;
                        }
                        case 2: {
                            Game.font.setColor(Color.RED);
                            break;
                        }
                        default: {
                            Game.font.setColor(Color.WHITE);
                            break;
                        }
                    }
                } else {
                    Game.font.setColor(Color.WHITE);
                }
                Game.getFont().draw(sb, a.get(i), v[i].x, v[i].y);
            }

        }

    }

    public void renderShopInventory(SpriteBatch sb) {
        BitmapFont font = Game.getFont();
        float size = invSize();
        float end = size >= 8 ? 8 : size;
        for (int i = 0; i < end; i++) {
            float x = scrx(2f / 3f);
            float y = scry(.85f - (i * .1f));


            float ind = (i + shopInvPos);
            if (ind < 0) {
                ind = size - 1;
                shopInvPos = (int) (size - 1);
            } else {
                ind %= size;
                shopInvPos %= size;
            }
            ArrayList<Item> itemStack = inv.getStack((int) ind);
            Item item = itemStack.get(0);
            String qty = "x" + itemStack.size();
            String name = (i + 1) + ". " + item.getName();
            String debug = (1 + (int) ind) + "/" + (int) size + " SELL:" + item.getSellPrice();


            font.draw(sb, name, x, y);
            font.draw(sb, qty, scrx(9f / 10f), y);
            font.draw(sb, debug, x, y - 20);
            //font.draw(sb, "_________________________________", x, y - 22);


            sb.draw(item.getIcon(), x, y + 10);
        }
        float per = shopInvPos / size;
        font.draw(sb, "_", scrx(.95f), scry(.86f));

        font.draw(sb, "|", scrx(.95f), scry(.85f - (.79f * per)));
        font.draw(sb, "_", scrx(.95f), scry(.85f - (.79f * ((size - 1) / size))));

    }

    public void renderAttackbox(ShapeRendererExt sr) {
        Attack.HitBoxShape hbs = getAttack().getHitBoxShape();
        if (hbs != null)
            switch (hbs) {

                case Circle:
                    Circle c = getAttackCircle();
                    sr.circle(c.center.x, c.center.y, c.radius);
                    break;
                case Rect:
                    sr.rect(getAttackBox());
                    break;
                case Triangle:
                    sr.triangle(getAttackTriangle());
                    break;
            }
    }

    //MOVEMENT------------------------------------------------------
    private float jump() {
        return 10; //(float) ((625 * dtJump) - (927.5 * Math.pow(dtJump, 2)));
    }

    private void jump(float dt) {
        //jumping=dJump.triggerUpdate(dt,jumping);
    }

    public void move(Vector2 vel) {
        if (!overrideControls) {
            try {
                float dt = Gdx.graphics.getDeltaTime();
                Vector2 abs = abs();
                Vector2 end = new Vector2(abs.x + (vel.x * dt), abs.y + (vel.y * dt));//endpoint of direction vector
                int gridW = cellW * (res + 1);
                float iw = getIcon().getWidth(),
                        ih = getIcon().getHeight();
                float c1 = end.x,
                        c2 = end.x + iw,
                        c3 = end.y,
                        c4 = end.y + ih;


                if (c1 < 0) {
                    end.x = gridW;
                } else if (c2 > gridW) {
                    end.x = iw;
                }


                if (c3 < 0) {
                    end.y = gridW;
                } else if (c4 > gridW) {
                    end.y = ih;
                }

                Vector2 comp = Physics.getVector(velocity, absPos, end);//get movement vector

                if (canMove) {
                    Vector2 start = new Vector2(abs());
                    start.add(comp);
                    player.setAbsPos(start);

                }
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
    }

    public void dig() {
        if (getStandingTile().isWall()) {
            int digCost=20;
            if (energy > digCost) {
                gm.clearArea(pos, true);
                addEnergy(-digCost);
            } else {
                canMove = false;
                new HoverText("-!-", SECOND, Color.YELLOW, player.abs(),true);
            }
        } else
            canMove = true;

    }

    private void swim(float dt) {
        dWater.update(dt);
        if (getStandingTile().isWater() &&
                notHaveAbility(WaterBreath.class) && dWater.isDone()) {
            player.addHp(-40);
            dWater.reset();
        }
    }





}


