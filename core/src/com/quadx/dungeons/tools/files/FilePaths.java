package com.quadx.dungeons.tools.files;

/**
 * Created by Jonas on 1/15/17.
 */


public class FilePaths {
    private static int os = 0; //integer to determine os of the test environment (0 windows, 1 mac, 2 linux)
    public static String getPath(String s) {
        char s1[]=s.toCharArray();

        if(os!=0){
            for(int i=0;i<s.length();i++){
                if(s.charAt(i)=='\\')
                   s1[i]='/';
            }
        }
        StringBuilder r= new StringBuilder();
        for (char aS1 : s1) {
            r.append(aS1);
        }
        return r.toString();
    }

    public static int checkOS() {
        String n=System.getProperty("os.name");
        n = n.toUpperCase();
        if (n.contains("WINDOWS")) {os = 0;}
        else if (n.contains("OSX")) {os = 1;}
        else if (n.contains("LINUX")) {os = 2;}
        return os;
    }
}
