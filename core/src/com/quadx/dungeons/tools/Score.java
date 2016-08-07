package com.quadx.dungeons.tools;


/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class Score {
    private final String score;
    private final String name;
    private final String ability;
    private final String gold;
    private final String kills;
    public Score(String n, String s, String g, String a,String as){
        score=""+s;
        name=n;
        gold=""+g;
        ability=a;
        kills=as;
    }
    public String toString(){
        String s=name;
        while (s.length()<20){
            s+=" ";
        }
        s+=score;
        while (s.length()<35){
            s+=" ";
        }
        s+=gold+"G";
        while (s.length()<55){
            s+=" ";
        }
        s+=ability;
        while (s.length()<80){
            s+=" ";
        }
        s+=kills+" KILLS";
        return s;
    }
    public String getSaveFormat(){
        return name+","+score+","+gold+","+ability+","+kills;
    }
    public String getScore(){
        return score;
    }
}
