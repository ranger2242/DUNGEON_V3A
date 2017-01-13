package com.quadx.dungeons.items.equipment;

import com.quadx.dungeons.tools.ImageLoader;

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
        tank.add(new Equipment( ImageLoader.equipBasic[5],Equipment.Type.Helmet,"Bulletproof Mask",new int[]{100,0,20,0,26,0,0}));
        tank.add(new Equipment( ImageLoader.equipBasic[0],Equipment.Type.Arms,"Armored Gauntlets",new int[]{30,0,0,30,15,0,0}));
        tank.add(new Equipment( ImageLoader.equipBasic[3],Equipment.Type.Chest,"Plate Armor",new int[]{68,0,0,0,59,0,-20}));
        tank.add(new Equipment( ImageLoader.equipBasic[6],Equipment.Type.Legs,"Armored Pants",new int[]{33,0,75,0,15,20,-5}));
        tank.add(new Equipment( ImageLoader.equipBasic[1],Equipment.Type.Boots,"Combat Boots",new int[]{0,0,30,10,12,0,15}));
        tank.add(new Equipment( ImageLoader.equipBasic[2],Equipment.Type.Cape,"National Flag Cape",new int[]{50,50,50,0,0,0,0}));
        tank.add(new Equipment( ImageLoader.equipBasic[7],Equipment.Type.Ring,"Brass Knuckles",new int[]{0,0,0,50,0,20,15}));
        tank.add(new Equipment( ImageLoader.equipBasic[4],Equipment.Type.Gloves,"Combat Gloves",new int[]{20,30,0,14,14,0,0}));

        investor.add(new Equipment( ImageLoader.equipBasic[5],Equipment.Type.Helmet,"Bankers Hat",new int[]{0,30,30,0,0,15,5}));
        investor.add(new Equipment(ImageLoader.equipBasic[0],Equipment.Type.Arms,"Bankers Cuffs",new int[]{0,0,0,21,0,21,0}));
        investor.add(new Equipment(ImageLoader.equipBasic[3],Equipment.Type.Chest,"Bankers Jacket",new int[]{120,50,0,0,20,0,0}));
        investor.add(new Equipment(ImageLoader.equipBasic[6],Equipment.Type.Legs,"Pinstripe Pants",new int[]{0,25,0,45,20,0,0}));
        investor.add(new Equipment(ImageLoader.equipBasic[1],Equipment.Type.Boots,"Brown Loafers",new int[]{0,55,34,0,0,0,35}));
        investor.add(new Equipment(ImageLoader.equipBasic[2],Equipment.Type.Cape,"Gold Chain",new int[]{0,40,0,0,4,35,0}));
        investor.add(new Equipment(ImageLoader.equipBasic[7],Equipment.Type.Ring,"Gold Ring",new int[]{55,10,40,0,0,0,0}));
        investor.add(new Equipment(ImageLoader.equipBasic[4],Equipment.Type.Gloves,"Bankers Counting Gloves",new int[]{0,0,0,41,3,15,0}));

        mage.add(new Equipment(ImageLoader.equipBasic[5],Equipment.Type.Helmet,"Perception Hood",new int[]{10,30,0,-10,5,24,0}));
        mage.add(new Equipment(ImageLoader.equipBasic[0],Equipment.Type.Arms,"Dark Wizard Sleeves",new int[]{0,0,0,-2,12,12,0}));
        mage.add(new Equipment(ImageLoader.equipBasic[3],Equipment.Type.Chest,"Cloak",new int[]{100,100,50,0,10,20,5}));
        mage.add(new Equipment(ImageLoader.equipBasic[6],Equipment.Type.Legs,"Dark Wizard Underwear",new int[]{0,60,0,0,3,0,25}));
        mage.add(new Equipment(ImageLoader.equipBasic[1],Equipment.Type.Boots,"Socks with Sandals",new int[]{0,29,0,0,0,30,21}));
        mage.add(new Equipment(ImageLoader.equipBasic[2],Equipment.Type.Cape,"Dark Wizard Cape",new int[]{0,0,25,0,0,0,36}));
        mage.add(new Equipment(ImageLoader.equipBasic[7],Equipment.Type.Ring,"Hate Filled Bloodring",new int[]{0,0,0,0,10,45,12}));
        mage.add(new Equipment(ImageLoader.equipBasic[4],Equipment.Type.Gloves,"Mitts",new int[]{0,45,45,0,0,0,0}));

        quick.add(new Equipment(ImageLoader.equipBasic[5],Equipment.Type.Helmet,"Panic Helmet",new int[]{0,0,0,0,30,0,0}));
        quick.add(new Equipment(ImageLoader.equipBasic[0],Equipment.Type.Arms,"Panic Arm Cast",new int[]{30,0,20,0,0,0,42}));
        quick.add(new Equipment(ImageLoader.equipBasic[3],Equipment.Type.Chest,"Panic Straight Jacket",new int[]{50,0,0,0,0,0,0}));
        quick.add(new Equipment(ImageLoader.equipBasic[6],Equipment.Type.Legs,"Panic Spandex",new int[]{0,0,20,0,0,0,20}));
        quick.add(new Equipment(ImageLoader.equipBasic[1],Equipment.Type.Boots,"Panic Cleats",new int[]{0,0,0,0,0,0,9}));
        quick.add(new Equipment(ImageLoader.equipBasic[2],Equipment.Type.Cape,"Panic Blanket",new int[]{0,0,0,0,22,0,0}));
        quick.add(new Equipment(ImageLoader.equipBasic[7],Equipment.Type.Ring,"Panic Ham and Cheese",new int[]{0,0,0,6,0,0,0}));
        quick.add(new Equipment(ImageLoader.equipBasic[4],Equipment.Type.Gloves,"Panic Rubber Gloves",new int[]{0,0,0,0,0,35,0}));

        brawler.add(new Equipment(ImageLoader.equipBasic[5],Equipment.Type.Helmet,"Skull Mask",new int[]{0,0,50,40,0,0,10}));
        brawler.add(new Equipment(ImageLoader.equipBasic[0],Equipment.Type.Arms,"Bone Bracers",new int[]{40,0,0,25,36,0,0}));
        brawler.add(new Equipment(ImageLoader.equipBasic[3],Equipment.Type.Chest,"Body Cavity",new int[]{0,0,0,38,24,0,0}));
        brawler.add(new Equipment(ImageLoader.equipBasic[6],Equipment.Type.Legs,"Pelt Pants",new int[]{0,0,0,0,0,0,12}));
        brawler.add(new Equipment(ImageLoader.equipBasic[1],Equipment.Type.Boots,"Dismembered Feet",new int[]{0,0,0,25,0,0,30}));
        brawler.add(new Equipment(ImageLoader.equipBasic[2],Equipment.Type.Cape,"Skin Cape",new int[]{300,0,0,0,0,0,0}));
        brawler.add(new Equipment(ImageLoader.equipBasic[7],Equipment.Type.Ring,"Sphincter Ring",new int[]{0,22,0,0,0,0,0}));
        brawler.add(new Equipment(ImageLoader.equipBasic[4],Equipment.Type.Gloves,"Claws",new int[]{0,0,0,20,0,0,0}));

        ref[0]=tank;
        ref[1]=investor;
        ref[2]=mage;
        ref[3]=quick;
        ref[4]=brawler;
    }

}
