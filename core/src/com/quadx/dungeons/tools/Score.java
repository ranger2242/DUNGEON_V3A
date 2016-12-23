package com.quadx.dungeons.tools;


/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class Score {
    private String score = null;
    private String name= null;
    private String ability= null;
    private String gold= null;
    private String kills= null;
    public Score(String n, String s, String g, String a,String as){
        score=""+s;
        name=n;
        gold=""+g;
        ability=a;
        kills=as;
    }

    public Score() {

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
