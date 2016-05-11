package com.quadx.dungeons.debug.windows;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 1/18/2016.
 */
public class MainWindow {
    public MainWindow(){
        //onCreate();
    }
    public void onCreate(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JLabel label = new JLabel("");

        frame.add(panel);
        panel.add(label);
        frame.revalidate();
        frame.setPreferredSize(new Dimension(400,600));
        frame.setSize(new Dimension(400,600));
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }
}
