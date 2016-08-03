package com.quadx.dungeons.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;


/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class ImageLoader {
    public static final ArrayList<Texture> attacks = new ArrayList<>();
    public static final ArrayList<Texture> abilities = new ArrayList<>();
    public static final ArrayList<Texture> abilities2 = new ArrayList<>();
    public static final Texture[] floors=new Texture[17];
    public static final Texture[] w =new Texture[48];
    public static final Texture[] a=new Texture[48];
    public static final Texture[] gold=new Texture[3];
    public static final Texture[] en0=new Texture[4];
    public static final Texture[] en1=new Texture[4];
    public static final Texture[] en3=new Texture[4];
    public static final Texture[] en4=new Texture[4];
    public static final Texture[] en5=new Texture[4];
    public static final Texture[] en6=new Texture[4];
    public static final Texture[] en7=new Texture[4];
    public static final Texture[] en8=new Texture[4];
    public static final Texture[] en9=new Texture[4];

    public static final Texture[] potion = new Texture[3];
    public static final Texture[] mana = new Texture[3];
    public static final Texture[] energy = new Texture[3];

    public static final Texture crate=new Texture("images\\icons\\items\\icCrate.png");
    public static final Texture warp=new Texture("images\\tiles\\icWarp.png");
    public static final Texture spellbook= new Texture("images\\icons\\items\\icSpellBook.png");

    public ImageLoader(){
        for(int i=0;i<17;i++){
            floors[i]=new Texture("images\\tiles\\floor\\f"+(i)+".png");
        }
        for(int i=0;i<48;i++){
                a[i] = new Texture("images\\tiles\\walls\\a" + (i) + ".png");
        }
        potion[0]=new Texture("images\\icons\\items\\icHpSmall.png");
        potion[1]=new Texture("images\\icons\\items\\icHpMed.png");
        potion[2]=new Texture("images\\icons\\items\\icHpLarge.png");

        mana[0]=new Texture("images\\icons\\items\\icMSmall.png");
        mana[1]=new Texture("images\\icons\\items\\icMMed.png");
        mana[2]=new Texture("images\\icons\\items\\icMLarge.png");

        energy[0]=new Texture("images\\icons\\items\\icESmall.png");
        energy[1]=new Texture("images\\icons\\items\\icEMed.png");
        energy[2]=new Texture("images\\icons\\items\\icELarge.png");

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
    private void loadAbilities(){
        abilities.add(new Texture("images\\icons\\abilities\\icTank.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icInvestor.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icMage.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icQuick.png"));
        abilities.add(new Texture("images\\icons\\abilities\\icBrawler.png"));

        abilities2.add(new Texture("images\\icons\\abilities\\icDigPlus.png"));
    }
    private void loadAttacks(){
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
    private void loadMonsters(){
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
