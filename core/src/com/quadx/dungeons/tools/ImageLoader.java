package com.quadx.dungeons.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class ImageLoader {
    public static ArrayList<Texture[]> enemies = new ArrayList<>();
    public static ArrayList<Texture> attacks = new ArrayList<>();
    public static ArrayList<Texture> abilities = new ArrayList<>();


    public Random rn=new Random();
    public static Texture[] floors=new Texture[13];
    public static Texture[] w =new Texture[48];
    public static Texture[] a=new Texture[48];
    public static Texture[] gold=new Texture[3];
    public static Texture[] en0=new Texture[4];
    public static Texture[] en1=new Texture[4];
    public static Texture[] en3=new Texture[4];
    public static Texture[] en4=new Texture[4];
    public static Texture[] en5=new Texture[4];
    public static Texture[] en6=new Texture[4];
    public static Texture[] en7=new Texture[4];
    public static Texture[] en8=new Texture[4];
    public static Texture[] en9=new Texture[4];



    public static Texture potion = new Texture("images\\icons\\items\\icPotion.png");
    public static Texture mana = new Texture("images\\icons\\items\\icMPlus.png");
    public static Texture crate=new Texture("images\\icons\\items\\icCrate.png");
    public static Texture warp=new Texture("images\\tiles\\icWarp.png");
    public static Texture spellbook= new Texture("images\\icons\\items\\icSpellBook.png");

    public ImageLoader(){
        for(int i=0;i<13;i++){
            floors[i]=new Texture("images\\tiles\\floor\\f"+(i)+".png");
        }
        for(int i=0;i<48;i++){
            try {
                a[i] = new Texture("images\\tiles\\walls\\a" + (i) + ".png");
            }
            catch (GdxRuntimeException e){}
        }
        gold[0]=new Texture("images\\icons\\items\\icCoinS.png");
        gold[1]=new Texture("images\\icons\\items\\icCoinM.png");
        gold[2]=new Texture("images\\icons\\items\\icCoinL.png");
        for(int i=0;i<48;i++){
                w[i] = new Texture("images\\tiles\\water\\w" +i + ".png");
        }

        loadAttacks();
        loadMonsters();
        loadAbilities();
    }
    void loadAbilities(){
        abilities.add(new Texture("images\\icons\\abilities\\icTank.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icInvestor.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icMage.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icQuick.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icBrawler.png"));

    }
    void loadAttacks(){
        attacks.add(new Texture("images\\icons\\attacks\\icBlind.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icDrain.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icFlame.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icFocus.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icHeal.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icIllusion.png"));//5
        attacks.add(new Texture("images\\icons\\attacks\\icProtect.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icRest.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icSacrifice.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icQuake.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icStab.png"));//10
        attacks.add(new Texture("images\\icons\\attacks\\icTorment.png"));
        attacks.add(new Texture("images\\icons\\attacks\\icLightning.png"));

    }
    void loadMonsters(){
        for(int i=0;i<4;i++){
            en0[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en0\\en0"+i+".png"));

        }
        for(int i=0;i<4;i++){
            en1[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en1\\en1"+i+".png"));

        }
        for(int i=0;i<4;i++){
            en3[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en3\\en3"+i+".png"));

        }
        for(int i=0;i<4;i++){
            en4[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en4\\en4"+i+".png"));

        }
        for(int i=0;i<4;i++){
            en5[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en5\\en5"+i+".png"));
        }
        for(int i=0;i<4;i++){
            en9[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en9\\en9"+i+".png"));
        }
        for(int i=0;i<4;i++){
            en6[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en6\\en6"+i+".png"));
        }
        for(int i=0;i<4;i++){
            en7[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en7\\en7"+i+".png"));
        }
        for(int i=0;i<4;i++){
            en8[i]=  new Texture(Gdx.files.internal("images\\icons\\monsters\\en8\\en8"+i+".png"));
        }
    }
}
