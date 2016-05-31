package com.quadx.dungeons.tools;


import com.quadx.dungeons.abilities.Ability;

/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class Score {
    protected String score;
    protected String name;
    protected String ability;
    protected String gold;
    public Score(int s, int g, String n, String a){
        score=""+s;
        name=n;
        gold=""+g;
        ability=a;
    }
    public Score(String n, String s, String g, String a){
        score=""+s;
        name=n;
        gold=""+g;
        ability=a;
    }
    public String toString(){
        String s=name;
        while (s.length()<20){
            s+=" ";
        }
        s+=score;
        while (s.length()<40){
            s+=" ";
        }
        s+=gold+"G";
        while (s.length()<60){
            s+=" ";
        }
        s+=ability;
        while (s.length()<80){
            s+=" ";
        }

        return s;
    }
    public String getSaveFormat(){
        return name+","+score+","+gold+","+ability;
    }
    public String getScore(){
        return score;
    }
}
