package ru.memory.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import ru.memory.game.MemoryGame;
import ru.memory.game.logic.Button;
import ru.memory.game.logic.Card;
import ru.memory.game.logic.Field;

public class GameScreen implements Screen {

    MemoryGame game;

    Stage stage;
    StretchViewport viewport;
    SpriteBatch batch;

    String difficult;
    Field field;

    BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/font_big.fnt"));

    public GameScreen(MemoryGame game, String difficult) {
        this.game = game;
        this.difficult = difficult;

        batch = new SpriteBatch();
        viewport = new StretchViewport(game.WIDTH, game.HEIGHT);
        stage = new Stage(viewport, batch);
        //stage.setDebugAll(true);

        font.getData().setScale(0.3f);

        initUI();
    }

    private void initUI() {
        Button back = new Button("back1", "back2", "back3", 0f,
                game.HEIGHT - 50f, 50f, 50f, game);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(MemoryGame.Screens.MenuScreen, "");
            }
        });
        stage.addActor(back);

        Button restart = new Button("restart1", "restart2", "restart3", game.WIDTH - 50f,
                0f, 50f, 50f, game);
        restart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (field.isGameStopped)
                    return;
                field.isGameStopped = true;
                for (int i = 0; i < field.height; ++i) {
                    for (int j = 0; j < field.width; ++j) {
                        field.field[i][j].animated = 1;
                    }
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        for (int i = 0; i < field.height; ++i) {
                            for (int j = 0; j < field.width; ++j) {
                                if (field.field[i][j].status != -1) field.field[i][j].startAnim();
                            }
                        }
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                field.restartGame();
                            }
                        }, (Card.ROTATE_TIME + 100f) / 1000f);
                    }
                }, (Card.ROTATE_TIME + 1200f) / 1000f);
            }
        });
        stage.addActor(restart);

        field = new Field(difficult, game, stage);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0.5f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        batch.begin();
        font.draw(batch, "Turns - " + field.turns, 630f, game.HEIGHT - 20f);
        font.draw(batch, "" + field.score / 2,
                game.WIDTH - field.cardSize / 2f - (difficult.equals("Easy") ? 5:10), field.cardOff_y_2 - 10f);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

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
        font.dispose();
    }

}
