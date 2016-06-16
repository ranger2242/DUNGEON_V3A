package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Texture;

public class WallPattern{
    public static boolean[][] p =new boolean[3][3];
    public WallPattern(){
    }

    public static Texture getTile(int mod){
        Texture t=null;
        boolean q=p[0][0];
        boolean w=p[0][1];
        boolean e=p[0][2];
        boolean a=p[1][0];
        boolean s=p[1][1];
        boolean d=p[1][2];
        boolean z=p[2][0];
        boolean x=p[2][1];
        boolean c=p[2][2];

        if(mod==0) {
            //p[][]
            if ((q || w || e) && (!a && !d)) t = ImageLoader.a[1];//check Top
            if ((z || x || c) && (!a && !d)) t = ImageLoader.a[5];//bottom
            if ((q || a || z) && (!w && !x)) t = ImageLoader.a[7];
            if ((e || d || c) && (!w && !x) || d) t = ImageLoader.a[3];//right
            if ((q && w && e && z && !c) || (q && w && e && !z && c)) t = ImageLoader.a[1];
            if ((z && x && c && q && !e) || (z && x && c && !q && e)) t = ImageLoader.a[5];
            if ((q && e && z && !c && a) || (!e && q && a && z && c)) t = ImageLoader.a[7];
            if ((q && e && !z && c && d) || (!q && e && z && c && d)) t = ImageLoader.a[3];
            if (q && a && w) t = ImageLoader.a[0];
            if (e && w && d) t = ImageLoader.a[2];
            if (d && c && x) t = ImageLoader.a[4];
            if (a && z && x) t = ImageLoader.a[6];
            if (c && !x && !a && !w && !d) t = ImageLoader.c[0];
            if (z && !x && !a && !w && !d) t = ImageLoader.c[2];
            if (q && !x && !a && !w && !d) t = ImageLoader.c[4];
            if (e && !x && !a && !w && !d) t = ImageLoader.c[6];
            if (a && d) t = ImageLoader.b[4];
            if (w && q && a && e && d) t = ImageLoader.b[3];
            if (a && z && d && c && x) t = ImageLoader.b[5];
            if (w && x) t = ImageLoader.b[1];
            if (a && q && w && z && x) t = ImageLoader.b[0];
            if (w && e && x && c && d) t = ImageLoader.b[2];
            if (!q && !w && !e && !d && !a && !z && !x && !c) {
                t = ImageLoader.walls[0];
            }
        }
        if(mod==1){
            if ((q || w || e) && (!a && !d)) t = ImageLoader.w[2];//check Top
            if ((z || x || c) && (!a && !d)) t = ImageLoader.w[7];//bottom
            if ((q || a || z) && (!w && !x)) t = ImageLoader.w[4];
            if ((e || d || c) && (!w && !x) || d) t = ImageLoader.w[5];//right
            if ((q && w && e && z && !c) || (q && w && e && !z && c)) t = ImageLoader.w[2];
            if ((z && x && c && q && !e) || (z && x && c && !q && e)) t = ImageLoader.w[7];
            if ((q && e && z && !c && a) || (!e && q && a && z && c)) t = ImageLoader.w[4];
            if ((q && e && !z && c && d) || (!q && e && z && c && d)) t = ImageLoader.w[5];

            if (q && a && w) t = ImageLoader.w[1];
            if (e && w && d) t = ImageLoader.w[3];
            if (d && c && x) t = ImageLoader.w[8];
            if (a && z && x) t = ImageLoader.w[6];
           if (c && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (z && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (q && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (e && !x && !a && !w && !d) t = ImageLoader.w[0];
            if (a && d) t = ImageLoader. w[16];
            if (w && q && a && e && d) t = ImageLoader.w[9];
            if (a && z && d && c && x) t = ImageLoader.w[12];
            if (w && x) t = ImageLoader.w[20];
            if (a && q && w && z && x) t = ImageLoader.w[10];
            if (w && e && x && c && d) t = ImageLoader.w[11];
            if ( w && d && a&& x ) {
               t= ImageLoader.w[17];
               // t = ImageLoader.w[0];
            }
        }
        return t;
    }
}
