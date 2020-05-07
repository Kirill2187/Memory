package ru.memory.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ru.memory.game.screens.GameScreen;
import ru.memory.game.screens.IntroScreen;
import ru.memory.game.screens.MenuScreen;

public class MemoryGame extends Game {

	public TextureAtlas atlas;
	public Texture background;
	public Skin skin;

	public final int WIDTH = 800;
	public final int HEIGHT = 450;

	public enum Screens {IntroScreen, MenuScreen, GameScreen}

	public void setScreen(Screens screen, String difficult) {
		switch (screen) {
			case GameScreen:
				super.setScreen(new GameScreen(this, difficult));
				break;
			case MenuScreen:
				super.setScreen(new MenuScreen(this));
				break;
			case IntroScreen:
				super.setScreen(new IntroScreen(this));
				break;
		}
	}
	
	@Override
	public void create () {
		load();
		setScreen(Screens.IntroScreen, "");
	}

	private void load() {
		atlas = new TextureAtlas(Gdx.files.internal("assets_out/assets.pack"));
		background = new Texture(Gdx.files.internal("background.png"));
		skin = new Skin(Gdx.files.internal("skin/skin.json"));
	}

	public Sprite getSprite(String name) {
		return atlas.createSprite(name);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}
