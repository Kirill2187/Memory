package ru.memory.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ru.memory.game.screens.IntroScreen;

public class MemoryGame extends Game {

	public TextureAtlas atlas;
	public Texture background;
	public Skin skin;

	public final int WIDTH = 800;
	public final int HEIGHT = 450;

	@Override
	public void create () {
		load();
		setScreen(new IntroScreen(this));
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
