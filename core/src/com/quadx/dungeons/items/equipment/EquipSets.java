package com.quadx.dungeons.items.equipment;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 8/3/2016.
 */
public class EquipSets {
    ArrayList<Equipment> tank= new ArrayList<>();
    ArrayList<Equipment> investor= new ArrayList<>();
    ArrayList<Equipment> mage= new ArrayList<>();
    ArrayList<Equipment> quick= new ArrayList<>();
    ArrayList<Equipment> brawler= new ArrayList<>();
    public ArrayList<Equipment>[] ref=new ArrayList[5];
    //helm, arms, chest,legs,boots,cape,ring,gloves
    //hp,m,e,att,def,int,spd
    public EquipSets(){
        tank.add(new Equipment(Equipment.Type.Helmet,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Arms,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Chest,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Legs,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Boots,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Cape,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Ring,"",new int[]{0,0,0,0,0,0,0}));
        tank.add(new Equipment(Equipment.Type.Gloves,"",new int[]{0,0,0,0,0,0,0}));

        investor.add(new Equipment(Equipment.Type.Helmet,"Bankers Hat",new int[]{0,0,0,0,0,31,3}));
        investor.add(new Equipment(Equipment.Type.Arms,"Bankers Cuffs",new int[]{0,0,0,21,0,0,0}));
        investor.add(new Equipment(Equipment.Type.Chest,"Bankers Jacket",new int[]{20,0,0,0,24,0,0}));
        investor.add(new Equipment(Equipment.Type.Legs,"Bankers Pinstripe Pants",new int[]{0,25,0,45,20,0,0}));
        investor.add(new Equipment(Equipment.Type.Boots,"Bankers Loafers",new int[]{0,0,34,0,0,0,0}));
        investor.add(new Equipment(Equipment.Type.Cape,"Bankers Gold Chain",new int[]{0,0,0,0,0,11,0}));
        investor.add(new Equipment(Equipment.Type.Ring,"Bankers Gold Ring",new int[]{17,0,0,0,0,0,0}));
        investor.add(new Equipment(Equipment.Type.Gloves,"Bankers Counting Gloves",new int[]{0,0,0,27,0,0,0}));

        mage.add(new Equipment(Equipment.Type.Helmet,"Dark Wizard Hood",new int[]{0,0,0,0,0,22,0}));
        mage.add(new Equipment(Equipment.Type.Arms,"Dark Wizard Sleeves",new int[]{0,0,0,0,28,0,0}));
        mage.add(new Equipment(Equipment.Type.Chest,"Dark Wizard Cloak",new int[]{45,0,0,0,0,5,0}));
        mage.add(new Equipment(Equipment.Type.Legs,"Dark Wizard Underwear",new int[]{0,0,0,0,3,0,0}));
        mage.add(new Equipment(Equipment.Type.Boots,"Dark Wizard Socks with Sandals",new int[]{0,29,0,0,0,30,21}));
        mage.add(new Equipment(Equipment.Type.Cape,"Dark Wizard Cape",new int[]{0,0,0,0,0,0,36}));
        mage.add(new Equipment(Equipment.Type.Ring,"Dark Wizard Bloodring",new int[]{0,0,0,0,0,45,0}));
        mage.add(new Equipment(Equipment.Type.Gloves,"Dark Wizard Mitts",new int[]{0,17,0,0,0,0,0}));

        quick.add(new Equipment(Equipment.Type.Helmet,"Panic Helmet",new int[]{0,0,0,0,30,0,0}));
        quick.add(new Equipment(Equipment.Type.Arms,"Panic Arm Cast",new int[]{30,0,20,0,0,0,42}));
        quick.add(new Equipment(Equipment.Type.Chest,"Panic Straight Jacket",new int[]{50,0,0,0,0,0,0}));
        quick.add(new Equipment(Equipment.Type.Legs,"Panic Spandex",new int[]{0,0,20,0,0,0,20}));
        quick.add(new Equipment(Equipment.Type.Boots,"Panic Cleats",new int[]{0,0,0,0,0,0,9}));
        quick.add(new Equipment(Equipment.Type.Cape,"Panic Blanket",new int[]{0,0,0,0,22,0,0}));
        quick.add(new Equipment(Equipment.Type.Ring,"Panic Ham and Cheese",new int[]{0,0,0,6,0,0,0}));
        quick.add(new Equipment(Equipment.Type.Gloves,"Panic Rubber Gloves",new int[]{0,0,0,0,0,35,0}));

        brawler.add(new Equipment(Equipment.Type.Helmet,"Skull Mask",new int[]{0,0,50,40,0,0,10}));
        brawler.add(new Equipment(Equipment.Type.Arms,"Bone Bracers",new int[]{40,0,0,25,36,0,0}));
        brawler.add(new Equipment(Equipment.Type.Chest,"Body Cavity",new int[]{0,0,0,38,24,0,0}));
        brawler.add(new Equipment(Equipment.Type.Legs,"Pelt Pants",new int[]{0,0,0,0,0,0,12}));
        brawler.add(new Equipment(Equipment.Type.Boots,"Dismembered Feet",new int[]{0,0,0,25,0,0,30}));
        brawler.add(new Equipment(Equipment.Type.Cape,"Skin Cape",new int[]{300,0,0,0,0,0,0}));
        brawler.add(new Equipment(Equipment.Type.Ring,"Sphincter Ring",new int[]{0,22,0,0,0,0,0}));
        brawler.add(new Equipment(Equipment.Type.Gloves,"Claws",new int[]{0,0,0,20,0,0,0}));

        ref[0]=investor;
        ref[1]=investor;
        ref[2]=mage;
        ref[3]=quick;
        ref[4]=brawler;
    }

}
