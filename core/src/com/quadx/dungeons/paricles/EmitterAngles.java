package com.quadx.dungeons.paricles;

/**
 * Created by Tom on 12/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class EmitterAngles {
    public static int angleHigh[]= new int[4];
    public static int angleLow[]= new int[4];
    public static int angleHHigh[];
    public static int angleLHigh[];

    public static void getAttackIndex(String s) {
        switch (s) {
            case ("Flame"): {
                angleHigh=new int[]{135,135,315,45};
                angleLow=new int[]{45,225,225,-45};
                angleHHigh=null;
                angleLHigh=null;
                break;}
            case ("Blind"): {
                angleHigh=new int[]{0,0,0,0};
                angleLow=new int[]{359,359,359,359};
                angleHHigh=null;
                angleLHigh=null;
                break;}
            case ("Drain"): {
                angleHigh=new int[]{0,0,0,0};
                angleLow=new int[]{90,180,270,0};
                angleHHigh=null;
                angleLHigh=null;
                break;}
            case ("Heal"): {
                angleHigh=new int[]{0,0,0,0};
                angleLow=new int[]{0,0,0,0};
                angleHHigh=new int[]{360,360,360,360};
                angleLHigh=new int[]{360,360,360,360};
                break;}
            case ("Illusion"): {
                angleHigh=new int[]{0,0,0,0};
                angleLow=new int[]{0,0,0,0};
                angleHHigh=new int[]{360,360,360,360};
                angleLHigh=new int[]{360,360,360,360};
                break;}
            case ("Stab"): {
                angleHigh=new int[]{90,180,270,0};
                angleLow=new int[]{90,180,270,0};
                angleHHigh=null;
                angleLHigh=null;
                break;}
            case ("Slash"): {
                angleHigh=new int[]{180,270,0,90};
                angleLow=new int[]{90,180,270,0};
                angleHHigh=new int[]{180,270,0,90};
                angleLHigh=null;
                break;}
            case ("Torment"): {
                angleHigh = new int[]{0, 90, 180, 270};
                angleLow = new int[]{90, 180, 270, 0};
                angleHHigh = new int[]{180, 270, 360, 90};
                angleLHigh = null;
            }
            case ("Protect"): {
                angleHigh=new int[]{360,360,360,360};
                angleLow=new int[]{0,0,0,0};
                break;}
            case ("Sacrifice"): {
                angleHigh=new int[]{360,360,360,360};
                angleLow=new int[]{0,0,0,0};
                break;}
        }
    }
}
