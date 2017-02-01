package com.quadx.dungeons.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;


/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class ImageLoader {
   public static Texture[] pl=new Texture[4];

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
    public static final Texture[] equipBasic= new Texture[8];

    public static final Texture crate=new Texture(FilePaths.getPath("images\\icons\\items\\icCrate.png"));

    public static final Texture warp=new Texture(FilePaths.getPath("images\\tiles\\icWarp.png"));
    public static Texture spellbook= new Texture(FilePaths.getPath("images\\icons\\items\\icSpellBook.png"));


    public ImageLoader(){
        pl[0]= new Texture(Gdx.files.internal("images/icons/player/00.png"));
        pl[1]=  new Texture(Gdx.files.internal("images/icons/player/03.png"));
        pl[2]=  new Texture(Gdx.files.internal("images/icons/player/01.png"));
        pl[3]=  new Texture(Gdx.files.internal("images/icons/player/02.png"));
        for(int i=0;i<17;i++){
            floors[i]=new Texture(FilePaths.getPath("images\\tiles\\floor\\f"+(i)+".png"));
        }
        for(int i=0;i<48;i++){
                a[i] = new Texture(FilePaths.getPath("images\\tiles\\walls\\a" + (i) + ".png"));
        }
        potion[0]=new Texture(FilePaths.getPath("images\\icons\\items\\icHpSmall.png"));
        potion[1]=new Texture(FilePaths.getPath("images\\icons\\items\\icHpMed.png"));
        potion[2]=new Texture(FilePaths.getPath("images\\icons\\items\\icHpLarge.png"));

        mana[0]=new Texture(FilePaths.getPath("images\\icons\\items\\icMSmall.png"));
        mana[1]=new Texture(FilePaths.getPath("images\\icons\\items\\icMMed.png"));
        mana[2]=new Texture(FilePaths.getPath("images\\icons\\items\\icMLarge.png"));

        energy[0]=new Texture(FilePaths.getPath("images\\icons\\items\\icESmall.png"));
        energy[1]=new Texture(FilePaths.getPath("images\\icons\\items\\icEMed.png"));
        energy[2]=new Texture(FilePaths.getPath("images\\icons\\items\\icELarge.png"));

        gold[0]=new Texture(FilePaths.getPath("images\\icons\\items\\icCoinS.png"));
        gold[1]=new Texture(FilePaths.getPath("images\\icons\\items\\icCoinM.png"));
        gold[2]=new Texture(FilePaths.getPath("images\\icons\\items\\icCoinL.png"));
        for(int i=0;i<48;i++){
                w[i] = new Texture(FilePaths.getPath("images\\tiles\\water\\w" +i + ".png"));
        }

        loadAttacks();
        loadMonsters();
        loadAbilities();
        loadEquip();
    }
    private void loadEquip(){
        equipBasic[0]=new Texture(FilePaths.getPath("images\\icons\\items\\icArms.png"));
        equipBasic[1]=new Texture(FilePaths.getPath("images\\icons\\items\\icBoots.png"));
        equipBasic[2]=new Texture(FilePaths.getPath("images\\icons\\items\\icCape.png"));
        equipBasic[3]=new Texture(FilePaths.getPath("images\\icons\\items\\icChest.png"));
        equipBasic[4]=new Texture(FilePaths.getPath("images\\icons\\items\\icGloves.png"));
        equipBasic[5]=new Texture(FilePaths.getPath("images\\icons\\items\\icHelmet.png"));
        equipBasic[6]=new Texture(FilePaths.getPath("images\\icons\\items\\icLegs.png"));
        equipBasic[7]=new Texture(FilePaths.getPath("images\\icons\\items\\icRing.png"));

    }
    private void loadAbilities(){
        abilities.add(new Texture(FilePaths.getPath("images\\icons\\abilities\\icTank.png")));
        abilities.add(new Texture(FilePaths.getPath("images\\icons\\abilities\\icInvestor.png")));
        abilities.add(new Texture(FilePaths.getPath("images\\icons\\abilities\\icMage.png")));
        abilities.add(new Texture(FilePaths.getPath("images\\icons\\abilities\\icQuick.png")));
        abilities.add(new Texture(FilePaths.getPath("images\\icons\\abilities\\icBrawler.png")));

        abilities2.add(new Texture(FilePaths.getPath("images\\icons\\abilities\\icWB.png")));
    }
    private void loadAttacks(){
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icBlind.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icDrain.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icFlame.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icFocus.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icHeal.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icIllusion.png")));//5
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icProtect.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icRest.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icSacrifice.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icQuake.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icStab.png")));//10
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icTorment.png")));
        attacks.add(new Texture(FilePaths.getPath("images\\icons\\attacks\\icLightning.png")));

    }
    private void loadMonsters(){
        for(int i=0;i<4;i++){
            en0[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en0\\en0"+i+".png")));

        }
        for(int i=0;i<4;i++){
            en1[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en1\\en1"+i+".png")));

        }
        for(int i=0;i<4;i++){
            en3[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en3\\en3"+i+".png")));

        }
        for(int i=0;i<4;i++){
            en4[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en4\\en4"+i+".png")));

        }
        for(int i=0;i<4;i++){
            en5[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en5\\en5"+i+".png")));
        }
        for(int i=0;i<4;i++){
            en9[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en9\\en9"+i+".png")));
        }
        for(int i=0;i<4;i++){
            en6[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en6\\en6"+i+".png")));
        }
        for(int i=0;i<4;i++){
            en7[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en7\\en7"+i+".png")));
        }
        for(int i=0;i<4;i++){
            en8[i]=  new Texture(Gdx.files.internal(FilePaths.getPath("images\\icons\\monsters\\en8\\en8"+i+".png")));
        }
    }
}
