package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Texture;

@SuppressWarnings("ConstantConditions")
public class WallPattern{

    public static final boolean[][] p =new boolean[3][3];
    public WallPattern(){
    }

    public static Texture getTile(int mod){
        Texture t=null;
        boolean q=p[0][0];
        boolean w=p[0][1];
        boolean e=p[0][2];
        boolean a=p[1][0];
       // boolean s=p[1][1];
        boolean d=p[1][2];
        boolean z=p[2][0];
        boolean x=p[2][1];
        boolean c=p[2][2];


        if(mod==0) {
            if (w&& (!a && !d)) t = ImageLoader.a[2];//check Top
            if (x && (!a && !d)) t = ImageLoader.a[7];//bottom
            if (a&& (!w && !x)) t = ImageLoader.a[4];
            if (d&& (!w && !x) || d) t = ImageLoader.a[5];//right
            if (c && !x && !a && !w && !d) t = ImageLoader.a[0];
            if (z && !x && !a && !w && !d) t = ImageLoader.a[0];
            if (q && !x && !a && !w && !d) t = ImageLoader.a[0];
            if (e && !x && !a && !w && !d) t = ImageLoader.a[0];
            if (a && d) t = ImageLoader. a[16];
            if (w && x) t = ImageLoader.a[20];
            if(!q&&!w &&!e &&!d &&!c &&!x && z &&!a )t = ImageLoader.a[29];
            if(q&&!w &&!e &&!d &&!c &&!x && !z &&!a )t = ImageLoader.a[28];
            if(!q&&!w &&e &&!d &&!c &&!x && !z &&!a )t = ImageLoader.a[27];
            if(!q&&!w &&!e &&!d &&c &&!x && !z &&!a )t = ImageLoader.a[26];
            if(w&&z &&!a &&!d&&!x &&!c)t=ImageLoader.a[30];
            if(w&&c &&! d &&!x &&!a &&!z) t = ImageLoader.a[31];
            if(x&&e) t = ImageLoader.a[32];
            if(x&&q) t=ImageLoader.a[33];
            if(d &&z) t = ImageLoader.a[22];
            if(d&&q)t=ImageLoader.a[23];
            if(a &&e) t = ImageLoader.a[24];
            if(a&&c)t=ImageLoader.a[25];
            if(a &&d &&!w&&!x)t=ImageLoader.a[18];
            if(!a &&!d &&w&&x)t=ImageLoader.a[20];
            if (a && w && !d &&!x &&c) t = ImageLoader.a[13];
            if (w && d &&!a &&!x &&z) t = ImageLoader.a[15];
            if (d && x &&!a &&!w &&e) t = ImageLoader.a[21];
            if (z&&  a && x &&!d && !w&&!q) t = ImageLoader.a[19];
            if (a && w && !d &&!x &&!c) t = ImageLoader.a[1];
            if (w && d &&!a &&!x &&!z) t = ImageLoader.a[3];
            if (d && x &&!a &&!w &&!e) t = ImageLoader.a[8];
            if (z&& a && x &&!d && !w &&q) t = ImageLoader.a[6];
            if(q&&!w&&e&&d&&c&&z&&!x&&!a) t = ImageLoader.a[34];
            if(q&&!w&&e&&!d&&c&&!x&&z&&a) t = ImageLoader.a[35];
            if(q&&w&&e&&!d&&c&&!x&&z&&!a) t = ImageLoader.a[36];
            if(q&&!w&&e&&!d&&c&&x&&z&&!a) t = ImageLoader.a[37];
            if(q&&!w&&!e&&!d&&c&&!x&&!z&&!a) t = ImageLoader.a[38];
            if(!q&&!w&&e&&!d&&!c&&!x&&z&&!a) t = ImageLoader.a[39];
            if(q&&!w&&e&&!d&&c&&!x&&!z&&!a) t = ImageLoader.a[40];
            if(!q&&!w&&e&&!d&&c&&!x&&z&&!a) t = ImageLoader.a[41];
            if(q&&!w&&!e&&!d&&c&&!x&&z&&!a) t = ImageLoader.a[42];
            if(q&&!w&&e&&!d&&!c&&!x&&z&&!a) t = ImageLoader.a[43];
            if(!q&&!w&&!e&&!d&&c&&!x&&z&&!a) t = ImageLoader.a[44];
            if(q&&!w&&!e&&!d&&!c&&!x&&z&&!a) t = ImageLoader.a[45];
            if(q&&!w&&e&&!d&&!c&&!x&&!z&&!a) t = ImageLoader.a[46];
            if(q&&!w&&e&&!d&&c&&!x&&!z&&!a) t = ImageLoader.a[47];
            if (w && a && d) t = ImageLoader.a[9];
            if (a  && d && x) t = ImageLoader.a[12];
            if (a && w && x) t = ImageLoader.a[10];
            if (w && x && d) t = ImageLoader.a[11];
            if ( w && d && a&& x ) {
                t= ImageLoader.a[17];
            }
        }
        if(mod==1){
            if ( w&& (!a && !d)) t = ImageLoader.w[2];//check Top
            if (x && (!a && !d)) t = ImageLoader.w[7];//bottom
            if (a&& (!w && !x)) t = ImageLoader.w[4];
            if (d&& (!w && !x) || d) t = ImageLoader.w[5];//right
            if (c && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (z && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (q && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (e && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (a && d) t = ImageLoader. w[16];
            if (w && x) t = ImageLoader.w[20];
            if(!q&&!w &&!e &&!d &&!c &&!x && z &&!a )t = ImageLoader.w[29];
            if(q&&!w &&!e &&!d &&!c &&!x && !z &&!a )t = ImageLoader.w[28];
            if(!q&&!w &&e &&!d &&!c &&!x && !z &&!a )t = ImageLoader.w[27];
            if(!q&&!w &&!e &&!d &&c &&!x && !z &&!a )t = ImageLoader.w[26];
            if(w&&z &&!a &&!d&&!x &&!c)t=ImageLoader.w[30];
            if(w&&c &&! d &&!x &&!a &&!z) t = ImageLoader.w[31];
            if(x&&e) t = ImageLoader.w[32];
            if(x&&q) t=ImageLoader.w[33];
            if(d &&z) t = ImageLoader.w[22];
            if(d&&q)t=ImageLoader.w[23];
            if(a &&e) t = ImageLoader.w[24];
            if(a&&c)t=ImageLoader.w[25];
            if(a &&d &&!w&&!x)t=ImageLoader.w[18];
            if(!a &&!d &&w&&x)t=ImageLoader.w[20];
            if (a && w && !d &&!x &&c) t = ImageLoader.w[13];
            if (w && d &&!a &&!x &&z) t = ImageLoader.w[15];
            if (d && x &&!a &&!w &&e) t = ImageLoader.w[21];
            if (a && x &&!d && !w &&q) t = ImageLoader.w[19];
            if (a && w && !d &&!x &&!c) t = ImageLoader.w[1];
            if (w && d &&!a &&!x &&!z) t = ImageLoader.w[3];
            if (d && x &&!a &&!w &&!e) t = ImageLoader.w[8];
            if (a && x &&!d && !w&&!q) t = ImageLoader.w[6];
            if(q&&!w&&e&&d&&c&&z&&!x&&!a) t = ImageLoader.w[34];
            if(q&&!w&&e&&!d&&c&&!x&&z&&a) t = ImageLoader.w[35];
            if(q&&w&&e&&!d&&c&&!x&&z&&!a) t = ImageLoader.w[36];
            if(q&&!w&&e&&!d&&c&&x&&z&&!a) t = ImageLoader.w[37];
            if(q&&!w&&!e&&!d&&c&&!x&&!z&&!a) t = ImageLoader.w[38];
            if(!q&&!w&&e&&!d&&!c&&!x&&z&&!a) t = ImageLoader.w[39];
            if(q&&!w&&e&&!d&&c&&!x&&!z&&!a) t = ImageLoader.w[40];
            if(!q&&!w&&e&&!d&&c&&!x&&z&&!a) t = ImageLoader.w[41];
            if(q&&!w&&!e&&!d&&c&&!x&&z&&!a) t = ImageLoader.w[42];
            if(q&&!w&&e&&!d&&!c&&!x&&z&&!a) t = ImageLoader.w[43];
            if(!q&&!w&&!e&&!d&&c&&!x&&z&&!a) t = ImageLoader.w[44];
            if(q&&!w&&!e&&!d&&!c&&!x&&z&&!a) t = ImageLoader.w[45];
            if(q&&!w&&e&&!d&&!c&&!x&&!z&&!a) t = ImageLoader.w[46];
            if(q&&!w&&e&&!d&&c&&!x&&!z&&!a) t = ImageLoader.w[47];
            if (w && a && d) t = ImageLoader.w[9];
            if (a  && d && x) t = ImageLoader.w[12];
            if (a && w && x) t = ImageLoader.w[10];
            if (w && x && d) t = ImageLoader.w[11];
            if ( w && d && a&& x ) {
                t= ImageLoader.w[17];
            }
        }
        return t;
    }
}
