package com.gravity.ball.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gravity.ball.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GRAVITY BALL";
	    config.width = 800;
	    config.height = 480;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
