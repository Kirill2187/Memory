package ru.memory.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.memory.game.MemoryGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.resizable = true;
		config.width = 16 * 60;
		config.height = 9 * 60;
		config.title = "Memory";

		new LwjglApplication(new MemoryGame(), config);
	}
}
