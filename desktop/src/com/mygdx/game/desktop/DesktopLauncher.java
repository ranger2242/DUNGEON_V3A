package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.quadx.dungeons.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.out.println("1");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.out.println("2");

		new LwjglApplication(new Game(), config);
		System.out.println("3");

	}
}
