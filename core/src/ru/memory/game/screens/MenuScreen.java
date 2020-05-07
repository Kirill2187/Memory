package ru.memory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import ru.memory.game.MemoryGame;
import ru.memory.game.logic.Button;

public class MenuScreen extends InputAdapter implements Screen {

    MemoryGame game;

    Stage stage;
    StretchViewport viewport;
    SpriteBatch batch;
    List<String> list;

    public MenuScreen(MemoryGame game) {
        this.game = game;

        batch = new SpriteBatch();
        viewport = new StretchViewport(game.WIDTH, game.HEIGHT);
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        init();
    }

    private void init() {
        Button play = new Button("single1", "single2", "single3", 320, 205, 75, 75, game);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game, list.getSelected(), 1));
            }
        });
        stage.addActor(play);

        Button multi = new Button("multi1", "multi2", "multi3", 405, 205, 75, 75, game);
        multi.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(new GameScreen(game, list.getSelected(), 2));
            }
        });
        stage.addActor(multi);

        Button quit = new Button("quit1", "quit2", "quit3", 0f, 0f, 50f, 50f, game);
        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Gdx.app.exit();
            }
        });
        stage.addActor(quit);

        list = new List(game.skin, "default");
        list.setItems("Easy", "Normal", "Hard", "Impossible");
        list.setWidth(190f);
        list.setHeight(100f);
        list.setPosition(game.WIDTH / 2f - list.getWidth() / 2f, 50f);
        stage.addActor(list);

        Label memory = new Label("Memory", game.skin);
        memory.setPosition(game.WIDTH / 2f - memory.getWidth() / 2f, game.HEIGHT - memory.getHeight());
        stage.addActor(memory);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(game.background, 0, 0, game.WIDTH, game.HEIGHT);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
