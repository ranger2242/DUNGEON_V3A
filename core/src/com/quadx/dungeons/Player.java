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
import com.quadx.dungeons.physics.Body;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.mapstate.MapStateExt;
import com.quadx.dungeons.tools.*;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.shapes.Circle;
import com.quadx.dungeons.tools.shapes.Line;
import com.quadx.dungeons.tools.shapes.Triangle;
import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.Game.rn;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.ImageLoader.pl;
import static com.quadx.dungeons.tools.ImageLoader.statIcons;
import static com.quadx.dungeons.tools.Tests.fastreg;
import static com.quadx.dungeons.tools.Tests.infiniteRegen;
import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player {
    private static Delta dWater = new Delta(10 * ft);
    private Delta dShopInvScroll = new Delta(10*ft);


    private final ArrayList<String> statsList = new ArrayList<>();
    public final ArrayList<Attack> attackList = new ArrayList<>();
    public final ArrayList<ArrayList<Item>> invList = new ArrayList<>();
    public final ArrayList<Equipment> equipedList = new ArrayList<>();
    public final ArrayList<Ability> secondaryAbilityList = new ArrayList<>();

    private final Vector2 absPos = new Vector2(0, 0);
    private final Vector2 texturePos = new Vector2();
    private final Vector2 dest = new Vector2();

    private Texture[] icons = new Texture[4];

    private ParticleEffect lvlupEffect;

    private ArrayList<Line> attackChain = new ArrayList<>();
    private Rectangle attackBox = new Rectangle();
    private Circle attackCircle = new Circle();
    private Triangle attackTri = new Triangle();

    private Ability ability = null;
    public Direction.Facing facing = Direction.Facing.North;
    public Item lastItem = null;

    private int x;
    private int y;
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
    private int shopInvPos=0;
    public int maxSec = 2;
    public int level;
    public int floor = 1;


    public boolean canMove = false;
    public boolean wasHit = false;
    public boolean safe = false;
    public boolean jumping = false;
    private boolean simpleStats=true;
    boolean overrideControls = false;
    private boolean renderEffect=false;

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
    private float dtClearHit = 0;
    private float gold = 0;
    private float velocity = 10;
    private float dtSafe = 0;
    private float dtHitInvincibility = 0;
    private float hpRegenMod = 1;
    private float dtJump = 0;
    public float dtMove = 0;
    private float lastDt=0;

    private double mRegenMod = 1;
    private double moveSpeed = .1f;

    private String name = "DEMO";


    public Player() {
        //AbilityMod.resetAbilities();
        level = 1;
        //getStatsList();
        fullHeal();
        //secondaryAbilityList.add(new WaterBreath());
    }

    //-----------------------------------------------SETTERS------------------------------------------------------------------
    public void setDest(Vector2 monPos) {
        Vector2 comp = Physics.getVxyComp(1, absPos, monPos);
        Vector2 neg = new Vector2(-comp.x, -comp.y);
        float kick = cellW * 4;
        Vector2 r = new Vector2(absPos.x + neg.x * kick, absPos.y + neg.y * kick);
        dest.set(r.x, GridManager.fixHeight(r));
        int gx = Math.round(dest.x / cell.x);
        int gy = Math.round(dest.y / cell.y);
        gm.clearArea(gx, gy, true);
        setPos(new Vector2(gx, gy));
        setAbsPos(dest);
        player.setPos(player.pos());
        player.setAbsPos(new Vector2(player.pos().x * cellW, player.pos().y * cellW));
    }

    public void setAbsPos(Vector2 a) {
        absPos.set(a);
        float x=(float) EMath.round(absPos.x / cellW);
        float y=(float) EMath.round(absPos.y / cellW);
        setPos(new Vector2(x,y));
    }

    public void setPos(Vector2 v) {
        x = (int) v.x;
        y = (int) v.y;
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
        new HoverText(gain + " EXP", .8f, Color.GREEN, player.getAbsPos().x, player.getAbsPos().y + 10, false);
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

    /*  public void setHpBuff(float f){ hpMult=f;}
        public void setMMod(float f){ mMult=f;}
        public void setEBuff(float f){ eMult=f;}
        public void setAttBuff(float f){ strMult=f;}
        public void setDefBuff(float f){ defMult=f;}
        public void setIntBuff(float f){ intMult=f;}
        public void setSpdBuff(float f){ spdMult=f;}*/
    //-----------------------------------------------ADDERS------------------------------------------------------------------
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
        new HoverText(g.getValue() + "G", 1, Color.GOLD, player.getAbsPos().x, player.getAbsPos().y, false);

    }

    public void addKills() {
        killCount++;
    }

    //-----------------------------------------------GETTERS------------------------------------------------------------------
    @SuppressWarnings("WeakerAccess")
    public boolean haveAbility(Class cls) {
        return secondaryAbilityList.stream().anyMatch(x -> x.getClass().equals(cls));
    }

    @SuppressWarnings("WeakerAccess")
    public boolean notHaveAbility(Class cls) {
        return !haveAbility(cls);
    }

    public boolean isInvEmpty() {
        return invList.isEmpty();
    }

    public boolean isOnTile(float x, float y) {
        return player.pos().x == x && player.pos().y == y;
    }


    public Rectangle getAttackBox() {
        //return new Rectangle(attackBox.x,,attackBox.width,attackBox.height);
        return attackBox;
    }

    public Triangle getAttackTriangle() {
        return attackTri;
    }

    public Circle getAttackCircle() {
        return attackCircle;
    }

    public ArrayList<Line> getAttackChain() {
        return attackChain;
    }

    public Attack getAttack() {
        if (Attack.pos > attackList.size())
            Attack.pos = attackList.size() - 1;
        return attackList.get(Attack.pos);
    }

    public Illusion.Dummy getDummy() {
        return new Illusion.Dummy((int) getHpMax() * 2, pos(), getAbsPos(), getHitBox());
    }

    public Rectangle getHitBox() {
        return new Rectangle(absPos.x, GridManager.fixHeight(absPos), getIcon().getWidth(), getIcon().getHeight());
    }

    public Vector2 getAbsPos() {
        if (absPos.y + jump() > absPos.y)
            return new Vector2(absPos.x, absPos.y + jump());
        else
            return new Vector2(absPos.x, absPos.y);
    }

    public Vector2 getTexturePos() {
        texturePos.set(getAbsPos().x, GridManager.fixHeight(getAbsPos()));
        return texturePos;
    }

    public Vector2 pos() {
        return new Vector2(x, y);
    }

    public int getLevel() {
        return level;
    }

    public int getGold() {
        return (int) gold;
    }

    public int getY() {
        return y;
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

    public float getExp() {
        return exp;
    }

    public float getMana() {
        return mana;
    }

    public float getHpRegen(){
        return regenGrowthFunction(level, getDefComp() / 2);
    }
    public float getManaRegen(){
        return regenGrowthFunction(level, getIntComp());
    }
    public float getEnergyRegen(){
        return regenGrowthFunction(level, getStrComp());
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public String getName() {
        return name;
    }


    public ArrayList<Ability> getSecondaryAbilityList() {
        return secondaryAbilityList;
    }


    DecimalFormat df = new DecimalFormat("0.00");
    public ArrayList<String> getStatsList() {
        statsList.clear();

        statsList.add(name);
        statsList.add("Level " + level);

        if(simpleStats){
            statsList.add(": " + (int) hp + "/" + getHpComp() + " :" + df.format(getHpRegen()));
            statsList.add(": " + (int) mana + "/" + getMComp() + " :" + df.format( getManaRegen()));
            statsList.add(": " + (int) energy + "/" + getEComp() + " :" +  df.format(getEnergyRegen()));
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

        }else {
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
        Texture ic=getIcon();
        return new Vector2(ic.getWidth(), ic.getHeight());
    }

    public Vector2 getFixPos() {
        return new Vector2(absPos.x,fixHeight(absPos));
    }

    //-----------------------------------------------MISC------------------------------------------------------------------
    public void updateVariables(float dt) {
        lastDt=dt;
        float n = .8f;
        if (jumping) {
            dtJump += dt;
        }
        if (dtJump >= n) {
            jumping = false;
            dtJump = 0;
        }
        //dtEnergyRe += dt;
        dtMove += dt;
        dtClearHit += dt;
        if (dtClearHit > .1f) {
            attackBox = new Rectangle(0, 0, 0, 0);
            attackCircle = new Circle();
            attackChain = new ArrayList<>();
            attackTri = new Triangle();
            dtClearHit = 0;
        }

        if (wasHit && dtHitInvincibility <= .2f)
            dtHitInvincibility += dt;
        else {
            dtHitInvincibility = 0;
            wasHit = false;
        }

        if (Dash.active)
            player.setAttackBox(getAttack().calculateHitBox());

        calculateArmorBuff();
        expLimit = (int) ((((Math.pow(1.2, level)) * 1000) / 2) - 300);
        calcVeloctiy();

        Investor.generatePlayerGold();
        regenPlayer(dt);
        resetBars();


        fixPosition();
        Inventory.fixPos();
    }

    public void fixPosition() {
        Vector2 iconDim=new Vector2(getIcon().getWidth() / 2,getIcon().getHeight() / 2);
        Pair<Vector2, Vector2> pos= Body.wrapPos(iconDim, absPos);
        setAbsPos(pos.getKey());
        setPos(pos.getValue());
    }

    private float jump() {
        return (float) ((625 * dtJump) - (927.5 * Math.pow(dtJump, 2)));
    }

    public void dig() {
        if (getStandingTile().isWall()) {
            int e = (int) Math.round((6.5 + level * 5));
            if (energy > e) {
                gm.clearArea(x, y, true);
                addEnergy(-e);
            } else {
                canMove = false;
                new HoverText("-!-", .5f, Color.YELLOW, player.getAbsPos().x + (player.getIcon().getWidth() / 2), player.getAbsPos().y + player.getIcon().getHeight() + 10, true);
            }
        } else
            canMove = true;

    }

    private void swim(Delta dWater) {
        if (getStandingTile().isWater() && notHaveAbility(WaterBreath.class) && dWater.isDone()) {
            player.addHp(-40);
            dWater.reset();
        }
    }

    private float regenGrowthFunction(int level, int stat) {

        float rate = level * (level / 192f) + (stat / 3650f) + .25f;
        float g = (60* ft) * rate;
        return g;
    }

    private int barStatGrowthFunction(int level) {
        return (int) (45 * Math.pow(Math.E, .25 * (level - 1) / 2) + 100);
    }

    private void calcVeloctiy() {
        float v = (float) (6 + .0136 * getSpdComp() + .000005 * Math.pow(getSpdComp(), 2));
        if (Dash.active) {
            v *= 6;
        }

        if (v < 5) v = 5;//min
        if (v > 18) v = 18;//max
        velocity= v;
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


    private void regenPlayer(float dt) {
        if (!infiniteRegen) {
            if (safe) dtSafe += dt;
            regenModifiers();
            if (getAbility().getClass().equals(Investor.class))
                attackList.stream().filter(a -> a.getMod() == 6 && (dtSafe > a.getLevel())).forEach(a -> {
                    safe = false;
                    dtSafe = 0;
                });

        } else {
            fullHeal();
        }
    }

    public void fullHeal() {
        setHp(getHpMax());
        setMana(getManaMax());
        setEnergy(getEnergyMax());
    }

    public void resetBars() {
        if (hp > hpMax) hp = hpMax;
        else if (hp < 0) hp = 0;

        if (mana > manaMax) mana = manaMax;
        else if (mana < 0) mana = 0;

        if (energy > energyMax) energy = energyMax;
        else if (energy < 0) energy = 0;
    }

    public void useItem(int i) {
        if (i >= 0 && i < invList.size()) {
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
                if (Inventory.pos >= invList.size() - 1) {
                    Inventory.pos = 0;
                }
                if (temp != null) {
                    equipedList.remove(temp);
                    addItemToInventory(temp);
                }

                equipedList.add((Equipment) item);
            } else if (item.isSpell) {
                SpellBook temp = (SpellBook) invList.get(i).get(0);
                invList.get(i).remove(0);
                player.attackList.add(temp.getAttack());
            } else {
                try {
                    try {
                        invList.get(i).remove(0);
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                    useItem(item);
                } catch (NullPointerException ignored) {
                }
            }
        }
    }

    public void useItem(Item item) {
        String s = "";
        if (item.getClass().equals(Gold.class)) {
            player.setGold(player.getGold() + item.getValue());
            StatManager.totalGold += item.getValue();
            out(name + " recieved " + item.getValue() + "G");
            new HoverText(item.getValue() + "G", .5f, Color.GOLD, player.getAbsPos().x, player.getAbsPos().y, false);
        }
        if (item.getHpmod() != 0) {
            hp += item.getHpmod();
            s = "+" + item.getHpmod() + " HP";
        }
        if (item.getEmod() != 0) {
            energy += item.getEmod();
            s = "+" + item.getEmod() + " E";
        }
        //Mana
        if (item.getManamod() != 0) {
            mana += item.getManamod();
            s = "+" + item.getManamod() + " M";
        }
        //str
        if (item.getStrmod() != 0) {
            str += item.getStrmod();
            s = "+" + item.getStrmod() + " STR";
        }
        //def
        if (item.getDefensemod() != 0) {
            def += item.getDefensemod();
            s = "+" + item.getDefensemod() + " DEF";
        }
        //intel
        if (item.getIntelmod() != 0) {
            intel += item.getIntelmod();
            s = "+" + item.getIntelmod() + " INT";
        }
        //spd
        if (item.getSpeedmod() != 0) {
            spd += item.getSpeedmod();
            s = "+" + item.getSpeedmod() + " SPD";
        }
        new HoverText(s, .5f, Color.GREEN, absPos.x, absPos.y, false);
    }

    public void addItemToInventory(Item item) {
        if (item != null) {
            lastItem = item;
            boolean added = false;
            for (ArrayList<Item> al : invList) {
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
                ArrayList<Item> al = new ArrayList<>();
                al.add(item);
                invList.add(al);
            }
            out(item.getName() + " added to inventory");
            new HoverText(item.getName(), 1, Color.WHITE, getAbsPos().x, getAbsPos().y, false);

        }
    }

    public void move(Vector2 vel) {
        if (!overrideControls) {
            try {
                float dt = Gdx.graphics.getDeltaTime();
                Vector2 abs = getAbsPos();
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

                Vector2 comp = Physics.getVxyComp(velocity, absPos, end);//get movement vector

                if (canMove) {
                    player.setAbsPos(new Vector2(absPos.x + comp.x, absPos.y + comp.y));
                    int x = (int) (EMath.round(absPos.x / cell.x));
                    int y = (int) (EMath.round(absPos.y / cell.x));
                    player.setPos(new Vector2(x, y));

                }
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
    }

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
            new HoverText("--LVL UP--", .8f, Color.GREEN, player.getAbsPos().x, player.getAbsPos().y - 20, true);
            exp = 0;
            level++;
            hpMax = barStatGrowthFunction(level) * 2;
            manaMax = barStatGrowthFunction(level);
            energyMax = barStatGrowthFunction(level);
            str = str + rn.nextInt(15);
            def = def + rn.nextInt(15);
            intel = intel + rn.nextInt(15);
            spd = spd + rn.nextInt(15);
            abilityPoints++;
            new HoverText("+1 Ability Point", .7f, Color.GREEN, absPos.x, absPos.y - 40, true);
            lvlupEffect = MapStateExt.loadParticles("ptFlame",absPos);
            renderEffect=true;
            lvlupEffect.start();
        }
    }

    private void calculateArmorBuff() {
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        int sum6 = 0;
        for (Equipment eq : equipedList) {
            sum1 += eq.getStrmod();
        }
        strMod = sum1;
        for (Equipment eq : equipedList) {
            sum2 += eq.getDefensemod();
        }
        defMod = sum2;
        for (Equipment eq : equipedList) {
            sum3 += eq.getIntelmod();
        }
        intMod = sum3;
        for (Equipment eq : equipedList) {
            sum4 += eq.getSpeedmod();
        }
        spdMod = sum4;
        for (Equipment eq : equipedList) {
            sum5 += eq.getHpmod();
        }
        hpMod = sum5;
        for (Equipment eq : equipedList) {
            sum6 += eq.getManamod();
        }
        manaMod = sum6;
    }

    public boolean checkIfDead(GameStateManager gsm) {
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

    private Cell getStandingTile() {
        if (GridManager.isInBounds(pos()))
            return dispArray[(int) pos().x][(int) pos().y];
        else return new Cell();
    }


    public boolean hasAP() {
        return getAbilityPoints() != 0;
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

    //UPDATE METHODS------------------------------------------------
    public void updateMapState(float dt) {
        dWater.update(dt);
        lastDt=dt;
        if (renderEffect)
            lvlupEffect.update(dt);
        swim(dWater);
        dig();
    }
    public void updateShopState(float dt){
        dShopInvScroll.update(dt);

    }

    //RENDER METHODS------------------------------------------------
    public void render(SpriteBatch sb){
        renderEffect(sb);
    }


    public void renderStatList(SpriteBatch sb, Vector2 pos) {
        Game.setFontSize(1);
        Game.getFont().setColor(Color.WHITE);
            Vector2[] v = HUD.generateStatListPos( pos);
            ArrayList<String> stats = getStatsList();
            for (int i = 0; i < stats.size(); i++) {
                //draw all stat icons here
                float x=v[i].x;
                float y=v[i].y;
                if( i>1 && i< statIcons.size())
                    sb.draw(statIcons.get(i), x-16,y-14);
                Game.font.draw(sb, stats.get(i),x , y);
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
    private void renderEffect(SpriteBatch sb){
        if(renderEffect){
            lvlupEffect.draw(sb);
            if(lvlupEffect.isComplete())
                lvlupEffect.dispose();
        }
    }

    public void renderShopInventory(SpriteBatch sb) {
        BitmapFont font = Game.getFont();
        float size = invList.size();
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
            ArrayList<Item> itemStack = invList.get((int) ind);
            Item item = itemStack.get(0);
            String qty = "x" + itemStack.size();
            String name = (i + 1) + ". " + item.getName();
            String debug = (1 + (int) ind) + "/" + (int) size+" SELL:"+item.getSellPrice();


            font.draw(sb, name, x, y);
            font.draw(sb, qty, scrx(9f / 10f), y);
            font.draw(sb, debug, x, y - 20);
            //font.draw(sb, "_________________________________", x, y - 22);


            sb.draw(item.getIcon(), x, y + 10);
        }
        float per = shopInvPos / size;
        font.draw(sb, "_", scrx(.95f), scry(.86f));

        font.draw(sb, "|", scrx(.95f), scry(.85f - (.79f * per)));
        font.draw(sb, "_", scrx(.95f), scry(.85f- (.79f*((size-1)/size))));

    }
    //--------------------------------------------------------------

    public boolean simpleStatsEnabled() {
        return simpleStats;
    }

    public void toggleSimpleStats() {
        simpleStats=!simpleStats;
    }

    public void forceLevelUp() {
        exp+=expLimit;
        checkLvlUp();
    }

    public void addShopInvOffset(int i) {
        if(dShopInvScroll.isDone()) {
            shopInvPos += i;
            dShopInvScroll.reset();
        }
    }

    public void removeFromInv(int ind) {
        invList.get(ind).remove(0);
        if (invList.get(ind).isEmpty()) {
            invList.remove(ind);
            Inventory.pos = setInBounds(ind,invList.size());
        }
    }

    public Item getSelectedItem() {
        if (invList.size() > 0)
            return invList.get(Inventory.pos).get(0);
        else
            return null;
    }
}


